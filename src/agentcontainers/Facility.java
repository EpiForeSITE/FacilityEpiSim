package agentcontainers;
import agents.Person;
import builders.SingleFacilityBuilder;
import disease.Disease;
import disease.FacilityOutbreak;
import disease.PersonDisease;
import org.apache.commons.math3.distribution.ExponentialDistribution;
import org.apache.commons.math3.distribution.GammaDistribution;
import repast.simphony.engine.schedule.ISchedulableAction;
import repast.simphony.engine.schedule.ISchedule;
import repast.simphony.engine.schedule.ScheduleParameters;
import repast.simphony.parameter.Parameters;
import repast.simphony.random.RandomHelper;
import repast.simphony.context.Context;
import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.util.ContextUtils;
import utils.TimeUtils;
import utils.MixedGamma;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Represents a healthcare facility agent in the disease transmission simulation.
 * <p>
 * {@code Facility} is the primary container for managing patient agents, disease outbreaks,
 * and healthcare operations. It tracks patient admissions, discharges, disease transmission,
 * and surveillance testing within a single facility. The facility operates within a {@link Region}
 * that coordinates multi-facility interactions if needed.
 * Key responsibilities include:
 * </p>
 * <ul>
 *   <li>Managing patient admission and discharge lifecycle</li>
 *   <li>Maintaining disease outbreak tracking via {@link FacilityOutbreak} agents</li>
 *   <li>Updating transmission rates based on current patient population</li>
 *   <li>Recording surveillance testing and clinical detection events</li>
 *   <li>Tracking statistics (patient days, admissions, population averages)</li>
 * </ul>
 * <p>
 * The facility is configured with parameters including isolation effectiveness, surveillance
 * adherence rates, and mean length of stay distributions specific to facility type.
 * </p>
 *
 * @author [Project Team]
 * @version 1.0
 * @see Region
 * @see FacilityOutbreak
 * @see Person
 * @see Disease
 */
public class Facility extends AgentContainer{


    private static final long serialVersionUID = -758171564017677907L;
	//private int currentPopulationSize = 0;
	private double betaIsolationReduction;
	private double timeBetweenMidstaySurveillanceTests = -1.0;
	private boolean onActiveSurveillance = false;
	private int type;
	private Region region;
	private double newPatientAdmissionRate;
	private double avgPopTarget;
	private double meanLOS;
	private double avgPopulation;
	private int numDaysTallied = 0;
	private double patientDays;
	private int numAdmissions = 0;
	private ExponentialDistribution distro;
	private ISchedule schedule;
	private ISchedulableAction nextAction;
	private ArrayList<FacilityOutbreak> outbreaks = new ArrayList<>();
	private LinkedList<Person> currentPatients = new LinkedList<>();
	private boolean stop = false;
	private double meanIntraEventTime;
	private int capacity;
	private double isolationEffectiveness;
	private int totalAdmissions;
	private int totalImports;
	private PrintWriter admissionsWriter;
	public boolean importation;
	private Parameters params = repast.simphony.engine.environment.RunEnvironment.getInstance().getParameters();
	


	// LOS distribution parameters (set by builder to avoid re-reading Parameters here)
	private double shape1;
	private double scale1;
	private double shape2;
	private double scale2;
	private double prob1;
	// Mixed LOS distribution
	private MixedGamma losDistro;
    
    // Constructor

	public Facility() {
		super();
		schedule = repast.simphony.engine.environment.RunEnvironment.getInstance().getCurrentSchedule();
		params = repast.simphony.engine.environment.RunEnvironment.getInstance().getParameters();
		region = new Region(this);
		try {
			if(!SingleFacilityBuilder.isBatchRun) {
            admissionsWriter = new PrintWriter("admissions.txt");
            admissionsWriter.println("time,patientid,importation");
			}
        }
	 catch (FileNotFoundException e) {
            e.printStackTrace();
        }
	}

	/**
	 * Creates and admits a new patient to this facility.
	 * Generates a new {@link Person} agent with diseases and calls {@link #admitPatient(Person)}
	 * to integrate the patient into the facility. Increments the total admission counter.
	 *
	 * @param sched the simulation schedule (currently unused but retained for API compatibility)
	 */
	public void admitNewPatient(ISchedule sched) {

		Person newPatient = new Person(this);
		admitPatient(newPatient);
		totalAdmissions++;
	}

	/**
	 * Admits an existing patient to this facility.
	 * Integrates a patient into facility operations by:
	 * <ul>
	 *   <li>Adding the patient to the facility's region and patient list</li>
	 *   <li>Starting a discharge timer based on random length of stay</li>
	 *   <li>Testing for colonized diseases via surveillance (if active surveillance enabled)</li>
	 *   <li>Starting clinical detection timers for any colonized diseases</li>
	 *   <li>Scheduling periodic surveillance tests (if enabled)</li>
	 *   <li>Updating transmission rate contributions</li>
	 *   <li>Recording admission statistics (if in measurement period)</li>
	 * </ul>
	 *
	 * @param p the patient to admit
	 */
	public void admitPatient(Person p){
		double admissionSurveillanceAdherence = params.getDouble("admissionSurveillanceAdherence");
		region.importToFacilityNew(this,p);
	    
		logPatientAdmission(schedule.getTickCount(), p.hashCode(), (boolean) p.getProperty("importation"));
		p.admitToFacility(this);
		

		p.startDischargeTimer(getRandomLOS());

		

		for(PersonDisease pd : p.getDiseases()){
			if(pd.isColonized()){
				
				if(pd.getDisease().isActiveSurveillanceAgent() && onActiveSurveillance){
					if(uniform() < pd.getDisease().getProbSurveillanceDetection() * admissionSurveillanceAdherence){
						pd.setDetected(true);
						pd.setDetectedBySurveillance();
						if(pd.getDisease().isolatePatientWhenDetected()) p.isolate();
					}
				}
				pd.startClinicalDetectionTimer();
			}
		}
		getCurrentPatients().add(p);
		getRegion().getPeople().add(p);

		if(onActiveSurveillance && !p.isIsolated() && getTimeBetweenMidstaySurveillanceTests() > 0)
			
			p.doSurveillanceTest();
			p.startNextPeriodicSurveillanceTimer();

		p.updateAllTransmissionRateContributions();

		if(!getRegion().isInBurnInPeriod()) updateAdmissionTally(p);
	}
	/**
	 * Discharges a patient from this facility.
	 * Removes a patient from active operations by:
	 * <ul>
	 *   <li>Removing the patient from the facility's region and patient list</li>
	 *   <li>Recording the discharge time</li>
	 *   <li>Creating a {@link agents.DischargedPatient} record (if in measurement period)</li>
	 *   <li>Updating stay statistics for tracking patient-days (if in measurement period)</li>
	 *   <li>Canceling all scheduled events for the patient</li>
	 *   <li>Removing the patient from the Repast context for garbage collection</li>
	 *   <li>Updating transmission rates across all disease outbreaks</li>
	 * </ul>
	 *
	 * @param p the patient to discharge
	 */
	public void dischargePatient(Person p){
		region.people.remove(p);


		getCurrentPatients().remove(p);
		updateTransmissionRate();
		SingleFacilityBuilder builder = getSimulationBuilder();
		p.setDischargeTime(TimeUtils.getSchedule().getTickCount());
		if (!region.isInBurnInPeriod()) {
			builder.dischargedPatients.add(new agents.DischargedPatient(p));
		}
		if(!getRegion().isInBurnInPeriod()) updateStayTally(p);
		p.destroyMyself(getRegion());
		// Remove from Repast context to allow dereferencing and garbage collection
		if (builder.getContext() != null) {
			builder.getContext().remove(p);
		}
		p.setNoMoreEvents(true);
	}

	/**
	 * Updates the transmission rate for all disease outbreaks in this facility.
	 * <p>
	 * Recalculates transmission rates across all tracked {@link FacilityOutbreak} agents
	 * based on the current patient population and disease states. Called when the patient
	 * population changes (admission or discharge).
	 * </p>
	 */
	public void updateTransmissionRate(){
		for(FacilityOutbreak fo : outbreaks) fo.updateTransmissionRate(region);
	}

	/**
	 * Generates a random length of stay (LOS) for a patient based on facility type.
	 * <p>
	 * For type 0 facilities (long-term acute care), uses a mixture of two gamma distributions
	 * to model realistic LOS patterns. Other facility types return -1.0.
	 * </p>
	 *
	 * @return the random length of stay in days, or -1.0 if facility type is not recognized
	 */
	public double getRandomLOS(){
		if(getType()==0){
			if (losDistro == null) {
				losDistro = new MixedGamma(shape1, scale1, shape2, scale2, prob1);
			}
			return losDistro.sample();
		}
		else{
			return -1.0;
		}
	}

	/**
	 * Admits an initial patient during the facility setup phase (burn-in period).
	 * <p>
	 * Similar to {@link #admitPatient(Person)} but without surveillance testing or
	 * admission statistics tracking, as these patients are used to establish a stable
	 * population during the burn-in period. The patient's discharge is scheduled based
	 * on the facility's mean length of stay.
	 * </p>
	 *
	 * @param p the initial patient to admit
	 */
	public void admitInitialPatient(Person p){
		p.admitToFacility(this);
		p.startDischargeTimer(exponential(1.0/getMeanLOS()));

		region.people.add(p);

		if(onActiveSurveillance) {
		}

		for(PersonDisease pd : p.getDiseases()){
			if(pd.isColonized()){
				pd.startClinicalDetectionTimer();
			}
		}
		getCurrentPatients().add(p);

		p.updateAllTransmissionRateContributions();
	}

	/**
	 * Updates the daily population statistics for this facility.
	 * <p>
	 * Calculates the running average population size and updates prevalence tallies
	 * for each disease outbreak. Called once per simulation day.
	 * </p>
	 */
	public void updatePopulationTally(){
		avgPopulation = (avgPopulation * numDaysTallied + region.people.size() / (numDaysTallied + 1));
		numDaysTallied++;

		for(FacilityOutbreak fo : outbreaks) {
			fo.updatePrevalenceTally();
		}

	}

	/**
	 * Updates statistics tracking patient-days for a discharged patient.
	 * <p>
	 * Increments the total patient-days counter and updates outbreak-specific
	 * stay statistics for each disease tracked by the facility.
	 * </p>
	 *
	 * @param p the discharged patient
	 */
	public void updateStayTally(Person p){
		setPatientDays(getPatientDays() + p.getCurrentLOS());


		if(!outbreaks.isEmpty()&&!p.personDiseases.isEmpty()) {
		for(int i=0; i<outbreaks.size(); i++) {
			outbreaks.get(i).updateStayTally(p.personDiseases.get(i));
			}
		}
	}

	/**
	 * Updates statistics tracking admissions and disease states.
	 * <p>
	 * Increments the admission counter and updates outbreak-specific admission tallies
	 * for each disease tracked by the facility. Admission statistics are used to
	 * calculate disease importation rates and endemic transmission patterns.
	 * </p>
	 *
	 * @param p the admitted patient
	 */
	public void updateAdmissionTally(Person p){
	    // Jan 10, 2025 WRR: it's appropriate to count the number of admissions in an int...
	    // because it's a total count of all the admissions EVER, and most of them have been
	    // discharged.  Things like current population size, or percentage of patients colonized
	    // should wherever possible be calculated from the relevant collection of "live" patients
		numAdmissions++;

		if(!outbreaks.isEmpty()&&!p.personDiseases.isEmpty()) {
			for(int i=0; i<outbreaks.size(); i++) {
				outbreaks.get(i).updateAdmissionTally(p.personDiseases.get(i));
			}
		}
	}

	/**
	 * Activates active surveillance testing in this facility.
	 * <p>
	 * Sets the flag to enable surveillance-based disease detection for all patients
	 * after the burn-in period ends.
	 * </p>
	 */
	public void startActiveSurveillance(){
		onActiveSurveillance = true;
	}

	/**
	 * Generates a uniform random number between 0 and 1.
	 *
	 * @return a random double in the range [0, 1)
	 */
	public double uniform() {
		return Math.random();
	}

	/**
	 * Samples from a gamma distribution with the specified shape and scale parameters.
	 *
	 * @param shape the shape parameter (k)
	 * @param scale the scale parameter (θ)
	 * @return a random sample from the gamma distribution
	 */
	public double gamma(double shape, double scale) {
		GammaDistribution gammaDistribution = new GammaDistribution(shape, scale);
		return gammaDistribution.sample();
	}

	/**
	 * Samples from an exponential distribution with the specified rate parameter.
	 *
	 * @param rate the rate parameter (λ), where mean = 1/λ
	 * @return a random sample from the exponential distribution
	 */
	public double exponential(double rate) {
		ExponentialDistribution exponentialDistribution = new ExponentialDistribution(rate);
		return exponentialDistribution.sample();
	}

	/**
	 * Creates and registers a new disease outbreak for this facility.
	 * <p>
	 * Instantiates a {@link FacilityOutbreak} agent for tracking disease transmission
	 * dynamics for a specific disease type.
	 * </p>
	 *
	 * @param d the disease to track
	 * @return the new FacilityOutbreak agent
	 */
	public FacilityOutbreak addOutbreaks(Disease d) {
		FacilityOutbreak newOutbreak = new FacilityOutbreak(meanIntraEventTime, d);
		newOutbreak.setFacility(this);

		outbreaks.add(newOutbreak);

		return newOutbreak;
	}
	public int getType() {
		return type;
	}

	public void addOutbreak(FacilityOutbreak outbreak) {
		outbreaks.add(outbreak);
	}

	public int getCapacity() {
		return this.capacity;
	}

	public void setIsolationEffectiveness(double isolationEffectiveness) {
		this.isolationEffectiveness = isolationEffectiveness;
	}

    public void setShape1(double shape1) { this.shape1 = shape1; }
    public void setScale1(double scale1) { this.scale1 = scale1; }
    public void setShape2(double shape2) { this.shape2 = shape2; }
    public void setScale2(double scale2) { this.scale2 = scale2; }
    public void setProb1(double prob1) { this.prob1 = prob1; }
    public void setLOSParams(double shape1, double scale1, double shape2, double scale2, double prob1) {
        this.shape1 = shape1;
        this.scale1 = scale1;
        this.shape2 = shape2;
        this.scale2 = scale2;
        this.prob1 = prob1;
        this.losDistro = new MixedGamma(shape1, scale1, shape2, scale2, prob1);
    }
    public double getShape1() { return shape1; }
    public double getScale1() { return scale1; }
    public double getShape2() { return shape2; }
    public double getScale2() { return scale2; }
    public double getProb1() { return prob1; }

	public int getPopulationSize() {
		return getCurrentPatients().size();
	}

	public double getTimeBetweenMidstaySurveillanceTests() {
	    return timeBetweenMidstaySurveillanceTests;
	}

	public void setTimeBetweenMidstaySurveillanceTests(double timeBetweenMidstaySurveillanceTests) {
	    this.timeBetweenMidstaySurveillanceTests = timeBetweenMidstaySurveillanceTests;
	}

	public double getMidstaySurveillanceAdherence() {
		double  midstaySurveillanceAdherence = params.getDouble("midstaySurveillanceAdherence");
		
	    return midstaySurveillanceAdherence;
	}

	public void setType(int type) {
	    this.type = type;
	}

	public double getAvgPopTarget() {
	    return avgPopTarget;
	}

	public void setAvgPopTarget(double avgPopTarget) {
	    this.avgPopTarget = avgPopTarget;
	}

	public double getMeanLOS() {
	    return meanLOS;
	}

	public void setMeanLOS(double meanLOS) {
	    this.meanLOS = meanLOS;
	}

	public double getBetaIsolationReduction() {
	    return betaIsolationReduction;
	}

	public void setBetaIsolationReduction(double betaIsolationReduction) {
	    this.betaIsolationReduction = betaIsolationReduction;
	}

	public double getNewPatientAdmissionRate() {
	    return newPatientAdmissionRate;
	}

	public void setNewPatientAdmissionRate(double newPatientAdmissionRate) {
	    this.newPatientAdmissionRate = newPatientAdmissionRate;
	}

	public Region getRegion() {
	    return region;
	}

	public void setRegion(Region region) {
	    this.region = region;
	}

	public LinkedList<Person> getCurrentPatients() {
	    return currentPatients;
	}
	
	public int getCurrentPatientCount() {
	    return currentPatients.size();
	}

	public void setCurrentPatients(LinkedList<Person> currentPatients) {
	    this.currentPatients = currentPatients;
	}

	public double getPatientDays() {
	    return patientDays;
	}

	public void setPatientDays(double patientDays) {
	    this.patientDays = patientDays;
	}

	public int getNumAdmissions() {
	    return numAdmissions;
	}

	public void setNumAdmissions(int numAdmissions) {
	    this.numAdmissions = numAdmissions;
	}

	public ArrayList<FacilityOutbreak> getOutbreaks() {
		return outbreaks;
	}
	public void logPatientAdmission(double time, int patientID, boolean importation) {
		if(!SingleFacilityBuilder.isBatchRun) {
		
          admissionsWriter.printf("%.2f,%d,%b%n", time, patientID, importation);
		}
    }
    
    /**
     * Gets a reference to the root context in Repast Simphony.
     * This uses ContextUtils to get the context containing this facility object,
     * which should be the root context where the simulation builder was added.
     * 
     * @return the root context
     */
    @SuppressWarnings("unchecked")
    public Context<Object> getRootContext() {
        return ContextUtils.getContext(this);
    }
    
    /**
     * Gets the SingleFacilityBuilder from the root context.
     * This allows access to the main simulation controller and its methods/data.
     * 
     * @return the SingleFacilityBuilder instance, or null if not found
     */
    public SingleFacilityBuilder getSimulationBuilder() {
        Context<Object> rootContext = getRootContext();
        for (Object obj : rootContext) {
            if (obj instanceof SingleFacilityBuilder) {
                return (SingleFacilityBuilder) obj;
            }
        }
        return null;
    }
    
    /**
     * Alternative method to get the region from the root context.
     * This demonstrates how to find specific objects in the context hierarchy.
     * 
     * @return the Region instance, or null if not found
     */
    public Region getRegionFromContext() {
        Context<Object> rootContext = getRootContext();
        for (Object obj : rootContext) {
            if (obj instanceof Region) {
                return (Region) obj;
            }
        }
        return null;
    }
}

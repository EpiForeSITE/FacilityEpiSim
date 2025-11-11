package disease;

import agents.Person;
import repast.simphony.parameter.Parameters;

public class Disease {

	private int type;
	private int simIndex;
	private String diseaseName;
	private Parameters params = repast.simphony.engine.environment.RunEnvironment.getInstance().getParameters();
	


	public double getBaselineBetaValue(int facilityType){
		params = repast.simphony.engine.environment.RunEnvironment.getInstance().getParameters();
		
		double longTermAcuteCareBeta = params.getDouble("beta");
		double acuteCareBeta = longTermAcuteCareBeta;
		double betaReduction = (Double) params.getValue("betaReduction");
		double nursingHomeBeta = acuteCareBeta / betaReduction;

		double betaVal = 0.0;

		if(facilityType == 0) betaVal = longTermAcuteCareBeta;
		else if(facilityType == 1) betaVal = acuteCareBeta;
		//else if(facilityType == 2) betaVal = nursingHomeBeta;

		return betaVal;
	}

	public double getMeanTimeToClinicalDetection(int facilityType){
		params = repast.simphony.engine.environment.RunEnvironment.getInstance().getParameters();

		double acuteCareMean = (Double) params.getValue("meanDetectionTime");
		//double nhChangeFactor = (Double) params.getValue("nursingHomeDetectionTimeFactor");
		//double nursingHomeMean = acuteCareMean * nhChangeFactor;

		double t = 0.0;

		if(facilityType == 0) t = acuteCareMean;
		else if(facilityType == 1) t = acuteCareMean;
		//else if(facilityType == 2) t = nursingHomeMean;

		return t;
	}


	public String getDiseaseName(){
		params = repast.simphony.engine.environment.RunEnvironment.getInstance().getParameters();
		return (String) params.getValue("diseaseName");
	}

	public double getAvgDecolonizationTime(){
		params = repast.simphony.engine.environment.RunEnvironment.getInstance().getParameters();
		return (Double) params.getValue("avgDecolonizationTime");
	}

	public double getProbSurveillanceDetection(){
		params = repast.simphony.engine.environment.RunEnvironment.getInstance().getParameters();
		return (Double) params.getValue("probSurveillanceDetection");
	}

	public boolean allowImportationsDuringBurnIn(){
		params = repast.simphony.engine.environment.RunEnvironment.getInstance().getParameters();
		return (Boolean) params.getValue("allowImportationsDuringBurnIn");
	}

	public boolean isolatePatientWhenDetected(){
		params = repast.simphony.engine.environment.RunEnvironment.getInstance().getParameters();
		return (Boolean) params.getValue("isolatePatientWhenDetected");
	}

	public boolean isActiveSurveillanceAgent(){
		params = repast.simphony.engine.environment.RunEnvironment.getInstance().getParameters();
		return (Boolean) params.getValue("isActiveSurveillanceAgent");
	}

	public double getImportationProb(){
	    params = repast.simphony.engine.environment.RunEnvironment.getInstance().getParameters();
		return (Double) params.getValue("importationRate");
	}
	public int getSimIndex() {
		return simIndex;
	}

	public void setType(int diseaseType) {
		type = diseaseType;
	}

	public void setSimIndex(int simIndex) {
	    this.simIndex = simIndex;
	}

	public void setDiseaseName(String diseaseName) {
	    this.diseaseName = diseaseName;
	}
}
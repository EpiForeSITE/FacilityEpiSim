package agents;

import org.apache.commons.math3.distribution.ExponentialDistribution;

import builders.SingleFacilityBuilder;
import repast.simphony.context.Context;
import repast.simphony.engine.schedule.ISchedulableAction;
import repast.simphony.engine.schedule.ISchedule;
import repast.simphony.util.ContextUtils;

abstract class Agent {
    public static int idCounter = 0;
    protected int id;
	

	Agent() {
	    this.id = idCounter++;
		
	}


	@Override
	public int hashCode() {
	    // TODO Auto-generated method stub
	    return this.id;
		    }
	
	


	
}

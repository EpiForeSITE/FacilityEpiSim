package agentcontainers;

import java.util.ArrayList;

/**
 * Abstract base class for containers that hold agent objects.
 * <p>
 * {@code AgentContainer} extends {@link ArrayList} to provide a typed collection
 * for managing agents in the simulation. Subclasses should implement specific
 * agent management logic and define the types of agents they contain.
 * </p>
 * <p>
 * This class serves as a foundation for creating specialized agent containers
 * within the Repast Simphony simulation framework, allowing for organized
 * management of agent populations and their interactions.
 * </p>
 *
 * @author [Project Team]
 * @version 1.0
 * @see ArrayList
 */
abstract class AgentContainer extends ArrayList<Object>{

	/**
	 * Constructs an empty {@code AgentContainer}.
	 */
	AgentContainer() {



	}


}

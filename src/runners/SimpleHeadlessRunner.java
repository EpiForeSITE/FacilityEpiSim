package runners;

import java.io.File;

import repast.simphony.batch.BatchScenarioLoader;
import repast.simphony.engine.controller.Controller;
import repast.simphony.engine.controller.DefaultController;
import repast.simphony.engine.environment.AbstractRunner;
import repast.simphony.engine.environment.ControllerRegistry;
import repast.simphony.engine.environment.DefaultRunEnvironmentBuilder;
import repast.simphony.engine.environment.RunEnvironmentBuilder;
import repast.simphony.batch.BatchScheduleRunner;
import repast.simphony.parameter.Parameters;
import repast.simphony.parameter.ParametersCreator;
import repast.simphony.scenario.ScenarioLoadException;

/**
 * Simple headless runner for single simulations
 */
public class SimpleHeadlessRunner {
    
    public static void main(String[] args) throws Exception {
        if (args.length < 1) {
            System.err.println("Usage: SimpleHeadlessRunner <scenario_dir>");
            System.exit(1);
        }
        
        String scenarioDir = args[0];
        File scenario = new File(scenarioDir);
        
        if (!scenario.exists()) {
            System.err.println("Scenario directory not found: " + scenarioDir);
            System.exit(1);
        }
        
        System.out.println("Loading scenario from: " + scenario.getAbsolutePath());
        
        // Create runner
        AbstractRunner scheduleRunner = new BatchScheduleRunner();
        RunEnvironmentBuilder runEnvironmentBuilder = new DefaultRunEnvironmentBuilder(scheduleRunner, true);
        Controller controller = new DefaultController(runEnvironmentBuilder);
        controller.setScheduleRunner(scheduleRunner);
        
        // Load scenario
        BatchScenarioLoader loader = new BatchScenarioLoader(scenario);
        ControllerRegistry registry = loader.load(runEnvironmentBuilder);
        controller.setControllerRegistry(registry);
        
        // Load parameters from the scenario's parameters.xml
        File paramFile = new File(scenario, "parameters.xml");
        Parameters params;
        if (paramFile.exists()) {
            System.out.println("Loading parameters from: " + paramFile.getAbsolutePath());
            repast.simphony.parameter.ParametersParser parser = new repast.simphony.parameter.ParametersParser(paramFile);
            params = parser.getParameters();
        } else {
            System.err.println("No parameters.xml found, creating default parameters");
            ParametersCreator creator = new ParametersCreator();
            creator.addParameter("randomSeed", Integer.class, (int) System.currentTimeMillis(), false);
            params = creator.createParameters();
        }
        
        System.out.println("Running simulation...");
        
        // Initialize and run
        controller.batchInitialize();
        controller.runParameterSetters(params);
        controller.runInitialize(params);
        controller.execute();
        controller.runCleanup();
        controller.batchCleanup();
        
        System.out.println("Simulation complete!");
    }
}

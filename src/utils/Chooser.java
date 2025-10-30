package utils;

import repast.simphony.random.RandomHelper;
import java.util.List;

/**
 * Utility class for random selection from lists based on probabilities.
 * <p>
 * Provides static methods for randomly choosing from a set of options,
 * either with uniform probability or weighted by density values.
 * </p>
 *
 * @author [Project Team]
 * @version 1.0
 */
public class Chooser {

	/**
	 * Randomly selects an option from a list based on weighted probabilities (density).
	 * <p>
	 * The density list should contain probability values that sum to 1.0.
	 * Each density value corresponds to the probability of selecting the option
	 * at that index in the options list.
	 * </p>
	 *
	 * @param options the list of options to choose from
	 * @param density the probability density for each option
	 * @return the randomly selected option
	 */
	public static Object choose(List<?> options, List<? extends Number> density) {
		double testValue = RandomHelper.getUniform().nextDoubleFromTo(0.0, 1.0);

		double cumulative = 0;
		int step = 0;
		int choiceStep = -1;

		for (Number d : density) {
			cumulative += d.doubleValue();
			if (testValue <= cumulative) {
				choiceStep = step;
				break;
			}
			step++;
		}

		return options.get(choiceStep);
	}

	/**
	 * Randomly selects one option from a list with uniform probability.
	 *
	 * @param options the list of options to choose from
	 * @return a randomly selected option
	 */
	public static Object chooseOne(List<?> options) {
		int index = RandomHelper.getUniform().nextIntFromTo(0, options.size() - 1);
		return options.get(index);
	}

	/**
	 * Randomly returns true or false with equal probability (50/50).
	 *
	 * @return a random boolean value
	 */
	public static boolean coinFlip() {
		List<Boolean> options = java.util.Arrays.asList(true, false);
		Object choice = chooseOne(options);
		return (Boolean) choice;
	}

	/**
	 * Returns true with probability {@code test}, false with probability {@code 1.0 - test}.
	 *
	 * @param test the probability of returning true (must be in range [0, 1])
	 * @return true with probability test, false otherwise
	 * @throws IllegalArgumentException if test is outside the range [0, 1]
	 */
	public static boolean randomTrue(Double test) {
		if (test < 0 || test > 1) {
			throw new IllegalArgumentException("value " + test + " out of bounds [0,1]");
		}
		List<Boolean> options = java.util.Arrays.asList(true, false);
		List<Double> density = java.util.Arrays.asList(test, 1.0 - test);
		Object choice = choose(options, density);
		return (Boolean) choice;
	}

}

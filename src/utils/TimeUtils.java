package utils;

import repast.simphony.engine.schedule.ISchedule;
import repast.simphony.engine.environment.RunEnvironment;

/**
 * Utility class for time conversion and schedule access in the simulation.
 * <p>
 * Provides constants for converting between simulation ticks and real-world time units,
 * and provides access to the Repast Simphony schedule.
 * </p>
 *
 * @author [Project Team]
 * @version 1.0
 */
public class TimeUtils {

	/** One day in simulation ticks */
	public static final double DAY = 1.0;

	/** One hour in simulation ticks */
	public static final double HOUR = DAY / 24.0;

	/** One minute in simulation ticks */
	public static final double MINUTE = HOUR / 60.0;

	/** One second in simulation ticks */
	public static final double SECOND = MINUTE / 60.0;

	/**
	 * Converts a simulation tick value to a human-readable time string.
	 * <p>
	 * Format: "Day X,H:MM" where X is the day number and H:MM is the hour and minute.
	 * </p>
	 *
	 * @param tick the simulation tick value
	 * @return a formatted time string
	 */
	public static String tickToTime(double tick) {
		double currTick = tick;
		int day = (int) currTick;
		currTick -= day;
		currTick = currTick / HOUR;
		int hour = (int) currTick;
		currTick -= hour;
		currTick = currTick * 60;
		int minute = (int) currTick;

		String minuteStr = String.valueOf(minute);
		if (minute < 10) {
			minuteStr = "0" + minuteStr;
		}

		return "Day " + day + "," + hour + ":" + minuteStr;
	}

	/**
	 * Gets the current simulation schedule from Repast Simphony.
	 *
	 * @return the current ISchedule instance
	 */
	public static ISchedule getSchedule() {
		return RunEnvironment.getInstance().getCurrentSchedule();
	}

}

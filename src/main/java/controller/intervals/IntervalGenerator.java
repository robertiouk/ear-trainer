package controller.intervals;

import controller.sound.Interval;

/**
 * Interface to define functionality for generating intervals.
 * 
 * @author Robertio
 *
 */
public interface IntervalGenerator {
	public Interval getNextInterval();
}

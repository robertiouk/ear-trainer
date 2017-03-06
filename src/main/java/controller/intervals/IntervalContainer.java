package controller.intervals;

import java.util.List;

import controller.sound.Interval;

/**
 * Holds a collection of Intervals.
 * 
 * @author Robertio
 *
 */
public interface IntervalContainer {
	public List<Interval> getIntervals();
	public void setIntervals(List<Interval> intervals);
}

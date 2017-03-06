package controller.intervals;

import java.util.List;
import java.util.Random;

import controller.Controller;
import controller.sound.Interval;
import controller.sound.SoundPlayer;

/**
 * Class to be used by GUI in order to handle generation and submission of Intervals.
 * 
 * @author Robertio
 *
 */
public class IntervalController implements IntervalContainer, IntervalGenerator, Controller {
	/** The list of intervals available. */
	private List<Interval> intervals = null;
	/** The current generated interval. */
	private Interval currentInterval = null;
	/** The sound player object. */
	private SoundPlayer soundPlayer = null;
	
	/**
	 * Constructor for IntervalController.
	 * 
	 * @param soundPlayer SoundPlayer
	 */
	public IntervalController(SoundPlayer soundPlayer) {
		this.soundPlayer = soundPlayer;
	}
	
	/**
	 * Randomly generates the next interval from the given list.
	 */
	@Override
	public Interval getNextInterval() {
		if (intervals == null) {
			return null;
		} else {
			currentInterval = pickRandomInterval();
			soundPlayer.playNewInterval(currentInterval);
			return currentInterval;
		}
	}

	@Override
	public List<Interval> getIntervals() {
		return intervals;
	}

	@Override
	public void setIntervals(List<Interval> intervals) {
		this.intervals = intervals;
	}
	
	/**
	 * Select an interval at random from the list.
	 * 
	 * @return Interval
	 */
	private Interval pickRandomInterval() {
		Interval interval = null;
		
		Random rand = new Random();
		int randomIndex = rand.nextInt(intervals.size());
		interval = intervals.get(randomIndex);
		
		return interval;
	}

	/**
	 * Submit an Interval for comparison against generated one.
	 * 
	 * @param interval Interval object to compare.
	 * @return True if correct, false if not.
	 */
	@Override
	public boolean submit(Enum<?> sound) {
		if (currentInterval == null) {
			return false;
		} else {
			return currentInterval.equals(sound);
		}
	}

	/**
	 * Triggers the sound player to play the current Interval.
	 */
	@Override
	public void play() {
		soundPlayer.playInterval(currentInterval);	
	}

	@Override
	public void updateSoundSettings() {
		soundPlayer.updateIntervalSettings();
	}
}

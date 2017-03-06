package controller.intervals;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.junit.Before;
import org.junit.Test;

import controller.sound.Interval;
import controller.sound.SoundPlayer;

public class TestIntervalController {
	private IntervalController intervalController = null;
	private SoundPlayer soundPlayer = null;
	
	@Before
	public void setup() {
		soundPlayer = mock(SoundPlayer.class);
		intervalController = new IntervalController(soundPlayer);
	}

	/**
	 * Build a random list of Intervals and pass to object.
	 * Generate 100 Intervals and check each one exists in
	 * original list.
	 */
	@Test
	public void testGenerateInterval() {
		// Build random list.
		List<Interval> intervals = buildMockIntervalList();
		intervalController.setIntervals(intervals);
		
		// Generate 100 intervals
		for (int i = 0; i < 100; ++i) {
			Interval currentInterval = intervalController.getNextInterval();
			
			assertTrue("Generated Interval does not exist in list", intervals.contains(currentInterval));
			verify(soundPlayer).playNewInterval(currentInterval);
			reset(soundPlayer);
		}
	}
	
	/**
	 * Pass a list of intervals to the interface. Should return the same list.
	 */
	@Test
	public void testSetGetIntervals() {
		List<Interval> intervalList = buildMockIntervalList();
		
		// Pass list
		intervalController.setIntervals(intervalList);
		
		List<Interval> targetList = intervalController.getIntervals();
		
		// Compare lists
		assertEquals("Lists are not the same size", intervalList.size(), targetList.size());
		assertTrue("Target list does not contain original list", targetList.containsAll(intervalList));
	}
	
	/**
	 * 1. Generate a new interval and submit. 
	 * 2. Assert submission returns true.
	 * 3. Generate a new interval and submit different interval.
	 * 4. Assert submission returns false. 
	 */
	@Test
	public void testSubmitInterval() {
		if (intervalController.getIntervals() == null) {
			intervalController.setIntervals(buildMockIntervalList());
		}
		
		// Generate next interval
		Interval interval = intervalController.getNextInterval();
		assertTrue(intervalController.submit(interval));
		
		// Now submit wrong interval
		interval = intervalController.getNextInterval();
		Interval wrongInterval = interval;
		while (wrongInterval.equals(interval)) {
			wrongInterval = getRandomInterval();
		}
		assertFalse(intervalController.submit(wrongInterval));
	}
	
	/**
	 * Test that the controller plays the interval when requested.
	 */
	@Test
	public void testPlayInterval() {
		reset(soundPlayer);
		
		if (intervalController.getIntervals() == null) {
			intervalController.setIntervals(buildMockIntervalList());
		}
		
		// Generate next interval
		Interval interval = intervalController.getNextInterval();
		
		intervalController.play();
		
		// Verify the interval was played
		verify(soundPlayer, times(1)).playNewInterval(interval);
		verify(soundPlayer, times(1)).playInterval(interval);
	}
	
	/**
	 * 1. Update sound settings.
	 * 2. Assert updateIntervalSettings was called on the sound player.
	 */
	@Test
	public void testUpdateSoundSettings() {
		IntervalController controller = new IntervalController(soundPlayer);
		
		controller.updateSoundSettings();
		
		verify(soundPlayer, times(1)).updateIntervalSettings();
	}

	/**
	 * Mock a list of 20 Intervals.
	 * 
	 * @return List<Interval>
	 */
	private List<Interval> buildMockIntervalList() {
		List<Interval> intervalList = new ArrayList<Interval>();
		// Create 20 intervals
		for (int i = 0; i < 20; ++i) {
			intervalList.add(getRandomInterval());
		}
		return intervalList;
	}
	
	/**
	 * Randomly select an Interval.
	 * 
	 * @return Interval.
	 */
	private Interval getRandomInterval() {
		Interval interval = null;
		
		Random rand = new Random();
		interval = Interval.values()[(rand.nextInt(Interval.values().length))];
		
		return interval;
	}
}

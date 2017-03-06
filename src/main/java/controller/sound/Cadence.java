package controller.sound;

import static java.util.Arrays.asList;

import java.util.List;

/**
 * Enumeration for Cadences. Each cadence holds a Map<Interval, Chord> that determines
 * the chords that make up the cadence.
 * 
 * @author Robertio
 *
 */
public enum Cadence {
	Plagal(asList(Interval.Perfect4th, Interval.Unison), asList(Chord.Major, Chord.Major)),
	Interrupted(asList(Interval.Perfect5th, Interval.Major6th), asList(Chord.Major, Chord.Minor)),
	Perfect(asList(Interval.Perfect5th, Interval.Unison), asList(Chord.Major, Chord.Major)),
	Imperfect(asList(Interval.Unison, Interval.Perfect5th), asList(Chord.Major, Chord.Major));
	
	
	private List<Interval> chordIntervals = null;
	private List<Chord> keyChords = null;
	private Cadence(List<Interval> intervals, List<Chord> keyChords) {
		this.chordIntervals = intervals;
		this.keyChords = keyChords;
	}
	public List<Chord> getKeyChords() {
		return keyChords;
	}	
	public List<Interval> getChordIntervals() {
		return chordIntervals;
	}
}

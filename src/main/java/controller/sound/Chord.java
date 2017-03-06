package controller.sound;

import static java.util.Arrays.asList;

import java.util.List;

/**
 * An enumeration for Chord types, which contain a list of Intervals
 * that make up the chord.
 * 
 * @author Robertio
 *
 */
public enum Chord {
	Major(asList(Interval.Major3rd, Interval.Perfect5th)),
	Minor(asList(Interval.Minor3rd, Interval.Perfect5th)),
	Suspended4(asList(Interval.Perfect4th, Interval.Perfect5th)),
	Augmented5(asList(Interval.Major3rd, Interval.Minor6th)),
	Minor6(asList(Interval.Minor3rd, Interval.Perfect5th, Interval.Major6th)),
	Major6(asList(Interval.Major3rd, Interval.Perfect5th, Interval.Major6th)),
	Minor7(asList(Interval.Minor3rd, Interval.Perfect5th, Interval.Minor7th)),
	Minor7b5(asList(Interval.Minor3rd, Interval.Diminished5th, Interval.Minor7th)),
	Minor7Sharp5(asList(Interval.Minor3rd, Interval.Minor6th, Interval.Minor7th)),
	Dominant7(asList(Interval.Major3rd, Interval.Perfect5th, Interval.Minor7th)),
	Dominant7b5(asList(Interval.Major3rd, Interval.Diminished5th, Interval.Minor7th)),
	Dominant7Sharp5(asList(Interval.Major3rd, Interval.Minor6th, Interval.Minor7th)),
	Major7(asList(Interval.Major3rd, Interval.Perfect5th, Interval.Major7th)),
	Diminished7(asList(Interval.Minor3rd, Interval.Diminished5th, Interval.Major6th /*<-bb7*/)),
	Minor9(asList(Interval.Minor3rd, Interval.Perfect5th, Interval.Minor7th, Interval.Major9th)),
	Minor7b9(asList(Interval.Minor3rd, Interval.Perfect5th, Interval.Minor7th, Interval.Minor9th)),
	Minor7b5b9(asList(Interval.Minor3rd, Interval.Diminished5th, Interval.Minor7th, Interval.Minor9th)),
	Minor7Sharp5b9(asList(Interval.Minor3rd, Interval.Minor6th, Interval.Minor7th, Interval.Minor9th)),
	Minor9b5(asList(Interval.Minor3rd, Interval.Diminished5th, Interval.Minor7th, Interval.Major9th)),
	Minor9Sharp5(asList(Interval.Minor3rd, Interval.Minor6th, Interval.Minor7th, Interval.Major9th)),
	Dominant9(asList(Interval.Major3rd, Interval.Perfect5th, Interval.Minor7th, Interval.Major9th)),
	Dominant7b9(asList(Interval.Major3rd, Interval.Perfect5th, Interval.Minor7th, Interval.Minor9th)),
	Dominant7Sharp9(asList(Interval.Major3rd, Interval.Perfect5th, Interval.Minor7th, Interval.Augmented9th)),
	Dominant7b5b9(asList(Interval.Major3rd, Interval.Diminished5th, Interval.Minor7th, Interval.Minor9th)),
	Dominant7b5Sharp9(asList(Interval.Major3rd, Interval.Diminished5th, Interval.Minor7th, Interval.Augmented9th)),
	Dominant7Sharp5b9(asList(Interval.Major3rd, Interval.Minor6th, Interval.Minor7th, Interval.Minor9th)),
	Dominant7Sharp5Sharp9(asList(Interval.Major3rd, Interval.Minor6th, Interval.Minor7th, Interval.Augmented9th)),
	Dominant9b5(asList(Interval.Major3rd, Interval.Diminished5th, Interval.Minor7th, Interval.Major9th)),
	Dominant9Sharp5(asList(Interval.Major3rd, Interval.Minor6th, Interval.Minor7th, Interval.Major9th)),
	Major9(asList(Interval.Major3rd, Interval.Perfect5th, Interval.Major7th, Interval.Major9th)),
	Dominant11(asList(Interval.Major3rd, Interval.Perfect5th, Interval.Minor7th, Interval.Major9th, Interval.Perfect11th)),
	Dominant13(asList(Interval.Major3rd, Interval.Perfect5th, Interval.Minor7th, Interval.Major9th, Interval.Perfect11th, Interval.Major13th));
	
	private List<Interval> intervals = null;
	private Chord(List<Interval> intervals) {
		this.intervals = intervals;
	}
	public List<Interval> getIntervals() {
		return intervals;
	}
}

package controller.sound;

/**
 * Enumeration to contain the interval type
 * 
 * @author Robertio
 *
 */
public enum Interval {
	Unison(0),
	Minor2nd(1),
	Major2nd(2),
	Minor3rd(3),
	Major3rd(4),
	Perfect4th(5),
	Diminished5th(6),
	Perfect5th(7),
	Minor6th(8),
	Major6th(9),
	Minor7th(10),
	Major7th(11),
	Octave(12),
	Minor9th(13),
	Major9th(14),
	Augmented9th(15),
	Major10th(16),
	Perfect11th(17),
	Major13th(21);

	private int value;

    private Interval(int value) {
            this.value = value;
    }
    
    public int getValue() {
    	return value;
    }
}

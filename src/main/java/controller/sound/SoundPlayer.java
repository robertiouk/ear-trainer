package controller.sound;



/**
 * This interface defines behaviour to play sound
 * 
 * @author Robertio
 *
 */
public interface SoundPlayer {
	public void playInterval(Interval interval);
	public void playNewInterval(Interval interval);
	public void playCadence(Cadence cadence);
	public void playNewCadence(Cadence cadence);
	public void playChord(Chord chord);
	public void playNewChord(Chord chord);
	public void updateIntervalSettings();
	public void updateChordSettings();
	public void updateCadenceSettings();
	public void updateGeneralSettings();
}

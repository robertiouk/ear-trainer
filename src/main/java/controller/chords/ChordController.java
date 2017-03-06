package controller.chords;

import java.util.List;
import java.util.Random;

import controller.Controller;
import controller.sound.Chord;
import controller.sound.SoundPlayer;

/**
 * Class used by GUI to generate random chords from a given list. Receives chord
 * 'submissions' and compares against the last generated chord.
 * 
 * @author Robertio
 *
 */
public class ChordController implements ChordContainer, ChordGenerator, Controller {
	/** The sound player for this controller. */
	private SoundPlayer soundPlayer = null;
	/** The list of chords to choose from. */
	private List<Chord> chordList = null;
	/** The current generated chord. */
	private Chord currentChord = null;
	
	/**
	 * Constructor for ChordController.
	 * 
	 * @param soundPlayer SoundPlayer implementation.
	 */
	public ChordController(SoundPlayer soundPlayer) {
		this.soundPlayer = soundPlayer;
	}
	
	/**
	 * Randomly pick another chord from the given list and assign
	 * to currentChord.
	 */
	@Override
	public Chord getNextChord() {
		Random rand = new Random();
		int chordIndex = rand.nextInt(chordList.size());
		Chord chord = chordList.get(chordIndex);
		
		// Record and play it
		currentChord = chord;
		soundPlayer.playNewChord(chord);
		
		return chord;
	}

	/**
	 * Assign given list of Chords to chordList.
	 * 
	 * @param chords List<Chord>
	 */
	@Override
	public void setChords(List<Chord> chords) {
		this.chordList = chords;
	}

	/**
	 * Return the assigned list of Chords.
	 * 
	 * @return List<Chord>
	 */
	@Override
	public List<Chord> getChords() {
		return chordList;
	}

	/**
	 * Compare the given chord against the generated one. If it
	 * matches then return true, otherwise return false.
	 * 
	 * @param chord Chord to compare.
	 * @return Boolean value that returns true if matches, false if not.
	 */
	@Override
	public boolean submit(Enum<?> sound) {
		if (currentChord == null) {
			return false;
		} else {
			return (currentChord.equals(sound)) ? true : false;
		}
	}

	/**
	 * Play the current generated chord.
	 */
	@Override
	public void play() {
		soundPlayer.playChord(currentChord);
	}

	@Override
	public void updateSoundSettings() {
		soundPlayer.updateChordSettings();
	}
}

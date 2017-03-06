package controller.chords;

import controller.sound.Chord;

/**
 * Interface for defining Chord generator behaviour.
 * 
 * @author Robertio
 *
 */
public interface ChordGenerator {
	public Chord getNextChord();
}

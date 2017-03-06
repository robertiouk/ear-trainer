package controller.chords;

import java.util.List;

import controller.sound.Chord;

/**
 * Interface defining behaviour for a Chord container.
 * 
 * @author Robertio
 *
 */
public interface ChordContainer {
	public void setChords(List<Chord> chords);
	public List<Chord> getChords();
}

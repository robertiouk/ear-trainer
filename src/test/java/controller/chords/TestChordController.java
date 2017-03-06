package controller.chords;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.junit.Before;
import org.junit.Test;

import controller.sound.Chord;
import controller.sound.SoundPlayer;

public class TestChordController {
	private SoundPlayer soundPlayer = null;
	
	@Before
	public void setup() {
		soundPlayer = mock(SoundPlayer.class);
	}

	/**
	 * 1. Pass the controller a list of chords.
	 * 2. Generate 1000 chords.
	 * 3. Assert the chords exist in the original list.
	 */
	@Test
	public void testGenerateChord() {
		ChordController controller = new ChordController(soundPlayer);
		
		// Get a random list of chords
		List<Chord> chordList = generateRandomChordList();
		controller.setChords(chordList);
		
		// Pass to chord list chord controller
		controller.setChords(chordList);
		
		// Generate 1000 chords
		for (int i = 0; i < 1000; ++i) {
			Chord chord = controller.getNextChord();
			
			// Assert the chord is in the list
			assertTrue("Generated chord is not in the list", chordList.contains(chord));
			// Verify that the chord was actually played
			verify(soundPlayer, atLeastOnce()).playNewChord(chord);
		}
	}

	/**
	 * 1. Build a list of random chords.
	 * 2. Pass to controller.
	 * 3. Get the chords from controller.
	 * 4. Assert the chords are the same.
	 */
	@Test
	public void testSetGetChords() {
		ChordController controller = new ChordController(soundPlayer);
		
		// Get a random list of chords
		List<Chord> chordList = generateRandomChordList();
		
		// Pass to chord controller
		controller.setChords(chordList);
		
		// Get chord list
		List<Chord> targetList = controller.getChords();
		
		// Assert the lists are the same
		assertEquals("Chord lists are not the same.", chordList, targetList);
	}
	
	/**
	 * 1. Pass list of random chords to controller.
	 * 2. Get controller to generate chord.
	 * 3. Submit same chord and assert result is true.
	 * 4. Submit different chord and assert result is false.
	 */
	@Test
	public void testSubmitChord() {
		ChordController controller = new ChordController(soundPlayer);
		
		// Get a list of random chords and pass to controller
		List<Chord> chordList = generateRandomChordList();
		controller.setChords(chordList);
		
		// Generate a chord
		Chord generatedChord = controller.getNextChord();
		
		// Get the wrong chord
		Random rand = new Random();
		Chord wrongChord = generatedChord;
		while (generatedChord.equals(wrongChord)) {
			int chordIndex = rand.nextInt(chordList.size()-1);
			wrongChord = chordList.get(chordIndex);
		}
		
		// Perform asserts
		assertTrue("Submitted same chord and returned false", controller.submit(generatedChord));
		assertFalse("Submitted different chord and returned true", controller.submit(wrongChord));
	}
	
	/**
	 * 1. Pass list of random chords to controller.
	 * 2. Get controller to generate chord.
	 * 4. Tell controller to play the chord.
	 * 5. Assert the chord was played at twice.
	 */
	@Test
	public void testPlayChord() {
		ChordController controller = new ChordController(soundPlayer);
		
		// Get a list of random chords and pass to controller
		List<Chord> chordList = generateRandomChordList();
		controller.setChords(chordList);
		
		// Generate chord and play it
		Chord generatedChord = controller.getNextChord();
		controller.play();
		
		// Chord should have been played
		verify(soundPlayer, times(1)).playChord(generatedChord);
		verify(soundPlayer, times(1)).playNewChord(generatedChord);
	}
	
	/**
	 * 1. Update sound settings.
	 * 2. Assert updateChordSettings was called on the sound player.
	 */
	@Test
	public void testUpdateSoundSettings() {
		ChordController controller = new ChordController(soundPlayer);
		
		controller.updateSoundSettings();
		
		verify(soundPlayer, times(1)).updateChordSettings();
	}
	
	/**
	 * Generate a random number for chords from the list of
	 * available chords.
	 * 
	 * @return List<Chord>
	 */
	private List<Chord> generateRandomChordList() {
		ArrayList<Chord> chordList = new ArrayList<Chord>();
		Random rand = new Random();
		int numberOfChords = rand.nextInt(Chord.values().length-3)+2;
		for (int i = 0; i < numberOfChords; ++i) {
			// Pick next chord
			int chordIndex = rand.nextInt(Chord.values().length-1);
			Chord chord = Chord.values()[chordIndex];
			while (chordList.contains(chord)) {
				chordIndex = rand.nextInt(Chord.values().length-1);
				chord = Chord.values()[chordIndex];
			}
			// Add to list
			chordList.add(chord);
		}
		
		return chordList;
	}
}

package controller.cadences;

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

import controller.sound.Cadence;
import controller.sound.SoundPlayer;

public class TestCadenceController {
	private SoundPlayer soundPlayer = null;
	
	@Before
	public void setup() {
		soundPlayer = mock(SoundPlayer.class);
	}

	/**
	 * 1. Pass the controller a list of cadences.
	 * 2. Generate 1000 cadences.
	 * 3. Assert the cadences exist in the original list.
	 */
	@Test
	public void testGenerateCadence() {
		CadenceController controller = new CadenceController(soundPlayer);
		
		// Get a random list of cadences
		List<Cadence> cadenceList = generateRandomCadenceList();
		
		// Pass to cadence list cadence controller
		controller.setCadences(cadenceList);
		
		// Generate 1000 cadences
		for (int i = 0; i < 1000; ++i) {
			Cadence cadence = controller.getNextCadence();
			
			// Assert the cadence is in the list
			assertTrue("Generated cadence is not in the list", cadenceList.contains(cadence));
			// Verify that the cadence was actually played
			verify(soundPlayer, atLeastOnce()).playNewCadence(cadence);
		}
	}

	/**
	 * 1. Build a list of random cadences.
	 * 2. Pass to controller.
	 * 3. Get the cadences from controller.
	 * 4. Assert the cadences are the same.
	 */
	@Test
	public void testSetGetCadences() {
		CadenceController controller = new CadenceController(soundPlayer);
		
		// Get a random list of cadences
		List<Cadence> cadenceList = generateRandomCadenceList();
		
		// Pass to cadence controller
		controller.setCadences(cadenceList);
		
		// Get cadence list
		List<Cadence> targetList = controller.getCadences();
		
		// Assert the lists are the same
		assertEquals("cadence lists are not the same.", cadenceList, targetList);
	}
	
	/**
	 * 1. Pass list of random cadences to controller.
	 * 2. Get controller to generate cadence.
	 * 3. Submit same cadence and assert result is true.
	 * 4. Submit different cadence and assert result is false.
	 */
	@Test
	public void testSubmitCadence() {
		CadenceController controller = new CadenceController(soundPlayer);
		
		// Get a list of random cadences and pass to controller
		List<Cadence> cadenceList = generateRandomCadenceList();
		controller.setCadences(cadenceList);
		
		// Generate a cadence
		Cadence generatedcadence = controller.getNextCadence();
		
		// Get the wrong cadence
		Random rand = new Random();
		Cadence wrongcadence = generatedcadence;
		while (generatedcadence.equals(wrongcadence)) {
			int cadenceIndex = rand.nextInt(Cadence.values().length-1);
			wrongcadence = cadenceList.get(cadenceIndex);
		}
		
		// Perform asserts
		assertTrue("Submitted same cadence and returned false", controller.submit(generatedcadence));
		assertFalse("Submitted different cadence and returned true", controller.submit(wrongcadence));
	}
	
	/**
	 * 1. Pass list of random cadences to controller.
	 * 2. Get controller to generate cadence.
	 * 4. Tell controller to play the cadence.
	 * 5. Assert the cadence was played at twice.
	 */
	@Test
	public void testPlayCadence() {
		CadenceController controller = new CadenceController(soundPlayer);
		
		// Get a list of random cadences and pass to controller
		List<Cadence> cadenceList = generateRandomCadenceList();
		controller.setCadences(cadenceList);
		
		// Generate cadence and play it
		Cadence generatedcadence = controller.getNextCadence();
		controller.play();
		
		// cadence should have been played
		verify(soundPlayer, times(1)).playNewCadence(generatedcadence);
		verify(soundPlayer, times(1)).playCadence(generatedcadence);
	}
	
	/**
	 * 1. Update sound settings.
	 * 2. Assert updateCadenceSettings was called on the sound player.
	 */
	@Test
	public void testUpdateSoundSettings() {
		CadenceController controller = new CadenceController(soundPlayer);
		
		controller.updateSoundSettings();
		
		verify(soundPlayer, times(1)).updateCadenceSettings();
	}
	
	/**
	 * Generate random cadences from the list of
	 * available cadences.
	 * 
	 * @return List<Cadence>
	 */
	private List<Cadence> generateRandomCadenceList() {
		ArrayList<Cadence> cadenceList = new ArrayList<Cadence>();
		Random rand = new Random();
		int numberOfcadences = 3;
		for (int i = 0; i < numberOfcadences; ++i) {
			// Pick next cadence
			int cadenceIndex = rand.nextInt(Cadence.values().length-1);
			Cadence cadence = Cadence.values()[cadenceIndex];
			while (cadenceList.contains(cadence)) {
				cadenceIndex = rand.nextInt(Cadence.values().length-1);
				cadence = Cadence.values()[cadenceIndex];
			}
			// Add to list
			cadenceList.add(cadence);
		}
		
		return cadenceList;
	}
}

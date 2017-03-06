package controller.cadences;

import java.util.List;
import java.util.Random;

import controller.Controller;
import controller.sound.Cadence;
import controller.sound.SoundPlayer;

/**
 * Class used by GUI to generate random cadences from a given list. Receives cadence
 * 'submissions' and compares against the last generated cadence.
 * 
 * @author Robertio
 *
 */
public class CadenceController implements CadenceContainer, CadenceGenerator, Controller {
	/** The SoundPlayer implementation. */
	private SoundPlayer soundPlayer = null;
	/** The list of Cadences to play. */
	private List<Cadence> cadenceList = null;
	/** The current generated cadence. */
	private Cadence currentCadence = null;
	
	/**
	 * Constructor for CadenceController.
	 * 
	 * @param soundPlayer SoundPlayer implementation.
	 */
	public CadenceController(SoundPlayer soundPlayer) {
		this.soundPlayer = soundPlayer;
	}
	
	/**
	 * Generate a random Cadence from cadenceList. Record
	 * the generated Cadence and play it.
	 */
	@Override
	public Cadence getNextCadence() {
		Random rand = new Random();
		int cadenceIndex = rand.nextInt(cadenceList.size());
		Cadence cadence = cadenceList.get(cadenceIndex);
		
		// Record it and play it
		currentCadence = cadence;
		soundPlayer.playNewCadence(cadence);
		
		return cadence;
	}

	/**
	 * Pass a given list of Cadences to cadenceList.
	 * 
	 * @param List<Cadence>
	 */
	@Override
	public void setCadences(List<Cadence> cadences) {
		this.cadenceList = cadences;
	}
	
	/**
	 * Return the given list of Cadences.
	 * 
	 *  @return List<Cadence>
	 */
	@Override
	public List<Cadence> getCadences() {
		return cadenceList;
	}
	
	/**
	 * Compare given Cadence against the current generated one.
	 * Return true if the same, false if not.
	 * 
	 * @param cadence The Cadence to compare.
	 * @return Boolean true if the same, false if not.
	 */
	@Override
	public boolean submit(Enum<?> sound) {
		if (currentCadence == null) {
			return false;
		} else {
			return (currentCadence.equals(sound)) ? true : false;
		}
	}

	/**
	 * Play the current Cadence.
	 */
	@Override
	public void play() {
		soundPlayer.playCadence(currentCadence);
	}

	@Override
	public void updateSoundSettings() {
		soundPlayer.updateCadenceSettings();
	}

}

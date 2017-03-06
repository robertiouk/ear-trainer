package client.menu;

import java.util.ArrayList;
import java.util.List;

import client.ClientImpl;
import client.command.ClientCommand;
import client.command.ClientType;
import controller.cadences.CadenceController;
import controller.sound.Cadence;

/**
 * Client to be passed to the Cadence GUI.
 * 
 * @author Robertio
 *
 */
public class CadenceClient extends ClientImpl {
	private CadenceController cadenceController;
	private Cadence currentCadence;
	
	public CadenceClient(CadenceController cadenceController) {
		this.cadenceController = cadenceController;

		applyCadences();
	}
	
	/**
	 * Give all cadences to the controller.
	 */
	private void applyCadences() {
		List<Cadence> cadences = new ArrayList<Cadence>();
		for (Cadence cadence : Cadence.values()) {
			cadences.add(cadence);
		}
		cadenceController.setCadences(cadences);
	}
	
	/**
	 * Create a command to exit the current form.
	 */
	@Override
	public void exit() {
		ClientCommand command = ClientCommand.CloseForm;
		ClientType type = ClientType.Cadence;
		sendCommand(command, type);
	}

	/**
	 * Submit a given cadence to the controller.
	 * 
	 * @param cadence Cadence
	 * @return Boolean Determines if given cadence is correct.
	 */
	public boolean submit(Cadence cadence) {
		return cadenceController.submit(cadence);
	}
	
	/**
	 * Get the cadence controller to play the current cadence. If
	 * no cadence has been selected then play a new cadence.
	 */
	public void play() {
		if (currentCadence == null) {
			getNextCadence();
		} else {
			cadenceController.play();
		}
	}
	
	/**
	 * Get the next cadence.
	 */
	public void getNextCadence() {
		currentCadence = cadenceController.getNextCadence();
	}
	
	/**
	 * Retrieve the list of cadences from the controller.
	 * 
	 * @return List<Cadence>
	 */
	public List<Cadence> getCadences() {
		return cadenceController.getCadences();
	}
	
	
	/**
	 * Update the sound settings on the controller.
	 */
	public void updateSettings() {
		cadenceController.updateSoundSettings();
	}
}

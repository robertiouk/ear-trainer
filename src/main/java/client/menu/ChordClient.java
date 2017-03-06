package client.menu;

import java.util.ArrayList;
import java.util.List;

import client.ClientImpl;
import client.command.ClientCommand;
import client.command.ClientType;
import controller.chords.ChordController;
import controller.settings.CompositeSetting;
import controller.settings.ControlSetting;
import controller.sound.Chord;
import data.ConfigController;

/**
 * Client for the Chord frame to be passed to the GUI.
 * 
 * @author Robertio
 *
 */
public class ChordClient extends ClientImpl implements SettingsControl {
	private static final String CHORD_SETTING = "chord";
	private static final String DEFAULT_SETTING = "medium";
	private ChordController chordController;
	private String currentSetting;
	private Chord currentChord;
	private CompositeSetting chordSettings;
	
	public ChordClient(ChordController chordController, ConfigController settings) {
		this.chordController = chordController;
		
		chordSettings = (CompositeSetting) settings.getSetting(CHORD_SETTING);
		applySettings((CompositeSetting) chordSettings.findSetting(DEFAULT_SETTING));
	}
	
	/**
	 * Create a command to exit the current form.
	 */
	@Override
	public void exit() {
		ClientCommand command = ClientCommand.CloseForm;
		ClientType type = ClientType.Chord;
		sendCommand(command, type);
	}

	/**
	 * Submit a given chord to the controller.
	 * 
	 * @param chord Chord
	 * @return Boolean Determines if given chord is correct.
	 */
	public boolean submit(Chord chord) {
		return chordController.submit(chord);
	}
	
	/**
	 * Get the chord controller to play the current chord. If
	 * no chord has been selected then play a new chord.
	 */
	public void play() {
		if (currentChord == null) {
			getNextChord();
		} else {
			chordController.play();
		}
	}
	
	/**
	 * Get the next chord.
	 */
	public void getNextChord() {
		currentChord = chordController.getNextChord();
	}
	
	/**
	 * Loop through given settings and pass as Chord list to the 
	 * controller.
	 * 
	 * @param settings CompositeSetting
	 */
	@Override
	public void applySettings(CompositeSetting settings) {
		currentSetting = settings.getName();
		// Loop through settings
		List<Chord> chordList = new ArrayList<Chord>();
		for (Chord chord: Chord.values()) {
			ControlSetting<?> setting = (ControlSetting<?>) settings.findSetting(chord.toString().toLowerCase());
			if ((Integer)setting.getValue() == 1) {
				chordList.add(chord);
			}
		}
		chordController.setChords(chordList);
	}
	
	/**
	 * Retrieve the list of chords from the controller.
	 * 
	 * @return List<Chord>
	 */
	public List<Chord> getChords() {
		return chordController.getChords();
	}

	/**
	 * Return the current setting.
	 */
	@Override
	public String getSetting() {
		return currentSetting;
	}

	/**
	 * Apply the given settings.
	 * 
	 * @param setting Setting.
	 */
	@Override
	public void setSetting(String setting) {
		applySettings((CompositeSetting) chordSettings.findSetting(setting));
	}

	/**
	 * Reload the current settings.
	 */
	@Override
	public void updateSettings() {
		applySettings((CompositeSetting) chordSettings.findSetting(currentSetting));	
		chordController.updateSoundSettings();
	}
}

package client.menu;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import client.ClientImpl;
import client.command.ClientCommand;
import client.command.ClientType;
import controller.intervals.IntervalController;
import controller.settings.CompositeSetting;
import controller.settings.ControlSetting;
import controller.sound.Interval;
import data.ConfigController;

/**
 * Client for the Interval to pass the GUI.
 * 
 * @author Robertio
 *
 */
public class IntervalClient extends ClientImpl implements SettingsControl {
	private static String DEFAULT_SETTING = "medium";
	private IntervalController intervalController;
	/** Map between setting name and Interval name. */
	private Map<String, String> settingLookup;
	private Interval currentInterval;
	private ConfigController settings;
	private String currentSetting;
	
	public IntervalClient(IntervalController intervalController, ConfigController settings) {
		this.intervalController = intervalController;
		this.settings = settings;
		buildMap();
		applySettings((CompositeSetting) settings.getSetting(DEFAULT_SETTING));
	}
		
	private void buildMap() {
		settingLookup = new HashMap<String, String>();
		settingLookup.put("Minor2nd", "m2");
		settingLookup.put("Major2nd", "M2");
		settingLookup.put("Minor3rd", "m3");
		settingLookup.put("Major3rd", "M3");
		settingLookup.put("Perfect4th", "p4");
		settingLookup.put("Diminished5th", "dim5");
		settingLookup.put("Perfect5th", "p5");
		settingLookup.put("Minor6th", "m6");
		settingLookup.put("Major6th", "M6");
		settingLookup.put("Minor7th", "m7");
		settingLookup.put("Major7th", "M7");
		settingLookup.put("Octave", "oct");
		settingLookup.put("Minor9th", "m9");
		settingLookup.put("Major9th", "M9");
		settingLookup.put("Augmented9th", "aug9");
		settingLookup.put("Major10th", "M10");
		settingLookup.put("Perfect11th", "p11");
		settingLookup.put("Major13th", "M13");		
	}

	/**
	 * Create a command to exit the current form.
	 */
	@Override
	public void exit() {
		ClientCommand command = ClientCommand.CloseForm;
		ClientType type = ClientType.Interval;
		sendCommand(command, type);
	}
	
	/**
	 * Submit the given interval to the controller.
	 * 
	 * @param interval Interval enumeration.
	 */
	public boolean submit(Interval interval) {
		return intervalController.submit(interval);
	}
	
	/**
	 * Call the play command on the controller.
	 */
	public void play() {
		if (currentInterval == null) {
			getNextInterval();
		} else {
			intervalController.play();
		}
	}
	
	/**
	 * Pick a new Interval.
	 */
	public void getNextInterval() {
		currentInterval = intervalController.getNextInterval();
	}
	
	/**
	 * Apply interval settings.
	 * 
	 * @param settings CompositeSetting.
	 */
	@Override
	public void applySettings(CompositeSetting settings) {
		currentSetting = settings.getName();
		// Loop through settings
		List<Interval> intervalList = new ArrayList<Interval>();
		for (Interval interval : Interval.values()) {
			String key = settingLookup.get(interval.toString());
			if (key != null) {
				ControlSetting<?> setting = (ControlSetting<?>) settings.findSetting(key);
				if ((Integer)setting.getValue() == 1) {
					intervalList.add(interval);
				}
			}
		}
		intervalController.setIntervals(intervalList);
	}
	
	/**
	 * Reload the current settings.
	 */
	@Override
	public void updateSettings() {
		applySettings((CompositeSetting) settings.getSetting(currentSetting));	
		intervalController.updateSoundSettings();
	}
	
	/**
	 * Return a copy of the controllers intervals.
	 * 
	 * @return List<Interval>
	 */
	public List<Interval> getIntervals() {
		return new ArrayList<Interval>(intervalController.getIntervals());		
	}
	
	/**
	 * Get the given settings.
	 * 
	 * @return String
	 */
	@Override
	public String getSetting() {
		return currentSetting;
	}
	
	/**
	 * Set settings by string.
	 * 
	 * @param setting String
	 */
	@Override
	public void setSetting(String setting) {
		applySettings((CompositeSetting) settings.getSetting(setting));
	}
}

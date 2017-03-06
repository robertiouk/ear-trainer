package client.menu;

import controller.settings.CompositeSetting;

/**
 * Defines functionality to modify settings.
 * 
 * @author Robertio
 *
 */
public interface SettingsControl {
	void applySettings(CompositeSetting settings);
	String getSetting();
	void setSetting(String setting);
	void updateSettings();
}

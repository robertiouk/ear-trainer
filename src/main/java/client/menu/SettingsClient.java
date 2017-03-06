package client.menu;

import org.apache.commons.configuration.ConfigurationException;

import client.ClientImpl;
import client.command.ClientCommand;
import client.command.ClientType;
import controller.settings.Setting;
import data.ConfigController;

/**
 * The client for the Settings frame.
 * 
 * @author Robertio
 *
 */
public class SettingsClient extends ClientImpl {
	private ConfigController controller;
	
	public SettingsClient(ConfigController controller) {
		this.controller = controller;
	}
	
	@Override
	public void exit() {
		ClientCommand command = ClientCommand.CloseForm;
		ClientType type = ClientType.Settings;
		sendCommand(command, type);
	}

	/**
	 * Apply settings to the config controller.
	 * 
	 * @param setting Setting.
	 */
	public void applySetting(Setting setting) {
		try {
			controller.writeConfig(setting);
		} catch (ConfigurationException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Get a setting from the config controller.
	 * 
	 * @param setting String
	 * @return Setting
	 */
	public Setting getSetting(String setting) {
		return controller.getSetting(setting);
	}
}

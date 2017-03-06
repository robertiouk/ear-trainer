package client.menu;

import client.ClientImpl;
import client.command.ClientCommand;
import client.command.ClientType;

/**
 * Client for the main menu to be passed to GUI.
 * 
 * @author Robertio
 *
 */
public class MenuClient extends ClientImpl {
	/**
	 * Constructor for MenuClient.
	 */
	public MenuClient() {
	}
	
	/**
	 * Create a command to open an Interval form and notify observers.
	 */
	public void openInterval() {
		ClientCommand command = ClientCommand.OpenForm;
		ClientType type = ClientType.Interval;
		sendCommand(command, type);
	}
	
	/**
	 * Create a command to open a Chord form and notify observers.
	 */
	public void openChord() {
		ClientCommand command = ClientCommand.OpenForm;
		ClientType type = ClientType.Chord;
		sendCommand(command, type);
	}
	
	/**
	 * Create a command to open a Cadence form and notify observers.
	 */
	public void openCadence() {
		ClientCommand command = ClientCommand.OpenForm;
		ClientType type = ClientType.Cadence;
		sendCommand(command, type);
	}
	
	/**
	 * Create a command to open a Settings form and notify observers.
	 */
	public void openSettings() {
		ClientCommand command = ClientCommand.OpenForm;
		ClientType type = ClientType.Settings;
		sendCommand(command, type);
	}

	/**
	 * Create a command to exit the current form.
	 */
	@Override
	public void exit() {
		ClientCommand command = ClientCommand.CloseForm;
		ClientType type = ClientType.MainMenu;
		sendCommand(command, type);
	}	
}

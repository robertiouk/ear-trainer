package client;

import java.util.Observable;

import client.command.ClientCommand;
import client.command.ClientType;
import client.command.CommandContainer;

/**
 * Implementation of Client interface.
 * 
 * @author Robertio
 *
 */
public abstract class ClientImpl extends Observable implements Client {

	/**
	 * Pack command and form type into a container and notify observers.
	 * 
	 * @param command ClientCommand to perform.
	 * @param type ClientType to perform command on.
	 */
	protected void sendCommand(ClientCommand command, ClientType type) {
		CommandContainer container = new CommandContainer(command, type);

		setChanged();
		notifyObservers(container);
	}
}

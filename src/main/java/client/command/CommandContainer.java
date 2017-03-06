package client.command;


/**
 * Container object for commands. This will be passed from a client to its
 * observers.
 * 
 * @author Robertio
 *
 */
public class CommandContainer {
	/** The command to pass on. */
	private ClientCommand command = null;
	/** The client to perform action on. */
	private ClientType clientType = null;
	
	/**
	 * Constructor for CommandContainer.
	 * 
	 * @param command ClientCommand type.
	 * @param clientType ClientType type.
	 */
	public CommandContainer(ClientCommand command, ClientType clientType) {
		this.command = command;
		this.clientType = clientType;
	}
	
	/**
	 * Getter for ClientCommand.
	 * 
	 * @return The ClientCommand.
	 */
	public ClientCommand getClientCommand() {
		return command;
	}
	
	/**
	 * Getter for ClientType.
	 * 
	 * @return The ClientType.
	 */
	public ClientType getClientType() {
		return clientType;
	}
}

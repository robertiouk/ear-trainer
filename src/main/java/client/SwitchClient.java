package client;

import client.command.ClientType;

/**
 * Interface to be passed to Client. Defines behaviour
 * for switching Clients.
 * 
 * @author Robertio
 *
 */
public interface SwitchClient {
	void switchClient(ClientType client);
}

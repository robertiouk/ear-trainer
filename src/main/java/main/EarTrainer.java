package main;

import gui.AppFrame;

import java.awt.Frame;

import javax.swing.JFrame;

import org.apache.commons.configuration.ConfigurationException;

import client.AppClient;
import client.CloseEvent;
import client.SwitchClient;
import client.command.ClientType;

public class EarTrainer {
	private AppFrame menu;
	
	public static void main(String[] args) throws ConfigurationException {
		new EarTrainer();
	}
	
	/**
	 * The constructor
	 */
	public EarTrainer() {
		// Schedule a job for the event-dispatching thread:
		// creating and showing this application's GUI
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				try {
					createAndShowGui();
				} catch (ConfigurationException | InstantiationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});		
	}
		
	private void createAndShowGui() throws ConfigurationException, InstantiationException {
		// Create and set up the window
		AppClient appClient = new AppClient(new SwitchClient() {			
			@Override
			public void switchClient(ClientType client) {
				switchFrame(client);
			}
		}, new CloseEvent() {
			@Override
			public void close() {
				menu.dispose();
			}			
		});
		menu = new AppFrame(appClient);
		menu.setVisible(true);
		menu.setExtendedState(Frame.MAXIMIZED_BOTH);
		menu.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	}	

	private void switchFrame(ClientType client) {
		menu.switchClient(client);
	}
}

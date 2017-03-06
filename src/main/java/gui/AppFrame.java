package gui;

import gui.appearance.Colours;
import gui.settings.Settings;

import java.awt.Color;
import java.awt.Font;
import java.awt.Toolkit;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;

import client.AppClient;
import client.command.ClientType;
import client.menu.CadenceClient;
import client.menu.ChordClient;
import client.menu.IntervalClient;
import client.menu.MenuClient;
import client.menu.SettingsClient;

public class AppFrame extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3750715696773374593L;
	private AppClient appClient;
	JDesktopPane desktopPane;
	JInternalFrame currentFrame;
	private Map<ClientType, JInternalFrame> frameLookup;
	
	public AppFrame(AppClient appClient) throws InstantiationException {
		this.appClient = appClient;
		buildFrames();
		
		setResizable(false);
		setTitle("Ear Trainer");		
		
		getContentPane().setBackground(Colours.DARK_GREY);
		getContentPane().setFont(new Font("Segoe UI", Font.PLAIN, 11));
		getContentPane().setLayout(null);
		
		JLabel lblEarTrainer = new JLabel("Ear Trainer");
		lblEarTrainer.setForeground(Color.WHITE);
		lblEarTrainer.setFont(new Font("Segoe UI", Font.PLAIN, 50));
		lblEarTrainer.setBounds(20, 0, 345, 83);
		getContentPane().add(lblEarTrainer);
		
		desktopPane = new JDesktopPane();
		desktopPane.setBounds(0, 123, 698, 231);
		desktopPane.setLocation(0,  100);
		desktopPane.setSize(Toolkit.getDefaultToolkit().getScreenSize().width, Toolkit.getDefaultToolkit().getScreenSize().height);
		getContentPane().add(desktopPane);
		
		
		JInternalFrame internalFrame = frameLookup.get(ClientType.MainMenu);
		internalFrame.setBorder(null);
		internalFrame.setBounds(0, 86, 718, 291);
		desktopPane.add(internalFrame);
		internalFrame.setVisible(true);
		internalFrame.setLocation(0,  0);
		internalFrame.setSize(Toolkit.getDefaultToolkit().getScreenSize().width, Toolkit.getDefaultToolkit().getScreenSize().height);
		((javax.swing.plaf.basic.BasicInternalFrameUI)internalFrame.getUI()).setNorthPane(null);
		desktopPane.add(frameLookup.get(ClientType.Interval));
		desktopPane.add(frameLookup.get(ClientType.Chord));
		desktopPane.add(frameLookup.get(ClientType.Cadence));
		desktopPane.add(frameLookup.get(ClientType.Settings));
		currentFrame = internalFrame;
		
		setUndecorated(true);
	}
	
	private void buildFrames() throws InstantiationException {
		frameLookup = new HashMap<ClientType, JInternalFrame>();
		
		frameLookup.put(ClientType.MainMenu, new MainMenu((MenuClient) appClient.getClient()));
		frameLookup.put(ClientType.Interval, new Interval((IntervalClient) appClient.getClient(ClientType.Interval), "Interval"));
		frameLookup.put(ClientType.Chord, new Chord((ChordClient) appClient.getClient(ClientType.Chord), "Chord"));
		frameLookup.put(ClientType.Cadence, new Cadence((CadenceClient) appClient.getClient(ClientType.Cadence), "Cadence"));
		frameLookup.put(ClientType.Settings, new Settings((SettingsClient) appClient.getClient(ClientType.Settings)));
	}
	
	public void switchClient(ClientType client) {
		currentFrame.setVisible(false);
		JInternalFrame internalFrame = frameLookup.get(client);
		internalFrame.setVisible(true);
		internalFrame.setLocation(0,  0);
		internalFrame.setSize(Toolkit.getDefaultToolkit().getScreenSize().width, Toolkit.getDefaultToolkit().getScreenSize().height);
		((javax.swing.plaf.basic.BasicInternalFrameUI)internalFrame.getUI()).setNorthPane(null);
		currentFrame = internalFrame;
	}
}
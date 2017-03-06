package client;

import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import javax.sound.midi.Instrument;
import javax.sound.midi.MidiChannel;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Synthesizer;

import main.EarTrainer;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.log4j.Logger;

import client.command.ClientType;
import client.command.CommandContainer;
import client.menu.CadenceClient;
import client.menu.ChordClient;
import client.menu.IntervalClient;
import client.menu.MenuClient;
import client.menu.SettingsClient;
import controller.cadences.CadenceController;
import controller.chords.ChordController;
import controller.intervals.IntervalController;
import controller.settings.CompositeSetting;
import controller.settings.ControlSetting;
import controller.sound.MidiPlayer;
import controller.sound.SoundPlayer;
import data.ConfigController;
import data.XmlConfigController;

/**
 * This class is the main client for the application. It is responsible for
 * handling the current Frame and passing to the GUI.
 * 
 * @author Robertio
 *
 */
public class AppClient implements Observer, Client {
	/** Constants. */
	private static final String SOUND_SETTING = "sound";
	private static final String FILE_PATH = "settings.xml";
	private Logger theLogger = Logger.getLogger(EarTrainer.class);
	private ClientImpl client;
	private ClientImpl mainMenu;
	private Map<ClientType, ClientImpl> clientLookup;
	private SwitchClient clientSwitcher;
	private SoundPlayer soundPlayer;
	private CloseEvent closeEvent;
	
	public AppClient(SwitchClient clientSwitcher, CloseEvent closeEvent) throws ConfigurationException {
		this.clientSwitcher = clientSwitcher;
		this.closeEvent = closeEvent;
		
		buildClients();
		
		client = mainMenu;
	}
	
	/**
	 * Build all the menu clients.
	 * 
	 * @throws ConfigurationException
	 */
	private void buildClients() throws ConfigurationException {
		clientLookup = new HashMap<ClientType, ClientImpl>();
		
		ConfigController configController = new XmlConfigController();
		try {
			configController.setConfigFile(FILE_PATH);
		} catch (ConfigurationException e) {
			theLogger.error("Failed to load settings", e);
			throw e;
		}
		mainMenu = new MenuClient();
		mainMenu.addObserver(this);
		
		// Build the sound player
		CompositeSetting setting = (CompositeSetting) configController.getSetting(SOUND_SETTING);
		Synthesizer synthesizer = buildSynthesizer(setting);
		soundPlayer = new MidiPlayer(synthesizer, setting);
		IntervalController intervalController = new IntervalController(soundPlayer);
		ClientImpl intervalClient = new IntervalClient(intervalController, configController);
		intervalClient.addObserver(this);
		ChordController chordController = new ChordController(soundPlayer);
		ClientImpl chordClient = new ChordClient(chordController, configController);
		chordClient.addObserver(this);
		CadenceController cadenceController = new CadenceController(soundPlayer);
		ClientImpl cadenceClient = new CadenceClient(cadenceController);
		cadenceClient.addObserver(this);
		ClientImpl settingsClient = new SettingsClient(configController);
		settingsClient.addObserver(this);
		
		clientLookup.put(ClientType.MainMenu, mainMenu);
		clientLookup.put(ClientType.Interval, intervalClient);
		clientLookup.put(ClientType.Chord, chordClient);
		clientLookup.put(ClientType.Cadence, cadenceClient);
		clientLookup.put(ClientType.Settings, settingsClient);
	}
	
	/**
	 * Update the sound settings.
	 */
	private void updateSoundSettings() {
		soundPlayer.updateGeneralSettings();	
	}

	/**
	 * Builds a synthesiser objects. 
	 */
	private Synthesizer buildSynthesizer(CompositeSetting soundSettings) {
		Synthesizer synth = null;
		try {
			synth = MidiSystem.getSynthesizer();
			synth.open();
	
			MidiChannel[] midiChannels = synth.getChannels();
			Instrument[] instruments = synth.getDefaultSoundbank().getInstruments();
			
			ControlSetting<?> instrumentSetting = (ControlSetting<?>) soundSettings.findSetting("instrument");
			Instrument instrument = instruments[(Integer) instrumentSetting.getValue()];
			synth.loadInstrument(instrument);	
			ControlSetting<?> channelSetting = (ControlSetting<?>) soundSettings.findSetting("channel");
			midiChannels[(Integer) channelSetting.getValue()].programChange(instrument.getPatch().getProgram());		
		} catch (MidiUnavailableException e) {
			theLogger.error("Error occured creating Synthesizer", e);
		}
		
		return synth;
	}

	
	/**
	 * Returns the current client.
	 * 
	 * @return Client
	 */
	public Client getClient() {
		return client;
	}
	
	/**
	 * Get a client by ClientType.
	 * 
	 * @param type ClientType.
	 * @return Client.
	 */
	public Client getClient(ClientType type) {
		return clientLookup.get(type);
	}

	@Override
	public void update(Observable o, Object arg) {
		if (arg instanceof CommandContainer)
		{
			CommandContainer container = (CommandContainer)arg;

			switch (container.getClientCommand()) {
			case CloseForm:
				if (client == mainMenu) {
					exit();
				} else {
					client = mainMenu;
					clientSwitcher.switchClient(ClientType.MainMenu);
				}
				if (client == getClient(ClientType.Settings)) {
					updateSoundSettings();
				}
				break;
			case OpenForm:				
				clientSwitcher.switchClient(container.getClientType());
				client = clientLookup.get(container.getClientType());
				break;
			default:
				break;
			
			}
		}
	}

	@Override
	public void exit() {
		closeEvent.close();
	}
}

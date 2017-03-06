package client.menu;

import static org.junit.Assert.assertEquals;

import java.util.Observable;
import java.util.Observer;

import org.apache.commons.configuration.ConfigurationException;
import org.junit.Before;
import org.junit.Test;

import client.command.ClientCommand;
import client.command.ClientType;
import client.command.CommandContainer;
import data.ConfigController;
import data.XmlConfigController;

public class TestMenuClient {
	private MenuClient menuClient = null;
	private Observer observer = null;
	public Object client = null;
	private ConfigController configController = null;
	
	private class TestObserver implements Observer {
		@Override
		public void update(Observable o, Object arg) {
			client = arg;
		}		
	}
	
	@Before
	public void setUp() throws Exception {
		configController = new XmlConfigController();
		try {
			configController.setConfigFile(System.getProperty("user.dir") + "\\tests\\test_settings.xml");
		} catch (ConfigurationException e) {
			System.out.println(e);
		}
		menuClient = new MenuClient();
		observer = new TestObserver();
		
		menuClient.addObserver(observer);
	}

	/**
	 * Call openInterval() and make sure the correct ClientCommand was
	 * returned to the observer.
	 */
	@Test
	public void testOpenInterval() {
		menuClient.openInterval();
		
		assertEquals("ClientCommand not returned to observer.", CommandContainer.class, client.getClass());
		assertEquals("Open form command not received.", ClientCommand.OpenForm, ((CommandContainer)client).getClientCommand());
		assertEquals("Interval client type not received.", ClientType.Interval, ((CommandContainer)client).getClientType());
	}

	/**
	 * Call openChord() and make sure the correct ClientCommand was
	 * returned to the observer.
	 */
	@Test
	public void testOpenChord() {
		menuClient.openChord();
		
		assertEquals("ClientCommand not returned to observer.", CommandContainer.class, client.getClass());
		assertEquals("Open form command not received.", ClientCommand.OpenForm, ((CommandContainer)client).getClientCommand());
		assertEquals("Chord client type not received.", ClientType.Chord, ((CommandContainer)client).getClientType());
	}

	/**
	 * Call openCadence() and make sure the correct ClientCommand was
	 * returned to the observer.
	 */
	@Test
	public void testOpenCadence() {
		menuClient.openCadence();
		
		assertEquals("ClientCommand not returned to observer.", CommandContainer.class, client.getClass());
		assertEquals("Open form command not received.", ClientCommand.OpenForm, ((CommandContainer)client).getClientCommand());
		assertEquals("Cadence client type not received.", ClientType.Cadence, ((CommandContainer)client).getClientType());
	}

	/**
	 * Call openSettings() and make sure the correct ClientCommand was
	 * returned to the observer.
	 */
	@Test
	public void testOpenSettings() {
		menuClient.openSettings();
		
		assertEquals("ClientCommand not returned to observer.", CommandContainer.class, client.getClass());
		assertEquals("Open form command not received.", ClientCommand.OpenForm, ((CommandContainer)client).getClientCommand());
		assertEquals("Settings client type not received.", ClientType.Settings, ((CommandContainer)client).getClientType());
	}
	
	/**
	 * Call exit() and make sure the correct ClientCommand was
	 * returned to the observer.
	 */
	@Test
	public void testExit() {
		menuClient.exit();
		
		assertEquals("ClientCommand not returned to observer.", CommandContainer.class, client.getClass());
		assertEquals("Close form command not received.", ClientCommand.CloseForm, ((CommandContainer)client).getClientCommand());
		assertEquals("Wrong client recieved", ClientType.MainMenu, ((CommandContainer)client).getClientType());
	}

}

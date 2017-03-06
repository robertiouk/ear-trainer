package client.menu;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.Observable;
import java.util.Observer;

import org.apache.commons.configuration.ConfigurationException;
import org.junit.Before;
import org.junit.Test;

import client.command.ClientCommand;
import client.command.ClientType;
import client.command.CommandContainer;
import controller.settings.Setting;
import data.ConfigController;

public class TestSettingsClient {
	private SettingsClient settingsClient;
	private Observer observer = null;
	public Object client = null;
	private ConfigController controller = null;
	
	private class TestObserver implements Observer {
		@Override
		public void update(Observable o, Object arg) {
			client = arg;
		}		
	}
	
	@Before
	public void setUp() throws Exception {
		controller = mock(ConfigController.class);
		//when(controller.getNextCadence()).thenReturn(Cadence.Imperfect);
		observer = new TestObserver();
		settingsClient = new SettingsClient(controller);
		
		settingsClient.addObserver(observer);
	}
	
	/**
	 * Verify that the correct exit command was passed to the observers.
	 */
	@Test
	public void testExit() {
		settingsClient.exit();
		
		assertEquals("ClientCommand not returned to observer.", CommandContainer.class, client.getClass());
		assertEquals("Close form command not received.", ClientCommand.CloseForm, ((CommandContainer)client).getClientCommand());
		assertEquals("Wrong client recieved", ClientType.Settings, ((CommandContainer)client).getClientType());	}

	/**
	 * Verify that the given setting was passed to the controller to write.
	 */
	@Test
	public void testApplySetting() {
		Setting testSetting = mock(Setting.class);
		settingsClient.applySetting(testSetting);
		
		try {
			verify(controller, times(1)).writeConfig(testSetting);
		} catch (ConfigurationException e) {
			fail("Caught exception: " + e);
		}
	}

	/**
	 * Verify the given setting name was passed to the controller to find a setting.
	 */
	@Test
	public void testGetSetting() {
		String settingName = "test";
		settingsClient.getSetting(settingName);
		
		verify(controller, times(1)).getSetting(settingName);
	}

}

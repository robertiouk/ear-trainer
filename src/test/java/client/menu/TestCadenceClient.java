package client.menu;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Observable;
import java.util.Observer;

import org.junit.Before;
import org.junit.Test;

import client.command.ClientCommand;
import client.command.ClientType;
import client.command.CommandContainer;
import controller.cadences.CadenceController;
import controller.sound.Cadence;

public class TestCadenceClient {
	private CadenceClient cadenceClient;
	private Observer observer = null;
	public Object client = null;
	private CadenceController controller = null;
	
	private class TestObserver implements Observer {
		@Override
		public void update(Observable o, Object arg) {
			client = arg;
		}		
	}
	
	@Before
	public void setUp() throws Exception {
		controller = mock(CadenceController.class);
		when(controller.getNextCadence()).thenReturn(Cadence.Imperfect);
		observer = new TestObserver();
		cadenceClient = new CadenceClient(controller);
		
		cadenceClient.addObserver(observer);
	}

	/**
	 * 1. Verify that the correct exit command was passed to the observers.
	 */
	@Test
	public void testExit() {
		cadenceClient.exit();
		
		assertEquals("ClientCommand not returned to observer.", CommandContainer.class, client.getClass());
		assertEquals("Close form command not received.", ClientCommand.CloseForm, ((CommandContainer)client).getClientCommand());
		assertEquals("Wrong client recieved", ClientType.Cadence, ((CommandContainer)client).getClientType());
	}
	
	/**
	 * 1. Submit a cadence.
	 * 2. Verify that the cadence was passed onto the controller.
	 */
	@Test
	public void testSubmit() {
		cadenceClient.submit(Cadence.Imperfect);
		
		verify(controller, times(1)).submit(Cadence.Imperfect);
	}

	/**
	 * 1. Play current cadence.
	 * 2. Verify that a new cadence was selected.
	 * 3. Play current cadence.
	 * 4. Verify that the same cadence was played.
	 */
	@Test
	public void testPlay() {
		cadenceClient.play();
		
		verify(controller, times(1)).getNextCadence();
		
		cadenceClient.play();
		
		verify(controller, times(1)).play();
	}

	/**
	 * 1. Get next cadence.
	 * 2. Verify that getNextCadence() was called on the controller.
	 */
	@Test
	public void testGetNextCadence() {
		cadenceClient.getNextCadence();
		
		verify(controller, times(1)).getNextCadence();
	}
	
	/**
	 * 1. Call getCadences().
	 * 2. Assert that the getCadences() method was called on the controller.
	 */
	@Test
	public void testGetChords() {
		cadenceClient.getCadences();
		
		verify(controller, times(1)).getCadences();
	}
	/**
	 * 1. Update settings.
	 * 2. Verify sound settings were updated.
	 */
	@Test
	public void updateSettings() {
		cadenceClient.updateSettings();
		
		verify(controller, times(1)).updateSoundSettings();
	}
}

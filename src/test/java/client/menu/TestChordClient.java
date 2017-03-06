package client.menu;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.runners.MockitoJUnitRunner;

import client.command.ClientCommand;
import client.command.ClientType;
import client.command.CommandContainer;
import controller.chords.ChordController;
import controller.settings.CompositeSetting;
import controller.settings.ControlSetting;
import controller.sound.Chord;
import data.ConfigController;

@RunWith(MockitoJUnitRunner.class)
public class TestChordClient {
	private ChordClient chordClient;
	private Observer observer = null;
	public Object client = null;
	private ChordController controller = null;
	private ConfigController config = null;
	private CompositeSetting setting1 = null;
	private CompositeSetting setting2 = null;
	
	private class TestObserver implements Observer {
		@Override
		public void update(Observable o, Object arg) {
			client = arg;
		}		
	}
	
	@Before
	public void setUp() throws Exception {
		buildSettings();
		controller = mock(ChordController.class);
		config = mock(ConfigController.class);
		observer = new TestObserver();
		CompositeSetting compositeMedium = mock(CompositeSetting.class);
		CompositeSetting composite = mock((CompositeSetting.class));
		when(compositeMedium.findSetting(anyString())).thenReturn(composite);
		@SuppressWarnings("unchecked")
		ControlSetting<Integer> setting = (ControlSetting<Integer>) mock(ControlSetting.class);
		when(config.getSetting(anyString())).thenReturn(compositeMedium);
		when(compositeMedium.findSetting("setting1")).thenReturn(setting1);
		when(compositeMedium.findSetting("setting2")).thenReturn(setting2);
		when(composite.findSetting(anyString())).thenReturn(setting);
		when(setting.getValue()).thenReturn(1);
		when(controller.getNextChord()).thenReturn(Chord.Augmented5);
		chordClient = new ChordClient(controller, config);
		
		chordClient.addObserver(observer);
	}

	/**
	 * 1. Verify that the correct exit command was passed to the observers.
	 */
	@Test
	public void testExit() {
		chordClient.exit();
		
		assertEquals("ClientCommand not returned to observer.", CommandContainer.class, client.getClass());
		assertEquals("Close form command not received.", ClientCommand.CloseForm, ((CommandContainer)client).getClientCommand());
		assertEquals("Wrong client recieved", ClientType.Chord, ((CommandContainer)client).getClientType());
	}
	
	/**
	 * 1. Submit a chord.
	 * 2. Verify that the chord was passed onto the controller.
	 */
	@Test
	public void testSubmit() {
		chordClient.submit(Chord.Augmented5);
		
		verify(controller, times(1)).submit(Chord.Augmented5);
	}

	/**
	 * 1. Play current chord.
	 * 2. Verify that a new chord was selected.
	 * 3. Play current chord.
	 * 4. Verify that the same chord was played.
	 */
	@Test
	public void testPlay() {
		chordClient.play();
		
		verify(controller, times(1)).getNextChord();
		
		chordClient.play();
		
		verify(controller, times(1)).play();
	}

	/**
	 * 1. Get next chord.
	 * 2. Verify that getNextChord() was called on the controller.
	 */
	@Test
	public void testGetNextChord() {
		chordClient.getNextChord();
		
		verify(controller, times(1)).getNextChord();
	}

	@Captor
    private ArgumentCaptor<List<Chord>> captor;
	
	/**
	 * 1. Give the client a list of settings.
	 * 2. Assert that the given settings have been passed to the controller.
	 */
	@Test
	public void testApplySettings() {
		ChordController testController = mock(ChordController.class);
		ChordClient testClient = new ChordClient(testController, config);
		
		testClient.applySettings(setting1);
		verify(testController, times(2)).setChords(captor.capture());
		assertEquals(5, captor.getAllValues().get(1).size());
		short result = 0;
		for (Chord chord : captor.getAllValues().get(1)) {
			if (chord == Chord.Major) {
				result |= 0x1;
			} else if (chord == Chord.Minor) {
				result |= 0x2;
			} else if (chord == Chord.Suspended4) {
				result |= 0x4;
			} else if (chord == Chord.Augmented5) {
				result |= 0x8;
			} else if (chord == Chord.Minor6) {
				result |= 0x10;
			}
		}
		assertEquals(0x1F, result);
	}

	/**
	 * 1. Call getChords().
	 * 2. Assert that the getChord() method was called on the controller.
	 */
	@Test
	public void testGetChords() {
		chordClient.getChords();
		
		verify(controller, times(1)).getChords();
	}

	/**
	 * 1. Apply setting1.
	 * 2. Verify that it has been set.
	 * 3. Apply setting2.
	 * 4. Verify that it has been set.
	 */
	@Test
	public void testSetGetSetting() {
		String setting1 = "setting1";
		String setting2 = "setting2";
		chordClient.setSetting(setting1);
		
		String actual = chordClient.getSetting();
		assertEquals(setting1, actual);
		
		chordClient.setSetting(setting2);
		actual = chordClient.getSetting();
		assertEquals(setting2, actual);	
	}
	
	/**
	 * 1. Update settings.
	 * 2. Verify new settings were passed to controller.
	 * 3. Verify sound settings were updated.
	 */
	@Test
	public void updateSettings() {
		chordClient.updateSettings();
		
		verify(controller, atLeastOnce()).setChords(captor.capture());
		verify(controller, times(1)).updateSoundSettings();
	}

	private void buildSettings() {
		setting1 = new CompositeSetting("setting1");
		setting1.addSetting(new ControlSetting<Integer>(1, "major"));
		setting1.addSetting(new ControlSetting<Integer>(1, "minor"));
		setting1.addSetting(new ControlSetting<Integer>(1, "suspended4"));
		setting1.addSetting(new ControlSetting<Integer>(1, "augmented5"));
		setting1.addSetting(new ControlSetting<Integer>(1, "minor6"));
		setting1.addSetting(new ControlSetting<Integer>(0, "major6"));
		setting1.addSetting(new ControlSetting<Integer>(0, "minor7"));
		setting1.addSetting(new ControlSetting<Integer>(0, "major7"));
		setting1.addSetting(new ControlSetting<Integer>(0, "minor7b5"));
		setting1.addSetting(new ControlSetting<Integer>(0, "minor7sharp5"));
		setting1.addSetting(new ControlSetting<Integer>(0, "dominant7"));
		setting1.addSetting(new ControlSetting<Integer>(0, "dominant7b5"));
		setting1.addSetting(new ControlSetting<Integer>(0, "dominant7sharp5"));
		setting1.addSetting(new ControlSetting<Integer>(0, "major7"));
		setting1.addSetting(new ControlSetting<Integer>(0, "diminished7"));
		setting1.addSetting(new ControlSetting<Integer>(0, "minor9"));
		setting1.addSetting(new ControlSetting<Integer>(0, "minor7b9"));
		setting1.addSetting(new ControlSetting<Integer>(0, "minor7b5b9"));
		setting1.addSetting(new ControlSetting<Integer>(0, "minor7sharp5b9"));
		setting1.addSetting(new ControlSetting<Integer>(0, "minor9b5"));
		setting1.addSetting(new ControlSetting<Integer>(0, "minor9sharp5"));
		setting1.addSetting(new ControlSetting<Integer>(0, "dominant9"));
		setting1.addSetting(new ControlSetting<Integer>(0, "dominant7b9"));
		setting1.addSetting(new ControlSetting<Integer>(0, "dominant7sharp9"));
		setting1.addSetting(new ControlSetting<Integer>(0, "dominant7b5sharp9"));
		setting1.addSetting(new ControlSetting<Integer>(0, "dominant7sharp5b9"));
		setting1.addSetting(new ControlSetting<Integer>(0, "dominant7sharp5sharp9"));
		setting1.addSetting(new ControlSetting<Integer>(0, "dominant7b5b9"));
		setting1.addSetting(new ControlSetting<Integer>(0, "dominant9b5"));
		setting1.addSetting(new ControlSetting<Integer>(0, "dominant9sharp5"));
		setting1.addSetting(new ControlSetting<Integer>(0, "major9"));
		setting1.addSetting(new ControlSetting<Integer>(0, "dominant11"));
		setting1.addSetting(new ControlSetting<Integer>(0, "dominant13"));
		setting2 = new CompositeSetting("setting2");
		setting2.addSetting(new ControlSetting<Integer>(0, "major"));
		setting2.addSetting(new ControlSetting<Integer>(0, "minor"));
		setting2.addSetting(new ControlSetting<Integer>(0, "suspended4"));
		setting2.addSetting(new ControlSetting<Integer>(0, "suspended5"));
		setting2.addSetting(new ControlSetting<Integer>(0, "augmented5"));
		setting2.addSetting(new ControlSetting<Integer>(0, "minor6"));
		setting2.addSetting(new ControlSetting<Integer>(0, "major6"));
		setting2.addSetting(new ControlSetting<Integer>(0, "minor7"));
		setting2.addSetting(new ControlSetting<Integer>(0, "major7"));
		setting2.addSetting(new ControlSetting<Integer>(0, "minor7b5"));
		setting2.addSetting(new ControlSetting<Integer>(0, "minor7sharp5"));
		setting2.addSetting(new ControlSetting<Integer>(0, "dominant7"));
		setting2.addSetting(new ControlSetting<Integer>(0, "dominant7b5"));
		setting2.addSetting(new ControlSetting<Integer>(0, "dominant7sharp5"));
		setting2.addSetting(new ControlSetting<Integer>(0, "major7"));
		setting2.addSetting(new ControlSetting<Integer>(0, "diminished7"));
		setting2.addSetting(new ControlSetting<Integer>(0, "minor9"));
		setting2.addSetting(new ControlSetting<Integer>(0, "minor7b9"));
		setting2.addSetting(new ControlSetting<Integer>(0, "minor7b5b9"));
		setting2.addSetting(new ControlSetting<Integer>(0, "minor7sharp5b9"));
		setting2.addSetting(new ControlSetting<Integer>(0, "minor9b5"));
		setting2.addSetting(new ControlSetting<Integer>(0, "minor9sharp5"));
		setting2.addSetting(new ControlSetting<Integer>(0, "dominant9"));
		setting2.addSetting(new ControlSetting<Integer>(0, "dominant7b9"));
		setting2.addSetting(new ControlSetting<Integer>(0, "dominant7sharp9"));
		setting2.addSetting(new ControlSetting<Integer>(0, "dominant7b5sharp9"));
		setting2.addSetting(new ControlSetting<Integer>(0, "dominant7sharp5b9"));
		setting2.addSetting(new ControlSetting<Integer>(0, "dominant7sharp5sharp9"));	
		setting2.addSetting(new ControlSetting<Integer>(0, "dominant7b5b9"));
		setting2.addSetting(new ControlSetting<Integer>(1, "dominant9b5"));
		setting2.addSetting(new ControlSetting<Integer>(1, "dominant9sharp5"));
		setting2.addSetting(new ControlSetting<Integer>(1, "major9"));
		setting2.addSetting(new ControlSetting<Integer>(1, "dominant11"));
		setting2.addSetting(new ControlSetting<Integer>(1, "dominant13"));
	}
}

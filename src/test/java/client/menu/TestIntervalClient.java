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
import controller.intervals.IntervalController;
import controller.settings.CompositeSetting;
import controller.settings.ControlSetting;
import controller.sound.Interval;
import data.ConfigController;

@RunWith(MockitoJUnitRunner.class)
public class TestIntervalClient {
	private IntervalClient intervalClient = null;
	private IntervalController controller = null;
	private ConfigController config = null;
	private Observer observer = null;
	public Object client = null;
	private CompositeSetting setting1;
	private CompositeSetting setting2;
	
	private class TestObserver implements Observer {
		@Override
		public void update(Observable o, Object arg) {
			client = arg;
		}		
	}
	
	@SuppressWarnings("unchecked")
	@Before
	public void setUp() throws Exception {
		buildTestSettings();
		controller = mock(IntervalController.class);
		config = mock(ConfigController.class);
		CompositeSetting composite = mock(CompositeSetting.class);
		ControlSetting<Integer> setting = (ControlSetting<Integer>) mock(ControlSetting.class);
		when(config.getSetting(anyString())).thenReturn(composite);
		when(config.getSetting("setting1")).thenReturn(setting1);
		when(config.getSetting("setting2")).thenReturn(setting2);
		when(composite.findSetting(anyString())).thenReturn(setting);
		when(setting.getValue()).thenReturn(1);
		when(controller.getNextInterval()).thenReturn(Interval.Augmented9th);
		intervalClient = new IntervalClient(controller, config);
		observer = new TestObserver();
		
		intervalClient.addObserver(observer);		
	}

	/**
	 * Call the submit function and assert the submit function was called 
	 * on the interval controller.
	 */
	@Test
	public void testSubmit() {
		intervalClient.submit(Interval.Augmented9th);
		
		verify(controller, atLeastOnce()).submit(Interval.Augmented9th);
	}
	
	/**
	 * 1. Play interval
	 * 2. Verify that new interval was generated.
	 * 3. Play again
	 * 4. Verify that the play was called.
	 */
	@Test
	public void testPlay() {
		intervalClient.play();
		
		verify(controller, atLeastOnce()).getNextInterval();
		
		intervalClient.play();
		
		verify(controller, atLeastOnce()).play();
	}
	
	@Test
	public void testExit() {
		intervalClient.exit();
		
		assertEquals("ClientCommand not returned to observer.", CommandContainer.class, client.getClass());
		assertEquals("Close form command not received.", ClientCommand.CloseForm, ((CommandContainer)client).getClientCommand());
		assertEquals("Wrong client recieved", ClientType.Interval, ((CommandContainer)client).getClientType());
	}
	
	/**
	 * 1. Verify that the getIntervals() function was called on the IntervalController.
	 */
	@Test
	public void testGetIntervals() {
		intervalClient.getIntervals();
		
		verify(controller, times(1)).getIntervals();
	}
	
	/**
	 * 1. Verify that the getNextInterval() function was called on the IntervalController.
	 */
	@Test
	public void testGetNextInterval() {
		intervalClient.getNextInterval();
		
		verify(controller, times(1)).getNextInterval();
	}
	
	@Captor
    private ArgumentCaptor<List<Interval>> captor;
	
	/**
	 * 1. Give the client a list of settings.
	 * 2. Assert that the given settings have been passed to the controller.
	 */
	@Test
	public void testApplySettings() {
		IntervalController testController = mock(IntervalController.class);
		IntervalClient testClient = new IntervalClient(testController, config);
		
		testClient.applySettings(setting1);
		verify(testController, times(2)).setIntervals(captor.capture());
		assertEquals(5, captor.getAllValues().get(1).size());
		short result = 0;
		for (Interval interval :  captor.getAllValues().get(1)) {
			if (interval == Interval.Minor2nd) {
				result |= 0x1;
			} else if (interval == Interval.Major2nd) {
				result |= 0x2;
			} else if (interval == Interval.Minor3rd) {
				result |= 0x4;
			} else if (interval == Interval.Major3rd) {
				result |= 0x8;
			} else if (interval == Interval.Perfect4th) {
				result |= 0x10;
			}
		}
		assertEquals(0x1F, result);
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
		intervalClient.setSetting(setting1);
		
		String actual = intervalClient.getSetting();
		assertEquals(setting1, actual);
		
		intervalClient.setSetting(setting2);
		actual = intervalClient.getSetting();
		assertEquals(setting2, actual);
	}
	
	/**
	 * 1. Update settings.
	 * 2. Verify new settings were passed to controller.
	 * 3. Verify sound settings were updated.
	 */
	@Test
	public void updateSettings() {
		intervalClient.updateSettings();
		
		verify(controller, atLeastOnce()).setIntervals(captor.capture());
		verify(controller, times(1)).updateSoundSettings();
	}
	
	private void buildTestSettings() {
		setting1 = new CompositeSetting("setting1");
		setting1.addSetting(new ControlSetting<Integer>(1, "m2"));
		setting1.addSetting(new ControlSetting<Integer>(1, "M2"));
		setting1.addSetting(new ControlSetting<Integer>(1, "m3"));
		setting1.addSetting(new ControlSetting<Integer>(1, "M3"));
		setting1.addSetting(new ControlSetting<Integer>(1, "p4"));
		setting1.addSetting(new ControlSetting<Integer>(0, "dim5"));
		setting1.addSetting(new ControlSetting<Integer>(0, "p5"));
		setting1.addSetting(new ControlSetting<Integer>(0, "m6"));
		setting1.addSetting(new ControlSetting<Integer>(0, "M6"));
		setting1.addSetting(new ControlSetting<Integer>(0, "m7"));
		setting1.addSetting(new ControlSetting<Integer>(0, "M7"));
		setting1.addSetting(new ControlSetting<Integer>(0, "oct"));
		setting1.addSetting(new ControlSetting<Integer>(0, "m9"));
		setting1.addSetting(new ControlSetting<Integer>(0, "M9"));
		setting1.addSetting(new ControlSetting<Integer>(0, "aug9"));
		setting1.addSetting(new ControlSetting<Integer>(0, "M10"));
		setting1.addSetting(new ControlSetting<Integer>(0, "p11"));
		setting1.addSetting(new ControlSetting<Integer>(0, "M13"));		
		setting2 = new CompositeSetting("setting2");
		setting2.addSetting(new ControlSetting<Integer>(0, "m2"));
		setting2.addSetting(new ControlSetting<Integer>(0, "M2"));
		setting2.addSetting(new ControlSetting<Integer>(0, "m3"));
		setting2.addSetting(new ControlSetting<Integer>(0, "M3"));
		setting2.addSetting(new ControlSetting<Integer>(0, "p4"));
		setting2.addSetting(new ControlSetting<Integer>(0, "dim5"));
		setting2.addSetting(new ControlSetting<Integer>(0, "p5"));
		setting2.addSetting(new ControlSetting<Integer>(0, "m6"));
		setting2.addSetting(new ControlSetting<Integer>(0, "M6"));
		setting2.addSetting(new ControlSetting<Integer>(0, "m7"));
		setting2.addSetting(new ControlSetting<Integer>(0, "M7"));
		setting2.addSetting(new ControlSetting<Integer>(1, "oct"));
		setting2.addSetting(new ControlSetting<Integer>(1, "m9"));
		setting2.addSetting(new ControlSetting<Integer>(1, "M9"));
		setting2.addSetting(new ControlSetting<Integer>(1, "aug9"));
		setting2.addSetting(new ControlSetting<Integer>(1, "M10"));
		setting2.addSetting(new ControlSetting<Integer>(1, "p11"));
		setting2.addSetting(new ControlSetting<Integer>(1, "M13"));		
	}
}

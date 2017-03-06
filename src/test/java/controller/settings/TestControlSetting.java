package controller.settings;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class TestControlSetting {
	/**
	 * 1. Build new ControlSetting & pass parameters.
	 * 2. Get name and assert it is not null.
	 * 3. Get value and assert is is not null.
	 */
	@Test
	public void testControlSetting() {
		ControlSetting<Integer> setting = new ControlSetting<Integer>(99, "asdf");
		
		assertTrue("Name is null.", setting.getName() != null);
		assertTrue("Value is null.", setting.getValue() != null);
	}

	/**
	 * 1. Build a ControlSetting.
	 * 2. Get the name and assert it equals the one passed in.
	 */
	@Test
	public void testGetName() {
		String name = "asdf";
		ControlSetting<Integer> setting = new ControlSetting<Integer>(99, name);

		assertEquals("Name is not as expected.", name, setting.getName());
	}

	/**
	 * 1. Build a ControlSetting.
	 * 2. Get the value and assert it equals the one passed in.
	 */
	@Test
	public void testGetValue() {
		Integer value = 99;
		ControlSetting<Integer> setting = new ControlSetting<Integer>(value, "asdf");
		
		assertEquals("Value is not as expected", value, setting.getValue());
	}

	/**
	 * 1. Build a ControlSetting.
	 * 2. Set a new value.
	 * 3. Get value and assert it equals new value.
	 */
	@Test
	public void testSetValue() {
		ControlSetting<Integer> setting = new ControlSetting<Integer>(99, "asdf");
		
		Integer newValue = 100;
		setting.setValue(newValue);
		
		assertEquals("Value is not as expected", newValue, setting.getValue());
	}

	
	/**
	 * 1. Build a ControlSetting.
	 * 2. Find setting by given name and assert value equals given value.
	 * 3. Find setting by wrong name and assert value is null.
	 */
	@Test
	public void testFindSetting() {
		String name = "asdf";
		Integer value = 99;
		ControlSetting<Integer> setting = new ControlSetting<Integer>(value, name);
		
		assertEquals(setting.findSetting(name), setting);
		assertEquals(setting.findSetting("xxx"), null);
	}

}

package controller.settings;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import java.util.ArrayList;

import org.junit.Test;

public class TestCompositeSetting {
	/**
	 * 1. Build a CompositeSetting.
	 * 2. Assert a name can be retrieved.
	 */
	@Test
	public void testCompositeSetting() {
		CompositeSetting setting = new CompositeSetting("asdf");
		
		assertTrue("Name is null.", setting.getName() != null);
	}

	/**
	 * 1. Build a CompositeSetting.
	 * 2. Get the name and assert it equals the one passed in.
	 */
	@Test
	public void testGetName() {
		String name = "asdf";
		CompositeSetting setting = new CompositeSetting(name);
		
		assertEquals("Name is not as expected.", name, setting.getName());
	}

	/**
	 * 1. Build a CompositeSetting.
	 * 2. Add a CompositeSetting.
	 * 3. Add a ControlSetting.
	 * 4. Assert the correct size of the settings list.
	 */
	@Test
	public void testAddSetting() {
		CompositeSetting setting = new CompositeSetting("asdf");
		CompositeSetting category = new CompositeSetting("category");
		ControlSetting<Integer> value = new ControlSetting<Integer>(99, "value");
		
		setting.addSetting(category);
		setting.addSetting(value);
		
		assertEquals("Size not as expected.", 2, setting.size());
	}

	/**
	 * 1. Build a CompositeSetting.
	 * 2. Add a layered CompositeSetting.
	 * 3. Find the setting on each layer and assert not null.
	 * 4. Find a setting that doesn't exist and assert null.
	 */
	@Test
	public void testFindSetting() {
		String categoryName = "category";
		String intName = "value";
		String stringName = "name";
		CompositeSetting rootSetting = new CompositeSetting("root");
		CompositeSetting category = new CompositeSetting(categoryName);
		ControlSetting<Integer> intValue = new ControlSetting<Integer>(99, intName);
		ControlSetting<String> stringValue = new ControlSetting<String>("asdf", stringName);
		
		category.addSetting(intValue);
		category.addSetting(stringValue);
		rootSetting.addSetting(category);
		
		// Perform searches on all layers
		assertEquals("Setting not found.", category, rootSetting.findSetting(categoryName));
		assertEquals("Setting not found.", intValue, rootSetting.findSetting(intName));
		assertEquals("Setting not found.", stringValue, rootSetting.findSetting(stringName));
		assertTrue("Result should be null.", rootSetting.findSetting("fake") == null);
	}

	/**
	 * 1. Build a CompositeSetting.
	 * 2. Add a list of Settings.
	 * 3. Iterate through CompositeSetting and compare with given list.
	 */
	@Test
	public void testIterator() {
		CompositeSetting setting = new CompositeSetting("asdf");
		ArrayList<Setting> expected = new ArrayList<Setting>();
		for (int i = 0; i < 100; ++i) {
			Setting newSetting = mock(Setting.class);
			expected.add(newSetting);
			setting.addSetting(newSetting);
		}
		
		// Iterate through setting
		int index = 0;
		for (Setting currentSetting: setting) {
			Setting expectedSetting = expected.get(index);
			assertEquals("Setting is not as expected.", expectedSetting, currentSetting);
			index++;
		}
	}

}

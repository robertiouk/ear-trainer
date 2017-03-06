package data;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.io.File;
import java.util.Iterator;
import java.util.Random;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.XMLConfiguration;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import controller.settings.CompositeSetting;
import controller.settings.ControlSetting;
import controller.settings.Setting;

public class XmlConfigControllerTest {
	private XMLConfiguration config = null;
	private File configFile = null;
	private String filepath = "";
	private XmlConfigController configController = null;

	@Rule
	public ExpectedException thrown = ExpectedException.none();
	
	@Before
	public void setUp() throws Exception {
		configController = new XmlConfigController();
		filepath = "settings.xml";
		configFile = new File(filepath);
		try {
			
			config = new XMLConfiguration(configFile);
		} catch (ConfigurationException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 1. Pass the controller a valid config file path.
	 * 2. Pass an invalid path and assert exception was thrown.
	 * @throws ConfigurationException 
	 */
	@Test
	public void testSetConfigFile() throws ConfigurationException {
		configController.setConfigFile(filepath);
		
		thrown.expect(ConfigurationException.class);
		configController.setConfigFile("asdf");
	}
	
	/**
	 * 1. Run through every key in the test file and retrieve from the 
	 * file passed in.
	 * 2. Make sure that the setting was retrieved.
	 * 3. Assert that the values match.
	 */
	@Test
	public void testReadConfig() {
		try {
			configController.setConfigFile(filepath);
		} catch (ConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Iterator<String> it = config.getKeys();
		while (it.hasNext()) {
			String key = it.next();
			
			Setting setting = configController.getSetting(key);
			
			// Get the expected value
			Object expected = config.getProperty(key);
			if (isInteger((String)expected)) {
				expected = Integer.parseInt((String)expected);
			} else {
				expected = (String)expected;
			}
			
			// Make sure a setting was retrieved
			assertFalse("No setting was retrieved for " + key  + ".", setting==null);
			
			// Only compare leaf settings
			if (setting instanceof ControlSetting) {
				Object actual = ((ControlSetting<?>)setting).getValue();
				
				assertEquals("Settings were not equal (" + key + ").", expected, actual);
			}
		}
	}

	/**
	 * 1. Read the settings.
	 * 1. Pass new Settings to the controller to write to file.
	 * 2. Read the new settings and assert that new values equal the given ones.
	 * 3. Re-write those settings to their initial state.
	 * @throws ConfigurationException 
	 */
	@Test
	public void testWriteConfig() throws ConfigurationException {
		configController.setConfigFile(filepath);
		
		Setting newSettings = configController.getSetting("settings");
		Setting originalSettings = new CompositeSetting("settings");
		cloneSettings((CompositeSetting)newSettings,(CompositeSetting)originalSettings);
		
		// Build new custom interval settings
		rewriteSettings((CompositeSetting)newSettings);
				
		// Write new settings
		configController.writeConfig(newSettings);
		
		// Read the new settings and compare with original
		configController.setConfigFile(filepath);
		configController.getSetting("settings");
		compareSettings((CompositeSetting)newSettings, (CompositeSetting)configController.getSetting("settings"));
		
		// Write the old settings
		newSettings = configController.getSetting("settings");
		copySettings((CompositeSetting)originalSettings, (CompositeSetting)newSettings);
		configController.writeConfig(newSettings);
		
		// Read settings and assert change
		configController.setConfigFile(filepath);
		configController.getSetting("settings");
		compareSettings((CompositeSetting)originalSettings, (CompositeSetting)configController.getSetting("settings"));
	}
	
	/**
	 * Clones a list of settings.
	 * 
	 * @param settings Settings to copy.
	 * @param copy Settings to copy to.
	 */
	@SuppressWarnings("unchecked")
	private void cloneSettings(CompositeSetting settings, CompositeSetting copy) {
		for (Setting setting: settings) {
			if (setting instanceof CompositeSetting) {
				CompositeSetting newCopy = new CompositeSetting(setting.getName());
				copy.addSetting(newCopy);
				cloneSettings((CompositeSetting)setting, newCopy);
			} else {
				ControlSetting<Integer> leafCopy = new ControlSetting<Integer>(((ControlSetting<Integer>)setting).getValue(), setting.getName());
				copy.addSetting(leafCopy);
			}
		}
	}
	
	/**
	 * Copies settings from source to an existing Setting.
	 * 
	 * @param settings Source Setting.
	 * @param copy Target Setting.
	 */
	@SuppressWarnings("unchecked")
	private void copySettings(CompositeSetting settings, CompositeSetting copy) {
		for (Setting setting: settings) {
			if (setting instanceof CompositeSetting) {
				copySettings((CompositeSetting)setting, (CompositeSetting)copy.findSetting(setting.getName()));
			} else {
				//((ControlSetting<Integer>)setting).setValue(((ControlSetting<Integer>)copy.findSetting(setting.getName())).getValue());
				ControlSetting<Integer> target = (ControlSetting<Integer>) copy.findSetting(setting.getName());
				target.setValue(((ControlSetting<Integer>)setting).getValue());
			}	
		}
	}
	
	/**
	 * Drill down into child settings and set a new random integer value.
	 * 
	 * @param settings CompositeSetting to rewrite.
	 */
	@SuppressWarnings("unchecked")
	private void rewriteSettings(CompositeSetting settings) {
		Random rand = new Random();
		
		for (Setting setting: settings) {
			if (setting instanceof CompositeSetting) {
				rewriteSettings((CompositeSetting)setting);
			} else {
				// Pick a new random value
				((ControlSetting<Integer>)setting).setValue(rand.nextInt(100));
			}
		}	
	}
	
	/**
	 * Drill down into child settings and compare against expected.
	 * 
	 * @param original Expected Settings.
	 * @param modified The new Settings.
	 */
	@SuppressWarnings("unchecked")
	private void compareSettings(CompositeSetting original, CompositeSetting modified) {
		for (Setting setting: original) {
			if (setting instanceof CompositeSetting) {
				compareSettings((CompositeSetting)setting, (CompositeSetting)modified.findSetting(setting.getName()));
			} else {
				// Compare the equivalent setting
				ControlSetting<Integer> expected = (ControlSetting<Integer>) setting;
				ControlSetting<Integer> value = (ControlSetting<Integer>) modified.findSetting(expected.getName());
				assertEquals("Expected value not found (" + expected.getName() + ").", expected.getValue(), value.getValue());
			}
		}
	}
	
	/**
	 * Determines whether a String value is numeric.
	 * 
	 * @param str String containing value.
	 * @return Boolean true / false.
	 */
    private boolean isInteger(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException nfe) {}
        return false;
    }
}

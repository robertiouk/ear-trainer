package data;

import java.util.Hashtable;
import java.util.Iterator;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.XMLConfiguration;

import controller.settings.CompositeSetting;
import controller.settings.ControlSetting;
import controller.settings.Setting;

public class XmlConfigController implements ConfigController {
	/** The XML configuration file. */
	XMLConfiguration config = null;
	/** The Setting read from config file. */
	Setting settings = null;
	/** Lookup table that contains XML keys for each Setting. */
	Hashtable<Setting, String> xmlKeyLookup = new Hashtable<Setting, String>();
	
	/**
	 * Load an XML config file and split all keys into Setting objects.
	 * 
	 * @param fileName String value of XML file.
	 */
	@Override
	public void setConfigFile(String fileName) throws ConfigurationException {
		config = new XMLConfiguration(fileName);
		xmlKeyLookup.clear();
		
		Iterator<String> configKeys = config.getKeys();
		
		// Build a list of settings
		settings = new CompositeSetting(config.getRootElementName());
		while (configKeys.hasNext()) {
			String key = configKeys.next();
			
			String[] nodes = key.split("\\.");
			Setting nodeSetting = settings;
			for (String node: nodes) {
				Setting currentRoot = nodeSetting;
				nodeSetting = currentRoot.findSetting(node);
				String value = (String) config.getProperty(key);
				
				// If setting doesn't exist then add it
				if (nodeSetting == null) {
					// Create composite setting or control setting?
					if (node.equals(nodes[nodes.length-1])) {
						if (isInteger(value)) {
							nodeSetting = new ControlSetting<Integer>(Integer.valueOf(value), node);
						} else {
							nodeSetting = new ControlSetting<String>(value, node);
						}
						xmlKeyLookup.put(nodeSetting, key);
					} else {
						nodeSetting = new CompositeSetting(node);
					}
					((CompositeSetting)currentRoot).addSetting(nodeSetting);
				} 
			}
		}
	}
	
	/**
	 * Find setting by given name.
	 * 
	 * @param setting String value. Accepts nested format: "node1.node2.leaf_setting"
	 */
	@Override
	public Setting getSetting(String setting) {
		// Split search string
		String[] nodes = setting.split("\\.");
		
		Setting currentSetting = settings;
		for (String node: nodes) {
			currentSetting = currentSetting.findSetting(node);
			if (currentSetting == null) {
				return null;
			}
		}
		
		return currentSetting;
	}

	/**
	 * Updates an XML config file and then saves the file with given
	 * Setting update.
	 * 
	 * @param setting The Setting to write.
	 */
	@Override
	public void writeConfig(Setting setting) throws ConfigurationException {
		updateConfigFile(setting);
		
		// Save config
		config.save();
	}
	
	/**
	 * Takes either a CompositeSetting or ControlSetting and updates
	 * XML config file.
	 * 
	 * @param setting The Setting to write.
	 */
	private void updateConfigFile(Setting setting) {
		// Get setting type
		if (setting instanceof ControlSetting<?>) {
			// Write the leaf node setting
			String key = xmlKeyLookup.get(setting);
			config.setProperty(key, ((ControlSetting<?>) setting).getValue());
		} else {
			// Write all root nodes
			CompositeSetting settingNode = (CompositeSetting) setting;
			// Run through settings
			for (Setting currentSetting: settingNode) {
				updateConfigFile(currentSetting);
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

package data;

import org.apache.commons.configuration.ConfigurationException;

import controller.settings.Setting;

/**
 * Interface to define the behaviour for a configuration file reader.
 * 
 * @author Robertio
 *
 */
public interface ConfigReader {
	public void setConfigFile(String fileName) throws ConfigurationException;
	public Setting getSetting(String setting);
}

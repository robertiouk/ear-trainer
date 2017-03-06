package data;

import org.apache.commons.configuration.ConfigurationException;

import controller.settings.Setting;

/**
 * Interface to define behaviour for configuration file writer.
 * 
 * @author Robertio
 *
 */
public interface ConfigWriter {
	public void setConfigFile(String fileName) throws ConfigurationException;
	public void writeConfig(Setting setting) throws ConfigurationException;
}

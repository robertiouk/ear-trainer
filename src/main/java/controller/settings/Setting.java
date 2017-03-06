package controller.settings;

/**
 * Composite design pattern 'component' interface for holding application settings.
 * 
 * @author Robertio
 *
 */
public interface Setting {
	public String getName();
	public Setting findSetting(String name);
}

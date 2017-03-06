package controller.settings;

import controller.exceptions.MissingSettingException;

/**
 * Leaf implementation of Setting interface.
 * 
 * @author Robertio
 *
 * @param <T> Generic type for this setting.
 */
public class ControlSetting<T> implements Setting {
	private T value = null;
	private String name = null;
		
	/**
	 * Constructor for ControlSetting.
	 * 
	 * @param value T - The generic value of the Setting.
	 * @param name String - The Setting name.
	 */
	public ControlSetting(T value, String name) {
		this.value = value;
		this.name = name;
	}
	
	/**
	 * Return the name of this Setting.
	 */
	@Override
	public String getName() {
		return name;
	}
	
	/**
	 * Return the value of this Setting.
	 * 
	 * @return T - Generic value.
	 */
	public T getValue() {
		return value;
	}
	
	/**
	 * Set a new value.
	 * 
	 * @param value T - new value.
	 */
	public void setValue(T value) {
		this.value = value;
	}

	/**
	 * Compares name against search term.
	 * 
	 * @param name String - The name of the required Setting.
	 * @return Setting Return this if name matches, null if not.
	 */
	@Override
	public Setting findSetting(String name) throws MissingSettingException {
		if (name.equals(this.name)) {
			return this;
		} else {
			return null;
		}
	}
}

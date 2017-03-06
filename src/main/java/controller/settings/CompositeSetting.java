package controller.settings;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Composite implementation of Setting interface.
 * 
 * @author Robertio
 *
 */
public class CompositeSetting implements Setting, Iterable<Setting> {
	private List<Setting> settings = null;
	private String name = null;
	
	/**
	 * Constructor for CompositeSetting.
	 * 
	 * @param name String - Setting name.
	 */
	public CompositeSetting(String name) {
		settings = new ArrayList<Setting>();
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
	 * Add a child setting.
	 * 
	 * @param setting - Setting to add to list of children.
	 */
	public void addSetting(Setting setting) {
		settings.add(setting);
	}

	/**
	 * Find setting by name. If this setting isn't it then search children.
	 * 
	 * @param name String - Setting name to find.
	 * @return Setting - Return the result if found, null if not.
	 */
	@Override
	public Setting findSetting(String name) {
		if (this.name.equals(name)) {
			return this;
		} else {
			for (Setting setting: settings) {
				Setting result = setting.findSetting(name);
				if (result != null) {
					return result;
				}
			}
		}
		// Didn't find it!
		return null;
	}

	/**
	 * Returns the Iterator from settings.
	 * 
	 * @return Iterator from settings.
	 */
	@Override
	public Iterator<Setting> iterator() {
		return settings.iterator();
	}
	
	/**
	 * Retruns the size of the settings list.
	 * 
	 * @return Integer size of settings list.
	 */
	public int size() {
		return settings.size();
	}
}

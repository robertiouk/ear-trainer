package controller;

import controller.settings.CompositeSetting;
import controller.settings.ControlSetting;

public class TestController implements MenuController {
	public static void main(String[] args) {
		CompositeSetting appSettings = new CompositeSetting("Application");
		CompositeSetting intervalSettings = new CompositeSetting("Intervals");
		CompositeSetting cadenceSettings = new CompositeSetting("Cadences");
		
		ControlSetting<String> intSetting1 = new ControlSetting<String>("Interval", "Title");
		ControlSetting<Integer> intSetting2 = new ControlSetting<Integer>(600, "Note Duration");
		intervalSettings.addSetting(intSetting1);
		intervalSettings.addSetting(intSetting2);
		
		appSettings.addSetting(intervalSettings);
		appSettings.addSetting(cadenceSettings);
		
		System.out.println(appSettings.getName());
		/*Setting result = appSettings.findSetting("Cadences");
		if (result == null) {
			System.out.println("Could not find setting!");
		} else {
			//ControlSetting<Integer> duration = (ControlSetting<Integer>) result;
			//System.out.println(duration.getName() + ": " + duration.getValue());
			System.out.println(result.getName());
		}*/
	}
}

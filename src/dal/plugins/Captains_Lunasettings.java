package dal.plugins;

import lunalib.lunaSettings.LunaSettings;

public class Captains_Lunasettings {
	public static void init() {
		LunaSettings.addSettingsListener(new qcDynamicSettings());
	}
}

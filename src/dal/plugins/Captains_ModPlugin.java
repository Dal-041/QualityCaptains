package dal.plugins;

import org.json.JSONException;

import com.fs.starfarer.api.BaseModPlugin;
import com.fs.starfarer.api.Global;
import dal.impl.campaign.skills.CaptainsAutomatedShips;
import dal.impl.campaign.skills.CaptainsSupportDoctrine;

public class Captains_ModPlugin extends BaseModPlugin {
	public static final String ModID = "QualityCaptains";
	
	@Override
	public void onApplicationLoad() {
		try {
			Captains_Utils.parseModSettings();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		Captains_Utils.loadQualityConfig();
		Captains_Utils.blankMilestones();
		if (Global.getSettings().getModManager().isModEnabled("lunalib")) {
			qcDynamicSettings temp = new qcDynamicSettings();
			Captains_Lunasettings.init();
			temp.reloadQualityConfig();
		}
	}
	
	@Override
	public void onGameLoad(boolean newGame) {
		boolean QCInstalled = Captains_Utils.getBool("qc_installed");
		if (QCInstalled) {
			CaptainsSupportDoctrine.recalcDPReductionCap();
			CaptainsAutomatedShips.refreshThreshold();
			Captains_Utils.installQC();
			Captains_Utils.blankMilestones();
			Captains_Utils.refreshMilestones();
		} else {
			Captains_Utils.uninstallQC();
		}
	}

	@Override
	public void onNewGameAfterEconomyLoad() {
		boolean QCInstalled = Captains_Utils.getBool("qc_installed");
		if (QCInstalled) {
			Captains_Utils.installQC();
			CaptainsSupportDoctrine.recalcDPReductionCap();
			CaptainsAutomatedShips.refreshThreshold();
		} else {
			Captains_Utils.uninstallQC();
		}
	}
}
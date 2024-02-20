package dal.impl.campaign.skills;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.characters.CharacterStatsSkillEffect;
import com.fs.starfarer.api.characters.MutableCharacterStatsAPI;
import com.fs.starfarer.api.loading.HullModSpecAPI;

public class CaptainsVayraSkills {
	public static boolean hasVSP = false;
	private static String hullmodID = "vayra_collapsedcargo";
	
	public static class hullmodCCH implements CharacterStatsSkillEffect {
		
		//@Override
		public void apply(MutableCharacterStatsAPI stats, String id, float level) {
			if (hasVSP) Global.getSector().getPlayerFaction().addKnownHullMod(hullmodID);
		}

		//@Override
		public void unapply(MutableCharacterStatsAPI stats, String id) {
		}
		
		//@Override
		public String getEffectDescription(float level) {
			if (hasVSP) {
				HullModSpecAPI hmCCH = Global.getSettings().getHullModSpec("vayra_collapsedcargo");
				if (hmCCH != null) return "Hull mod: " + hmCCH.getDisplayName() + " - Collapses cargo holds into makeshift armor";
			}
			return "";
		}
		
		//@Override
		public String getEffectPerLevelDescription() {
			return null;
		}

		//@Override
		public ScopeDescription getScopeDescription() {
			return ScopeDescription.NONE;
		}
	}
}

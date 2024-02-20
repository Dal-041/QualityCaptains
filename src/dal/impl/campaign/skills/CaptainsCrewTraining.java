package dal.impl.campaign.skills;

import com.fs.starfarer.api.characters.MutableCharacterStatsAPI;
import com.fs.starfarer.api.characters.ShipSkillEffect;
import com.fs.starfarer.api.characters.SkillSpecAPI;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;
import com.fs.starfarer.api.impl.campaign.skills.BaseSkillEffectDescription;
import com.fs.starfarer.api.ui.TooltipMakerAPI;

public class CaptainsCrewTraining {
	
	public static int PEAK_SECONDS = 30;
	public static int CR_PERCENT = 15;
	
	public static class Level1 extends BaseSkillEffectDescription implements ShipSkillEffect {
		
		public void apply(MutableShipStatsAPI stats, HullSize hullSize, String id, float level) {
			if (!isCivilian(stats)) {
				stats.getMaxCombatReadiness().modifyFlat(id, CR_PERCENT * 0.01f, "Crew training skill");
			}
		}
		
		public void unapply(MutableShipStatsAPI stats, HullSize hullSize, String id) {
			stats.getMaxCombatReadiness().unmodify(id);
		}
		
		public String getEffectDescription(float level) {
			return null;
		}
			
		public void createCustomDescription(MutableCharacterStatsAPI stats, SkillSpecAPI skill, 
											TooltipMakerAPI info, float width) {
			init(stats, skill);
			
			info.addPara("+%s maximum combat readiness for combat ships", 0f, hc, hc,
					"" + (int) CR_PERCENT + "%");
			//addOPThresholdInfo(info, data, stats, OP_THRESHOLD);
		}
	}
	public static class Level2 implements ShipSkillEffect {
		
		public void apply(MutableShipStatsAPI stats, HullSize hullSize, String id, float level) {
			if (!BaseSkillEffectDescription.isCivilian(stats)) {
				stats.getPeakCRDuration().modifyFlat(id, PEAK_SECONDS);
			}
		}
		
		public void unapply(MutableShipStatsAPI stats, HullSize hullSize, String id) {
			stats.getPeakCRDuration().unmodifyFlat(id);
		}
		
		public String getEffectDescription(float level) {
			return "+" + PEAK_SECONDS + " seconds peak operating time for combat ships";
		}

		public String getEffectPerLevelDescription() {
			return null;
		}

		public ScopeDescription getScopeDescription() {
			return ScopeDescription.ALL_SHIPS;
		}
	}
}






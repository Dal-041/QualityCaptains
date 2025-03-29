package dal.impl.campaign.skills;

import java.awt.Color;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.FleetDataAPI;
import com.fs.starfarer.api.characters.CharacterStatsSkillEffect;
import com.fs.starfarer.api.characters.DescriptionSkillEffect;
import com.fs.starfarer.api.characters.FleetTotalItem;
import com.fs.starfarer.api.characters.FleetTotalSource;
import com.fs.starfarer.api.characters.MutableCharacterStatsAPI;
import com.fs.starfarer.api.characters.ShipSkillEffect;
import com.fs.starfarer.api.characters.SkillSpecAPI;
import com.fs.starfarer.api.characters.LevelBasedEffect.ScopeDescription;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;
import com.fs.starfarer.api.impl.campaign.AICoreOfficerPluginImpl;
import com.fs.starfarer.api.impl.campaign.ids.Skills;
import com.fs.starfarer.api.impl.campaign.ids.Strings;
import com.fs.starfarer.api.impl.campaign.ids.Tags;
import com.fs.starfarer.api.impl.campaign.skills.AutomatedShips;
import com.fs.starfarer.api.impl.campaign.skills.BaseSkillEffectDescription;
import com.fs.starfarer.api.impl.hullmods.Automated;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;

public class CaptainsAutomatedShips extends AutomatedShips {
	
	public static boolean USE_AUTOMATED_LIMITS = true;
	public static boolean USE_AUTOMATED_BATTLE_SCALING = true;
	public static boolean USE_AUTOMATED_DYNAMIC_SCALING = false;
	public static boolean USE_AUTOMATED_LEVEL_SCALING = false;

	public static float BATTLESIZE = Global.getSettings().getBattleSize();
	public static float THRESHOLD_DIVISOR = 10f;
	public static float AUTOMATED_POINTS_THRESHOLD_FULL = 120;
	//public static float BATTLESIZE_MULT = BATTLESIZE/300;
	//public static float DP_THRESHOLD = 30;
	//public static float FINAL_THRESHOLD = (DP_THRESHOLD*BATTLESIZE_MULT);
	
	public static float MAX_CR_BONUS = 80f;

	public static void refreshThreshold() {

		if (USE_AUTOMATED_BATTLE_SCALING) {
			BATTLESIZE = Global.getSettings().getBattleSize();
			BaseSkillEffectDescription.AUTOMATED_POINTS_THRESHOLD = (BATTLESIZE / THRESHOLD_DIVISOR);
		}
		else if (USE_AUTOMATED_LEVEL_SCALING) {
			if (Global.getSector() != null && Global.getSector().getPlayerStats() != null) {
				BaseSkillEffectDescription.AUTOMATED_POINTS_THRESHOLD = AUTOMATED_POINTS_THRESHOLD_FULL * 
					((float)Global.getSector().getPlayerStats().getLevel() / (float)Global.getSettings().getLevelupPlugin().getMaxLevel());
			} else {
				BaseSkillEffectDescription.AUTOMATED_POINTS_THRESHOLD = AUTOMATED_POINTS_THRESHOLD_FULL * 
						(1f / (float)Global.getSettings().getLevelupPlugin().getMaxLevel());
			}
		}
		else {
			//LoadQualityTechv2 initializes this to the custom value.
			//BaseSkillEffectDescription.AUTOMATED_POINTS_THRESHOLD = AUTOMATED_POINTS_THRESHOLD_FULL;
		}
	}
	
	
	public static class Level0 implements DescriptionSkillEffect {
		public String getString() {
			refreshThreshold();
			int alpha = Math.round(AICoreOfficerPluginImpl.ALPHA_MULT);
			int beta = Math.round(AICoreOfficerPluginImpl.BETA_MULT);
			int gamma = Math.round(AICoreOfficerPluginImpl.GAMMA_MULT);
			//if (BaseSkillEffectDescription.USE_RECOVERY_COST) {
			if (USE_AUTOMATED_LIMITS) {
				return "*The total \"automated ship points\" are equal to the deployment points cost of " +
						"all automated ships in the fleet, with a multiplier for installed AI cores - " +
						alpha + Strings.X + " for an Alpha Core, " +			
						beta + Strings.X + " for a Beta Core, and " +			
						gamma + Strings.X + " for a Gamma Core. "
						+ "Due to safety interlocks, ships with AI cores do not contribute to the deployment point distribution.";
			} else {
				return "There is no limit on the number of automated ships you can deploy. Due to safety interlocks, ships with AI cores do not contribute to the deployment point distribution.";
			}
		}
		public Color[] getHighlightColors() {
			Color h = Misc.getHighlightColor();
			h = Misc.getDarkHighlightColor();
			Color bad = Misc.getNegativeHighlightColor();
			//bad = Misc.setAlpha(bad, 200);
			return new Color[] {h, h, h, bad};
		}
		public String[] getHighlights() {
			int alpha = (int) Math.round(AICoreOfficerPluginImpl.ALPHA_MULT);
			int beta = (int) Math.round(AICoreOfficerPluginImpl.BETA_MULT);
			int gamma = (int) Math.round(AICoreOfficerPluginImpl.GAMMA_MULT);
			return new String [] {"" + alpha + Strings.X, "" + beta + Strings.X, "" + gamma + Strings.X, 
					"do not contribute to the deployment point distribution"};
		}
		public Color getTextColor() {
			return null;
		}
	}
	
	
	public static class Level1 extends BaseSkillEffectDescription implements ShipSkillEffect, FleetTotalSource {
		
		public FleetTotalItem getFleetTotalItem() {
			return getAutomatedPointsTotal();
		}
		
		public void apply(MutableShipStatsAPI stats, HullSize hullSize, String id, float level) {
			refreshThreshold();
			if (Misc.isAutomated(stats) && !Automated.isAutomatedNoPenalty(stats)) {
				float crBonus = 0f;
				if (USE_AUTOMATED_LIMITS) {
					crBonus = computeAndCacheThresholdBonus(stats, "auto_cr", MAX_CR_BONUS, ThresholdBonusType.AUTOMATED_POINTS);
				} else {
					crBonus = MAX_CR_BONUS;
				}
				SkillSpecAPI skill = Global.getSettings().getSkillSpec(Skills.AUTOMATED_SHIPS);
				stats.getMaxCombatReadiness().modifyFlat(id, crBonus * 0.01f, skill.getName() + " skill");
			}
		}
			
		public void unapply(MutableShipStatsAPI stats, HullSize hullSize, String id) {
			stats.getMaxCombatReadiness().unmodifyFlat(id);
		}
		
		public String getEffectDescription(float level) {
			return null;
		}
			
		public void createCustomDescription(MutableCharacterStatsAPI stats, SkillSpecAPI skill, 
											TooltipMakerAPI info, float width) {
			init(stats, skill);
			
			FleetDataAPI data = getFleetData(null);
			float crBonus = computeAndCacheThresholdBonus(data, stats, "auto_cr", MAX_CR_BONUS, ThresholdBonusType.AUTOMATED_POINTS);
			
			if (USE_AUTOMATED_LIMITS) {
				info.addPara("+%s automated ship combat readiness (maximum bonus: %s)", 0f, hc, hc,
					"" + (int) crBonus + "%",
					"" + (int) MAX_CR_BONUS + "%");
				addAutomatedThresholdInfo(info, data, stats);
			} else {
				info.addPara("Automated ships can restore up to %s combat readiness", 0f, hc, hc,
						"" + (int) MAX_CR_BONUS + "%");
				//addAutomatedThresholdInfo(info, data, stats);
			}
		}
		
		public ScopeDescription getScopeDescription() {
			return ScopeDescription.ALL_SHIPS;
		}
	}
	
	public static class Level2f extends BaseSkillEffectDescription implements CharacterStatsSkillEffect {

		public void apply(MutableCharacterStatsAPI stats, String id, float level) {
			if (stats.isPlayerStats()) {
				Misc.getAllowedRecoveryTags().add(Tags.AUTOMATED_RECOVERABLE);
			}
		}

		public void unapply(MutableCharacterStatsAPI stats, String id) {
			if (stats.isPlayerStats()) {
				Misc.getAllowedRecoveryTags().remove(Tags.AUTOMATED_RECOVERABLE);
			}
		}
		
		public void createCustomDescription(MutableCharacterStatsAPI stats, SkillSpecAPI skill, 
				TooltipMakerAPI info, float width) {
			init(stats, skill);
			info.addPara("Enables the recovery of some automated ships, such as derelict drones", hc, 0f);
			info.addPara("Automated ships can only be captained by AI cores", hc, 0f);
			info.addSpacer(5f);
		}
		
		public String getEffectPerLevelDescription() {
			return null;
		}

		public ScopeDescription getScopeDescription() {
			return ScopeDescription.FLEET;
		}
	}
	
	public static class Level2 extends BaseSkillEffectDescription implements CharacterStatsSkillEffect {

		public void apply(MutableCharacterStatsAPI stats, String id, float level) {
			if (stats.isPlayerStats()) {
				Misc.getAllowedRecoveryTags().add(Tags.AUTOMATED_RECOVERABLE);
			}
		}

		public void unapply(MutableCharacterStatsAPI stats, String id) {
			if (stats.isPlayerStats()) {
				Misc.getAllowedRecoveryTags().remove(Tags.AUTOMATED_RECOVERABLE);
			}
		}
		
		public void createCustomDescription(MutableCharacterStatsAPI stats, SkillSpecAPI skill, 
				TooltipMakerAPI info, float width) {
			init(stats, skill);
			info.addPara("Enables the recovery of automated ships, such as derelict drones", hc, 0f);
			info.addPara("Automated ships can only be captained by AI cores", hc, 0f);
			if (USE_AUTOMATED_LIMITS && USE_AUTOMATED_BATTLE_SCALING) { 
				info.addPara("Your automated ship limit is determined by your battlesize setting / " + Math.round(THRESHOLD_DIVISOR), hc, 0f); 
			} else if (USE_AUTOMATED_LIMITS) {
				info.addPara("Your automated ship limit is " + (int)BaseSkillEffectDescription.AUTOMATED_POINTS_THRESHOLD, hc, 0f); 
			}
			if (USE_AUTOMATED_LIMITS && USE_AUTOMATED_LEVEL_SCALING) {
				info.addPara("Your automated ship limit is proportional to your level: " + (int)BaseSkillEffectDescription.AUTOMATED_POINTS_THRESHOLD + " (maximum: " + (int)AUTOMATED_POINTS_THRESHOLD_FULL + ")", hc, 0f);
			}
			info.addSpacer(5f);
		}
		
		public String getEffectPerLevelDescription() {
			return null;
		}

		public ScopeDescription getScopeDescription() {
			return ScopeDescription.FLEET;
		}
	}

}






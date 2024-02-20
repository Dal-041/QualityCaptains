package dal.impl.campaign.skills;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.FleetDataAPI;
import com.fs.starfarer.api.characters.CharacterStatsSkillEffect;
import com.fs.starfarer.api.characters.FleetTotalItem;
import com.fs.starfarer.api.characters.FleetTotalSource;
import com.fs.starfarer.api.characters.MutableCharacterStatsAPI;
import com.fs.starfarer.api.characters.ShipSkillEffect;
import com.fs.starfarer.api.characters.SkillSpecAPI;
import com.fs.starfarer.api.characters.LevelBasedEffect.ScopeDescription;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;
import com.fs.starfarer.api.impl.campaign.ids.HullMods;
import com.fs.starfarer.api.impl.campaign.ids.Stats;
import com.fs.starfarer.api.impl.campaign.skills.BaseSkillEffectDescription;
import com.fs.starfarer.api.loading.HullModSpecAPI;
import com.fs.starfarer.api.ui.TooltipMakerAPI;

public class CaptainsAuxiliarySupport {
	
	public static float AUXILIARY_EFFECT_BONUS = 100; //900
	public static float CIVILIAN_UPKEEP_REDUCTION = 20;
	public static float CIVILIAN_FUEL_REDUCTION = 10;
	
	public static int BURN_BONUS = 1;
	
	public static class Level1f extends BaseSkillEffectDescription implements CharacterStatsSkillEffect, FleetTotalSource {
		
		public FleetTotalItem getFleetTotalItem() {
			return getMilitarizedOPTotal();
		}
		
		public void apply(MutableCharacterStatsAPI stats, String id, float level) {
			FleetDataAPI data = null;
			if (stats.getFleet() != null) data = stats.getFleet().getFleetData();
			float auxBonus = computeAndCacheThresholdBonus(data, stats, "aux_effect", AUXILIARY_EFFECT_BONUS, ThresholdBonusType.MILITARIZED_OP);
			stats.getDynamic().getMod(Stats.AUXILIARY_EFFECT_ADD_PERCENT).modifyFlat(id, AUXILIARY_EFFECT_BONUS);
		}
		public void unapply(MutableCharacterStatsAPI stats, String id) {
			stats.getDynamic().getMod(Stats.AUXILIARY_EFFECT_ADD_PERCENT).unmodifyFlat(id);
		}
		
		public String getEffectDescription(float level) {
			return null;
		}
			
		public void createCustomDescription(MutableCharacterStatsAPI stats, SkillSpecAPI skill, 
											TooltipMakerAPI info, float width) {
			init(stats, skill);
			
			FleetDataAPI data = getFleetData(null);
			float auxBonus = computeAndCacheThresholdBonus(data, stats, "aux_effect", AUXILIARY_EFFECT_BONUS, ThresholdBonusType.MILITARIZED_OP);
			
			HullModSpecAPI mil = Global.getSettings().getHullModSpec(HullMods.MILITARIZED_SUBSYSTEMS);
			//HullModSpecAPI ep = Global.getSettings().getHullModSpec(HullMods.ESCORT_PACKAGE);
			//HullModSpecAPI ap = Global.getSettings().getHullModSpec(HullMods.ASSAULT_PACKAGE);
			
			/*
			info.addPara("+%s to combat effects of " + mil.getDisplayName() + ", " +
					ep.getDisplayName() + ", and " + ap.getDisplayName() + " (maximum: %s)", 0f, hc, hc,
					"" + (int) auxBonus + "%",
					"" + (int) AUXILIARY_EFFECT_BONUS + "%");
			*/
			info.addPara("This skill is defunct.", 0f);
			addMilitarizedOPThresholdInfo(info, data, stats);
			
			//info.addSpacer(5f);
		}
		
	}
	
	public static class Level1 extends BaseSkillEffectDescription implements CharacterStatsSkillEffect {
		
		public void apply(MutableCharacterStatsAPI stats, String id, float level) {
			FleetDataAPI data = null;
			if (stats.getFleet() != null) data = stats.getFleet().getFleetData();
			//float auxBonus = computeAndCacheThresholdBonus(data, stats, "aux_effect", AUXILIARY_EFFECT_BONUS, ThresholdBonusType.MILITARIZED_OP);
			stats.getDynamic().getMod(Stats.AUXILIARY_EFFECT_ADD_PERCENT).modifyFlat(id, AUXILIARY_EFFECT_BONUS);
		}
		public void unapply(MutableCharacterStatsAPI stats, String id) {
			stats.getDynamic().getMod(Stats.AUXILIARY_EFFECT_ADD_PERCENT).unmodifyFlat(id);
		}
		
		public String getEffectDescription(float level) {
			return null;
		}
			
		public void createCustomDescription(MutableCharacterStatsAPI stats, SkillSpecAPI skill, 
											TooltipMakerAPI info, float width) {
			init(stats, skill);
			
			FleetDataAPI data = getFleetData(null);
			//float auxBonus = computeAndCacheThresholdBonus(data, stats, "aux_effect", AUXILIARY_EFFECT_BONUS, ThresholdBonusType.MILITARIZED_OP);
			
			HullModSpecAPI mil = Global.getSettings().getHullModSpec(HullMods.MILITARIZED_SUBSYSTEMS);
			//HullModSpecAPI ep = Global.getSettings().getHullModSpec(HullMods.ESCORT_PACKAGE);
			//HullModSpecAPI ap = Global.getSettings().getHullModSpec(HullMods.ASSAULT_PACKAGE);
			
			/*
			info.addPara("+%s to combat effects of " + mil.getDisplayName() + ", " +
					ep.getDisplayName() + ", and " + ap.getDisplayName() + "", 0f, hc, hc,
					"" + (int) AUXILIARY_EFFECT_BONUS + "%");
			*/
			info.addPara("This skill is defunct.", 0f);
			//addMilitarizedOPThresholdInfo(info, data, stats);
			
			//info.addSpacer(5f);
		}
		
	}
	
	public static class Level2 extends BaseSkillEffectDescription implements ShipSkillEffect {
		public void apply(MutableShipStatsAPI stats, HullSize hullSize, String id, float level) {
			if(isCivilian(stats)) {
				stats.getSuppliesPerMonth().modifyMult(id, 1f - (float)(CIVILIAN_UPKEEP_REDUCTION/100f));
				stats.getFuelUseMod().modifyMult(id, 1f - (float)(CIVILIAN_UPKEEP_REDUCTION/200f));
			}
		}

		public void unapply(MutableShipStatsAPI stats, HullSize hullSize, String id) {
			if(isCivilian(stats)) {
				stats.getSuppliesPerMonth().unmodify(id);
			}
		}
		
		public String getEffectDescription(float level) {
			return "-" + (int)(CIVILIAN_UPKEEP_REDUCTION) + "% supply use for maintenance and -" + Math.round(CIVILIAN_FUEL_REDUCTION) + "% fuel burned per LY for civilian ships";
		}
		
		public String getEffectPerLevelDescription() {
			return null;
		}
		
		public boolean hasCustomDescription() {
			return false;
		}

		public ScopeDescription getScopeDescription() {
			return ScopeDescription.ALL_SHIPS;
		}
	}
	
	public static class Level3 extends BaseSkillEffectDescription implements ShipSkillEffect {
		public void apply(MutableShipStatsAPI stats, HullSize hullSize, String id, float level) {
			if (isCivilian(stats)) {
				stats.getMaxBurnLevel().modifyFlat(id, BURN_BONUS);
			}
		}
			
		public void unapply(MutableShipStatsAPI stats, HullSize hullSize, String id) {
			stats.getMaxBurnLevel().unmodifyFlat(id);
		}
		
		@Override
		public boolean hasCustomDescription() {
			return false;
		}

		public String getEffectDescription(float level) {
			return "Increases the burn level of all non-militarized civilian-grade ships by " + BURN_BONUS;
		}
			
		public ScopeDescription getScopeDescription() {
			return ScopeDescription.ALL_SHIPS;
		}
	}

}






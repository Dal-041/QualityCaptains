package dal.impl.campaign.skills;

import com.fs.starfarer.api.characters.FleetStatsSkillEffect;
import com.fs.starfarer.api.characters.ShipSkillEffect;
import com.fs.starfarer.api.characters.LevelBasedEffect.ScopeDescription;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;
import com.fs.starfarer.api.fleet.FleetMemberAPI;
import com.fs.starfarer.api.fleet.MutableFleetStatsAPI;
import com.fs.starfarer.api.impl.campaign.DModManager;
import com.fs.starfarer.api.impl.campaign.ids.Stats;

public class CaptainsMakeshiftEquipment {
	
	public static float MINING_VALUE_MULT = 1.5f;
	
	public static float SURVEY_COST_MULT = 0.5f;
	public static float SURVEY_COST_MULT_PERC = 50;
	
	//public static float UPKEEP_MULT = 0.8f;
	
	public static float DIRECT_JUMP_DISCOUNT = 90;
	
	public static float DMOD_DISCOUNT_MULT = 0.5f;
	
	//public static float HULL_DAMAGE_CR_LOSS_REDUCTION = 40;
	
	
	
	public static class Level1 implements FleetStatsSkillEffect {
		public void apply(MutableFleetStatsAPI stats, String id, float level) {
			String desc = "Surveying skill";
			stats.getDynamic().getStat(Stats.SURVEY_COST_MULT).modifyMult(id, (float)(SURVEY_COST_MULT_PERC / 100f), desc);
		}

		public void unapply(MutableFleetStatsAPI stats, String id) {
			stats.getDynamic().getStat(Stats.SURVEY_COST_MULT).unmodifyMult(id);
		}

		public String getEffectDescription(float level) {
			return "-" + (int) Math.round((float)(100f - SURVEY_COST_MULT_PERC)) + "% resources required to survey planets";
		}

		public String getEffectPerLevelDescription() {
			return null;
		}

		public ScopeDescription getScopeDescription() {
			return ScopeDescription.FLEET;
		}
	}
	
	
	public static class Level2 implements FleetStatsSkillEffect {
		public void apply(MutableFleetStatsAPI stats, String id, float level) {
			String desc = "jury-rigging discount";
			stats.getDynamic().getStat(Stats.DIRECT_JUMP_CR_MULT).modifyMult(id, (float)(1f - DIRECT_JUMP_DISCOUNT / 100f), desc);
		}

		public void unapply(MutableFleetStatsAPI stats, String id) {
			stats.getDynamic().getStat(Stats.DIRECT_JUMP_CR_MULT).unmodifyMult(id);
		}

		public String getEffectDescription(float level) {
			return "-" + (int) Math.round((float)(DIRECT_JUMP_DISCOUNT)) + "% CR penalty for transverse jumping into hyperspace (ability tooltip will not update)";
		}

		public String getEffectPerLevelDescription() {
			return null;
		}

		public ScopeDescription getScopeDescription() {
			return ScopeDescription.FLEET;
		}
	}
	
	public static class Level2f implements FleetStatsSkillEffect {
		public void apply(MutableFleetStatsAPI stats, String id, float level) {
			String desc = "Surveying skill";
			stats.getDynamic().getStat(Stats.PLANET_MINING_VALUE_MULT).modifyMult(id, MINING_VALUE_MULT, desc);
		}
		
		public void unapply(MutableFleetStatsAPI stats, String id) {
			stats.getDynamic().getStat(Stats.PLANET_MINING_VALUE_MULT).unmodify(id);
		}
		
		public String getEffectDescription(float level) {
			return "+" + (int) Math.round((1f - MINING_VALUE_MULT) * 100f) + "% resources extracted from surface deposits on uncolonized planets";
		}
		
		public String getEffectPerLevelDescription() {
			return null;
		}
		
		public ScopeDescription getScopeDescription() {
			return ScopeDescription.FLEET;
		}
	}
	
	public static class Level3 implements ShipSkillEffect {
		public void apply(MutableShipStatsAPI stats, HullSize hullSize, String id, float level) {
			stats.getDynamic().getMod(Stats.DMOD_REDUCE_MAINTENANCE).modifyFlat(id, 1f);
			FleetMemberAPI member = stats.getFleetMember();
			float discount = 0f;
			float dmodsN = 0;
			if (member != null && member.getVariant() != null) {
				dmodsN = DModManager.getNumNonBuiltInDMods(member.getVariant());
			}
			discount = (25 * dmodsN);
			if (dmodsN > 0) {
				stats.getSuppliesPerMonth().modifyMult(id, 1 + discount * (1f - DMOD_DISCOUNT_MULT)/100);
			}
		}
		
		public void unapply(MutableShipStatsAPI stats, HullSize hullSize, String id) {
			stats.getDynamic().getMod(Stats.DMOD_REDUCE_MAINTENANCE).unmodify(id);
			stats.getSuppliesPerMonth().unmodifyMult(id);
		}	
		
		public String getEffectDescription(float level) {
			return Math.round(DMOD_DISCOUNT_MULT * 100f) + "% of (D) hull deployment cost reduction also applies to maintenance cost" ;
		}
		
		public String getEffectPerLevelDescription() {
			return null;
		}
		
		public ScopeDescription getScopeDescription() {
			return ScopeDescription.ALL_SHIPS;
		}
	}
	
	
}




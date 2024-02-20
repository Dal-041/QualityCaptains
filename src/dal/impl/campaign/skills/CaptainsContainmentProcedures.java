package dal.impl.campaign.skills;

import com.fs.starfarer.api.characters.FleetStatsSkillEffect;
import com.fs.starfarer.api.characters.ShipSkillEffect;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;
import com.fs.starfarer.api.fleet.MutableFleetStatsAPI;
import com.fs.starfarer.api.impl.campaign.ids.Stats;

public class CaptainsContainmentProcedures {
	
	public static float CREW_LOSS_REDUCTION_NON_COMBAT = 50f;
	public static float CREW_LOSS_REDUCTION_COMBAT = 40f;
	public static float FUEL_SALVAGE_BONUS = 50f;
	public static int FUEL_USE_REDUCTION_PERC = 25;
	
	public static float CORONA_EFFECT_MULT = 0.5f;
	public static int CORONA_EFFECT_PERC_MULT = 50;
	
	public static class Level2 implements FleetStatsSkillEffect {
		public void apply(MutableFleetStatsAPI stats, String id, float level) {
			stats.getDynamic().getStat(Stats.EMERGENCY_BURN_CR_MULT).modifyMult(id, 0f);
		}
		
		public void unapply(MutableFleetStatsAPI stats, String id) {
			stats.getDynamic().getStat(Stats.EMERGENCY_BURN_CR_MULT).unmodify(id);
		}	
		
		public String getEffectDescription(float level) {
			return "The \"Emergency Burn\" ability no longer reduces combat readiness";
		}
		
		public String getEffectPerLevelDescription() {
			return null;
		}
		
		public ScopeDescription getScopeDescription() {
			return ScopeDescription.FLEET;
		}
	}
	
	public static class Level3 implements FleetStatsSkillEffect {
		public void apply(MutableFleetStatsAPI stats, String id, float level) {
			stats.getDynamic().getStat(Stats.FUEL_SALVAGE_VALUE_MULT_FLEET).modifyFlat(id, FUEL_SALVAGE_BONUS * 0.01f);
		}
		
		public void unapply(MutableFleetStatsAPI stats, String id) {
			stats.getDynamic().getStat(Stats.FUEL_SALVAGE_VALUE_MULT_FLEET).unmodify(id);
		}
		
		public String getEffectDescription(float level) {
			return "";
		}
		
		public String getEffectPerLevelDescription() {
			return null;
		}
		
		public ScopeDescription getScopeDescription() {
			return ScopeDescription.FLEET;
		}
	}
	
	public static class Level4 implements ShipSkillEffect {
		public void apply(MutableShipStatsAPI stats, HullSize hullSize, String id, float level) {
			stats.getFuelUseMod().modifyMult(id, 1f - ((float)FUEL_USE_REDUCTION_PERC / 100f));
		}
		
		public void unapply(MutableShipStatsAPI stats, HullSize hullSize, String id) {
			stats.getFuelUseMod().unmodifyMult(id);
		}
		
		public String getEffectDescription(float level) {
			return "-" + FUEL_USE_REDUCTION_PERC + "% fuel consumed by travel and +" + FUEL_SALVAGE_BONUS + "% more fuel salvaged";
		}
		
		public ScopeDescription getScopeDescription() {
			return ScopeDescription.FLEET;
		}

		public String getEffectPerLevelDescription() {
			return null;
		}

	}
	
	public static class Level1 implements ShipSkillEffect {
		public void apply(MutableShipStatsAPI stats, HullSize hullSize, String id, float level) {
			stats.getCrewLossMult().modifyMult(id, 1f - CREW_LOSS_REDUCTION_COMBAT / 100f);
		}
		
		public void unapply(MutableShipStatsAPI stats, HullSize hullSize, String id) {
			stats.getCrewLossMult().unmodify(id);
		}	
		
		public String getEffectDescription(float level) {
			return "-" + (int)(CREW_LOSS_REDUCTION_COMBAT) + "% crew lost in combat and -" + (int)(CREW_LOSS_REDUCTION_NON_COMBAT) + "% crew lost in non-combat operations";
		}
		
		public String getEffectPerLevelDescription() {
			return null;
		}
		
		public ScopeDescription getScopeDescription() {
			return ScopeDescription.FLEET;
		}
	}
	
	public static class Level1B implements FleetStatsSkillEffect {
		public void apply(MutableFleetStatsAPI stats, String id, float level) {
			stats.getDynamic().getStat(Stats.NON_COMBAT_CREW_LOSS_MULT).modifyMult(id, 1f - CREW_LOSS_REDUCTION_NON_COMBAT / 100f);
		}
		
		public void unapply(MutableFleetStatsAPI stats, String id) {
			stats.getDynamic().getStat(Stats.NON_COMBAT_CREW_LOSS_MULT).unmodify(id);
		}	
		
		public String getEffectDescription(float level) {
			return "";
		}
		
		public String getEffectPerLevelDescription() {
			return null;
		}
		
		public ScopeDescription getScopeDescription() {
			return ScopeDescription.NONE;
		}
	}
	
	public static class Level3B implements ShipSkillEffect {
		public void apply(MutableShipStatsAPI stats, HullSize hullSize, String id, float level) {
			//stats.getDynamic().getStat(Stats.HULL_DAMAGE_CR_LOSS).modifyMult(id, HULL_DAMAGE_CR_MULT);
			stats.getDynamic().getStat(Stats.CORONA_EFFECT_MULT).modifyMult(id, (float)(CORONA_EFFECT_PERC_MULT/100f));
		}
		
		public void unapply(MutableShipStatsAPI stats, HullSize hullSize, String id) {
			//stats.getDynamic().getStat(Stats.HULL_DAMAGE_CR_LOSS).unmodify(id);
			stats.getDynamic().getStat(Stats.CORONA_EFFECT_MULT).unmodify(id);
		}	
		
		public String getEffectDescription(float level) {
			return "-" + (int) Math.round((100f - CORONA_EFFECT_PERC_MULT)) + "% combat readiness lost from being in star corona or similar terrain";
		}
		
		public String getEffectPerLevelDescription() {
			return null;
		}
		
		public ScopeDescription getScopeDescription() {
			return ScopeDescription.ALL_SHIPS;
		}
	}	
}

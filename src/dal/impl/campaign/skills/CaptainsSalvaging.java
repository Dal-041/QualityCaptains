package dal.impl.campaign.skills;

import com.fs.starfarer.api.characters.FleetStatsSkillEffect;
import com.fs.starfarer.api.fleet.MutableFleetStatsAPI;
import com.fs.starfarer.api.impl.campaign.ids.Stats;

public class CaptainsSalvaging {
	
	public static boolean SALVAGE_RARE = false;
	
	public static float CREW_LOSS_REDUCTION = 60f;
	public static float SALVAGE_BONUS = 50f;
	public static float SALVAGE_BONUS_RARE = 35f;
	public static float COMBAT_SALVAGE = 30f;
	
	public static float FUEL_SALVAGE_BONUS = 25f;
	
	public static float MIN_HULL = 30f;
	public static float MAX_HULL = 40f;
	
	public static float MIN_CR = 30f;
	public static float MAX_CR = 40f;
	
	public static class Level1A implements FleetStatsSkillEffect {
		public void apply(MutableFleetStatsAPI stats, String id, float level) {
			stats.getDynamic().getMod(Stats.RECOVERED_HULL_MIN).modifyFlat(id, MIN_HULL * 0.01f);
			stats.getDynamic().getMod(Stats.RECOVERED_HULL_MAX).modifyFlat(id, MAX_HULL * 0.01f);
		}
		
		public void unapply(MutableFleetStatsAPI stats, String id) {
			stats.getDynamic().getMod(Stats.RECOVERED_HULL_MIN).unmodify(id);
			stats.getDynamic().getMod(Stats.RECOVERED_HULL_MAX).unmodify(id);
		}
		
		public String getEffectDescription(float level) {
			return "Recovered ships start with " + (int) MIN_HULL + "-" + (int)MAX_HULL + "% hull integrity";
			//return "Recovered ships start with " + (int)MAX_HULL + "% hull integrity";
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
			stats.getDynamic().getMod(Stats.RECOVERED_CR_MIN).modifyFlat(id, MIN_CR * 0.01f);
			stats.getDynamic().getMod(Stats.RECOVERED_CR_MAX).modifyFlat(id, MAX_CR * 0.01f);
		}
		
		public void unapply(MutableFleetStatsAPI stats, String id) {
			stats.getDynamic().getMod(Stats.RECOVERED_CR_MIN).unmodify(id);
			stats.getDynamic().getMod(Stats.RECOVERED_CR_MAX).unmodify(id);
		}
		
		public String getEffectDescription(float level) {
			return "Recovered ships start with " + (int) MIN_CR + "-" + (int)MAX_CR + "% combat readiness";
		}
		
		public String getEffectPerLevelDescription() {
			return null;
		}
		
		public ScopeDescription getScopeDescription() {
			return ScopeDescription.FLEET;
		}
	}
	
	public static class Level1 implements FleetStatsSkillEffect {
		public void apply(MutableFleetStatsAPI stats, String id, float level) {
			String desc = "Salvaging skill";
			if(!SALVAGE_RARE) { stats.getDynamic().getStat(Stats.SALVAGE_VALUE_MULT_FLEET_NOT_RARE).modifyFlat(id, SALVAGE_BONUS * 0.01f, desc); }
			else if (SALVAGE_RARE) {
				//stats.getDynamic().getStat(Stats.SALVAGE_VALUE_MULT_FLEET_NOT_RARE).modifyFlat(id, (SALVAGE_BONUS-SALVAGE_BONUS_RARE) * 0.01f, desc);
				stats.getDynamic().getStat(Stats.SALVAGE_VALUE_MULT_FLEET_INCLUDES_RARE).modifyFlat(id, SALVAGE_BONUS_RARE * 0.01f, desc);
			}
		}

		public void unapply(MutableFleetStatsAPI stats, String id) {
			stats.getDynamic().getStat(Stats.SALVAGE_VALUE_MULT_FLEET_NOT_RARE).unmodify(id);
			if (SALVAGE_RARE) stats.getDynamic().getStat(Stats.SALVAGE_VALUE_MULT_FLEET_INCLUDES_RARE).unmodify(id);
		}

		public String getEffectDescription(float level) {
			if (SALVAGE_RARE) return "+" + (int)(SALVAGE_BONUS_RARE) + "% resources and rare items recovered from abandoned stations and other derelicts";
			return "+" + (int) SALVAGE_BONUS + "% resources - but not rare items, such as blueprints - recovered from abandoned stations and other derelicts";
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
			stats.getDynamic().getStat(Stats.FUEL_SALVAGE_VALUE_MULT_FLEET).modifyFlat(id, FUEL_SALVAGE_BONUS * 0.01f);
		}
		
		public void unapply(MutableFleetStatsAPI stats, String id) {
			stats.getDynamic().getStat(Stats.FUEL_SALVAGE_VALUE_MULT_FLEET).unmodify(id);
		}
		
		public String getEffectDescription(float level) {
			float max = 0f;
			max += FUEL_SALVAGE_BONUS;
			return "+" + (int) max + "% fuel salvaged";
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
			stats.getDynamic().getStat(Stats.BATTLE_SALVAGE_MULT_FLEET).modifyFlat(id, COMBAT_SALVAGE * 0.01f);
		}
		
		public void unapply(MutableFleetStatsAPI stats, String id) {
			stats.getDynamic().getStat(Stats.BATTLE_SALVAGE_MULT_FLEET).unmodify(id);
		}
		
		public String getEffectDescription(float level) {
			return "+" + (int) COMBAT_SALVAGE + "% post-battle salvage";
		}
		
		public String getEffectPerLevelDescription() {
			return null;
		}
		
		public ScopeDescription getScopeDescription() {
			return ScopeDescription.FLEET;
		}
	}
	
	public static class Level5 implements FleetStatsSkillEffect {
		public void apply(MutableFleetStatsAPI stats, String id, float level) {
			stats.getDynamic().getStat(Stats.NON_COMBAT_CREW_LOSS_MULT).modifyMult(id, 1f - CREW_LOSS_REDUCTION / 100f);
		}

		public void unapply(MutableFleetStatsAPI stats, String id) {
			stats.getDynamic().getStat(Stats.NON_COMBAT_CREW_LOSS_MULT).unmodify(id);
		}	

		public String getEffectDescription(float level) {
			return "-" + (int)(CREW_LOSS_REDUCTION) + "% crew lost in non-combat operations";
		}

		public String getEffectPerLevelDescription() {
			return null;
		}

		public ScopeDescription getScopeDescription() {
			return ScopeDescription.FLEET;
		}
	}
}




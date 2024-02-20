package dal.impl.campaign.skills;

import com.fs.starfarer.api.characters.CharacterStatsSkillEffect;
import com.fs.starfarer.api.characters.FleetStatsSkillEffect;
import com.fs.starfarer.api.characters.MutableCharacterStatsAPI;
import com.fs.starfarer.api.characters.ShipSkillEffect;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;
import com.fs.starfarer.api.fleet.MutableFleetStatsAPI;
import com.fs.starfarer.api.impl.campaign.ids.Stats;

public class CaptainsFieldRepairs {
	
	public static float REPAIR_RATE_BONUS = 100f;
	public static float INSTA_REPAIR_PERCENT = 30f; //50
	public static float DMOD_REDUCTION = 2f;
	public static float MAINTENANCE_COST_REDUCTION = 20;
	
	public static class Level1 implements CharacterStatsSkillEffect {
		public void apply(MutableCharacterStatsAPI stats, String id, float level) {
			stats.getRepairRateMult().modifyPercent(id, REPAIR_RATE_BONUS);
		}

		public void unapply(MutableCharacterStatsAPI stats, String id) {
			stats.getRepairRateMult().unmodify(id);
		}
		
		public String getEffectDescription(float level) {
			return "" + (int) REPAIR_RATE_BONUS + "% faster ship repairs out of combat";
		}
		
		public String getEffectPerLevelDescription() {
			return null;
		}

		public ScopeDescription getScopeDescription() {
			return ScopeDescription.ALL_SHIPS;
		}
	}
	
	public static class Level2 implements ShipSkillEffect {
		public void apply(MutableShipStatsAPI stats, HullSize hullSize, String id, float level) {
			stats.getDynamic().getMod(Stats.INSTA_REPAIR_FRACTION).modifyFlat(id, (float)INSTA_REPAIR_PERCENT / 100f);
		}
		
		public void unapply(MutableShipStatsAPI stats, HullSize hullSize, String id) {
			stats.getDynamic().getMod(Stats.INSTA_REPAIR_FRACTION).unmodify(id);
		}	
		
		public String getEffectDescription(float level) {
			return "" + (int) Math.round(INSTA_REPAIR_PERCENT) + "% of hull and armor damage taken automatically repaired after combat ends for all ships, at no cost.";
		}
		
		public String getEffectPerLevelDescription() {
			return null;
		}
		
		public ScopeDescription getScopeDescription() {
			return ScopeDescription.ALL_SHIPS;
		}
	}
	
	public static class Level3 implements FleetStatsSkillEffect {
		public void apply(MutableFleetStatsAPI stats, String id, float level) {
			stats.getDynamic().getMod(Stats.SHIP_DMOD_REDUCTION).modifyFlat(id, DMOD_REDUCTION);	
		}
		
		public void unapply(MutableFleetStatsAPI stats, String id) {
			stats.getDynamic().getMod(Stats.SHIP_DMOD_REDUCTION).unmodifyFlat(id);
		}
		
		public String getEffectDescription(float level) {
			//return "Recovered non-friendly ships have an average of " + (int) DMOD_REDUCTION + " less subsystem with lasting damage";
			//return "Recovered ships have up to " + (int) DMOD_REDUCTION + " less d-mods";
			return "Recovered ships have fewer d-mods on average";
		}
		
		public String getEffectPerLevelDescription() {
			return null;
		}
		
		public ScopeDescription getScopeDescription() {
			return ScopeDescription.FLEET;
		}
	}
	
	public static class Level4 implements FleetStatsSkillEffect {
		public void apply(MutableFleetStatsAPI stats, String id, float level) {
		}
		
		public void unapply(MutableFleetStatsAPI stats, String id) {
		}
		
		public String getEffectDescription(float level) {
			if (CaptainsFieldRepairsScript.MONTHS_PER_DMOD_REMOVAL == 1) {
				return "Chance to remove one d-mod per month from a randomly selected ship in your fleet";
			} else if (CaptainsFieldRepairsScript.MONTHS_PER_DMOD_REMOVAL == 2) {
				return "Chance to remove a d-mod from a randomly selected ship in your fleet every two months";
			} else {
				return "Chance to remove a d-mod from a randomly selected ship in your fleet every " +
							//CaptainsFieldRepairsScript.MONTHS_PER_DMOD_REMOVAL + " months";
							"few months, \ndepending on its size and complexity.";
			}
		}
		
		public String getEffectPerLevelDescription() {
			return null;
		}
		
		public ScopeDescription getScopeDescription() {
			return ScopeDescription.FLEET;
		}
	}
	
	public static class Level5 implements ShipSkillEffect {
		public void apply(MutableShipStatsAPI stats, HullSize hullSize, String id, float level) {
			stats.getSuppliesPerMonth().modifyMult(id, 1f - ((float)(MAINTENANCE_COST_REDUCTION)/100f), "Fleet logistics skill");
		}

		public void unapply(MutableShipStatsAPI stats, HullSize hullSize, String id) {
			stats.getSuppliesPerMonth().unmodify(id);
		}
		
		public String getEffectDescription(float level) {
			return "-" + Math.round(MAINTENANCE_COST_REDUCTION) + "% supply use for maintenance";
		}
		
		public String getEffectPerLevelDescription() {
			return null;
		}

		public ScopeDescription getScopeDescription() {
			return ScopeDescription.ALL_SHIPS;
		}
	}
}

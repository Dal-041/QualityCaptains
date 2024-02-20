package dal.impl.campaign.skills;

import com.fs.starfarer.api.characters.ShipSkillEffect;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;

public class CaptainsPointDefense {
	
	public static boolean loadStock = false;
	public static float FIGHTER_DAMAGE_BONUS = 50f; 
	public static float MISSILE_DAMAGE_BONUS = 50f;
	
	public static float FIGHTER_DAMAGE_BONUS_FTR_MULT = 0.25f;
	public static float MISSILE_DAMAGE_BONUS_FTR_MULT = 0.5f;
	
	public static float TURRET_TURN_BONUS = 25; //Context: Adv gyros is +75%
	
	public static float PD_RANGE_BONUS_FLAT = 100f;
	public static float PD_RANGE_BONUS_PERC = 40f;
	public static float PD_RANGE_BONUS_FTR_MULT = 0.5f;
	
	
	
	public static class Level1 implements ShipSkillEffect {

		public void apply(MutableShipStatsAPI stats, HullSize hullSize, String id, float level) {
			if (hullSize != HullSize.FIGHTER) {
				stats.getDamageToFighters().modifyFlat(id, FIGHTER_DAMAGE_BONUS / 100f);
			}
			else if (hullSize == HullSize.FIGHTER) {
				stats.getDamageToFighters().modifyFlat(id, (FIGHTER_DAMAGE_BONUS / 100f) * FIGHTER_DAMAGE_BONUS_FTR_MULT);
			}
		}
		
		public void unapply(MutableShipStatsAPI stats, HullSize hullSize, String id) {
			stats.getDamageToFighters().unmodify(id);
		}	
		
		public String getEffectDescription(float level) {
			return "+" + (int)(FIGHTER_DAMAGE_BONUS) + "% damage to fighters, and 1/" + Math.round(1/FIGHTER_DAMAGE_BONUS_FTR_MULT) + " the bonus to fighters launched from your ship.";
		}
		
		public String getEffectPerLevelDescription() {
			return null;
		}
		
		public ScopeDescription getScopeDescription() {
			return ScopeDescription.PILOTED_SHIP;
		}
	}
	
	public static class Level2 implements ShipSkillEffect {

		public void apply(MutableShipStatsAPI stats, HullSize hullSize, String id, float level) {
			if (hullSize != HullSize.FIGHTER) {
				stats.getDamageToMissiles().modifyFlat(id, MISSILE_DAMAGE_BONUS / 100f);
			}
			else if (hullSize == HullSize.FIGHTER) {
				stats.getDamageToMissiles().modifyFlat(id, (MISSILE_DAMAGE_BONUS / 100f) * MISSILE_DAMAGE_BONUS_FTR_MULT);
			}
		}
		
		public void unapply(MutableShipStatsAPI stats, HullSize hullSize, String id) {
			stats.getDamageToMissiles().unmodify(id);
		}	
		
		public String getEffectDescription(float level) {
			return "+" + (int)(MISSILE_DAMAGE_BONUS) + "% damage to missiles and 1/" + Math.round(1/MISSILE_DAMAGE_BONUS_FTR_MULT) + " the bonus to fighters launched from your ship.";
		}
		
		public String getEffectPerLevelDescription() {
			return null;
		}
		
		public ScopeDescription getScopeDescription() {
			return ScopeDescription.PILOTED_SHIP;
		}
	}
	
	public static class Level3 implements ShipSkillEffect {
		
		public void apply(MutableShipStatsAPI stats, HullSize hullSize, String id, float level) {
			stats.getNonBeamPDWeaponRangeBonus().modifyFlat(id, PD_RANGE_BONUS_FLAT);
			stats.getBeamPDWeaponRangeBonus().modifyFlat(id, PD_RANGE_BONUS_FLAT);
		}
		
		public void unapply(MutableShipStatsAPI stats, HullSize hullSize, String id) {
			stats.getNonBeamPDWeaponRangeBonus().unmodifyFlat(id);
			stats.getBeamPDWeaponRangeBonus().unmodifyFlat(id);
		}	
		
		public String getEffectDescription(float level) {
			//return "+" + (int)(PD_RANGE_BONUS_FLAT) + " range ";
			return "Extends the range of point-defense weapons by " + (int)(PD_RANGE_BONUS_FLAT) + "";
		}
		
		public String getEffectPerLevelDescription() {
			return null;
		}
		
		public ScopeDescription getScopeDescription() {
			return ScopeDescription.PILOTED_SHIP;
		}
	}
	
	public static class Level4 implements ShipSkillEffect {

		public void apply(MutableShipStatsAPI stats, HullSize hullSize, String id, float level) {
			stats.getWeaponTurnRateBonus().modifyPercent(id, TURRET_TURN_BONUS);
		}
		
		public void unapply(MutableShipStatsAPI stats, HullSize hullSize, String id) {
			stats.getWeaponTurnRateBonus().unmodify(id);
		}	
		
		public String getEffectDescription(float level) {
			return "+" + (int)(TURRET_TURN_BONUS) + "% weapon turn rate";
		}
		
		public String getEffectPerLevelDescription() {
			return null;
		}
		
		public ScopeDescription getScopeDescription() {
			return ScopeDescription.PILOTED_SHIP;
		}
	}
	
}

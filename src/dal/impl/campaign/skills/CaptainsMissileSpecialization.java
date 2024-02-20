package dal.impl.campaign.skills;

import com.fs.starfarer.api.characters.ShipSkillEffect;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;

public class CaptainsMissileSpecialization {
	
	public static boolean loadStock = false;
	public static float MISSILE_AMMO_BONUS_1 = 50f; //100
	public static float MISSILE_AMMO_BONUS_2 = 50f;
	
	public static float MISSILE_SPEC_HEALTH_BONUS = 30f; //50
	public static float MISSILE_SPEC_DAMAGE_BONUS = 25f; //No :V
	public static float MISSILE_SPEC_ROF_BONUS = 35f; //50
	
	//0.91
	public static float MISSILE_SPEC_SPEED_BONUS = 25f; //25
	public static float MISSILE_SPEC_RANGE_MULT = 0.8f; //0.8
	public static float MISSILE_SPEC_ACCEL_BONUS = 25f; //50
	public static float MISSILE_TURN_RATE_BONUS = 25f; //50
	public static float MISSILE_TURN_ACCEL_BONUS = 40f; //100

	public static class Level1 implements ShipSkillEffect {
		
		public void apply(MutableShipStatsAPI stats, HullSize hullSize, String id, float level) {
			if (level == 1) {
				stats.getMissileAmmoBonus().modifyPercent(id, MISSILE_AMMO_BONUS_1);
			} else if (level >= 2) {
				stats.getMissileAmmoBonus().modifyPercent(id, MISSILE_AMMO_BONUS_1 + MISSILE_AMMO_BONUS_2);
			}
		}
		
		public void unapply(MutableShipStatsAPI stats, HullSize hullSize, String id) {
			stats.getMissileAmmoBonus().unmodify(id);
		}
		
		public String getEffectDescription(float level) {
			return "+" + (int)(MISSILE_AMMO_BONUS_1) + "% missile weapon ammo capacity";
		}
		
		public String getEffectPerLevelDescription() {
			return null;
		}
		
		public ScopeDescription getScopeDescription() {
			return ScopeDescription.PILOTED_SHIP;
		}
		
	}
	
	public static class Level1B implements ShipSkillEffect {
		
		public void apply(MutableShipStatsAPI stats, HullSize hullSize, String id, float level) {
			//stats.getMissileAmmoBonus().modifyPercent(id, MISSILE_AMMO_BONUS_2);
		}
		
		public void unapply(MutableShipStatsAPI stats, HullSize hullSize, String id) {
			//stats.getMissileAmmoBonus().unmodify(id);
		}
		
		public String getEffectDescription(float level) {
			return "+" + (int)(MISSILE_AMMO_BONUS_2) + "% additional missile weapon ammo capacity";
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
			stats.getMissileHealthBonus().modifyPercent(id, MISSILE_SPEC_HEALTH_BONUS);
		}
		
		public void unapply(MutableShipStatsAPI stats, HullSize hullSize, String id) {
			stats.getMissileHealthBonus().unmodify(id);
		}
		
		public String getEffectDescription(float level) {
			//return "+" + (int)(MISSILE_SPEC_PERK_HEALTH_BONUS) + "% missile, rocket, bomb, and torpedo hitpoints";
			return "+" + (int)(MISSILE_SPEC_HEALTH_BONUS) + "% hitpoints for launched ordinance";
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
			//stats.getMissileWeaponDamageMult().modifyPercent(id, MISSILE_SPEC_PERK_DAMAGE_BONUS);
			stats.getMissileRoFMult().modifyPercent(id, MISSILE_SPEC_ROF_BONUS);
		}
		
		public void unapply(MutableShipStatsAPI stats, HullSize hullSize, String id) {
			//stats.getMissileWeaponDamageMult().unmodify(id);
			stats.getMissileRoFMult().unmodify(id);
		}
		
		public String getEffectDescription(float level) {
			return "+" + (int)(MISSILE_SPEC_ROF_BONUS) + "% rate of fire for missile weapons";
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
			//stats.getMissileMaxSpeedBonus().modifyPercent(id, MISSILE_SPEC_SPEED_BONUS);
			//stats.getMissileWeaponRangeBonus().modifyMult(id, MISSILE_SPEC_RANGE_MULT);
			
			stats.getMissileAccelerationBonus().modifyPercent(id, MISSILE_SPEC_ACCEL_BONUS);
			stats.getMissileMaxTurnRateBonus().modifyPercent(id, MISSILE_TURN_RATE_BONUS);
			stats.getMissileTurnAccelerationBonus().modifyPercent(id, MISSILE_TURN_ACCEL_BONUS);
			
		}
		
		public void unapply(MutableShipStatsAPI stats, HullSize hullSize, String id) {
			stats.getMissileMaxSpeedBonus().unmodify(id);
			stats.getMissileWeaponRangeBonus().unmodify(id);
			
			stats.getMissileAccelerationBonus().unmodify(id);
			stats.getMissileMaxTurnRateBonus().unmodify(id);
			stats.getMissileTurnAccelerationBonus().unmodify(id);
		}	
		
		public String getEffectDescription(float level) {
			return "+" + (int)(MISSILE_TURN_RATE_BONUS) + "% missile maneuverability";
		}
		
		public String getEffectPerLevelDescription() {
			return null;
		}
		
		public ScopeDescription getScopeDescription() {
			return ScopeDescription.PILOTED_SHIP;
		}
	}
}

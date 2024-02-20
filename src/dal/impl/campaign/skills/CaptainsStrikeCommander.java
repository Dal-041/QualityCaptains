package dal.impl.campaign.skills;

import com.fs.starfarer.api.characters.ShipSkillEffect;
import com.fs.starfarer.api.characters.LevelBasedEffect.ScopeDescription;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;

public class CaptainsStrikeCommander {
	
//	public static final float MISSILE_SPEED_BONUS = 25f;
//	public static final float MISSILE_RANGE_MULT = 0.8f;
	
	public static float TARGET_LEADING_BONUS = 80f; //100
	public static float MISSILE_HITPOINTS_BONUS = 30f; //50
	public static float STRIKE_DAMAGE_BONUS = 20f;
	public static float DAMAGE_TAKEN_REDUCTION = 15f;
	public static float SPEED_BONUS = 15f;
	public static float FIGHTER_REPLACEMENT_RATE_BONUS = 20f;
	
	//public static float TARGET_SIZE_DAMAGE_BONUS = 15f;
	public static float FTR_DAMAGE_TO_CAPITALS = 15;
	public static float FTR_DAMAGE_TO_CRUISERS = 10;
	public static float FTR_DAMAGE_TO_DESTROYERS = 5;
	public static float FTR_DAMAGE_TO_FRIGATES = 0;
	
	public static class Level4 implements ShipSkillEffect {

		public void apply(MutableShipStatsAPI stats, HullSize hullSize, String id, float level) {
			stats.getAutofireAimAccuracy().modifyFlat(id, TARGET_LEADING_BONUS * 0.01f);
		}
		
		public void unapply(MutableShipStatsAPI stats, HullSize hullSize, String id) {
			stats.getAutofireAimAccuracy().unmodify(id);
		}	
		
		public String getEffectDescription(float level) {
			return "+" + (int)(TARGET_LEADING_BONUS) + "% target leading accuracy";
		}
		
		public String getEffectPerLevelDescription() {
			return null;
		}
		
		public ScopeDescription getScopeDescription() {
			return ScopeDescription.SHIP_FIGHTERS;
		}
	}

	public static class Level3 implements ShipSkillEffect {

		public void apply(MutableShipStatsAPI stats, HullSize hullSize, String id, float level) {
			stats.getDamageToCapital().modifyMult(id, 1 + (FTR_DAMAGE_TO_CAPITALS/100f));
			stats.getDamageToCruisers().modifyMult(id, 1 + (FTR_DAMAGE_TO_CRUISERS/100f));
			stats.getDamageToDestroyers().modifyMult(id, 1 + (FTR_DAMAGE_TO_DESTROYERS/100f));
			stats.getDamageToFrigates().modifyMult(id, 1 + (FTR_DAMAGE_TO_FRIGATES/100f));
		}
		
		public void unapply(MutableShipStatsAPI stats, HullSize hullSize, String id) {
			stats.getDamageToCapital().unmodifyMult(id);
			stats.getDamageToCruisers().unmodifyMult(id);
			stats.getDamageToDestroyers().unmodifyMult(id);
			stats.getDamageToFrigates().unmodifyMult(id);
		}
		
		public String getEffectDescription(float level) {
			String desc = "";
			/*
			if (FTR_DAMAGE_TO_FRIGATES > 0) {
				desc += "+" + Math.round(FTR_DAMAGE_TO_FRIGATES) + "% damage to frigates\n";
			}
			if (FTR_DAMAGE_TO_DESTROYERS > 0) {
				desc += "+" + Math.round(FTR_DAMAGE_TO_DESTROYERS) + "% damage to destroyers\n";
			}
			if (FTR_DAMAGE_TO_CRUISERS > 0) {
				desc += "+" + Math.round(FTR_DAMAGE_TO_CRUISERS) + "% damage to cruisers\n";
			}
			if (FTR_DAMAGE_TO_CAPITALS > 0) {
				desc += "+" + Math.round(FTR_DAMAGE_TO_CAPITALS) + "% damage to capital ships";
			}
			*/
			desc += "+" + Math.round(FTR_DAMAGE_TO_FRIGATES) + "/" + Math.round(FTR_DAMAGE_TO_DESTROYERS) + "/" + Math.round(FTR_DAMAGE_TO_CRUISERS) + "/"
			+ Math.round(FTR_DAMAGE_TO_CAPITALS) + "% damage to targets based on hull class";
			return desc;
		}
		
		public String getEffectPerLevelDescription() {
			return null;
		}
		
		public ScopeDescription getScopeDescription() {
			return ScopeDescription.SHIP_FIGHTERS;
		}
	}
	
//	public static class Level1 implements ShipSkillEffect {
//
//		public void apply(MutableShipStatsAPI stats, HullSize hullSize, String id, float level) {
//			stats.getMissileMaxSpeedBonus().modifyPercent(id, MISSILE_SPEED_BONUS);
//			stats.getMissileWeaponRangeBonus().modifyMult(id, MISSILE_RANGE_MULT);
//			
//			stats.getMissileAccelerationBonus().modifyPercent(id, MISSILE_SPEED_BONUS);
//			stats.getMissileMaxTurnRateBonus().modifyPercent(id, MISSILE_SPEED_BONUS * 2f);
//			stats.getMissileTurnAccelerationBonus().modifyPercent(id, MISSILE_SPEED_BONUS);
//			
//		}
//		
//		public void unapply(MutableShipStatsAPI stats, HullSize hullSize, String id) {
//			stats.getMissileMaxSpeedBonus().unmodify(id);
//			stats.getMissileWeaponRangeBonus().unmodify(id);
//			
//			stats.getMissileAccelerationBonus().unmodify(id);
//			stats.getMissileMaxTurnRateBonus().unmodify(id);
//			stats.getMissileTurnAccelerationBonus().unmodify(id);
//		}	
//		
//		public String getEffectDescription(float level) {
//			return "+" + (int)(MISSILE_SPEED_BONUS) + "% missile speed and maneuverability";
//		}
//		
//		public String getEffectPerLevelDescription() {
//			return null;
//		}
//		
//		public ScopeDescription getScopeDescription() {
//			return ScopeDescription.SHIP_FIGHTERS;
//		}
//	}

	public static class Level1 implements ShipSkillEffect {

		public void apply(MutableShipStatsAPI stats, HullSize hullSize, String id, float level) {
			stats.getMaxSpeed().modifyPercent(id, SPEED_BONUS);
			stats.getAcceleration().modifyPercent(id, SPEED_BONUS);
			stats.getDeceleration().modifyPercent(id, SPEED_BONUS);
			stats.getTurnAcceleration().modifyPercent(id, SPEED_BONUS * 2f);
			stats.getMaxTurnRate().modifyPercent(id, SPEED_BONUS);
		}
		
		public void unapply(MutableShipStatsAPI stats, HullSize hullSize, String id) {
			stats.getMaxSpeed().unmodify(id);
			stats.getAcceleration().unmodify(id);
			stats.getDeceleration().unmodify(id);
			stats.getTurnAcceleration().unmodify(id);
			stats.getMaxTurnRate().unmodify(id);
		}
		
		public String getEffectDescription(float level) {
			return "+" + (int)(SPEED_BONUS) + "% top speed and maneuverability";
		}
		
		public String getEffectPerLevelDescription() {
			return null;
		}
		
		public ScopeDescription getScopeDescription() {
			return ScopeDescription.SHIP_FIGHTERS;
		}
	}
	
	public static class Level2 implements ShipSkillEffect {

		public void apply(MutableShipStatsAPI stats, HullSize hullSize, String id, float level) {
			stats.getMissileHealthBonus().modifyPercent(id, MISSILE_HITPOINTS_BONUS);
		}
		
		public void unapply(MutableShipStatsAPI stats, HullSize hullSize, String id) {
			stats.getMissileHealthBonus().unmodify(id);
		}
		
		public String getEffectDescription(float level) {
			return "+" + (int)(MISSILE_HITPOINTS_BONUS) + "% missile, rocket, bomb, and torpedo hitpoints";
		}
		
		public String getEffectPerLevelDescription() {
			return null;
		}
		
		public ScopeDescription getScopeDescription() {
			return ScopeDescription.SHIP_FIGHTERS;
		}
	}
	
	/*
	public static class Level3 implements ShipSkillEffect {

		public void apply(MutableShipStatsAPI stats, HullSize hullSize, String id, float level) {
			stats.getDamageToDestroyers().modifyPercent(id, STRIKE_DAMAGE_BONUS);
			stats.getDamageToCruisers().modifyPercent(id, STRIKE_DAMAGE_BONUS);
			stats.getDamageToCapital().modifyPercent(id, STRIKE_DAMAGE_BONUS);
		}
		
		public void unapply(MutableShipStatsAPI stats, HullSize hullSize, String id) {
			stats.getDamageToDestroyers().unmodify(id);
			stats.getDamageToCruisers().unmodify(id);
			stats.getDamageToCapital().unmodify(id);
		}	
		
		public String getEffectDescription(float level) {
			return "+" + (int)(STRIKE_DAMAGE_BONUS) + "% damage to ships of destroyer size and larger";
		}
		
		public String getEffectPerLevelDescription() {
			return null;
		}
		
		public ScopeDescription getScopeDescription() {
			return ScopeDescription.SHIP_FIGHTERS;
		}
	}
	*/
	
	public static class Level5 implements ShipSkillEffect {

		public void apply(MutableShipStatsAPI stats, HullSize hullSize, String id, float level) {
			float timeMult = 1f / ((100f + FIGHTER_REPLACEMENT_RATE_BONUS) / 100f);
			stats.getFighterRefitTimeMult().modifyMult(id, timeMult);
		}
		
		public void unapply(MutableShipStatsAPI stats, HullSize hullSize, String id) {
			stats.getFighterRefitTimeMult().unmodify(id);
		}	
		
		public String getEffectDescription(float level) {
			return "" + (int)(FIGHTER_REPLACEMENT_RATE_BONUS) + "% faster fighter replacements";
		}
		
		public String getEffectPerLevelDescription() {
			return null;
		}
		
		public ScopeDescription getScopeDescription() {
			return ScopeDescription.SHIP_FIGHTERS;
		}
	}
	
	public static class Level6 implements ShipSkillEffect {
		public void apply(MutableShipStatsAPI stats, HullSize hullSize, String id, float level) {
			stats.getHullDamageTakenMult().modifyMult(id, 1f - DAMAGE_TAKEN_REDUCTION / 100f);
			stats.getArmorDamageTakenMult().modifyMult(id, 1f - DAMAGE_TAKEN_REDUCTION / 100f);
			stats.getShieldDamageTakenMult().modifyMult(id, 1f - DAMAGE_TAKEN_REDUCTION / 100f);
		}
		
		public void unapply(MutableShipStatsAPI stats, HullSize hullSize, String id) {
			stats.getHullDamageTakenMult().unmodify(id);
			stats.getArmorDamageTakenMult().unmodify(id);
			stats.getShieldDamageTakenMult().unmodify(id);
		}
		
		public String getEffectDescription(float level) {
			return "-" + (int)(DAMAGE_TAKEN_REDUCTION) + "% damage taken";
		}
		
		public String getEffectPerLevelDescription() {
			return null;
		}

		public ScopeDescription getScopeDescription() {
			return ScopeDescription.SHIP_FIGHTERS;
		}
	}
	
}

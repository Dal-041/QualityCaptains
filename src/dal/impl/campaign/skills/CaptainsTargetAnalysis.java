package dal.impl.campaign.skills;

import com.fs.starfarer.api.characters.ShipSkillEffect;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;
import com.fs.starfarer.api.impl.campaign.skills.TargetAnalysis;

public class CaptainsTargetAnalysis {
	
	public static boolean loadStock = false;
	private static TargetAnalysis.Level1 lev1 = new TargetAnalysis.Level1();
	private static TargetAnalysis.Level2 lev2 = new TargetAnalysis.Level2();
	private static TargetAnalysis.Level3 lev3 = new TargetAnalysis.Level3();
	private static TargetAnalysis.Level4 lev4 = new TargetAnalysis.Level4();
	
	public static float DAMAGE_TO_COMPONENTS_BONUS = 80;
	public static float DAMAGE_TO_SHIELDS_BONUS = 15;
	public static float HIT_STRENGTH_BONUS = 25;
	
	public static float DAMAGE_TO_CAPITALS = 10;
	public static float DAMAGE_TO_CRUISERS = 10;
	public static float DAMAGE_TO_DESTROYERS = 5;
	public static float DAMAGE_TO_FRIGATES = 0;
	
	public static class Level1 implements ShipSkillEffect {
		public void apply(MutableShipStatsAPI stats, HullSize hullSize, String id, float level) {
			if (!loadStock) {
			stats.getDamageToFrigates().modifyMult(id, 1 + (DAMAGE_TO_FRIGATES/100f));
			stats.getDamageToCapital().modifyMult(id, 1 + (DAMAGE_TO_CAPITALS/100f));
			stats.getDamageToCruisers().modifyMult(id, 1 + (DAMAGE_TO_CRUISERS/100f));
			stats.getDamageToDestroyers().modifyMult(id, 1 + (DAMAGE_TO_DESTROYERS/100f));
			} else {
				lev1.apply(stats, hullSize, id, level);
			}
		}
		
		public void unapply(MutableShipStatsAPI stats, HullSize hullSize, String id) {
			if (!loadStock) {
			stats.getDamageToFrigates().unmodifyMult(id);
			stats.getDamageToCapital().unmodifyMult(id);
			stats.getDamageToCruisers().unmodifyMult(id);
			stats.getDamageToDestroyers().unmodifyMult(id);
			} else {
				lev1.unapply(stats, hullSize, id);
			}
		}
		
		public String getEffectDescription(float level) {
			if (!loadStock) {
			String desc = "";
			/*
			if (DAMAGE_TO_FRIGATES > 0) {
				desc += "+" + Math.round(DAMAGE_TO_FRIGATES) + "% damage to frigates\n";
			}
			if (DAMAGE_TO_DESTROYERS > 0) {
				desc += "+" + Math.round(DAMAGE_TO_DESTROYERS) + "% damage to destroyers\n";
			}
			if (DAMAGE_TO_CRUISERS > 0) {
				desc += "+" + Math.round(DAMAGE_TO_CRUISERS) + "% damage to cruisers\n";
			}
			if (DAMAGE_TO_CAPITALS > 0) {
				desc += "+" + Math.round(DAMAGE_TO_CAPITALS) + "% damage to capital ships";
			}
			*/
			desc += "+" + Math.round(DAMAGE_TO_FRIGATES) + "/" + Math.round(DAMAGE_TO_DESTROYERS) + "/" + Math.round(DAMAGE_TO_CRUISERS) + "/" + Math.round(DAMAGE_TO_CAPITALS) + "% extra damage depending on the target's hull size";
			return desc;
			} else {
				return lev1.getEffectDescription(level);
			}
		}
		
		public String getEffectPerLevelDescription() {
			if (!loadStock) {
				return null;
			} else {
				return lev1.getEffectPerLevelDescription();
			}
		}
		
		public ScopeDescription getScopeDescription() {
			if (!loadStock) {
				return ScopeDescription.PILOTED_SHIP;
			} else {
				return lev1.getScopeDescription();
			}
		}
	}

	public static class Level2 implements ShipSkillEffect {

		public void apply(MutableShipStatsAPI stats, HullSize hullSize, String id, float level) {
			if (!loadStock) {
				stats.getDamageToTargetShieldsMult().modifyPercent(id, DAMAGE_TO_SHIELDS_BONUS);
			} else {
				lev2.apply(stats, hullSize, id, level);
			}
		}
		
		public void unapply(MutableShipStatsAPI stats, HullSize hullSize, String id) {
			if (!loadStock) {
				stats.getDamageToTargetShieldsMult().unmodify(id);
			} else {
				lev2.unapply(stats, hullSize, id);
			}
		}
		
		public String getEffectDescription(float level) {
			if (!loadStock) {
				return "+" + (int)(DAMAGE_TO_SHIELDS_BONUS) + "% damage to shields";
			} else {
				return lev2.getEffectDescription(level);
			}
		}
		
		public String getEffectPerLevelDescription() {
			if (!loadStock) {
				return null;
			} else {
				return lev2.getEffectPerLevelDescription();
			}
		}

		public ScopeDescription getScopeDescription() {
			if (!loadStock) {
				return ScopeDescription.PILOTED_SHIP;
			} else {
				return lev2.getScopeDescription();
			}
		}

	}
	
	public static class Level3 implements ShipSkillEffect {

		public void apply(MutableShipStatsAPI stats, HullSize hullSize, String id, float level) {
			if (!loadStock) {
				stats.getHitStrengthBonus().modifyPercent(id, HIT_STRENGTH_BONUS);
			} else {
				lev3.apply(stats, hullSize, id, level);
			}
		}
		
		public void unapply(MutableShipStatsAPI stats, HullSize hullSize, String id) {
			if (!loadStock) {
				stats.getHitStrengthBonus().unmodify(id);
			} else {
				lev3.unapply(stats, hullSize, id);
			}
		}	
		
		public String getEffectDescription(float level) {
			if (!loadStock) {
				return "+" + (int)(HIT_STRENGTH_BONUS) + "% effective hit strength against armor";
			} else {
				return lev3.getEffectDescription(level);
			}
		}
		
		public String getEffectPerLevelDescription() {
			if (!loadStock) {
				return null;
			} else {
				return lev3.getEffectPerLevelDescription();
			}
		}
		
		public ScopeDescription getScopeDescription() {
			if (!loadStock) {
				return ScopeDescription.PILOTED_SHIP;
			} else {
				return lev3.getScopeDescription();
			}
		}
	}
	
	public static class Level4 implements ShipSkillEffect {

		public void apply(MutableShipStatsAPI stats, HullSize hullSize, String id, float level) {
			if (!loadStock) {
				stats.getDamageToTargetEnginesMult().modifyPercent(id, DAMAGE_TO_COMPONENTS_BONUS);
				stats.getDamageToTargetWeaponsMult().modifyPercent(id, DAMAGE_TO_COMPONENTS_BONUS);
			} else {
				lev4.apply(stats, hullSize, id, level);
			}
		}
		
		public void unapply(MutableShipStatsAPI stats, HullSize hullSize, String id) {
			if (!loadStock) {
				stats.getDamageToTargetEnginesMult().unmodify(id);
				stats.getDamageToTargetWeaponsMult().unmodify(id);
			} else {
				lev4.unapply(stats, hullSize, id);
			}
		}
		
		public String getEffectDescription(float level) {
			if (!loadStock) {
				return "+" + (int)(DAMAGE_TO_COMPONENTS_BONUS) + "% damage to weapons and engines";
			} else {
				return lev4.getEffectDescription(level);
			}
		}
		
		public String getEffectPerLevelDescription() {
			if (!loadStock) {
				return null;
			} else {
				return lev4.getEffectPerLevelDescription();
			}
		}
		
		public ScopeDescription getScopeDescription() {
			if (!loadStock) {
				return ScopeDescription.PILOTED_SHIP;
			} else {
				return lev4.getScopeDescription();
			}
		}
	}
	
}

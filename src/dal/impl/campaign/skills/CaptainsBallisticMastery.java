package dal.impl.campaign.skills;

import com.fs.starfarer.api.characters.ShipSkillEffect;

import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;
import com.fs.starfarer.api.impl.campaign.skills.BallisticMastery;

public class CaptainsBallisticMastery {
	
	//Fire control
	
	public static boolean loadStock = false;
	public static float PROJ_SPEED_BONUS = 30;
	public static float RANGE_BONUS = 10f;
	public static float DAMAGE_TO_SHIELDS_BONUS = 10f;
	
	private static BallisticMastery.Level1 lev1 = new BallisticMastery.Level1();
	private static BallisticMastery.Level2 lev2 = new BallisticMastery.Level2();
	private static BallisticMastery.Level3 lev3 = new BallisticMastery.Level3();
	
	public static class Level1 implements ShipSkillEffect {
				
		public void apply(MutableShipStatsAPI stats, HullSize hullSize, String id, float level) {
			if (!loadStock) {
				stats.getDamageToTargetShieldsMult().modifyPercent(id, DAMAGE_TO_SHIELDS_BONUS);
			} else {
				lev1.apply(stats, hullSize, id, level);
			}
		}
		public void unapply(MutableShipStatsAPI stats, HullSize hullSize, String id) {
			if (!loadStock) {
				stats.getDamageToTargetShieldsMult().unmodify(id);
			} else {
				lev1.unapply(stats, hullSize, id);
			}
		}
		
		public String getEffectDescription(float level) {
			if (!loadStock) {
				return "+" + (int)(DAMAGE_TO_SHIELDS_BONUS) + "% damage dealt to shields";
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
				stats.getBallisticWeaponRangeBonus().modifyPercent(id, RANGE_BONUS);
				stats.getEnergyWeaponRangeBonus().modifyPercent(id, RANGE_BONUS);
			} else {
				lev2.apply(stats, hullSize, id, level);
			}
		}
		
		public void unapply(MutableShipStatsAPI stats, HullSize hullSize, String id) {
			if (!loadStock) {
				stats.getBallisticWeaponRangeBonus().unmodify(id);
				stats.getEnergyWeaponRangeBonus().unmodify(id);
			} else {
				lev2.unapply(stats, hullSize, id);
			}
		}
		
		public String getEffectDescription(float level) {
			if (!loadStock) {
				return "+" + (int)RANGE_BONUS + "% ballistic and energy weapon range";
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
				stats.getBallisticProjectileSpeedMult().modifyPercent(id, PROJ_SPEED_BONUS);
				stats.getEnergyProjectileSpeedMult().modifyPercent(id, PROJ_SPEED_BONUS);
			} else {
				lev3.apply(stats, hullSize, id, level);
			}
		}
		
		public void unapply(MutableShipStatsAPI stats, HullSize hullSize, String id) {
			if (!loadStock) {
				stats.getBallisticProjectileSpeedMult().unmodify(id);
				stats.getEnergyProjectileSpeedMult().unmodify(id);
			} else {
				lev3.unapply(stats, hullSize, id);;
			}
		}
		
		public String getEffectDescription(float level) {
			if (!loadStock) {
				return "+" + (int)(PROJ_SPEED_BONUS) + "% ballistic and energy projectile speed";
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
}
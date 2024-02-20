package dal.impl.campaign.skills;

import org.lwjgl.util.vector.Vector2f;

import com.fs.starfarer.api.characters.AfterShipCreationSkillEffect;
import com.fs.starfarer.api.characters.MutableCharacterStatsAPI;
import com.fs.starfarer.api.characters.ShipSkillEffect;
import com.fs.starfarer.api.characters.SkillSpecAPI;
import com.fs.starfarer.api.characters.LevelBasedEffect.ScopeDescription;
import com.fs.starfarer.api.combat.BeamAPI;
import com.fs.starfarer.api.combat.CombatEntityAPI;
import com.fs.starfarer.api.combat.DamageAPI;
import com.fs.starfarer.api.combat.DamagingProjectileAPI;
import com.fs.starfarer.api.combat.MissileAPI;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;
import com.fs.starfarer.api.combat.listeners.DamageDealtModifier;
import com.fs.starfarer.api.impl.campaign.skills.BaseSkillEffectDescription;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;

public class CaptainsRangedSpecialization {
	
	public static boolean CRITS = false; //LOL
	public static boolean RANGED_BONUS = false;
	
	public static float PROJ_SPEED_BONUS = 25;
	
	public static float MIN_RANGE = 800;
	public static float MAX_RANGE = 2000;
	public static float MAX_CHANCE_PERCENT = 25; // used as damage percent mod when CRITS == false
	public static float CRIT_DAMAGE_BONUS_PERCENT = 100; //My god
	
	public static float RANGE_BONUS = 10;
	public static float BALLISTIC_ROF_BONUS = 10;
	public static float SHIELD_BEAM_DAM_REDUCTION = 15;
	
	//public static final float DAMAGE_TO_COMPONENTS_BONUS = 50;
	public static float DAMAGE_TO_SHIELDS_BONUS = 15;	

	public static class Level1 extends BaseSkillEffectDescription implements AfterShipCreationSkillEffect {
		public void applyEffectsAfterShipCreation(ShipAPI ship, String id) {
			if (RANGED_BONUS) {
				ship.addListener(new RangedSpecDamageDealtMod());
			}
		}

		public void unapplyEffectsAfterShipCreation(ShipAPI ship, String id) {
			if (RANGED_BONUS) {
				ship.removeListenerOfClass(RangedSpecDamageDealtMod.class);
			}
		}
		
		public void apply(MutableShipStatsAPI stats, HullSize hullSize, String id, float level) {
			if (!RANGED_BONUS) {
				stats.getDamageToTargetShieldsMult().modifyPercent(id, DAMAGE_TO_SHIELDS_BONUS);
			}
		}
		public void unapply(MutableShipStatsAPI stats, HullSize hullSize, String id) {
			if (!RANGED_BONUS) {
				stats.getDamageToTargetShieldsMult().unmodify(id);
			}
		}
		
		public String getEffectDescription(float level) {
			return null;
		}
		
		public void createCustomDescription(MutableCharacterStatsAPI stats, SkillSpecAPI skill, 
											TooltipMakerAPI info, float width) {
			init(stats, skill);
			
			if (CRITS) {
				info.addPara("Ballistic and energy weapons have a chance to deal %s damage at long range",
						0f, hc, hc, "+" + (int) CRIT_DAMAGE_BONUS_PERCENT + "%");
				info.addPara(indent + "%s chance at %s range and below, " +
						   "%s chance at %s range and above",
						0f, tc, hc, 
						"0%",
						"" + (int) MIN_RANGE,
						"" + (int) MAX_CHANCE_PERCENT + "%",
						"" + (int) MAX_RANGE
						);
			} else if (RANGED_BONUS) {
				info.addPara("Ballistic and energy weapons deal up to %s damage at long range",
						0f, hc, hc, "+" + (int) MAX_CHANCE_PERCENT + "%");
				info.addPara(indent + "%s at %s range and below, " +
						   "%s at %s range and above",
						0f, tc, hc, 
						"0%",
						"" + (int) MIN_RANGE,
						"" + (int) MAX_CHANCE_PERCENT + "%",
						"" + (int) MAX_RANGE
						);
			} else info.addPara("+"+"%s damage inflicted against shields", 0f, hc, hc, "" + (int)DAMAGE_TO_SHIELDS_BONUS + "%");
			//info.addPara(indent + "Beam weapons have their damage increased by the chance percentage instead", tc, 0f);
		}
		
		public ScopeDescription getScopeDescription() {
			return ScopeDescription.PILOTED_SHIP;
		}
	}
	
	public static class Level2 implements ShipSkillEffect {
		public void apply(MutableShipStatsAPI stats, HullSize hullSize, String id, float level) {
			stats.getProjectileSpeedMult().modifyPercent(id, PROJ_SPEED_BONUS);
		}
		
		public void unapply(MutableShipStatsAPI stats, HullSize hullSize, String id) {
			stats.getProjectileSpeedMult().unmodify(id);
		}
		
		public String getEffectDescription(float level) {
			return "+" + (int)(PROJ_SPEED_BONUS) + "% ballistic and energy projectile speed";
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
			stats.getBallisticWeaponRangeBonus().modifyPercent(id, RANGE_BONUS);
			stats.getEnergyWeaponRangeBonus().modifyPercent(id, RANGE_BONUS);
		}
		
		public void unapply(MutableShipStatsAPI stats, HullSize hullSize, String id) {
			stats.getBallisticWeaponRangeBonus().unmodify(id);
			stats.getEnergyWeaponRangeBonus().unmodify(id);
		}
		
		public String getEffectDescription(float level) {
			return "+" + (int)RANGE_BONUS + "% ballistic and energy weapon range";
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
			stats.getBallisticRoFMult().modifyPercent(id, BALLISTIC_ROF_BONUS);
		}
		
		public void unapply(MutableShipStatsAPI stats, HullSize hullSize, String id) {
			stats.getBallisticRoFMult().unmodify(id);
		}
		
		public String getEffectDescription(float level) {
			return "+" + (int)BALLISTIC_ROF_BONUS + "% ballistic ROF";
		}
		
		public String getEffectPerLevelDescription() {
			return null;
		}

		public ScopeDescription getScopeDescription() {
			return ScopeDescription.PILOTED_SHIP;
		}
	}
	
	public static class Level5 implements ShipSkillEffect {
		public void apply(MutableShipStatsAPI stats, HullSize hullSize, String id, float level) {
			stats.getBeamShieldDamageTakenMult().modifyMult(id, 1f - SHIELD_BEAM_DAM_REDUCTION / 100f);
		}
		
		public void unapply(MutableShipStatsAPI stats, HullSize hullSize, String id) {
			stats.getBeamShieldDamageTakenMult().unmodify(id);
		}
		
		public String getEffectDescription(float level) {
			return "-" + (int)SHIELD_BEAM_DAM_REDUCTION + "% shield damage taken by beam weapons";
		}
		
		public String getEffectPerLevelDescription() {
			return null;
		}

		public ScopeDescription getScopeDescription() {
			return ScopeDescription.PILOTED_SHIP;
		}
	}

	public static class RangedSpecDamageDealtMod implements DamageDealtModifier {
		public String modifyDamageDealt(Object param,
								   		CombatEntityAPI target, DamageAPI damage,
								   		Vector2f point, boolean shieldHit) {
			if (param instanceof MissileAPI) return null;
			
			Vector2f from = null;
			if (param instanceof DamagingProjectileAPI) {
				from = ((DamagingProjectileAPI)param).getSpawnLocation();
			} else if (param instanceof BeamAPI) {
				from = ((BeamAPI)param).getFrom();
			} else {
				return null;
			}
			
			float chancePercent = 0f;
			float dist = Misc.getDistance(from, point);
			float f = (dist - MIN_RANGE) / (MAX_RANGE - MIN_RANGE);
			if (f < 0) f = 0;
			if (f > 1) f = 1;
			
			//f = 0.5f;
			//f = 1f;
			//System.out.println("RangedSpec mult: " + f);
			
			String id = null;
			
			chancePercent = (int) Math.round(MAX_CHANCE_PERCENT * f);
			if (chancePercent <= 0) return null;
			
			//System.out.println("Chance: " + chancePercent);
			
			Vector2f vel = new Vector2f();
			if (target instanceof ShipAPI) {
				vel.set(target.getVelocity());
			}
			
			if (param instanceof DamagingProjectileAPI) {
				if (CRITS) {
					if ((float) Math.random() < chancePercent * 0.01f) {
						id = "ranged_spec_dam_mod";
						damage.getModifier().modifyPercent(id, CRIT_DAMAGE_BONUS_PERCENT);
						//Misc.spawnExtraHitGlow(param, point, vel, f);
					}
				} else {
					id = "ranged_spec_dam_mod";
					damage.getModifier().modifyPercent(id, chancePercent);
					//Misc.spawnExtraHitGlow(param, point, vel, f);
				}
			} else if (param instanceof BeamAPI) {
				if (CRITS) {
					if ((float) Math.random() < chancePercent * 0.01f) {
						id = "ranged_spec_dam_mod";
						damage.getModifier().modifyPercent(id, CRIT_DAMAGE_BONUS_PERCENT);
						//Misc.spawnExtraHitGlow(param, point, vel, f);
					}
				} else {
					id = "ranged_spec_dam_mod";
					damage.getModifier().modifyPercent(id, chancePercent);
					//Misc.spawnExtraHitGlow(param, point, vel, f);
				}
			}
			
			return id;
		}
	}
	
	
	
//	public static class TestDamageModifier implements DamageDealtModifier {
//		public String modifyDamageDealt(Object param,
//								   		CombatEntityAPI target, DamageAPI damage,
//								   		Vector2f point, boolean shieldHit) {
//			//if (true) return null;
//			String id = "dam_mod1" + (float) Math.random();
//			damage.getModifier().modifyMult(id, 0.1f);
//			return id;
//		}
//	}
//	
//	public static class TestDamageModifierTaken implements DamageTakenModifier {
//		public String modifyDamageTaken(Object param,
//				CombatEntityAPI target, DamageAPI damage,
//				Vector2f point, boolean shieldHit) {
//			//if (true) return null;
//			String id = "dam_mod2" + (float) Math.random();
//			damage.getModifier().modifyMult(id, 10f);
//			return id;
//		}
//	}
	
}












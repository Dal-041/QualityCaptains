package dal.impl.campaign.skills;

import java.awt.Color;

import org.lwjgl.util.vector.Vector2f;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.characters.AfterShipCreationSkillEffect;
import com.fs.starfarer.api.characters.DescriptionSkillEffect;
import com.fs.starfarer.api.characters.ShipSkillEffect;
import com.fs.starfarer.api.characters.LevelBasedEffect.ScopeDescription;
import com.fs.starfarer.api.combat.CombatEntityAPI;
import com.fs.starfarer.api.combat.DamageAPI;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;
import com.fs.starfarer.api.combat.ShipHullSpecAPI.ShipTypeHints;
import com.fs.starfarer.api.combat.listeners.AdvanceableListener;
import com.fs.starfarer.api.combat.listeners.DamageTakenModifier;
import com.fs.starfarer.api.impl.campaign.skills.PolarizedArmor;
import com.fs.starfarer.api.impl.campaign.skills.PolarizedArmor.PolarizedArmorEffectMod;
import com.fs.starfarer.api.util.Misc;

public class CaptainsPolarizedArmor {
	
	public static boolean loadStock = false;
	private static PolarizedArmor.Level0 lev0 = new PolarizedArmor.Level0();
	private static PolarizedArmor.Level1 lev1 = new PolarizedArmor.Level1();
	private static PolarizedArmor.Level2 lev2 = new PolarizedArmor.Level2();
	private static PolarizedArmor.Level3 lev3 = new PolarizedArmor.Level3();
	private static PolarizedArmor.Level4 lev4 = new PolarizedArmor.Level4();
	
	public static float ARMOR_FRACTION_BONUS = 0.05f;
	public static float EFFECTIVE_ARMOR_BONUS = 65f;
	public static float EMP_BONUS_PERCENT = 50f;
	public static float HULL_DAM_REDUCTION = 25f;
	
	public static float MIN_BONUS_PERC = 20f;
	public static float MIN_BONUS_SHIELDLESS_PERC = 40f;
	
	public static float MAX_DAMAGE_REDUCTION_BONUS = 0.05f;
	public static float MIN_ARMOR_FRACTION_BONUS = 0.05f;
	
	public static float OVERLOAD_REDUCTION = 20;
		
		public static class Level0 implements DescriptionSkillEffect {
			public String getString() {
				if (!loadStock) {
					return "\n\n*For bonuses, all ships are treated as having at least " + (int) MIN_BONUS_PERC + "% hard flux. For shieldless ships, " + (int) MIN_BONUS_SHIELDLESS_PERC + "%";
				} else {
					return lev0.getString();
				}
			}
			public Color[] getHighlightColors() {
				if (!loadStock) {
					Color h = Misc.getHighlightColor();
					h = Misc.getDarkHighlightColor();
					return new Color[] {h,h};
				} else {
					return lev0.getHighlightColors();
				}
			}
			public String[] getHighlights() {
				if (!loadStock) {
					return new String [] {"" + (int) MIN_BONUS_PERC + "%","" + (int) MIN_BONUS_SHIELDLESS_PERC + "%"};
				} else {
					return lev0.getHighlights();
				}
			}
			public Color getTextColor() {
				if (!loadStock) {
					return null;
				} else {
					return lev0.getTextColor();
				}
			}
		}
		
		public static class PolarizedArmorEffectMod implements DamageTakenModifier, AdvanceableListener {
			protected ShipAPI ship;
			protected String id;
			
			protected PolarizedArmor.PolarizedArmorEffectMod PAEM = new PolarizedArmor.PolarizedArmorEffectMod(ship, id);
					
			public PolarizedArmorEffectMod(ShipAPI ship, String id) {
				this.ship = ship;
				this.id = id;
			}
			
			public void advance(float amount) {
				if (loadStock) {
					PAEM.advance(amount);
				} else {
	 			MutableShipStatsAPI stats = ship.getMutableStats();
				
				float fluxLevel = ship.getHardFluxLevel();
				float armorBonus = 0f;
				float empBonus = 0f;
				
				if (ship.getShield() == null && !ship.getHullSpec().isPhase() &&
						(ship.getPhaseCloak() == null || !ship.getHullSpec().getHints().contains(ShipTypeHints.PHASE))) {
					if (fluxLevel < MIN_BONUS_SHIELDLESS_PERC * 0.01f) {
						armorBonus = EFFECTIVE_ARMOR_BONUS * MIN_BONUS_SHIELDLESS_PERC * 0.01f;
						empBonus = EMP_BONUS_PERCENT * MIN_BONUS_SHIELDLESS_PERC * 0.01f;
					} else {
						armorBonus = EFFECTIVE_ARMOR_BONUS * fluxLevel;
						empBonus = EMP_BONUS_PERCENT * fluxLevel;
					}
				} else if (ship.getMaxFlux() > 0) {
					if (fluxLevel < MIN_BONUS_PERC * 0.01f) {
						armorBonus = EFFECTIVE_ARMOR_BONUS * MIN_BONUS_PERC * 0.01f;
						empBonus = EMP_BONUS_PERCENT * MIN_BONUS_PERC * 0.01f;
					} else {
						armorBonus = EFFECTIVE_ARMOR_BONUS * fluxLevel;
						empBonus = EMP_BONUS_PERCENT * fluxLevel;
					}
				} 
				
				if (ship.getMaxFlux() == 0) {
					armorBonus = 0;
					empBonus = 0;
				}
				
				//float armorBonus = ship.getArmorGrid().getArmorRating() * ARMOR_FRACTION_BONUS * fluxLevel;
				//armorBonus = 1090000;
				//wefwef we fe stats.getMaxArmorDamageReduction().modifyFlat(id, 0.1f);
				//stats.getEffectiveArmorBonus().modifyFlat(id, armorBonus);
				stats.getEffectiveArmorBonus().modifyPercent(id, armorBonus);
				stats.getEmpDamageTakenMult().modifyMult(id, 1f - empBonus * 0.01f);
				
				//Color c = new Color(255, 200, 100, 100);
				Color c = ship.getSpriteAPI().getAverageColor();
				c = Misc.setAlpha(c, 127);
				float b = 0f;
				if (fluxLevel > 0.75f) {
					b = 1f * (fluxLevel - 0.75f) / 0.25f;
				}
				if (b > 0) {
					ship.setJitter(this, c, 1f * fluxLevel * b, 1, 0f);
				}
				}
			}

			public String modifyDamageTaken(Object param,
									   		CombatEntityAPI target, DamageAPI damage,
									   		Vector2f point, boolean shieldHit) {
				if (!loadStock) {
					return null;
				} else {
					return PAEM.modifyDamageTaken(param, target, damage, point, shieldHit);
				}
			}

		}
		
		public static class Level1 implements ShipSkillEffect {

			public void apply(MutableShipStatsAPI stats, HullSize hullSize, String id, float level) {
				if (loadStock) {
					lev1.apply(stats, hullSize, id, level);
				} else {
					stats.getMaxArmorDamageReduction().modifyFlat(id, 0.05f);
				}
			}
			
			public void unapply(MutableShipStatsAPI stats, HullSize hullSize, String id) {
				
				stats.getMaxArmorDamageReduction().unmodify(id);
			}
			
			public String getEffectDescription(float level) {
				return "+" + Math.round(MAX_DAMAGE_REDUCTION_BONUS * 100) + "% to maximum damage reduction by armor";
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
				stats.getMinArmorFraction().modifyFlat(id, MIN_ARMOR_FRACTION_BONUS);
			}
			
			public void unapply(MutableShipStatsAPI stats, HullSize hullSize, String id) {
				stats.getMinArmorFraction().unmodify(id);
			}
			
			public String getEffectDescription(float level) {
				return "+" + Math.round(MIN_ARMOR_FRACTION_BONUS * 100f) + "% to minimum effectiveness of stripped armor";
			}
			
			public String getEffectPerLevelDescription() {
				return null;
			}

			public ScopeDescription getScopeDescription() {
				return ScopeDescription.PILOTED_SHIP;
			}
		}
		
		
		public static class Level2 implements AfterShipCreationSkillEffect {
			public void applyEffectsAfterShipCreation(ShipAPI ship, String id) {
				ship.addListener(new PolarizedArmorEffectMod(ship, id));
			}
			public void unapplyEffectsAfterShipCreation(ShipAPI ship, String id) {
				MutableShipStatsAPI stats = ship.getMutableStats();
				ship.removeListenerOfClass(PolarizedArmorEffectMod.class);
				stats.getEffectiveArmorBonus().unmodify(id);
				stats.getEmpDamageTakenMult().unmodify(id);
			}
				
			public void apply(MutableShipStatsAPI stats, HullSize hullSize, String id, float level) {}
			public void unapply(MutableShipStatsAPI stats, HullSize hullSize, String id) {}
			
			public String getEffectDescription(float level) {
				//return "Up to +" + (int)Math.round(ARMOR_FRACTION_BONUS * 100f) + "% of base armor for damage reduction calculation only, based on current hard flux level";
				
				return "Up to +" + (int)(EFFECTIVE_ARMOR_BONUS) + "% armor rating for reducing incoming damage, based on current hard flux level*";
			}
			public String getEffectPerLevelDescription() {
				return null;
			}
			public ScopeDescription getScopeDescription() {
				return ScopeDescription.PILOTED_SHIP;
			}
		}
		
		public static class Level3 implements ShipSkillEffect {
			public void apply(MutableShipStatsAPI stats, HullSize hullSize, String id, float level) {}
			public void unapply(MutableShipStatsAPI stats, HullSize hullSize, String id) {}
			
			public String getEffectDescription(float level) {
				return "EMP damage taken reduced by up to " + (int)Math.round(EMP_BONUS_PERCENT) + "%, based on current hard flux level*";
			}
			public String getEffectPerLevelDescription() {
				return null;
			}
			public ScopeDescription getScopeDescription() {
				return ScopeDescription.PILOTED_SHIP;
			}
		}
		
		public static class Level3B implements ShipSkillEffect {
			
			public void apply(MutableShipStatsAPI stats, HullSize hullSize, String id, float level) {
				stats.getOverloadTimeMod().modifyMult(id, 1f - OVERLOAD_REDUCTION / 100f);
			}
			
			public void unapply(MutableShipStatsAPI stats, HullSize hullSize, String id) {
				stats.getOverloadTimeMod().unmodify(id);
			}	
			
			public String getEffectDescription(float level) {
				return "-" + Math.round(OVERLOAD_REDUCTION) + "% overload duration";
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
				stats.getHullDamageTakenMult().modifyMult(id, 1 - (HULL_DAM_REDUCTION / 100f));
			}
			
			public void unapply(MutableShipStatsAPI stats, HullSize hullSize, String id) {
				stats.getHullDamageTakenMult().unmodify(id);
			}
			
			public String getEffectDescription(float level) {
				return "-" + (int)(HULL_DAM_REDUCTION) + "% hull damage taken";
				//return "+" + (int)(VENT_RATE_BONUS) + "% flux vent rate";
			}
			
			public String getEffectPerLevelDescription() {
				return null;
			}

			public ScopeDescription getScopeDescription() {
				return ScopeDescription.PILOTED_SHIP;
			}
		}
		
	}
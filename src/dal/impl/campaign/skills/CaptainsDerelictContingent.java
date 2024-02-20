package dal.impl.campaign.skills;

import java.awt.Color;

import com.fs.starfarer.api.characters.AfterShipCreationSkillEffect;
import com.fs.starfarer.api.characters.DescriptionSkillEffect;
import com.fs.starfarer.api.characters.FleetStatsSkillEffect;
import com.fs.starfarer.api.characters.MutableCharacterStatsAPI;
import com.fs.starfarer.api.characters.ShipSkillEffect;
import com.fs.starfarer.api.characters.SkillSpecAPI;
import com.fs.starfarer.api.characters.LevelBasedEffect.ScopeDescription;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;
import com.fs.starfarer.api.fleet.FleetMemberAPI;
import com.fs.starfarer.api.fleet.MutableFleetStatsAPI;
import com.fs.starfarer.api.impl.campaign.DModManager;
import com.fs.starfarer.api.impl.campaign.ids.Stats;
import com.fs.starfarer.api.impl.campaign.skills.BaseSkillEffectDescription;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;

public class CaptainsDerelictContingent {
	
	public static float MAX_DMODS = 5;
	public static float CREW_LOSS_REDUCTION_PER_DMOD = 10f;
	public static float CR_PER_DMOD = 3f;
	
	public static float SHIELDLESS_ARMOR_BONUS_PER_DMOD = 0.05f;
	public static int SHIELDLESS_ARMOR_BONUS_PER_DMOD_PERC = 5;
	public static int SHIELDLESS_FLUX_BONUS_PERC = 20;
	
	//public static float DMOD_DISCOUNT_MULT = 50f;
	public static float DMOD_EFFECT_PERC_MULT = 50f;
	
	public static float MINUS_CR_PER_DMOD = 0f;
	public static float MINUS_DP_PERCENT_PER_DMOD = 6f;
	
	public static float EXTRA_DMODS = 4.0F;
	
	
	public static boolean isDmoddedAndOfficer(MutableShipStatsAPI stats) {
		if (stats == null) return false;
		
		if (stats.getEntity() instanceof ShipAPI) {
			ShipAPI ship = (ShipAPI) stats.getEntity();
			if (ship.getCaptain().isDefault()) return false;
			return DModManager.getNumDMods(ship.getVariant()) > 0;
		} else { 
			FleetMemberAPI member = stats.getFleetMember();
			if (member == null) return false;
			if (member.getCaptain().isDefault()) return false;
			return DModManager.getNumDMods(member.getVariant()) > 0;
		}
		
	}
	
	public static boolean isCommanded(MutableShipStatsAPI stats) {
		if (stats == null) return false;
		
		if (stats.getEntity() instanceof ShipAPI) {
			ShipAPI ship = (ShipAPI) stats.getEntity();
			if (ship.getCaptain().isDefault()) return false;
			return true;
		} else { 
			FleetMemberAPI member = stats.getFleetMember();
			if (member == null) return false;
			if (member.getCaptain().isDefault()) return false;
			return true;
		}
		
	}
	
	public static class Level0 implements DescriptionSkillEffect {
		public String getString() {
			return "*Maximum effect reached " +
					"at " + (int) MAX_DMODS + " d-mods."
					;
		}
		public Color[] getHighlightColors() {
			Color h = Misc.getHighlightColor();
			h = Misc.getDarkHighlightColor();
			return new Color[] {h};
		}
		public String[] getHighlights() {
			return new String [] {"" + (int) MAX_DMODS};
		}
		public Color getTextColor() {
			return null;
		}
	}
	
	public static class Level1 extends BaseSkillEffectDescription implements ShipSkillEffect {
		public void apply(MutableShipStatsAPI stats, HullSize hullSize, String id, float level) {
			FleetMemberAPI member = stats.getFleetMember();
			float dmods = 0;
			if (member != null) {
				dmods = DModManager.getNumDMods(member.getVariant());
				if (dmods > MAX_DMODS) dmods = MAX_DMODS;
			}
			
			if (dmods > 0) {
				float depBonus = dmods * MINUS_DP_PERCENT_PER_DMOD;
				//stats.getDynamic().getMod(Stats.DEPLOYMENT_POINTS_MOD).modifyPercent(id, -depBonus);
				stats.getDynamic().getMod(Stats.DEPLOYMENT_POINTS_MOD).modifyMult(id, 1f - (depBonus/100f));
			
				//stats.getDynamic().getMod(Stats.DMOD_REDUCE_MAINTENANCE).modifyFlat(id, 1f);
			}
		}
		
		public void unapply(MutableShipStatsAPI stats, HullSize hullSize, String id) {
			stats.getDynamic().getMod(Stats.DEPLOYMENT_POINTS_MOD).unmodify(id);
			stats.getDynamic().getMod(Stats.DMOD_REDUCE_MAINTENANCE).unmodify(id);
		}
		
		public void createCustomDescription(MutableCharacterStatsAPI stats, SkillSpecAPI skill, 
				TooltipMakerAPI info, float width) {
			init(stats, skill);

			info.addPara("Deployment point cost of ships reduced by %s per d-mod*", 0f, 
						hc, hc, "" + (int)MINUS_DP_PERCENT_PER_DMOD + "%");

			//info.addPara("(D) hull deployment cost reduction also applies to maintenance cost", hc, 0f);

		}
	}
	
	public static class Level1f extends BaseSkillEffectDescription implements ShipSkillEffect, AfterShipCreationSkillEffect {
		public void applyEffectsAfterShipCreation(ShipAPI ship, String id) {
			MutableShipStatsAPI stats = ship.getMutableStats();
			if (isDmoddedAndOfficer(stats)) {
				
				float dmods = DModManager.getNumDMods(ship.getVariant());
				if (dmods <= 0) return;
				if (dmods > MAX_DMODS) dmods = MAX_DMODS;
				
				if (ship.getShield() == null ) {
				//if (ship.getVariant().hasHullMod("shield_shunt")) {
					stats.getMinArmorFraction().modifyFlat(id, (float)(SHIELDLESS_ARMOR_BONUS_PER_DMOD_PERC / 100f) * dmods);
				}
			}
		}

		public void unapplyEffectsAfterShipCreation(ShipAPI ship, String id) {
			
			if (ship.getVariant().hasHullMod("shield_shunt")) {
				MutableShipStatsAPI stats = ship.getMutableStats();
				stats.getMinArmorFraction().unmodifyFlat(id);
			}
		}
		
		
		public void apply(MutableShipStatsAPI stats, HullSize hullSize, String id, float level) {
			if (isDmoddedAndOfficer(stats)) {
				FleetMemberAPI member = stats.getFleetMember();
				float dmods = DModManager.getNumDMods(member.getVariant());
				if (dmods <= 0) return;
				if (dmods > MAX_DMODS) dmods = MAX_DMODS;

				stats.getCrewLossMult().modifyMult(id, 1f - ((CREW_LOSS_REDUCTION_PER_DMOD * dmods) * 0.01f));
				//stats.getMaxCombatReadiness().modifyFlat(id, (CR_PER_DMOD * dmods) * 0.01f, "Derelict Contingent skill");
			}
		}
		
		public void unapply(MutableShipStatsAPI stats, HullSize hullSize, String id) {
			stats.getCrewLossMult().unmodifyMult(id);
			//stats.getMaxCombatReadiness().unmodifyFlat(id);
		}
		
		public void createCustomDescription(MutableCharacterStatsAPI stats, SkillSpecAPI skill, 
				TooltipMakerAPI info, float width) {
			init(stats, skill);

			
			info.addPara("%s crew lost due to hull damage in combat per d-mod*", 0f, hc, hc,
					"-" + (int)CREW_LOSS_REDUCTION_PER_DMOD + "%,"
			);
			info.addPara("*For up to " + (int) MAX_DMODS + " d-mods.", 5f, hc, hc,
					""
			);
			/*
			info.addPara("%s maximum combat readiness per d-mod*", 0f, hc, hc,
					"+" + (int)CR_PER_DMOD + "%"
			);
			*/
		}
	}
	
	public static class Level2 extends BaseSkillEffectDescription implements ShipSkillEffect, AfterShipCreationSkillEffect {
		public void applyEffectsAfterShipCreation(ShipAPI ship, String id) {
			MutableShipStatsAPI stats = ship.getMutableStats();
				
			if (ship.getVariant().hasHullMod("shield_shunt")) {
				stats.getFluxDissipation().modifyMult(id, 1f + (float)(SHIELDLESS_FLUX_BONUS_PERC / 100f));
			}
		}

		public void unapplyEffectsAfterShipCreation(ShipAPI ship, String id) {
			if (ship.getVariant().hasHullMod("shield_shunt")) {
				MutableShipStatsAPI stats = ship.getMutableStats();
				stats.getFluxDissipation().unmodifyFlat(id);
			}
		}
		
		public void apply(MutableShipStatsAPI stats, HullSize hullSize, String id, float level) {
			
		}
		
		public void unapply(MutableShipStatsAPI stats, HullSize hullSize, String id) {
			
		}
		
		public void createCustomDescription(MutableCharacterStatsAPI stats, SkillSpecAPI skill, 
				TooltipMakerAPI info, float width) {
			init(stats, skill);

			info.addPara("%s flux dissipation rate for shield-shunted ships", 0f, hc, hc,
					"+" + SHIELDLESS_FLUX_BONUS_PERC + "%"
			);
		}
	}
	
	public static class Level3 implements ShipSkillEffect {
		public void apply(MutableShipStatsAPI stats, HullSize hullSize, String id, float level) {
			stats.getDynamic().getStat(Stats.DMOD_EFFECT_MULT).modifyMult(id, (float)(DMOD_EFFECT_PERC_MULT/100f));
		}
		
		public void unapply(MutableShipStatsAPI stats, HullSize hullSize, String id) {
			stats.getDynamic().getStat(Stats.DMOD_EFFECT_MULT).unmodify(id);
		}	
		
		public String getEffectDescription(float level) {
			return "Negative effects of \"lasting damage\" hullmods (d-mods) reduced by " + Math.round(100f - DMOD_EFFECT_PERC_MULT) + "%";
		}
		
		public String getEffectPerLevelDescription() {
			return null;
		}
		
		public ScopeDescription getScopeDescription() {
			return ScopeDescription.ALL_SHIPS;
		}
	}
	
	public static class Level2O implements FleetStatsSkillEffect {
		public void apply(MutableFleetStatsAPI stats, String id, float level) {
			stats.getDynamic().getMod(Stats.SHIP_DMOD_REDUCTION).modifyFlat(id, -EXTRA_DMODS);	
		}
		
		public void unapply(MutableFleetStatsAPI stats, String id) {
			stats.getDynamic().getMod(Stats.SHIP_DMOD_REDUCTION).unmodify(id);
		}
		
		public String getEffectDescription(float level) {
			//return "Recovered ships have " + (int) EXTRA_DMODS + " more d-mods on average";
			return "Recovered ships have more d-mods than normal";
		}
		
		public String getEffectPerLevelDescription() {
			return null;
		}
		
		public ScopeDescription getScopeDescription() {
			return ScopeDescription.FLEET;
		}
	}
}

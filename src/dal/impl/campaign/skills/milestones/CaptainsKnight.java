package dal.impl.campaign.skills.milestones;

import java.awt.Color;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.lwjgl.util.vector.Vector2f;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.BattleAPI;
import com.fs.starfarer.api.campaign.CombatDamageData;
import com.fs.starfarer.api.campaign.econ.MarketAPI;
import com.fs.starfarer.api.characters.AfterShipCreationSkillEffect;
import com.fs.starfarer.api.characters.FleetStatsSkillEffect;
import com.fs.starfarer.api.characters.MarketSkillEffect;
import com.fs.starfarer.api.characters.MutableCharacterStatsAPI;
import com.fs.starfarer.api.characters.ShipSkillEffect;
import com.fs.starfarer.api.characters.SkillSpecAPI;
import com.fs.starfarer.api.characters.LevelBasedEffect.ScopeDescription;
import com.fs.starfarer.api.characters.MutableCharacterStatsAPI.SkillLevelAPI;
import com.fs.starfarer.api.combat.BattleCreationContext;
import com.fs.starfarer.api.combat.BattleObjectiveAPI;
import com.fs.starfarer.api.combat.BeamAPI;
import com.fs.starfarer.api.combat.CollisionGridAPI;
import com.fs.starfarer.api.combat.CombatEngineAPI;
import com.fs.starfarer.api.combat.CombatEntityAPI;
import com.fs.starfarer.api.combat.CombatFleetManagerAPI;
import com.fs.starfarer.api.combat.CombatLayeredRenderingPlugin;
import com.fs.starfarer.api.combat.CombatNebulaAPI;
import com.fs.starfarer.api.combat.CombatUIAPI;
import com.fs.starfarer.api.combat.DamageAPI;
import com.fs.starfarer.api.combat.DamageType;
import com.fs.starfarer.api.combat.DamagingProjectileAPI;
import com.fs.starfarer.api.combat.EmpArcEntityAPI;
import com.fs.starfarer.api.combat.EveryFrameCombatPlugin;
import com.fs.starfarer.api.combat.FogOfWarAPI;
import com.fs.starfarer.api.combat.MissileAPI;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.MutableStat;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipSystemAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;
import com.fs.starfarer.api.combat.ShipSystemAPI.SystemState;
import com.fs.starfarer.api.combat.ShipVariantAPI;
import com.fs.starfarer.api.combat.ViewportAPI;
import com.fs.starfarer.api.combat.WeaponAPI;
import com.fs.starfarer.api.combat.WeaponAPI.WeaponType;
import com.fs.starfarer.api.combat.listeners.AdvanceableListener;
import com.fs.starfarer.api.combat.listeners.CombatListenerManagerAPI;
import com.fs.starfarer.api.fleet.MutableFleetStatsAPI;
import com.fs.starfarer.api.impl.campaign.ids.Stats;
import com.fs.starfarer.api.impl.campaign.skills.BaseSkillEffectDescription;
import com.fs.starfarer.api.impl.combat.PhaseCloakStats;
import com.fs.starfarer.api.loading.DamagingExplosionSpec;
import com.fs.starfarer.api.loading.WeaponSlotAPI;
import com.fs.starfarer.api.loading.WeaponSpecAPI;
import com.fs.starfarer.api.mission.FleetSide;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;
import com.fs.starfarer.campaign.fleet.Battle;

import dal.impl.campaign.skills.milestones.CaptainsBabylon.CaptainsBabylonListener;

public class CaptainsKnight { //KOL
	
	public static int MIN_OPPONENTS = 5;
	public static float PPT_PERC = 33;
	public static boolean SECOND_WIND = false;
	
	public static boolean enabled = false;
	public static final String skillID = "captains_knight";
	public static final String skillName = "Knight";
	public static final String skillDesc = "On a fine day I set out upon the sea. There I found monsters, who I slew, sirens, who lured me toward rocks, and my quarry, who begged peace.";
	public static final String skillAuthor = "The Lost Pilgrim, 2nd Edition";
	public static final String skillIcon = "/graphics/icons/skills/"+ skillID + ".png";
	
	public static class Level1 implements ShipSkillEffect, AfterShipCreationSkillEffect {

		public void applyEffectsAfterShipCreation(ShipAPI ship, String id) {
			ship.addListener(new CaptainsKnightListener(ship));
		}

		public void unapplyEffectsAfterShipCreation(ShipAPI ship, String id) {
			ship.removeListenerOfClass(CaptainsKnightListener.class);
		}
		
		public void apply(MutableShipStatsAPI stats, HullSize hullSize, String id, float level) {

		}

		public void unapply(MutableShipStatsAPI stats, HullSize hullSize, String id) {

		}
		
		public String getEffectDescription(float level) {
			if (enabled) {
				String desc = "When fighting " + MIN_OPPONENTS + " or more opponents alone:\n- Your ship's CR degredation rate is reduced by " + PPT_PERC + "%";
				if (SECOND_WIND) {
					desc += "\n- Your ship's Combat Readiness will be restored if it drops below 40% once";
				}
				return desc;
				
			}
			return "";
		}
		
		public String getEffectPerLevelDescription() {
			return null;
		}
		
		public ScopeDescription getScopeDescription() {
			if (enabled) return ScopeDescription.PILOTED_SHIP;
			return ScopeDescription.NONE;
		}
		
	}
	
	public static class Level2 implements ShipSkillEffect, AfterShipCreationSkillEffect {

		public void applyEffectsAfterShipCreation(ShipAPI ship, String id) {
		}

		public void unapplyEffectsAfterShipCreation(ShipAPI ship, String id) {
		}
		
		public void apply(MutableShipStatsAPI stats, HullSize hullSize, String id, float level) {
		}

		public void unapply(MutableShipStatsAPI stats, HullSize hullSize, String id) {
		}
		
		public String getEffectDescription(float level) {
			if (enabled && SECOND_WIND) {
				return "";
			}
			return "";
		}
		
		public String getEffectPerLevelDescription() {
			return null;
		}
		
		public ScopeDescription getScopeDescription() {
			if (enabled) return ScopeDescription.NONE;
			return ScopeDescription.NONE;
		}
		
	}
	
	public static class CaptainsKnightListener implements AdvanceableListener {
		protected ShipAPI ship;
		protected boolean inited = false;
		protected float basePPT = 0;
		protected float crDeployment = 0;
		protected boolean passed_wind = false;
		protected boolean active = false;
		
		public CaptainsKnightListener(ShipAPI ship) {
			this.ship = ship;
		}
		
		protected void init() {
			if (ship != null) {
				basePPT = ship.getMutableStats().getPeakCRDuration().computeEffective(ship.getHullSpec().getNoCRLossSeconds());
				crDeployment = ship.getCRAtDeployment();
			}
			inited = true;
		}
		
		
		public void advance(float amount) {
			if (!inited) {
				init();
			}
			boolean solo = false;
			boolean brigands = false;
			if(ship != null && ship.getCaptain().isPlayer()) {
				if (Global.getCombatEngine().isPaused() || Global.getCombatEngine().isCombatOver()) {
					return;
				}
				solo = (Global.getCombatEngine().getFleetManager(FleetSide.PLAYER).getDeployedCopy().size() == 1);
				brigands = (Global.getCombatEngine().getFleetManager(FleetSide.ENEMY).getDeployedCopy().size() >= MIN_OPPONENTS);
				if (solo && brigands) {
					if (!active) ship.getMutableStats().getCRLossPerSecondPercent().modifyMult(skillID + "_listener", 1-(PPT_PERC/100f));
					active = true;
				} else {
					 if (active) ship.getMutableStats().getCRLossPerSecondPercent().unmodify(skillID + "_listener");
					 active = false;
				}
				if (SECOND_WIND && !passed_wind && ship.getCurrentCR() < 0.405f) {
					if (crDeployment > 0.40f) {
						ship.setCurrentCR(crDeployment);
					} else {
						ship.setCurrentCR(0.41f);
					}
					passed_wind = true;
				}
			}
		}
	}

	
	public static void toggleMilestone() {
		for (SkillLevelAPI skill : Global.getSector().getPlayerStats().getSkillsCopy()) {
			if (skill.getSkill().getId().equals(skillID)) {
				if (skill.getLevel()==0) {
					skill.getSkill().setDescription(skillDesc);
					skill.getSkill().setAuthor(skillAuthor);
					skill.getSkill().setName(skillName);
					try {
						Global.getSettings().loadTexture(skillIcon);
					} catch (IOException e) {
						
					}
					skill.getSkill().setSpriteName(skillIcon);
					//skill.getSkill().getTags().remove("npc_only");
					skill.getSkill().setPermanent(true);
					skill.setLevel(1);
					enabled = true;
				} else {
					skill.getSkill().setDescription("Follow the path of a Knight.");
					skill.getSkill().setAuthor("");
					skill.getSkill().setName("Future Milestone");
					try {
						Global.getSettings().loadTexture("graphics/icons/skills/blank.png");
					} catch (IOException e) {
						
					}
					skill.getSkill().setSpriteName("graphics/icons/skills/blank.png");				
					//skill.getSkill().addTag("npc_only");
					skill.getSkill().setPermanent(false);
					skill.setLevel(0);
					enabled = false;
				}
			}
		}
	}
	
	public static void enableDisable() {
		for (SkillLevelAPI skill : Global.getSector().getPlayerStats().getSkillsCopy()) {
			if (skill.getSkill().getId().equals(skillID)) {
				if (skill.getLevel() > 0) {
					skill.getSkill().setDescription(skillDesc);
					skill.getSkill().setAuthor(skillAuthor);
					skill.getSkill().setName(skillName);
					try {
						Global.getSettings().loadTexture(skillIcon);
					} catch (IOException e) {
						
					}
					skill.getSkill().setSpriteName(skillIcon);
					skill.getSkill().setPermanent(true);
					enabled = true;
				} else {
					skill.getSkill().setDescription("Follow the path of a Knight.");
					skill.getSkill().setAuthor("");
					skill.getSkill().setName("Future Milestone");
					try {
						Global.getSettings().loadTexture("graphics/icons/skills/blank.png");
					} catch (IOException e) {
						
					}
					skill.getSkill().setSpriteName("graphics/icons/skills/blank.png");				
					skill.getSkill().setPermanent(false);
					enabled = false;
				}
			}
		}
	}
}



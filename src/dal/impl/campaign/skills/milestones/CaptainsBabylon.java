package dal.impl.campaign.skills.milestones;

import java.io.IOException;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.characters.ShipSkillEffect;
import com.fs.starfarer.api.characters.AfterShipCreationSkillEffect;
import com.fs.starfarer.api.characters.MutableCharacterStatsAPI.SkillLevelAPI;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipSystemAPI;
import com.fs.starfarer.api.combat.ShipSystemAPI.SystemState;
import com.fs.starfarer.api.combat.listeners.AdvanceableListener;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;
import com.fs.starfarer.api.fleet.FleetMemberAPI;
import com.fs.starfarer.api.impl.campaign.ids.Stats;
import com.fs.starfarer.api.impl.combat.PhaseCloakStats;

public class CaptainsBabylon {
	
	public static boolean FLUX_LEVEL_AFFECTS_SPEED = PhaseCloakStats.FLUX_LEVEL_AFFECTS_SPEED;
	public static float MIN_SPEED_MULT = PhaseCloakStats.MIN_SPEED_MULT;
	public static float BASE_FLUX_LEVEL_FOR_MIN_SPEED = PhaseCloakStats.BASE_FLUX_LEVEL_FOR_MIN_SPEED;
	public static boolean USE_UNNERF = true;
	public static float SPEED_BONUS = 0.25f;
	
	public static boolean enabled = false;
	public static final String skillID = "captains_babylon";
	public static final String skillName = "Babylonian";
	public static final String skillDesc = "Once we were united, a people and one. Now we are shattered, and we remember your crime.";
	public static final String skillAuthor = "Unknown";
	public static final String skillIcon = "/graphics/icons/skills/"+ skillID + ".png";
	
	//runcode dal.impl.campaign.skills.milestones.CaptainsBabylon.toggleMilestone();

	public static boolean isFlagship(MutableShipStatsAPI stats) {
		FleetMemberAPI member = stats.getFleetMember();
		if (member == null) return false;
		return member.getCaptain().isPlayer();
	}
	
	public static class Level1 implements ShipSkillEffect, AfterShipCreationSkillEffect {

		public void applyEffectsAfterShipCreation(ShipAPI ship, String id) {
			ship.addListener(new CaptainsBabylonListener(ship));
		}

		public void unapplyEffectsAfterShipCreation(ShipAPI ship, String id) {
			ship.removeListenerOfClass(CaptainsBabylonListener.class);
		}
		
		public void apply(MutableShipStatsAPI stats, HullSize hullSize, String id, float level) {
			if (!USE_UNNERF || !PhaseCloakStats.FLUX_LEVEL_AFFECTS_SPEED) {
				stats.getMaxSpeed().modifyMult(id, 1+SPEED_BONUS);
			}
		}

		public void unapply(MutableShipStatsAPI stats, HullSize hullSize, String id) {
			if (!USE_UNNERF ||!PhaseCloakStats.FLUX_LEVEL_AFFECTS_SPEED) {
				stats.getMaxSpeed().unmodifyMult(id);
			}
		}
		
		public String getEffectDescription(float level) {
			if (enabled) {
				if (USE_UNNERF && PhaseCloakStats.FLUX_LEVEL_AFFECTS_SPEED) {
					return "You no longer suffer reduced speed from instability while phased";
				}
				return "Phase ships receive a " + Math.round(SPEED_BONUS*100) + "% speed bonus";
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
	
	public static float getSpeedMult(ShipAPI ship, float effectLevel) {
		if (getDisruptionLevel(ship) <= 0f) return 1f;
		return MIN_SPEED_MULT + (1f - MIN_SPEED_MULT) * (1f - getDisruptionLevel(ship) * effectLevel); 
	}
	
	protected static float getDisruptionLevel(ShipAPI ship) {
		//return disruptionLevel;
		//if (true) return 0f;
		if (FLUX_LEVEL_AFFECTS_SPEED) {
			float threshold = ship.getMutableStats().getDynamic().getMod(
					Stats.PHASE_CLOAK_FLUX_LEVEL_FOR_MIN_SPEED_MOD).computeEffective(BASE_FLUX_LEVEL_FOR_MIN_SPEED);
			if (threshold <= 0) return 1f;
			float level = ship.getHardFluxLevel() / threshold;
			if (level > 1f) level = 1f;
			return level;
		}
		return 0f;
	}
	
	public static class CaptainsBabylonListener implements AdvanceableListener {
		protected ShipAPI ship;
		protected boolean inited = false;
		protected boolean isPhase = false;
		public CaptainsBabylonListener(ShipAPI ship) {
			this.ship = ship;
		}
		
		protected void init() {
			inited = true;
		}
		
		
		public void advance(float amount) {
			if (ship == null) return;
			if (!inited) {
				init();
			}
			if(ship.getCaptain() == null) return;
			if(ship.getCaptain().isPlayer()) {
				ShipSystemAPI cloak = ship.getPhaseCloak();
				if (cloak == null) return;
				if (Global.getCombatEngine().isPaused()) {
					return;
				}
				if (PhaseCloakStats.FLUX_LEVEL_AFFECTS_SPEED) {
					/*
					for (String key : ship.getMutableStats().getMaxSpeed().getMultMods().keySet()) {
						if (key != null && key.startsWith("phasecloak") && key.endsWith("_2")) {
							ship.getMutableStats().getMaxSpeed().unmodifyMult(key);
							//$print(key);
						}
					}
					*/
					if (ship.getPhaseCloak() != null) {
						SystemState state = ship.getPhaseCloak().getState();
						if (state == SystemState.ACTIVE || state == SystemState.OUT || state == SystemState.IN) {
							float mult = getSpeedMult(ship, ship.getPhaseCloak().getEffectLevel());
							if (mult < 1f) {
								ship.getMutableStats().getMaxSpeed().modifyMult(skillID + "_2_inverse", 1/mult);
							} else {
								ship.getMutableStats().getMaxSpeed().unmodifyMult(skillID + "_2_inverse");
							}
						}
					}
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
					skill.getSkill().setDescription("Defeat an other-worldly enemy.");
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
					skill.getSkill().setDescription("Defeat an other-worldly enemy.");
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

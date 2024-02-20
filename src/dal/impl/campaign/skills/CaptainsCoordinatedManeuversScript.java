package dal.impl.campaign.skills;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.characters.PersonAPI;
import com.fs.starfarer.api.combat.BaseEveryFrameCombatPlugin;
import com.fs.starfarer.api.combat.BattleObjectiveAPI;
import com.fs.starfarer.api.combat.CombatEngineAPI;
import com.fs.starfarer.api.combat.CombatFleetManagerAPI;
import com.fs.starfarer.api.combat.DeployedFleetMemberAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ViewportAPI;
import com.fs.starfarer.api.impl.campaign.ids.BattleObjectives;
import com.fs.starfarer.api.impl.campaign.ids.Stats;
import com.fs.starfarer.api.input.InputEventAPI;

public class CaptainsCoordinatedManeuversScript extends BaseEveryFrameCombatPlugin {
	public static final Object KEY_STATUS = new Object();
	
	public static float BASE_MAXIMUM = 10;
	public static float PER_BUOY = 5;
	public static float MULT_BUOY = 1f;
	
	public static boolean AFFECTS_SPEED = true;
	public static boolean AFFECTS_MANEUVER = true;
	public static boolean APPLY_TO_SHIPS = true;
	public static boolean APPLY_TO_FIGHTERS = true;
	public static boolean APPLY_TO_MISSILES = true;
	
	public static float MULT_SHIPS = 1.0f;
	public static float MULT_FIGHTERS = 1.0f;
	public static float MULT_MISSILES = 1.0f;
	public static float MULT_SPEED = 1.0f;
	public static float MULT_MANEUVER = 1.0f;
	
	public static final String BONUS_ID = "coord_maneuvers_bonus";
	
	private CombatEngineAPI engine;
	public void init(CombatEngineAPI engine) {
		this.engine = engine;
	}
	
	private ShipAPI prevPlayerShip = null;
	private int skipFrames = 0;
	private Set<CombatFleetManagerAPI> needsCleanup = new HashSet<CombatFleetManagerAPI>();
	public void advance(float amount, List<InputEventAPI> events) {
		if (engine == null) return;
		if (engine.isPaused()) return;
		
		
		// if the player changed flagships:
		// skip a few frames to make sure the status ends up on top of the status list
		ShipAPI playerShip = engine.getPlayerShip();
		if (playerShip != prevPlayerShip) {
			prevPlayerShip = playerShip;
			skipFrames = 20;
		}
		
		if (skipFrames > 0) {
			skipFrames--;
			return;
		}
		
		updateForSide(engine.getFleetManager(0));
		updateForSide(engine.getFleetManager(1));
		
	}
	
	
	private void updateForSide(CombatFleetManagerAPI manager) {

//		PersonAPI commander = manager.getFleetCommander();
//		if (commander == null) {
//			cleanUpIfNeeded(manager);
//			return;
//		}
//		float max = BASE_MAXIMUM + commander.getStats().getDynamic().getValue(Stats.COORDINATED_MANEUVERS_MAX, 0f);
		
		float max = 0f;
		for (PersonAPI commander : manager.getAllFleetCommanders()) {
			max = Math.max(max, BASE_MAXIMUM + commander.getStats().getDynamic().getValue(Stats.COORDINATED_MANEUVERS_MAX, 0f));
		}
		
		if (max <= 0f) {
			cleanUpIfNeeded(manager);
			return;
		}
		
		boolean buoysOnly = true;
		float total = 0f;
		List<DeployedFleetMemberAPI> deployed = manager.getDeployedCopyDFM();
		for (DeployedFleetMemberAPI member : deployed) {
			if (member.isFighterWing()) continue;
			if (member.isStationModule()) continue;
			
			float curr = member.getShip().getMutableStats().getDynamic().getValue(Stats.COORDINATED_MANEUVERS_FLAT, 0f);
			total += curr;
		}
		
		if (total > 0) buoysOnly = false;
		
		int numBuoys = 0;
		for (BattleObjectiveAPI obj : engine.getObjectives()) {
			if (obj.getOwner() == manager.getOwner() && BattleObjectives.NAV_BUOY.equals(obj.getType())) {
				total += PER_BUOY * MULT_BUOY;
				numBuoys++;
			}
		}

		
		if (total <= 0f) {
			cleanUpIfNeeded(manager);
			return;
		}
		
		//if (total > max) total = max;
		
		boolean includeSelf = false; //Removing this might break saves and meh
		includeSelf = true;
		
		float bonus = Math.min(max, Math.max(0f, total)); //moved
		//String targTypes = "";
		String boonTypes = "";
		//short targN = 0;
		
		if (CaptainsCoordinatedManeuversScript.AFFECTS_SPEED && CaptainsCoordinatedManeuversScript.AFFECTS_MANEUVER) {
			boonTypes = "top speed and maneuvering";
		} else if (CaptainsCoordinatedManeuversScript.AFFECTS_SPEED) {
			boonTypes = "top speed";
		} else if (CaptainsCoordinatedManeuversScript.AFFECTS_MANEUVER) {
			boonTypes = "maneuvering";
		}
		
		/*
		if (CaptainsCoordinatedManeuversScript.APPLY_TO_SHIPS) targN++;
		if (CaptainsCoordinatedManeuversScript.APPLY_TO_FIGHTERS) targN++;
		if (CaptainsCoordinatedManeuversScript.APPLY_TO_MISSILES) targN++;
		
		if (targN >= 3) {
			targTypes = "ships, fighters, and missiles";
		} else if (targN == 2) {
			if (CaptainsCoordinatedManeuversScript.APPLY_TO_SHIPS) {
				targTypes += "ships and ";
				if (CaptainsCoordinatedManeuversScript.APPLY_TO_FIGHTERS) targTypes += "fighters";
				if (CaptainsCoordinatedManeuversScript.APPLY_TO_MISSILES) targTypes += "missiles";
			} else {
				targTypes = "fighters and missiles";
			}
		} else {
			if (CaptainsCoordinatedManeuversScript.APPLY_TO_SHIPS) targTypes += "ships";
			if (CaptainsCoordinatedManeuversScript.APPLY_TO_FIGHTERS) targTypes += "fighters";
			if (CaptainsCoordinatedManeuversScript.APPLY_TO_MISSILES) targTypes += "missiles";
		}
		*/
		for (DeployedFleetMemberAPI member : deployed) {
			if (!APPLY_TO_FIGHTERS) { if (member.isFighterWing()) continue; }
			if (member.getShip() == null) continue;
			if (!APPLY_TO_FIGHTERS) { if (member.getShip().isFighter()) continue; }
			if (!APPLY_TO_SHIPS) { if (!member.getShip().isFighter()) continue; }
			
			/*Dead vanilla code
			float curr = member.getShip().getMutableStats().getDynamic().getValue(Stats.COORDINATED_MANEUVERS_FLAT, 0f);
			if (includeSelf) curr = 0f;
			*/

			if (AFFECTS_SPEED) {
				if (member.getShip().isFighter()) {
					member.getShip().getMutableStats().getMaxSpeed().modifyPercent(BONUS_ID, bonus * MULT_SPEED * MULT_FIGHTERS);
				} else {
					member.getShip().getMutableStats().getMaxSpeed().modifyPercent(BONUS_ID, bonus * MULT_SPEED * MULT_SHIPS);
				}
			}
			if (AFFECTS_MANEUVER) {
				if (member.getShip().isFighter()) {
					member.getShip().getMutableStats().getTurnAcceleration().modifyPercent(BONUS_ID, bonus * MULT_MANEUVER * MULT_FIGHTERS);
					member.getShip().getMutableStats().getAcceleration().modifyPercent(BONUS_ID, bonus * MULT_MANEUVER * MULT_FIGHTERS);
				} else {
					member.getShip().getMutableStats().getTurnAcceleration().modifyPercent(BONUS_ID, bonus * MULT_MANEUVER * MULT_SHIPS);
					member.getShip().getMutableStats().getAcceleration().modifyPercent(BONUS_ID, bonus * MULT_MANEUVER * MULT_SHIPS);
				}
			}
		}
		if (APPLY_TO_MISSILES) {
			for (DeployedFleetMemberAPI member : deployed) {
				if (member.getShip() == null) continue;
				if (AFFECTS_SPEED) {
					member.getShip().getMutableStats().getMissileMaxSpeedBonus().modifyPercent(BONUS_ID, bonus * MULT_SPEED * MULT_MISSILES);
				}
				if (AFFECTS_MANEUVER) {
					member.getShip().getMutableStats().getMissileAccelerationBonus().modifyPercent(BONUS_ID, bonus * MULT_MANEUVER * MULT_MISSILES);
					member.getShip().getMutableStats().getMissileMaxTurnRateBonus().modifyPercent(BONUS_ID, bonus * MULT_MANEUVER * MULT_MISSILES);
					member.getShip().getMutableStats().getMissileTurnAccelerationBonus().modifyPercent(BONUS_ID, bonus * MULT_MANEUVER * MULT_MISSILES);
				}
			}
		}
		
		needsCleanup.add(manager);
		
		
		if (manager.getOwner() == engine.getPlayerShip().getOwner()) {
			//if (engine.getPlayerShip().isShuttlePod()) return;
			
			//float curr = engine.getPlayerShip().getMutableStats().getDynamic().getValue(Stats.COORDINATED_MANEUVERS_FLAT, 0f);
			//if (includeSelf) curr = 0f;

			//float bonus = Math.min(max, Math.max(0f, total - curr)); //moved out of loops
			
			String title = "Coordinated Maneuvers:" + " " + (int) Math.min(max, total) + "%";
			//String data = "+" + (int)bonus + "% top speed (ship: " + (int) curr + "%)";
			String data = "+" + (int)bonus + "% " + boonTypes;
			if (buoysOnly) {
				title = "Nav Buoy";
				if (numBuoys > 1) {
					title += "s";
					title += " (" + numBuoys + ")";
				}
				data = "+" + (int)bonus + "% " + boonTypes;
			}
			String icon = Global.getSettings().getSpriteName("ui", "icon_tactical_coordinated_maneuvers");
			engine.maintainStatusForPlayerShip(KEY_STATUS, icon,
						title, 
						data, false);
		}
	}
	
	protected void cleanUpIfNeeded(CombatFleetManagerAPI manager) {
		if (needsCleanup.contains(manager)) {
			needsCleanup.remove(manager);
			List<DeployedFleetMemberAPI> deployed = manager.getDeployedCopyDFM();
			for (DeployedFleetMemberAPI member : deployed) {
				if (!APPLY_TO_FIGHTERS) { if (member.isFighterWing()) continue; }
				if (member.getShip() == null) continue;
				if (!APPLY_TO_FIGHTERS) { if (member.getShip().isFighter()) continue; }
				if (!APPLY_TO_SHIPS) { if (!member.getShip().isFighter()) continue; }
				
				if (AFFECTS_SPEED) {
					member.getShip().getMutableStats().getMaxSpeed().unmodifyPercent(BONUS_ID);
				}
				if (AFFECTS_MANEUVER) {
					member.getShip().getMutableStats().getTurnAcceleration().unmodifyPercent(BONUS_ID);
					member.getShip().getMutableStats().getAcceleration().unmodifyPercent(BONUS_ID);
				}
			}
			if (APPLY_TO_MISSILES) {
				for (DeployedFleetMemberAPI member : deployed) {
					if (member.getShip() == null) continue;
					if (AFFECTS_SPEED) {
						member.getShip().getMutableStats().getMissileMaxSpeedBonus().unmodifyPercent(BONUS_ID);
					}
					if (AFFECTS_MANEUVER) {
						member.getShip().getMutableStats().getMissileAccelerationBonus().unmodifyPercent(BONUS_ID);
						member.getShip().getMutableStats().getMissileMaxTurnRateBonus().unmodifyPercent(BONUS_ID);
						member.getShip().getMutableStats().getMissileTurnAccelerationBonus().unmodifyPercent(BONUS_ID);
					}
				}
			}
		}
	}
	
	
//	public static PersonAPI getCommander(CombatFleetManagerAPI manager) {
//		List<DeployedFleetMemberAPI> deployed = manager.getDeployedCopyDFM();
//		if (deployed.isEmpty()) return null;
//		
//		PersonAPI defaultCommander = manager.getDefaultCommander();
//		for (DeployedFleetMemberAPI member : deployed) {
//			if (member.isFighterWing()) continue;
//			FleetMemberAPI m = member.getMember();
//			PersonAPI commander = m.getFleetCommanderForStats();
//			if (commander == null && m.getFleetData() != null) {
//				commander = m.getFleetData().getCommander();
//			}
//			if (commander == null) {
//				commander = defaultCommander;
//			}
//			return commander;
//		}
//		return null;
//	}
	

	public void renderInUICoords(ViewportAPI viewport) {
	}

	public void renderInWorldCoords(ViewportAPI viewport) {
	}

}

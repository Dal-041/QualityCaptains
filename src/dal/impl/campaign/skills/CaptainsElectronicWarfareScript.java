package dal.impl.campaign.skills;

import java.util.ArrayList;
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

public class CaptainsElectronicWarfareScript extends BaseEveryFrameCombatPlugin {
	public static Object KEY_STATUS = new Object();
	public static Object KEY_STATUS2 = new Object();
	public static Object KEY_STATUS_ENEMY_RATING = new Object();
	
	public static float BASE_MAXIMUM = 20;
	
	public static float PER_JAMMER = 5;
	public static float JAMMER_MULT = 1;
	
	public static boolean APPLY_TO_SHIPS = true;
	public static boolean APPLY_TO_FIGHTERS = true;
	public static boolean APPLY_TO_TURRETS = true;
	public static boolean APPLY_TO_MISSILES = true;
	
	public static boolean APPLIES_TO_RECOIL = true;
	public static boolean APPLIES_TO_AUTOAIM = true;
	public static boolean APPLIES_TO_RANGE = false;
	public static boolean APPLIES_TO_TURRET_TURNRATE = false;
	public static boolean APPLIES_TO_MISSILE_TURNRATE = false;
	
	public static float AUTOAIM_MULT = 1.0f;
	public static float RANGE_MULT = 1.0f;
	public static float RECOIL_MULT = 2.0f;
	public static float TURRET_TURN_MULT = 1.0f;
	public static float MISSILE_TURN_MULT = 1.0f;
	
	public static String PENALTY_ID = "electronic_warfare_penalty";

	//QC
	public static String penaltyStr;
	public static ArrayList<String> penTypes;

	private CombatEngineAPI engine;
	public void init(CombatEngineAPI engine) {
		this.engine = engine;

		penTypes = new ArrayList<String>();
		penaltyStr = "";

		if (APPLY_TO_TURRETS) {
			if (APPLIES_TO_RECOIL) { penTypes.add(" recoil"); }
			if (APPLIES_TO_RANGE) { penTypes.add(" range"); }
			if (APPLIES_TO_AUTOAIM) { penTypes.add(" autoaim"); }
			if (APPLIES_TO_TURRET_TURNRATE) { penTypes.add(" turret turnrate"); }
		}

		if (APPLY_TO_MISSILES && APPLIES_TO_MISSILE_TURNRATE) { penTypes.add(" missile maneuver"); }

		if (penTypes.size() >= 3) {
			for (int i = 0; i < penTypes.size(); i++) {
				penaltyStr += penTypes.get(i);
				if (i < penTypes.size()-1) penaltyStr += ",";
				if (i == 0) penaltyStr += "\n";
				if (i == penTypes.size()-2) penaltyStr += " and";
			}
		} else {
			for (int i = 0; i < penTypes.size(); i++) {
				penaltyStr += penTypes.get(i);
				if (i==0 && penTypes.size() > 1) penaltyStr += " and";
			}
		}
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
		
		
		int [] player = getTotalAndMaximum(engine.getFleetManager(0));
		int [] enemy = getTotalAndMaximum(engine.getFleetManager(1));
		
		if (player == null || enemy == null) {
			cleanUpIfNeeded(engine.getFleetManager(0));
			cleanUpIfNeeded(engine.getFleetManager(1));
			return;
		}

		if (engine.getFleetManager(0) == null || engine.getFleetManager(1) == null) {
			cleanUpIfNeeded(engine.getFleetManager(0));
			cleanUpIfNeeded(engine.getFleetManager(1));
			return;
		}

		float pTotal = player[0];
		float pMax = player[1];
		
		float eTotal = enemy[0];
		float eMax = enemy[1];

		if (pTotal <= 0) cleanUpIfNeeded(engine.getFleetManager(1));
		if (eTotal <= 0) cleanUpIfNeeded(engine.getFleetManager(0));


		int totalPenalty = Math.round(Math.min(BASE_MAXIMUM, pTotal + eTotal));
		if (totalPenalty <= 0f) return;

		float ecmRatingToPenaltyMult = 1f;

		float playerPenalty = (int) Math.min(eTotal * ecmRatingToPenaltyMult, eMax);
		if (pTotal > 0 && playerPenalty > 0) {
			float pMult = eTotal / (eTotal + pTotal);
			playerPenalty *= pMult;
		}

		float enemyPenalty = (int) Math.min(pTotal * ecmRatingToPenaltyMult, pMax);
		if (eTotal > 0 && enemyPenalty > 0) {
			float eMult = pTotal / (eTotal + pTotal);
			enemyPenalty *= eMult;
		}

		playerPenalty = Math.round(playerPenalty);
		enemyPenalty = Math.round(enemyPenalty);


		String icon = Global.getSettings().getSpriteName("ui", "icon_tactical_electronic_warfare");

		//Old

		String spaceStr = "";
		if (penTypes.size() >= 3) spaceStr = "                    ";
		String dataA = spaceStr + "-" + (int) playerPenalty + "%" + penaltyStr;
		String dataE = spaceStr + "-" + (int) enemyPenalty + "% enemy" + penaltyStr;


		if (playerPenalty > 0) {
			applyPenalty(engine.getFleetManager(0), playerPenalty, eMax);

			String sMax = "";
			if (eMax <= playerPenalty) sMax = " (max)";
			String title = "Enemy ECM rating: " + (int) eTotal + "%";
			String data = "" + (int)playerPenalty + "% impact on allies" + sMax;
			if (eMax <= 0) {
				dataA = "fully neutralized";
			}
			engine.maintainStatusForPlayerShip(KEY_STATUS_ENEMY_RATING, icon, title, dataA, eMax > 0);
		}

		if (enemyPenalty > 0) {
			applyPenalty(engine.getFleetManager(1), enemyPenalty, pMax);

			String sMax = "";
			if (pMax <= enemyPenalty) sMax = " (max)";
			String title = "Allied ECM rating:" + " " + (int) pTotal + "%";
			String data = "" + (int)enemyPenalty + "% impact on enemy" + sMax;
			if (pMax <= 0) {
				data = "fully neutralized";
			}
			engine.maintainStatusForPlayerShip(KEY_STATUS, icon, title, data, false);
		}

		if (playerPenalty > 0 && eMax > 0 && engine.getPlayerShip() != null) {
			int eccm = 100 - (int) Math.round(engine.getPlayerShip().getMutableStats().getDynamic().getValue(Stats.ELECTRONIC_WARFARE_PENALTY_MOD, 100f));
			if (eccm > 100) eccm = 100;
			if (eccm < 0) eccm = 0;
			//eccm = -eccm;
			if (eccm != 0) {
				engine.maintainStatusForPlayerShip(KEY_STATUS2, icon, "On-board ECCM", "" + eccm + "% enemy ecm neutralized", false);
			}
		}

		/*

		boolean playerWon = winner.getOwner() == engine.getPlayerShip().getOwner(); 
		
		String title = "ECM rating:" + " " + (int) pTotal + "% vs " + (int)eTotal + "%:";
		String penaltyStr = "";
		ArrayList<String> penTypes = new ArrayList<String>();
		if (APPLY_TO_TURRETS) {
			if (APPLIES_TO_RECOIL) { penTypes.add(" recoil"); }
			if (APPLIES_TO_RANGE) { penTypes.add(" range"); }
			if (APPLIES_TO_AUTOAIM) { penTypes.add(" autoaim"); }
			if (APPLIES_TO_TURRET_TURNRATE) { penTypes.add(" turret turnrate"); }
		}
		if (APPLY_TO_MISSILES && APPLIES_TO_MISSILE_TURNRATE) { penTypes.add(" missile maneuver"); }

		if (penTypes.size() >= 3) {
			for (int i = 0; i < penTypes.size(); i++) {
				penaltyStr += penTypes.get(i);
				if (i < penTypes.size()-1) penaltyStr += ",";
				if (i == 0) penaltyStr += "\n";
				if (i == penTypes.size()-2) penaltyStr += " and";
			}
		} else {
			for (int i = 0; i < penTypes.size(); i++) {
				penaltyStr += penTypes.get(i);
				if (i==0 && penTypes.size() > 1) penaltyStr += " and";
			}
		}
		
		if (penaltyStr != "") {
			String spaceStr = "";
			if (penTypes.size() >= 3) spaceStr = "                     ";
			String data = spaceStr + "-" + (int)penalty + "%" + penaltyStr;
			if (playerWon) {
				data = spaceStr + "-" + (int)penalty + "% enemy" + penaltyStr;
			}
		
			String icon = Global.getSettings().getSpriteName("ui", "icon_tactical_electronic_warfare");
			if (engine.getPlayerShip() != null && !playerWon) {
				int eccm = 100 - (int) Math.round(engine.getPlayerShip().getMutableStats().getDynamic().getValue(Stats.ELECTRONIC_WARFARE_PENALTY_MOD, 100f));
				if (eccm > 100) eccm = 100;
				if (eccm < 0) eccm = 0;
				//eccm = -eccm;
				if (eccm != 0) {
					//engine.maintainStatusForPlayerShip(KEY_STATUS2, icon, "On-board ECCM", "up to " + eccm + "% ecm neutralized", false);
					engine.maintainStatusForPlayerShip(KEY_STATUS2, icon, "On-board ECCM", "" + eccm + "% ecm neutralized", false);
				}
			}
			engine.maintainStatusForPlayerShip(KEY_STATUS, icon, title, data, !playerWon);
		}
		 */
	}
	
	private void applyPenalty(CombatFleetManagerAPI manager, float penalty, float maxPenalty) {
		List<DeployedFleetMemberAPI> deployed = manager.getDeployedCopyDFM();
		for (DeployedFleetMemberAPI member : deployed) {
			if (!APPLY_TO_FIGHTERS) { if (member.isFighterWing()) continue; }
			if (member.getShip() == null) continue;
			if (!APPLY_TO_FIGHTERS) { if (member.getShip().isFighter()) continue; }
			if (!APPLY_TO_SHIPS) { if (!member.getShip().isFighter()) continue; }
			
			float currPenalty = penalty * member.getShip().getMutableStats().getDynamic().getValue(Stats.ELECTRONIC_WARFARE_PENALTY_MULT);
			currPenalty = member.getShip().getMutableStats().getDynamic().getValue(Stats.ELECTRONIC_WARFARE_PENALTY_MOD, currPenalty);
			if (currPenalty < 0) currPenalty = 0;
			
			float maxMod = penalty * member.getShip().getMutableStats().getDynamic().getValue(Stats.ELECTRONIC_WARFARE_PENALTY_MAX_FOR_SHIP_MOD, 0);
			float currMax = maxPenalty + maxMod;
			if (currPenalty > currMax) {
				currPenalty = currMax;
			}
			if (APPLY_TO_TURRETS) {
				if (APPLIES_TO_TURRET_TURNRATE) {
					member.getShip().getMutableStats().getBeamWeaponTurnRateBonus().modifyMult(PENALTY_ID, 1f - TURRET_TURN_MULT * currPenalty/100f);
				}
				if (APPLIES_TO_RECOIL) {
					member.getShip().getMutableStats().getRecoilPerShotMult().modifyMult(PENALTY_ID, 1f + RECOIL_MULT * currPenalty/100f);
					member.getShip().getMutableStats().getRecoilDecayMult().modifyMult(PENALTY_ID, 1f - RECOIL_MULT * currPenalty/100f);
					member.getShip().getMutableStats().getMaxRecoilMult().modifyMult(PENALTY_ID, 1f + RECOIL_MULT * currPenalty/100f);
				}
				if (APPLIES_TO_AUTOAIM) {
					member.getShip().getMutableStats().getAutofireAimAccuracy().modifyMult(PENALTY_ID, 1f - AUTOAIM_MULT * currPenalty/100f);
				}
				if (APPLIES_TO_RANGE) {
					member.getShip().getMutableStats().getBallisticWeaponRangeBonus().modifyMult(PENALTY_ID, 1f - RANGE_MULT * currPenalty/100f);
					member.getShip().getMutableStats().getEnergyWeaponRangeBonus().modifyMult(PENALTY_ID, 1f - RANGE_MULT * currPenalty/100f);
				}
			}
			if (APPLY_TO_MISSILES) {
				if (APPLIES_TO_RANGE) {
					member.getShip().getMutableStats().getMissileWeaponRangeBonus().modifyMult(PENALTY_ID, 1f - RANGE_MULT * currPenalty/100f);
				}
				if (APPLIES_TO_MISSILE_TURNRATE) {
					member.getShip().getMutableStats().getMissileAccelerationBonus().modifyMult(PENALTY_ID, 1f - MISSILE_TURN_MULT * currPenalty/100f);
					member.getShip().getMutableStats().getMissileMaxTurnRateBonus().modifyMult(PENALTY_ID, 1f - MISSILE_TURN_MULT * currPenalty/100f);
					member.getShip().getMutableStats().getMissileTurnAccelerationBonus().modifyMult(PENALTY_ID, 1f - MISSILE_TURN_MULT * currPenalty/100f);
				}
			}
		}
		
		needsCleanup.add(manager);
	}
	
	protected void cleanUpIfNeeded(CombatFleetManagerAPI manager) {
		if (needsCleanup.contains(manager)) {
			needsCleanup.remove(manager);
			List<DeployedFleetMemberAPI> deployed = manager.getDeployedCopyDFM();
			for (DeployedFleetMemberAPI member : deployed) {
				if (!APPLY_TO_FIGHTERS) { if (member.isFighterWing()) continue; }
				if (member.getShip() == null) continue;
				if (!APPLY_TO_SHIPS) { if (!member.getShip().isFighter()) continue; }
				
				if (APPLY_TO_TURRETS) {
					if (APPLIES_TO_TURRET_TURNRATE) {
						member.getShip().getMutableStats().getBeamWeaponTurnRateBonus().unmodify(PENALTY_ID);
					}
					if (APPLIES_TO_RECOIL) {
						member.getShip().getMutableStats().getRecoilPerShotMult().unmodify(PENALTY_ID);
						member.getShip().getMutableStats().getRecoilDecayMult().unmodify(PENALTY_ID);
						member.getShip().getMutableStats().getMaxRecoilMult().unmodify(PENALTY_ID);
					}
					if (APPLIES_TO_AUTOAIM) {
						member.getShip().getMutableStats().getAutofireAimAccuracy().unmodify(PENALTY_ID);
					}
					if (APPLIES_TO_RANGE) {
						member.getShip().getMutableStats().getBallisticWeaponRangeBonus().unmodify(PENALTY_ID);
						member.getShip().getMutableStats().getEnergyWeaponRangeBonus().unmodify(PENALTY_ID);
					}
				}
				if (APPLY_TO_MISSILES) {
					if (APPLIES_TO_RANGE) {
						member.getShip().getMutableStats().getMissileWeaponRangeBonus().unmodify(PENALTY_ID);
					}
					if (APPLIES_TO_MISSILE_TURNRATE) {
						member.getShip().getMutableStats().getMissileAccelerationBonus().unmodify(PENALTY_ID);
						member.getShip().getMutableStats().getMissileMaxTurnRateBonus().unmodify(PENALTY_ID);
						member.getShip().getMutableStats().getMissileTurnAccelerationBonus().unmodify(PENALTY_ID);
					}
				}
			}
		}
	}
	
	private int [] getTotalAndMaximum(CombatFleetManagerAPI manager) {
//		PersonAPI commander = manager.getFleetCommander();
//		if (commander == null) {
//			return null;
//		}
//		float max = BASE_MAXIMUM + commander.getStats().getDynamic().getValue(Stats.ELECTRONIC_WARFARE_MAX, 0f);
		
		float max = 0f;
		for (PersonAPI commander : manager.getAllFleetCommanders()) {
			max = Math.max(max, BASE_MAXIMUM + commander.getStats().getDynamic().getValue(Stats.ELECTRONIC_WARFARE_MAX, 0f));
		}
		
		
		float total = 0f;
		List<DeployedFleetMemberAPI> deployed = manager.getDeployedCopyDFM();
		float canCounter = 0f;
		for (DeployedFleetMemberAPI member : deployed) {
			if (member.isFighterWing()) continue;
			if (member.isStationModule()) continue;
			float curr = member.getShip().getMutableStats().getDynamic().getValue(Stats.ELECTRONIC_WARFARE_FLAT, 0f);
			total += curr;
			canCounter += member.getShip().getMutableStats().getDynamic().getValue(Stats.SHIP_BELONGS_TO_FLEET_THAT_CAN_COUNTER_EW, 0f);
		}
		
		for (BattleObjectiveAPI obj : engine.getObjectives()) {
			if (obj.getOwner() == manager.getOwner() && BattleObjectives.SENSOR_JAMMER.equals(obj.getType())) {
				total += PER_JAMMER * JAMMER_MULT;
			}
		}

		int counter = 0;
		if (canCounter > 0) counter = 1;

		return new int [] {(int) total, (int) max, counter};
	}

	
	
	
	
	public void renderInUICoords(ViewportAPI viewport) {
	}

	public void renderInWorldCoords(ViewportAPI viewport) {
	}

}

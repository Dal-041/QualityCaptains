package dal.impl.campaign.skills;

import com.fs.starfarer.api.characters.ShipSkillEffect;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;
import com.fs.starfarer.api.fleet.FleetMemberAPI;
import com.fs.starfarer.api.impl.campaign.ids.Stats;
import com.fs.starfarer.api.impl.campaign.skills.WolfpackTactics;

public class CaptainsWolfpackTactics {
	
	public static boolean loadStock = false;
	private static WolfpackTactics.Level1A lev1 = new WolfpackTactics.Level1A();
	
	public static float DAMAGE_TO_LARGER_BONUS = 10f;
	public static float DAMAGE_TO_LARGER_BONUS_DEST = 0f;
	public static float PEAK_TIME_BONUS = 50f;
	public static float PEAK_TIME_BONUS_DEST = 35f;
	
	public static float FLAGSHIP_SPEED_BONUS = 25f;
	public static float FLAGSHIP_CP_BONUS = 100f;
	
	public static int NFLUX_BOOST = 10;
	public static int MANEUVER_BONUS = 40;
	public static int SPEED_BONUS = 10;
	
	public static int ARMOR_BONUS = 150;
	
	public static boolean isFrigateAndOfficer(MutableShipStatsAPI stats) {
		if (stats.getEntity() instanceof ShipAPI) {
			ShipAPI ship = (ShipAPI) stats.getEntity();
			if (!ship.isFrigate()) return false;
			return !ship.getCaptain().isDefault();
		} else {
			FleetMemberAPI member = stats.getFleetMember();
			if (member == null) return false;
			if (!member.isFrigate()) return false;
			return !member.getCaptain().isDefault();
		}
	}
	
	public static boolean isDestroyerAndOfficer(MutableShipStatsAPI stats) {
		if (stats.getEntity() instanceof ShipAPI) {
			ShipAPI ship = (ShipAPI) stats.getEntity();
			if (!ship.isDestroyer()) return false;
			return !ship.getCaptain().isDefault();
		} else {
			FleetMemberAPI member = stats.getFleetMember();
			if (member == null) return false;
			if (!member.isDestroyer()) return false;
			return !member.getCaptain().isDefault();
		}
	}
	
	public static boolean isFrigateAndFlagship(MutableShipStatsAPI stats) {
		if (stats.getEntity() instanceof ShipAPI) {
			ShipAPI ship = (ShipAPI) stats.getEntity();
			if (!ship.isFrigate()) return false;
			if (ship.getFleetMember() != null && 
					ship.getFleetMember().getFleetCommander() == ship.getCaptain()) {
				return true;
			}
			return ship.getCaptain().isPlayer();
		} else {
			FleetMemberAPI member = stats.getFleetMember();
			if (member == null) return false;
			if (!member.isFrigate()) return false;
			if (member.isFlagship()) {
				return true;
			}
			return member.getCaptain().isPlayer();
		}
	}
	public static boolean isDestroyerAndFlagship(MutableShipStatsAPI stats) {
		if (stats.getEntity() instanceof ShipAPI) {
			ShipAPI ship = (ShipAPI) stats.getEntity();
			if (!ship.isDestroyer()) return false;
			if (ship.getFleetMember() != null && 
					ship.getFleetMember().getFleetCommander() == ship.getCaptain()) {
				return true;
			}
			return ship.getCaptain().isPlayer();
		} else {
			FleetMemberAPI member = stats.getFleetMember();
			if (member == null) return false;
			if (!member.isDestroyer()) return false;
			if (member.isFlagship()) {
				return true;
			}
			return member.getCaptain().isPlayer();
		}
	}
	
	public static boolean isFlagship(MutableShipStatsAPI stats) {
		if (stats.getEntity() instanceof ShipAPI) {
			ShipAPI ship = (ShipAPI) stats.getEntity();
			if (ship.getFleetMember() != null && 
					ship.getFleetMember().getFleetCommander() == ship.getCaptain()) {
				return true;
			}
			return ship.getCaptain().isPlayer();
		} else {
			FleetMemberAPI member = stats.getFleetMember();
			if (member == null) return false;
			if (member.isFlagship()) {
				return true;
			}
			return member.getCaptain().isPlayer();
		}
	}
	
	public static class Level1A implements ShipSkillEffect {
		public void apply(MutableShipStatsAPI stats, HullSize hullSize, String id, float level) {
			if (!loadStock) {
			if (isFrigateAndOfficer(stats)) {
				stats.getDamageToDestroyers().modifyPercent(id, DAMAGE_TO_LARGER_BONUS);
				stats.getDamageToCruisers().modifyPercent(id, DAMAGE_TO_LARGER_BONUS);
				stats.getDamageToCapital().modifyPercent(id, DAMAGE_TO_LARGER_BONUS);
				
				stats.getPeakCRDuration().modifyPercent(id, PEAK_TIME_BONUS);
				stats.getDynamic().getMod(Stats.INDIVIDUAL_SHIP_RECOVERY_MOD).modifyFlat(id, 1000f);
			} else if (isDestroyerAndOfficer(stats)) {
				//stats.getDamageToCruisers().modifyPercent(id, DAMAGE_TO_LARGER_BONUS_DEST);
				//stats.getDamageToCapital().modifyPercent(id, DAMAGE_TO_LARGER_BONUS_DEST);
				
				stats.getPeakCRDuration().modifyPercent(id, PEAK_TIME_BONUS_DEST);
				//stats.getDynamic().getMod(Stats.INDIVIDUAL_SHIP_RECOVERY_MOD).modifyFlat(id, 1000f);
			}
			} else {
				lev1.apply(stats, hullSize, id, level);
			}
		}
		
		public void unapply(MutableShipStatsAPI stats, HullSize hullSize, String id) {
			if (!loadStock) {
			stats.getDamageToDestroyers().unmodifyPercent(id);
			stats.getDamageToCruisers().unmodifyPercent(id);
			stats.getDamageToCapital().unmodifyPercent(id);
			
			stats.getPeakCRDuration().unmodifyPercent(id);
			//stats.getDynamic().getMod(Stats.INDIVIDUAL_SHIP_RECOVERY_MOD).unmodify(id);
			} else {
					lev1.unapply(stats, hullSize, id);
			}
		}
		
		public String getEffectDescription(float level) {
			if (!loadStock) {
			//return "+" + (int)Math.round(DAMAGE_TO_LARGER_BONUS) + "% damage to ships larger than frigates";
			
			return "+" + (int)Math.round(DAMAGE_TO_LARGER_BONUS) + "% damage to ships larger than frigates if frigate, " +
				   //"+" + (int)Math.round(DAMAGE_TO_LARGER_BONUS_DEST) + "% damage to capital ships and cruisers if destroyer\n" +
				   "+" + (int)(PEAK_TIME_BONUS) + "% seconds peak operating time if frigate, " + 
				   "+" + (int)(PEAK_TIME_BONUS_DEST) + "% if destroyer\n"
//			return "+" + (int)Math.round(DAMAGE_TO_LARGER_BONUS) + "% damage to ships larger than frigates\n" +
//			"+" + (int)(PEAK_TIME_BONUS) + " seconds peak operating time\n" +
			+ "If lost in combat, these ships are almost always recoverable";
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
			if (isFrigateAndOfficer(stats)) {
				stats.getMaxSpeed().modifyFlat(id, SPEED_BONUS);
				stats.getAcceleration().modifyPercent(id, MANEUVER_BONUS);
				stats.getDeceleration().modifyPercent(id, MANEUVER_BONUS);
				stats.getTurnAcceleration().modifyPercent(id, MANEUVER_BONUS * 2f);
				stats.getMaxTurnRate().modifyPercent(id, MANEUVER_BONUS);
			}
		}
		
		public void unapply(MutableShipStatsAPI stats, HullSize hullSize, String id) {
			stats.getAcceleration().unmodify(id);
			stats.getDeceleration().unmodify(id);
			stats.getTurnAcceleration().unmodify(id);
			stats.getMaxTurnRate().unmodify(id);
			stats.getMaxSpeed().unmodify(id);
		}
		
		public String getEffectDescription(float level) {
			return "+" + (int)(MANEUVER_BONUS) + "% maneuverability and +" + (int)(SPEED_BONUS) + " su/s top speed";
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
			if (isFrigateAndOfficer(stats)) {
				//stats.getMaxSpeed().modifyFlat(id, SPEED_BONUS);
				stats.getZeroFluxSpeedBoost().modifyFlat(id, NFLUX_BOOST);
			}
		}
		
		public void unapply(MutableShipStatsAPI stats, HullSize hullSize, String id) {
			//stats.getMaxSpeed().unmodify(id);
			stats.getZeroFluxSpeedBoost().unmodify(id);
		}
		
		public String getEffectDescription(float level) {
			return "+" + (int)(NFLUX_BOOST) + " su/s to 0-flux speed boost";
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
			if (isFrigateAndOfficer(stats)) {
				stats.getEffectiveArmorBonus().modifyFlat(id, ARMOR_BONUS);
			}
		}
		
		public void unapply(MutableShipStatsAPI stats, HullSize hullSize, String id) {
			stats.getEffectiveArmorBonus().unmodify(id);
		}	
		
		public String getEffectDescription(float level) {
			return "+" + (int)(ARMOR_BONUS) + " armor for damage reduction calculation only";
		}
		
		public String getEffectPerLevelDescription() {
			return null;
		}
		
		public ScopeDescription getScopeDescription() {
			return ScopeDescription.PILOTED_SHIP;
		}
		
	}
}

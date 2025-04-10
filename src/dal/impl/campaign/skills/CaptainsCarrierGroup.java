package dal.impl.campaign.skills;

import com.fs.starfarer.api.characters.ShipSkillEffect;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;
import com.fs.starfarer.api.fleet.FleetMemberAPI;
import com.fs.starfarer.api.impl.campaign.skills.BaseSkillEffectDescription;

public class CaptainsCarrierGroup {
	
	public static float FIGHTER_DAMAGE_REDUCTION = 15;
	public static float FIGHTER_REPLACEMENT_RATE_BONUS = 20;
	public static float OFFICER_MULT = 1.5f;
	
	public static boolean isSourceOfficer(MutableShipStatsAPI stats) {
		if (stats.getEntity() instanceof ShipAPI) {
			ShipAPI ship = (ShipAPI) stats.getEntity();
			if (ship == null || ship.getCaptain() == null) return false;
			if (ship.getWing() != null && ship.getWing().getSourceShip() != null) return !ship.getWing().getSourceShip().getCaptain().isDefault();
			return false;
		} else {
			return false;
		}
	}
	
	public static boolean isOfficer(MutableShipStatsAPI stats) {
		FleetMemberAPI member = stats.getFleetMember();
		if (member == null) return false;
		return !member.getCaptain().isDefault();
	}

	public static class Level1 implements ShipSkillEffect {
		public void apply(MutableShipStatsAPI stats, HullSize hullSize, String id, float level) {
			float effect = FIGHTER_DAMAGE_REDUCTION;
			if (isSourceOfficer(stats)) effect *= OFFICER_MULT;
			stats.getHullDamageTakenMult().modifyMult(id, 1f - effect / 100f);
			stats.getArmorDamageTakenMult().modifyMult(id, 1f - effect / 100f);
			stats.getShieldDamageTakenMult().modifyMult(id, 1f - effect / 100f);
		}
		
		public void unapply(MutableShipStatsAPI stats, HullSize hullSize, String id) {
			stats.getHullDamageTakenMult().unmodify(id);
			stats.getArmorDamageTakenMult().unmodify(id);
			stats.getShieldDamageTakenMult().unmodify(id);
		}
		
		public String getEffectDescription(float level) {
			return "-" + (int)(FIGHTER_DAMAGE_REDUCTION) + "% damage taken by piloted ship's fighters";
		}
		
		public String getEffectPerLevelDescription() {
			return null;
		}

		public ScopeDescription getScopeDescription() {
			return ScopeDescription.ALL_FIGHTERS;
		}
	}
	
	public static class Level2 implements ShipSkillEffect {

		public void apply(MutableShipStatsAPI stats, HullSize hullSize, String id, float level) {
			if (BaseSkillEffectDescription.hasFighterBays(stats)) {
				float rateBonus = FIGHTER_REPLACEMENT_RATE_BONUS;
				if (isOfficer(stats)) rateBonus *= OFFICER_MULT;
				float timeMult = 1f / ((100f + rateBonus) / 100f);
				stats.getFighterRefitTimeMult().modifyMult(id, timeMult);
			}
		}
		
		public void unapply(MutableShipStatsAPI stats, HullSize hullSize, String id) {
			stats.getFighterRefitTimeMult().unmodify(id);
		}	
		
		public String getEffectDescription(float level) {
			return "" + (int)(FIGHTER_REPLACEMENT_RATE_BONUS) + "% faster fighter replacements on piloted ship";
		}
		
		public String getEffectPerLevelDescription() {
			return null;
		}
		
		public ScopeDescription getScopeDescription() {
			return ScopeDescription.ALL_SHIPS;
		}
	}

}






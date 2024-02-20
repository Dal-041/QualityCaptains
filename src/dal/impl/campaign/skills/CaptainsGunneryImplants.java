package dal.impl.campaign.skills;

import com.fs.starfarer.api.characters.ShipSkillEffect;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;
import com.fs.starfarer.api.fleet.FleetMemberAPI;
import com.fs.starfarer.api.impl.campaign.ids.Stats;

public class CaptainsGunneryImplants {
	
	public static boolean loadStock = false;
	public static float RECOIL_BONUS = 35f;
	public static float TARGET_LEADING_BONUS = 100f;
	public static float RANGE_BONUS = 10f;
	public static float WEAPON_HP_BONUS = 20f;
	public static float GEN_SKILL_MULT = 0.5f;
	
	public static boolean isOfficer(MutableShipStatsAPI stats) {
		FleetMemberAPI member = stats.getFleetMember();
		if (member == null) return false;
		return !member.getCaptain().isDefault();
	}

	public static class Level1 implements ShipSkillEffect {
		public void apply(MutableShipStatsAPI stats, HullSize hullSize, String id, float level) {
			stats.getMaxRecoilMult().modifyMult(id, 1f - (0.01f * RECOIL_BONUS));
			stats.getRecoilPerShotMult().modifyMult(id, 1f - (0.01f * RECOIL_BONUS));
			// slower recoil recovery, also, to match the reduced recoil-per-shot
			// overall effect is same as without skill but halved in every respect
			stats.getRecoilDecayMult().modifyMult(id, 1f - (0.01f * RECOIL_BONUS));
		}
		
		public void unapply(MutableShipStatsAPI stats, HullSize hullSize, String id) {
			stats.getMaxRecoilMult().unmodify(id);
			stats.getRecoilPerShotMult().unmodify(id);
			stats.getRecoilDecayMult().unmodify(id);
		}
		
		public String getEffectDescription(float level) {
			return "-" + (int)(RECOIL_BONUS) + "% weapon recoil";
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
			stats.getMaxRecoilMult().modifyMult(id, 1f - (0.01f * RECOIL_BONUS));
			stats.getRecoilPerShotMult().modifyMult(id, 1f - (0.01f * RECOIL_BONUS));
			// slower recoil recovery, also, to match the reduced recoil-per-shot
			// overall effect is same as without skill but halved in every respect
			stats.getRecoilDecayMult().modifyMult(id, 1f - (0.01f * RECOIL_BONUS));
		}
		
		public void unapply(MutableShipStatsAPI stats, HullSize hullSize, String id) {
			stats.getMaxRecoilMult().unmodify(id);
			stats.getRecoilPerShotMult().unmodify(id);
			stats.getRecoilDecayMult().unmodify(id);
		}
		
		public String getEffectDescription(float level) {
			return "-" + (int)(RECOIL_BONUS * GEN_SKILL_MULT) + "% weapon recoil";
		}
		
		public String getEffectPerLevelDescription() {
			return null;
		}

		public ScopeDescription getScopeDescription() {
			return ScopeDescription.PILOTED_SHIP;
		}
	}

	public static class Level2 implements ShipSkillEffect {
		public void apply(MutableShipStatsAPI stats, HullSize hullSize, String id, float level) {
			stats.getAutofireAimAccuracy().modifyFlat(id, TARGET_LEADING_BONUS * 0.01f);
			//stats.getCargoMod().modifyFlat(id, 100 * level);
		}
		
		public void unapply(MutableShipStatsAPI stats, HullSize hullSize, String id) {
			stats.getAutofireAimAccuracy().unmodify(id);
			//stats.getCargoMod().unmodify();
		}
		
		public String getEffectDescription(float level) {
			return "+" + (int)(TARGET_LEADING_BONUS) + "% target leading accuracy for autofiring weapons";
		}
		
		public String getEffectPerLevelDescription() {
			return null;
		}

		public ScopeDescription getScopeDescription() {
			return ScopeDescription.PILOTED_SHIP;
		}
	}
	
	public static class Level2B implements ShipSkillEffect {
		public void apply(MutableShipStatsAPI stats, HullSize hullSize, String id, float level) {
			stats.getAutofireAimAccuracy().modifyFlat(id, TARGET_LEADING_BONUS * 0.01f * 0.5f);
			//stats.getCargoMod().modifyFlat(id, 100 * level);
		}
		
		public void unapply(MutableShipStatsAPI stats, HullSize hullSize, String id) {
			stats.getAutofireAimAccuracy().unmodify(id);
			//stats.getCargoMod().unmodify();
		}
		
		public String getEffectDescription(float level) {
			return "+" + (int)(TARGET_LEADING_BONUS * GEN_SKILL_MULT) + "% target leading accuracy for autofiring weapons";
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
			return "+" + (int)(RANGE_BONUS) + "% ballistic and energy weapon range";
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
			stats.getBallisticWeaponRangeBonus().modifyPercent(id, RANGE_BONUS/2f);
			stats.getEnergyWeaponRangeBonus().modifyPercent(id, RANGE_BONUS/2f);
		}
		
		public void unapply(MutableShipStatsAPI stats, HullSize hullSize, String id) {
			stats.getBallisticWeaponRangeBonus().unmodify(id);
			stats.getEnergyWeaponRangeBonus().unmodify(id);
		}
		
		public String getEffectDescription(float level) {
			return "+" + (int)(RANGE_BONUS * GEN_SKILL_MULT) + "% ballistic and energy weapon range";
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
			stats.getWeaponHealthBonus().modifyPercent(id, WEAPON_HP_BONUS);
		}
		
		public void unapply(MutableShipStatsAPI stats, HullSize hullSize, String id) {
			stats.getWeaponHealthBonus().unmodify(id);
		}
		
		public String getEffectDescription(float level) {
			return "Increases the damage weapons can take before being disabled by " + (int)(WEAPON_HP_BONUS) + "%";
		}
		
		public String getEffectPerLevelDescription() {
			return null;
		}

		public ScopeDescription getScopeDescription() {
			return ScopeDescription.PILOTED_SHIP;
		}
	}	
}

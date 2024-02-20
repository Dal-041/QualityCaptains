package dal.impl.campaign.skills;

import com.fs.starfarer.api.campaign.FleetDataAPI;
import com.fs.starfarer.api.characters.FleetTotalItem;
import com.fs.starfarer.api.characters.FleetTotalSource;
import com.fs.starfarer.api.characters.MutableCharacterStatsAPI;
import com.fs.starfarer.api.characters.ShipSkillEffect;
import com.fs.starfarer.api.characters.SkillSpecAPI;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;
import com.fs.starfarer.api.impl.campaign.skills.BaseSkillEffectDescription;
import com.fs.starfarer.api.ui.TooltipMakerAPI;

public class CaptainsWeaponDrills {
	
	public static float DAMAGE_PERCENT = 10;
	public static float WEAPON_ROF_BONUS = 6;
	public static float TURRET_LEADING_PERC = 25;
	public static float WEAPON_RECOIL_NEG_PERC = 20;
	public static float WEAPON_REPAIR_BONUS = 30;
	
	//Vanilla: +10% damage all ships in fleet
	
	//Mod: +ROF/-FluxPS?, +Leading, -Recoil
	//Elite: +Equipment repairs for fleet?
		
	public static class Level1 implements ShipSkillEffect {
		public void apply(MutableShipStatsAPI stats, HullSize hullSize, String id, float level) {
			//if (!isCivilian(stats)) {
				stats.getMaxRecoilMult().modifyMult(id, 1f - (0.01f * WEAPON_RECOIL_NEG_PERC));
				stats.getRecoilPerShotMult().modifyMult(id, 1f - (0.01f * WEAPON_RECOIL_NEG_PERC));
				// slower recoil recovery, also, to match the reduced recoil-per-shot
				// overall effect is same as without skill but halved in every respect
				stats.getRecoilDecayMult().modifyMult(id, 1f - (0.01f * WEAPON_RECOIL_NEG_PERC));
			//}
		}
		
		public void unapply(MutableShipStatsAPI stats, HullSize hullSize, String id) {
			stats.getMaxRecoilMult().unmodify(id);
			stats.getRecoilPerShotMult().unmodify(id);
			stats.getRecoilDecayMult().unmodify(id);
		}
		
		public String getEffectDescription(float level) {
			return "-" + (int)(WEAPON_RECOIL_NEG_PERC) + "% weapon recoil";
		}
		
		public String getEffectPerLevelDescription() {
			return "";
		}

		public ScopeDescription getScopeDescription() {
			return ScopeDescription.ALL_SHIPS;
		}
	}

	public static class Level2 implements ShipSkillEffect {
		public void apply(MutableShipStatsAPI stats, HullSize hullSize, String id, float level) {
			//if (!isCivilian(stats)) {
				stats.getAutofireAimAccuracy().modifyFlat(id, TURRET_LEADING_PERC * 0.01f);
			//}
		}
		
		public void unapply(MutableShipStatsAPI stats, HullSize hullSize, String id) {
			stats.getAutofireAimAccuracy().unmodify(id);
		}
		
		public String getEffectDescription(float level) {
			return "+" + (int)(TURRET_LEADING_PERC) + "% target leading accuracy for autofiring weapons";
		}
		
		public String getEffectPerLevelDescription() {
			return "";
		}

		public ScopeDescription getScopeDescription() {
			return ScopeDescription.ALL_SHIPS;
		}
	}
	
	public static class Level3 implements ShipSkillEffect {

		public void apply(MutableShipStatsAPI stats, HullSize hullSize, String id, float level) {
			float timeMult = 1f / ((100f + WEAPON_REPAIR_BONUS) / 100f);
			stats.getCombatWeaponRepairTimeMult().modifyMult(id, timeMult);
		}
		
		public void unapply(MutableShipStatsAPI stats, HullSize hullSize, String id) {
			stats.getCombatWeaponRepairTimeMult().unmodify(id);
		}
		
		public String getEffectDescription(float level) {
			return "" + (int)(WEAPON_REPAIR_BONUS) + "% faster in-combat weapon repairs";
		}
		
		public String getEffectPerLevelDescription() {
			return null;
		}

		public ScopeDescription getScopeDescription() {
			return ScopeDescription.ALL_SHIPS;
		}
	}
	
	public static class Level4 implements ShipSkillEffect {
		
		public void apply(MutableShipStatsAPI stats, HullSize hullSize, String id, float level) {
				stats.getBallisticRoFMult().modifyPercent(id, WEAPON_ROF_BONUS);
				stats.getEnergyRoFMult().modifyPercent(id, WEAPON_ROF_BONUS);
				//stats.getMissileRoFMult().modifyPercent(id, WEAPON_ROF_BONUS);
				//stats.getBallisticWeaponFluxCostMod().modifyPercent(id, -WEAPON_ROF_BONUS/2);
				//stats.getBallisticWeaponFluxCostMod().modifyPercent(id, -WEAPON_ROF_BONUS/2);
		}
			
		public void unapply(MutableShipStatsAPI stats, HullSize hullSize, String id) {
			stats.getBallisticRoFMult().unmodifyPercent(id);
			stats.getEnergyRoFMult().unmodifyPercent(id);
			//stats.getBallisticWeaponFluxCostMod().unmodifyPercent(id);
			//stats.getEnergyWeaponFluxCostMod().unmodifyPercent(id);
			//stats.getMissileRoFMult().unmodifyPercent(id);
		}
		
		public String getEffectDescription(float level) {
			return "+" + (int)WEAPON_ROF_BONUS + "% ROF for ballistic and energy weapons";
			//return "+" + (int)WEAPON_ROF_BONUS + "% ROF and -" + (float)WEAPON_ROF_BONUS/2 + " flux use for ballistic and energy weapons";
		}
		
		public ScopeDescription getScopeDescription() {
			return ScopeDescription.ALL_SHIPS;
		}

		public String getEffectPerLevelDescription() {
			return null;
		}
	}
	
	//Stock
	public static class Level1f extends BaseSkillEffectDescription implements ShipSkillEffect, FleetTotalSource {
		
		public FleetTotalItem getFleetTotalItem() {
			return getCombatOPTotal();
		}
		
		public void apply(MutableShipStatsAPI stats, HullSize hullSize, String id, float level) {
			if (!isCivilian(stats)) {
				float damBonus = computeAndCacheThresholdBonus(stats, "wd_dam", DAMAGE_PERCENT, ThresholdBonusType.OP_LOW);
				stats.getBallisticWeaponDamageMult().modifyPercent(id, damBonus);
				stats.getEnergyWeaponDamageMult().modifyPercent(id, damBonus);
				stats.getMissileWeaponDamageMult().modifyPercent(id, damBonus);
			}
		}
			
		public void unapply(MutableShipStatsAPI stats, HullSize hullSize, String id) {
			stats.getBallisticWeaponDamageMult().unmodifyPercent(id);
			stats.getEnergyWeaponDamageMult().unmodifyPercent(id);
			stats.getMissileWeaponDamageMult().unmodifyPercent(id);
		}
		
		public String getEffectDescription(float level) {
			return null;
		}
			
		public void createCustomDescription(MutableCharacterStatsAPI stats, SkillSpecAPI skill, 
											TooltipMakerAPI info, float width) {
			init(stats, skill);
			
			FleetDataAPI data = getFleetData(null);
			float damBonus = computeAndCacheThresholdBonus(data, stats, "wd_dam", DAMAGE_PERCENT, ThresholdBonusType.OP_LOW);
			
			info.addPara("+%s weapon damage for combat ships (maximum: %s)", 0f, hc, hc,
					"" + (int) damBonus + "%",
					"" + (int) DAMAGE_PERCENT + "%");
			addOPThresholdInfo(info, data, stats, OP_LOW_THRESHOLD);
			
			//info.addSpacer(5f);
		}
		
		public ScopeDescription getScopeDescription() {
			return ScopeDescription.ALL_SHIPS;
		}
	}

}






package dal.impl.campaign.skills;

import com.fs.starfarer.api.campaign.FleetDataAPI;
import com.fs.starfarer.api.characters.FleetTotalItem;
import com.fs.starfarer.api.characters.FleetTotalSource;
import com.fs.starfarer.api.characters.MutableCharacterStatsAPI;
import com.fs.starfarer.api.characters.ShipSkillEffect;
import com.fs.starfarer.api.characters.SkillSpecAPI;
import com.fs.starfarer.api.characters.LevelBasedEffect.ScopeDescription;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;
import com.fs.starfarer.api.fleet.FleetMemberAPI;
import com.fs.starfarer.api.impl.campaign.ids.Stats;
import com.fs.starfarer.api.impl.campaign.skills.BaseSkillEffectDescription;
import com.fs.starfarer.api.ui.TooltipMakerAPI;

public class CaptainsFighterUplink {
	
	public static boolean loadStock = false;
	//public static float DAMAGE_PERCENT = 10;
	public static float MAX_SPEED_PERCENT = 10;
	public static float CREW_LOSS_PERCENT = 50;
	public static float DAMAGE_TO_FIGHTERS_BONUS = 15;
	public static float DAMAGE_TO_MISSILES_BONUS = 15;
	public static float TARGET_LEADING_BONUS = 50;
	
	public static float OFFICER_MULT = 1.5f;
	
	public static boolean isSourceOfficer(MutableShipStatsAPI stats) {
		if (stats.getEntity() instanceof ShipAPI) {
			ShipAPI ship = (ShipAPI) stats.getEntity();
			if (ship == null ) return false;
			if (ship.getWing() != null && ship.getWing().getSourceShip() != null) return !ship.getWing().getSourceShip().getCaptain().isDefault();
			return false;
		} else {
			//FleetMemberAPI member = stats.getFleetMember();
			//if (member == null) return false;
			//return !member.getCaptain().isDefault();
			return false;
		}
	}
	
	public static class Level1 extends BaseSkillEffectDescription implements ShipSkillEffect {
		
		public void apply(MutableShipStatsAPI stats, HullSize hullSize, String id, float level) {
			float effect = CREW_LOSS_PERCENT;
			if (isSourceOfficer(stats)) effect *= OFFICER_MULT; 
			stats.getDynamic().getStat(Stats.FIGHTER_CREW_LOSS_MULT).modifyMult(id, 1f - effect / 100f);
		}
		
		public void unapply(MutableShipStatsAPI stats, HullSize hullSize, String id) {
			stats.getDynamic().getStat(Stats.FIGHTER_CREW_LOSS_MULT).unmodifyMult(id);
		}
		
		public String getEffectDescription(float level) {
			return null;
		}
		
		public void createCustomDescription(MutableCharacterStatsAPI stats, SkillSpecAPI skill, 
											TooltipMakerAPI info, float width) {
			init(stats, skill);


			FleetDataAPI data = getFleetData(null);
			info.addPara("-%s crew lost due to fighter losses in combat", 0f, hc, hc,
					"" + (int) CREW_LOSS_PERCENT + "%");
			//info.addSpacer(5f);
		}
		
		public ScopeDescription getScopeDescription() {
			return ScopeDescription.ALL_SHIPS;
		}

	}
	
	
	public static class Level2 extends BaseSkillEffectDescription implements ShipSkillEffect, FleetTotalSource {
		
		public FleetTotalItem getFleetTotalItem() {
			return getFighterBaysTotal();
		}
		
		public void apply(MutableShipStatsAPI stats, HullSize hullSize, String id, float level) {
			float effect = MAX_SPEED_PERCENT;
			if (isSourceOfficer(stats)) effect *= OFFICER_MULT; 
			
			stats.getMaxSpeed().modifyPercent(id, effect);
			stats.getAcceleration().modifyPercent(id, effect * 2f);
			stats.getDeceleration().modifyPercent(id, effect * 2f);
		}
		
		public void unapply(MutableShipStatsAPI stats, HullSize hullSize, String id) {
			
			stats.getMaxSpeed().unmodifyPercent(id);
			stats.getAcceleration().unmodifyPercent(id);
			stats.getDeceleration().unmodifyPercent(id);
		}
		
		public String getEffectDescription(float level) {
			return null;
		}
		
		public void createCustomDescription(MutableCharacterStatsAPI stats, SkillSpecAPI skill, 
											TooltipMakerAPI info, float width) {
			init(stats, skill);
			
			info.addPara("+%s top speed", 0f, hc, hc,
					"" + (int) MAX_SPEED_PERCENT + "%");
			
			//info.addSpacer(5f);
		}
		
		public ScopeDescription getScopeDescription() {
			return ScopeDescription.ALL_FIGHTERS;
		}

	}
	
	public static class Level2A implements ShipSkillEffect {

		public void apply(MutableShipStatsAPI stats, HullSize hullSize, String id, float level) {
			float effect1 = DAMAGE_TO_FIGHTERS_BONUS;
			float effect2 = DAMAGE_TO_MISSILES_BONUS;
			if (isSourceOfficer(stats)) effect1 *= OFFICER_MULT; 
			if (isSourceOfficer(stats)) effect2 *= OFFICER_MULT;
			stats.getDamageToFighters().modifyFlat(id, effect1 / 100f);
			stats.getDamageToMissiles().modifyFlat(id, effect2 / 100f);
		}
		
		public void unapply(MutableShipStatsAPI stats, HullSize hullSize, String id) {
			stats.getDamageToFighters().unmodify(id);
			stats.getDamageToMissiles().unmodify(id);
		}	
		
		public String getEffectDescription(float level) {
			return "+" + (int)(DAMAGE_TO_FIGHTERS_BONUS) + "% damage to fighters and missiles";
		}
		
		public String getEffectPerLevelDescription() {
			return null;
		}
		
		public ScopeDescription getScopeDescription() {
			return ScopeDescription.ALL_FIGHTERS;
		}
	}
	
	
	public static class Level3 implements ShipSkillEffect {

		public void apply(MutableShipStatsAPI stats, HullSize hullSize, String id, float level) {
			float effect = TARGET_LEADING_BONUS;
			if (isSourceOfficer(stats)) effect *= OFFICER_MULT; 
			stats.getAutofireAimAccuracy().modifyFlat(id, effect * 0.01f);
		}
		
		public void unapply(MutableShipStatsAPI stats, HullSize hullSize, String id) {
			stats.getAutofireAimAccuracy().unmodify(id);
		}	
		
		public String getEffectDescription(float level) {
			return "+" + (int)(TARGET_LEADING_BONUS) + "% target leading accuracy";
		}
		
		public String getEffectPerLevelDescription() {
			return null;
		}
		
		public ScopeDescription getScopeDescription() {
			return ScopeDescription.ALL_FIGHTERS;
		}
	}
}






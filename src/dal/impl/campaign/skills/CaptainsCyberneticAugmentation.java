package dal.impl.campaign.skills;

import java.awt.Color;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.characters.CharacterStatsSkillEffect;
import com.fs.starfarer.api.characters.DescriptionSkillEffect;
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
import com.fs.starfarer.api.util.Misc;

public class CaptainsCyberneticAugmentation {
	
	public static float MAX_ELITE_SKILLS_BONUS = 1;
	public static float ECCM_BONUS = 5;
	
	public static boolean USE_ENDURANCE_BONUS = true;
	public static boolean USE_GUNNERY_BONUS = true;
	public static boolean USE_SEX_BONUS = false;
	
	public static boolean isOfficer(MutableShipStatsAPI stats) {
		if (stats.getEntity() instanceof ShipAPI) {
			ShipAPI ship = (ShipAPI) stats.getEntity();
			return !ship.getCaptain().isDefault();
		} else {
			FleetMemberAPI member = stats.getFleetMember();
			if (member == null) return false;
			return !member.getCaptain().isDefault();
		}
	}
	
	public static boolean isNoOfficer(MutableShipStatsAPI stats) {
		if (stats.getEntity() instanceof ShipAPI) {
			ShipAPI ship = (ShipAPI) stats.getEntity();
//			if (ship == Global.getCombatEngine().getShipPlayerIsTransferringCommandFrom()) {
//				return false; // player is transferring command, no bonus until the shuttle is done flying
//				// issue: won't get called again when transfer finishes
//			}
			return ship.getCaptain().isDefault();
		} else {
			FleetMemberAPI member = stats.getFleetMember();
			if (member == null) return true;
			return member.getCaptain().isDefault();
		}
	}
	
	public static boolean isOriginalNoOfficer(MutableShipStatsAPI stats) {
		if (stats.getEntity() instanceof ShipAPI) {
			ShipAPI ship = (ShipAPI) stats.getEntity();
//			if (ship == Global.getCombatEngine().getShipPlayerIsTransferringCommandFrom()) {
//				return false; // player is transferring command, no bonus until the shuttle is done flying
//			}
			return ship.getOriginalCaptain() != null && ship.getOriginalCaptain().isDefault();
		} else {
			FleetMemberAPI member = stats.getFleetMember();
			if (member == null) return true;
			return member.getCaptain().isDefault();
		}
	}
	
	public static class Level0 implements DescriptionSkillEffect {
		public String getString() {
			int base = (int)Global.getSettings().getInt("officerMaxEliteSkills");
			return "\n*The base maximum number of elite skills per officer is " + base + ".";
		}
		public Color[] getHighlightColors() {
			Color h = Misc.getDarkHighlightColor();
			return new Color[] {h};
		}
		public String[] getHighlights() {
			int base = (int)Global.getSettings().getInt("officerMaxEliteSkills");
			return new String [] {"" + base};
		}
		public Color getTextColor() {
			return null;
		}
	}
	
	public static class Level1 implements CharacterStatsSkillEffect {
		
		public void apply(MutableCharacterStatsAPI stats, String id, float level) {
			stats.getDynamic().getMod(Stats.OFFICER_MAX_ELITE_SKILLS_MOD).modifyFlat(id, MAX_ELITE_SKILLS_BONUS);
		}
		
		public void unapply(MutableCharacterStatsAPI stats, String id) {
			stats.getDynamic().getMod(Stats.OFFICER_MAX_ELITE_SKILLS_MOD).unmodify(id);
		}
		
		public String getEffectDescription(float level) {
			return "+" + Math.round(MAX_ELITE_SKILLS_BONUS) + " to maximum number of elite skills* for officers under your command";
		}
		
		public String getEffectPerLevelDescription() {
			return null;
		}
		
		public ScopeDescription getScopeDescription() {
			return ScopeDescription.NONE;
		}
	}

	public static class Level2 implements ShipSkillEffect {
		public void apply(MutableShipStatsAPI stats, HullSize hullSize, String id, float level) {
			if (USE_SEX_BONUS && isNoOfficer(stats)) {
				//new CaptainsSystemsExpertise.Level1().apply(stats, hullSize, id, level);
				new CaptainsSystemsExpertise.Level3B().apply(stats, hullSize, id, level);
				new CaptainsSystemsExpertise.Level4B().apply(stats, hullSize, id, level);
			} 
		}
		
		public void unapply(MutableShipStatsAPI stats, HullSize hullSize, String id) {
			//new CaptainsSystemsExpertise.Level1().unapply(stats, hullSize, id);
			new CaptainsSystemsExpertise.Level3B().unapply(stats, hullSize, id);
			new CaptainsSystemsExpertise.Level4B().unapply(stats, hullSize, id);
		}
		
		public String getEffectDescription(float level) {
			if (USE_SEX_BONUS) {
				return "Ships without officers gain basic Systems Expertise at " + (int)(CaptainsSystemsExpertise.NO_OFFICER_FACTOR * 100f) + "% strength";
			}
			return "";
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
			if (USE_GUNNERY_BONUS && isNoOfficer(stats)) {
				new CaptainsGunneryImplants.Level1B().apply(stats, hullSize, id, level);
				new CaptainsGunneryImplants.Level2B().apply(stats, hullSize, id, level);
				new CaptainsGunneryImplants.Level3B().apply(stats, hullSize, id, level);
			} 
		}
		
		public void unapply(MutableShipStatsAPI stats, HullSize hullSize, String id) {
			new CaptainsGunneryImplants.Level1B().unapply(stats, hullSize, id);
			new CaptainsGunneryImplants.Level2B().unapply(stats, hullSize, id);
			new CaptainsGunneryImplants.Level3B().unapply(stats, hullSize, id);
		}
		
		public String getEffectDescription(float level) {
			if (!USE_GUNNERY_BONUS) {
				return "";
			}
			return "Ships without officers gain basic Gunnery Implant abilities at " + (int)(CaptainsGunneryImplants.GEN_SKILL_MULT * 100f) + "% strength";
		}
		
		public String getEffectPerLevelDescription() {
			return null;
		}
		
		public ScopeDescription getScopeDescription() {
			return ScopeDescription.PILOTED_SHIP;
		}
	}
}

package dal.impl.campaign.skills;

import java.awt.Color;

import com.fs.starfarer.api.characters.CharacterStatsSkillEffect;
import com.fs.starfarer.api.characters.DescriptionSkillEffect;
import com.fs.starfarer.api.characters.MutableCharacterStatsAPI;
import com.fs.starfarer.api.characters.ShipSkillEffect;
import com.fs.starfarer.api.characters.SkillSpecAPI;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;
import com.fs.starfarer.api.fleet.FleetMemberAPI;
import com.fs.starfarer.api.impl.campaign.ids.Stats;
import com.fs.starfarer.api.impl.campaign.skills.BaseSkillEffectDescription;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;

public class CaptainsCoordinatedManeuvers {
	
	//public static float CP_BONUS = 2f;
	
	public static boolean NAV_BY_LEVELS = true;
	public static boolean NAV_BY_SIZE = false;
	
	public static int PER_FRIG = 1;
	public static int PER_DEST = 1;
	public static int PER_CRSR = 1;
	public static int PER_CAP = 1;
	
	public static float CP_REGEN_FRIGATES = 50f;
	public static float CP_REGEN_DESTROYERS = 25f;
	
	public static class Level0 implements DescriptionSkillEffect {
		
		public String getString() {
			String boonTypes = "";
			String targTypes = "";
			int targN = 0;
			
			if (CaptainsCoordinatedManeuversScript.AFFECTS_SPEED && CaptainsCoordinatedManeuversScript.AFFECTS_MANEUVER) {
				boonTypes = "the speed and maneuvering";
			} else if (CaptainsCoordinatedManeuversScript.AFFECTS_SPEED) {
				boonTypes = "the speed";
			} else if (CaptainsCoordinatedManeuversScript.AFFECTS_MANEUVER) {
				boonTypes = "the maneuvering";
			}
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
			
			String maxEff = (int)CaptainsCoordinatedManeuversScript.BASE_MAXIMUM + "%";
//			String buoy = "+" + (int)CoordinatedManeuversScript.PER_BUOY + "%";
//			return "Does not apply to fighters. Bonus from each ship only applies to other ships.\n" +
//				   "Nav buoys grant " + buoy + " each, up to a maximum of " + max + " without skill.";
//			return "Nav buoys grant " + buoy + " top speed each, up to a maximum of " + max + " without skills. " +
//					"Does not apply to fighters. Bonus from each ship does not apply to itself.";
			if (boonTypes != "" && targTypes != "") {
				return "*The total nav rating for the deployed ships of the fleet increases " + boonTypes + " of all " + targTypes + " in the fleet, up to a maximum of " +
					   	   "" + maxEff + ".";			
			} else {
				return "* Nav rating confers no bonuses or penalties.";	
			}		
		}
		public Color[] getHighlightColors() {
			Color h = Misc.getHighlightColor();
			h = Misc.getDarkHighlightColor();
			return new Color[] {h, h};
		}
		public String[] getHighlights() {
			String max = (int)CaptainsCoordinatedManeuversScript.BASE_MAXIMUM + "%";
			String jammer = "+" + (int)CaptainsCoordinatedManeuversScript.PER_BUOY + "%";
			return new String [] {jammer, max};
		}
		public Color getTextColor() {
			return null;
		}
	}
	
	public static boolean isFrigateOrDestroyerAndOfficer(MutableShipStatsAPI stats) {
		FleetMemberAPI member = stats.getFleetMember();
		if (member == null) return false;
		if (!member.isFrigate() && !member.isDestroyer()) return false;
		
		return !member.getCaptain().isDefault();
	}
	
	public static boolean isOfficer(MutableShipStatsAPI stats) {
		FleetMemberAPI member = stats.getFleetMember();
		if (member == null) return false;
		return !member.getCaptain().isDefault();
	}
	
	public static int officerLevel(MutableShipStatsAPI stats) {
		FleetMemberAPI member = stats.getFleetMember();
		if (member == null || member.getCaptain().isDefault()) return 0;
		if (member.isFlagship()) return 5;
		return member.getCaptain().getStats().getLevel();
	}
	
	public static float getClassPts(HullSize hullSize) {
		float value = 0f;
		switch (hullSize) {
			case CAPITAL_SHIP: value = PER_CAP; break;
			case CRUISER: value = PER_CRSR; break;
			case DESTROYER: value = PER_DEST; break;
			case FRIGATE: value = PER_FRIG; break;
		default:
			break;
		}
		return value;
	}
	
	public static class Level1A implements ShipSkillEffect {
		public void apply(MutableShipStatsAPI stats, HullSize hullSize, String id, float level) {
			float bonus = 0f;
			if (NAV_BY_LEVELS) {
				bonus += officerLevel(stats);
			}
			if (NAV_BY_SIZE) {
				bonus += getClassPts(hullSize);
			}
			if (bonus > 0f) {
				stats.getDynamic().getMod(Stats.COORDINATED_MANEUVERS_FLAT).modifyFlat(id, bonus);
			}
		}
		public void unapply(MutableShipStatsAPI stats, HullSize hullSize, String id) {
			stats.getDynamic().getMod(Stats.COORDINATED_MANEUVERS_FLAT).unmodify(id);
		}
		public String getEffectDescription(float level) {
			if (NAV_BY_LEVELS && !NAV_BY_SIZE) {
				return "The Nav rating of your fleet* increases by the sum your deployed officer's levels, your flagship counting as 5";
			} else if (NAV_BY_LEVELS && NAV_BY_SIZE){
				return "The Nav rating of your fleet* increases by the sum your deployed officer's levels, your flagship counting as 5,\n and "+ 
						PER_FRIG + "/" + PER_DEST + "/" + PER_CRSR + "/" + PER_CAP 
						+" for each deployed ship by class.";
			} else if (NAV_BY_SIZE) {
				return "The Nav rating of your fleet* increases by "+ PER_FRIG + "/" + PER_DEST + "/" + PER_CRSR + "/" + PER_CAP 
						+" for each deployed ship by class.";
			}
			return "The Nav rating of your fleet is determined only by your Nav Relay hullmods.";
		}
		
		public String getEffectPerLevelDescription() {
			return null;
		}
		public ScopeDescription getScopeDescription() {
			return ScopeDescription.PILOTED_SHIP;
		}
	}
	
	public static class Level1C extends BaseSkillEffectDescription implements ShipSkillEffect {
		public void apply(MutableShipStatsAPI stats, HullSize hullSize, String id, float level) {
			if (isFrigateOrDestroyerAndOfficer(stats)) {
				float bonus = 0f;
				if (hullSize == HullSize.FRIGATE) bonus = CP_REGEN_FRIGATES;
				if (hullSize == HullSize.DESTROYER) bonus = CP_REGEN_DESTROYERS;
				if (bonus > 0f) {
					stats.getDynamic().getMod(Stats.COMMAND_POINT_RATE_FLAT).modifyFlat(id, bonus * 0.01f);
				}
			}
		}
		public void unapply(MutableShipStatsAPI stats, HullSize hullSize, String id) {
			stats.getDynamic().getMod(Stats.COMMAND_POINT_RATE_FLAT).unmodify(id);
		}
		public String getEffectDescription(float level) {
			return null;
		}
		
		public void createCustomDescription(MutableCharacterStatsAPI stats, SkillSpecAPI skill, 
				TooltipMakerAPI info, float width) {
			init(stats, skill);

			info.addPara("+%s to command point recovery rate from deployed frigates, " +
					     "+%s from destroyers", 0f, hc, hc,
					     "" + (int) CP_REGEN_FRIGATES + "%",
					     "" + (int) CP_REGEN_DESTROYERS + "%");
		}
		
		public String getEffectPerLevelDescription() {
			return null;
		}
		public ScopeDescription getScopeDescription() {
			return ScopeDescription.PILOTED_SHIP;
		}
	}
}

package dal.impl.campaign.skills;

import java.awt.Color;
import java.util.ArrayList;
import com.fs.starfarer.api.characters.DescriptionSkillEffect;
import com.fs.starfarer.api.characters.MutableCharacterStatsAPI;
import com.fs.starfarer.api.characters.ShipSkillEffect;
import com.fs.starfarer.api.characters.SkillSpecAPI;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;
import com.fs.starfarer.api.fleet.FleetMemberAPI;
import com.fs.starfarer.api.impl.campaign.ids.Skills;
import com.fs.starfarer.api.impl.campaign.ids.Stats;
import com.fs.starfarer.api.impl.campaign.skills.BaseSkillEffectDescription;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;

public class CaptainsElectronicWarfare {
	
//	public static final float LEVEL_1_BONUS = 0f;
//	public static final float LEVEL_2_BONUS = 5f;
//	public static final float LEVEL_3_BONUS = 5f;
	
	//public static float PER_SHIP_BONUS = 2f;
	public static boolean ECM_FOR_SKILLS = false;
	public static boolean ECM_FOR_LEVELS = true;
	public static boolean ECM_FOR_HULLSIZE = false;
	public static int PER_FRIG = 1;
	public static int PER_DEST = 1;
	public static int PER_CRSR = 1;
	public static int PER_CAP = 1;

	public static float CAP_RANGE = 500f;
	public static float CAP_RATE = 2f;

	
	public static float getBase(HullSize hullSize) {
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

	public static boolean isOfficer(MutableShipStatsAPI stats) {
		FleetMemberAPI member = stats.getFleetMember();
		if (member == null) return false;
		return !member.getCaptain().isDefault();
	}
	
	public static int officerLevel(MutableShipStatsAPI stats) {
		FleetMemberAPI member = stats.getFleetMember();
		if (member == null) return 0;
		if (member.getCaptain().isDefault()) return 0;
		if (member.isFlagship()) return 5;
		return member.getCaptain().getStats().getLevel();
	}
	
	public static boolean hasEWARSkills(MutableShipStatsAPI stats) {
		if (stats.getFleetMember() == null || stats.getFleetMember().getCaptain() == null) return false;
		if (stats.getFleetMember().getCaptain().isDefault()) return false;
		if (stats.getFleetMember().getCaptain().getStats().getSkillLevel(Skills.GUNNERY_IMPLANTS) > 0 
				|| stats.getFleetMember().getCaptain().getStats().getSkillLevel(Skills.ENERGY_WEAPON_MASTERY) > 0 
				|| stats.getFleetMember().getCaptain().getStats().getSkillLevel(Skills.ELECTRONIC_WARFARE) > 0) {
			return true;
		}
		return false;
	}
	
	public static int countEWARSkills(MutableShipStatsAPI stats) {
		int count = 0;
		if (stats.getFleetMember() == null || stats.getFleetMember().getCaptain() == null) return 0;
		if (stats.getFleetMember().getCaptain().isDefault()) return 0;
		if (stats.getFleetMember().getCaptain().getStats().getSkillLevel(Skills.GUNNERY_IMPLANTS) > 0) count++;
		if (stats.getFleetMember().getCaptain().getStats().getSkillLevel(Skills.ENERGY_WEAPON_MASTERY) > 0) count++;
		if (stats.getFleetMember().getCaptain().getStats().getSkillLevel(Skills.ELECTRONIC_WARFARE) > 0) count++;
		return count;
	}
	
	public static class Level0 implements DescriptionSkillEffect {
		public String getString() {
			String max = (int)CaptainsElectronicWarfareScript.BASE_MAXIMUM + "%";
//			return "*Enemy weapon range is reduced by half of the total ECM rating of the deployed ships, " +
//				"up to a maximum of " + max +
//				". Does not apply to fighters, affects all weapons including missiles.";
//			return "*Reduces enemy weapon range. The total reduction is the lesser of " + max +
//					" and the combined ECM rating for both sides. " +
//					"The side with the lower ECM rating gets a higher penalty. " +
//					"Does not apply to fighters, affects all weapons including missiles.";

//			return "*Enemy weapon range is reduced by the total ECM rating of your deployed ships, "
//					+ "up to a maximum of " + max + ". This penalty is reduced by the ratio "
//					+ "of the enemy ECM rating to yours." +
//					"Does not apply to fighters, affects all weapons including missiles.";

			boolean penGuns = false;
			String subjects = "";
			String penaltyStr = "";
			ArrayList<String> penTypes = new ArrayList<String>();
			if (CaptainsElectronicWarfareScript.APPLY_TO_TURRETS) {
				if (CaptainsElectronicWarfareScript.APPLIES_TO_RECOIL) { penTypes.add(" recoil"); penGuns = true; }
				if (CaptainsElectronicWarfareScript.APPLIES_TO_RANGE) { penTypes.add(" range"); penGuns = true; }
				if (CaptainsElectronicWarfareScript.APPLIES_TO_AUTOAIM) { penTypes.add(" autoaim"); penGuns = true; }
				if (CaptainsElectronicWarfareScript.APPLIES_TO_TURRET_TURNRATE) { penTypes.add(" turret turnrate"); penGuns = true; }
			}
			if (CaptainsElectronicWarfareScript.APPLY_TO_MISSILES && CaptainsElectronicWarfareScript.APPLIES_TO_MISSILE_TURNRATE) { penTypes.add(" missile maneuvers"); }

			if (penTypes.size() >= 3) {
				for (int i = 0; i < penTypes.size(); i++) {
					penaltyStr += penTypes.get(i);
					if (i < penTypes.size()-1) penaltyStr += ",";
					if (i == penTypes.size()-2) penaltyStr += " and";
				}
			} else {
				for (int i = 0; i < penTypes.size(); i++) {
					penaltyStr += penTypes.get(i);
					if (i==0 && penTypes.size() > 1) penaltyStr += " and";
				}
			}
			
			if (penaltyStr == "") penaltyStr = " nothing";

			if (CaptainsElectronicWarfareScript.APPLY_TO_SHIPS) { subjects += "to ships";
				if (CaptainsElectronicWarfareScript.APPLY_TO_FIGHTERS) { subjects += " and fighters"; }
			} else if (CaptainsElectronicWarfareScript.APPLY_TO_FIGHTERS) {
				subjects = "only to fighters";
			} else {
				subjects = "to nothing";
			}
			
			String summary = "Applies " + subjects + ", impacting" + penaltyStr + ".";


			return "The total ECM rating for allied deployed ships applies a penalty to " +
				   "the other side's weapon performance, reduced by the ratio of the enemy's ECM rating to yours."
					+ "This effect scales up to a maximum of "
					+ max + ". " + summary;
		}
		public Color[] getHighlightColors() {
			Color h = Misc.getHighlightColor();
			h = Misc.getDarkHighlightColor();
			return new Color[] {h, h, h};
		}
		public String[] getHighlights() {
			String max = (int)CaptainsElectronicWarfareScript.BASE_MAXIMUM + "%";
			String jammer = "+" + (int)CaptainsElectronicWarfareScript.PER_JAMMER + "%";
			return new String [] {jammer, max};
		}
		public Color getTextColor() {
			return null;
		}
	}

	public static class Level0WithNewline extends Level0 {
		public String getString() {
			return "\n" + super.getString();
		}
	}


	public static class Level1A extends BaseSkillEffectDescription implements ShipSkillEffect {
		public void apply(MutableShipStatsAPI stats, HullSize hullSize, String id, float level) {
			float bonus = 0f;
			if (ECM_FOR_HULLSIZE && !BaseSkillEffectDescription.isCivilian(stats)) {
				bonus = getBase(hullSize);
			}
			if (ECM_FOR_LEVELS) {
				bonus += officerLevel(stats);
			}
			if (ECM_FOR_SKILLS) {
				bonus += countEWARSkills(stats);
			}
			if (bonus > 0f) {
				stats.getDynamic().getMod(Stats.ELECTRONIC_WARFARE_FLAT).modifyFlat(id, bonus);
			}
		}
		public void unapply(MutableShipStatsAPI stats, HullSize hullSize, String id) {
			stats.getDynamic().getMod(Stats.ELECTRONIC_WARFARE_FLAT).unmodify(id);
		}
		
		public void createCustomDescription(MutableCharacterStatsAPI stats, SkillSpecAPI skill, 
				TooltipMakerAPI info, float width) {
			
			init(stats, skill);
			
			if (ECM_FOR_HULLSIZE) {
				info.addPara((int)PER_FRIG +"/"+ (int)PER_DEST +"/"+ (int)PER_CRSR +"/"+ (int)PER_CAP + " ECM rating for each deployed ship, depending on ship class", hc, 0f);
			}
			if (ECM_FOR_LEVELS) {
				info.addPara("The ECM rating of your fleet increases by the sum your deployed officer's levels, your flagship counting as 5", hc, 0f);
			}
			if (ECM_FOR_SKILLS) {
				info.addPara("The ECM rating of your fleet increases for deployed officers with Gunnery Implants or Energy Mastery by 1% per skill", hc, 0f);
			}
			if (!ECM_FOR_LEVELS && !ECM_FOR_HULLSIZE && !ECM_FOR_SKILLS) {
				info.addPara("The ECM rating of your fleet is determined by your deployed ships' ECM hullmods", hc, 0f);
			}
			info.addSpacer(3f);
		}

		public String getEffectPerLevelDescription() {
			return null;
		}
		public ScopeDescription getScopeDescription() {
			return ScopeDescription.ALL_COMBAT_SHIPS;
		}
	}


	public static class Level1B implements ShipSkillEffect {
		public void apply(MutableShipStatsAPI stats, HullSize hullSize, String id, float level) {
			if (!BaseSkillEffectDescription.isCivilian(stats)) {
				stats.getDynamic().getMod(Stats.SHIP_OBJECTIVE_CAP_RANGE_MOD).modifyFlat(id, CAP_RANGE);
				stats.getDynamic().getStat(Stats.SHIP_OBJECTIVE_CAP_RATE_MULT).modifyMult(id, CAP_RATE);
			}
		}
		public void unapply(MutableShipStatsAPI stats, HullSize hullSize, String id) {
			stats.getDynamic().getMod(Stats.SHIP_OBJECTIVE_CAP_RANGE_MOD).unmodifyFlat(id);
			stats.getDynamic().getStat(Stats.SHIP_OBJECTIVE_CAP_RATE_MULT).unmodifyMult(id);
		}
		public String getEffectDescription(float level) {
			return "Combat objectives are captured much more quickly and from longer range";
		}
		public String getEffectPerLevelDescription() {
			return null;
		}
		public ScopeDescription getScopeDescription() {
			return ScopeDescription.ALL_SHIPS;
		}
	}
}

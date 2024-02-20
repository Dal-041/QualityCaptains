package dal.impl.campaign.skills;

import java.awt.Color;

import com.fs.starfarer.api.campaign.econ.MarketAPI;
import com.fs.starfarer.api.characters.CharacterStatsSkillEffect;
import com.fs.starfarer.api.characters.MutableCharacterStatsAPI;
import com.fs.starfarer.api.characters.SkillSpecAPI;
import com.fs.starfarer.api.characters.MarketSkillEffect;
import com.fs.starfarer.api.impl.campaign.ids.Stats;
import com.fs.starfarer.api.impl.campaign.skills.BaseSkillEffectDescription;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;

public class CaptainsIndustrialPlanning {
	
	public static int SUPPLY_BONUS = 1;
	public static float ACCESS_BONUS = 25f;
	public static float CUSTOM_PRODUCTION_BONUS = 50f;
	public static int SHIP_QUAL_BONUS = 10;
	
	public static class Level1 implements CharacterStatsSkillEffect {
		public void apply(MutableCharacterStatsAPI stats, String id, float level) {
			stats.getDynamic().getMod(Stats.SUPPLY_BONUS_MOD).modifyFlat(id, SUPPLY_BONUS);
		}

		public void unapply(MutableCharacterStatsAPI stats, String id) {
			stats.getDynamic().getMod(Stats.SUPPLY_BONUS_MOD).unmodifyFlat(id);
		}
		
		public String getEffectDescription(float level) {
			return "All industries supply " + SUPPLY_BONUS + " more unit of all the commodities they produce";
		}
		
		public String getEffectPerLevelDescription() {
			return null;
		}

		public ScopeDescription getScopeDescription() {
			return ScopeDescription.GOVERNED_OUTPOST;
		}
	}
	
	public static class Level1B implements MarketSkillEffect {
		public void apply(MarketAPI market, String id, float level) {
			market.getAccessibilityMod().modifyFlat(id, (ACCESS_BONUS * 0.01f), "Industrial Planning");
		}

		public void unapply(MarketAPI market, String id) {
			market.getAccessibilityMod().unmodifyFlat(id);
		}
		
		public String getEffectDescription(float level) {
			return "+" + (int)(ACCESS_BONUS) + "% accessibility";
		}
		
		public String getEffectPerLevelDescription() {
			return null;
		}

		public ScopeDescription getScopeDescription() {
			return ScopeDescription.GOVERNED_OUTPOST;
		}
	}
	
	public static class Level2 extends BaseSkillEffectDescription implements CharacterStatsSkillEffect {
		public void apply(MutableCharacterStatsAPI stats, String id, float level) {
			//stats.getDynamic().getMod(Stats.CUSTOM_PRODUCTION_MOD).modifyPercent(id, CUSTOM_PRODUCTION_BONUS, "Industrial planning");
			stats.getDynamic().getMod(Stats.CUSTOM_PRODUCTION_MOD).modifyMult(id, 1f + CUSTOM_PRODUCTION_BONUS/100f, "Industrial planning");
		}

		public void unapply(MutableCharacterStatsAPI stats, String id) {
			//stats.getDynamic().getMod(Stats.CUSTOM_PRODUCTION_MOD).unmodifyPercent(id);
			stats.getDynamic().getMod(Stats.CUSTOM_PRODUCTION_MOD).unmodifyMult(id);
		}
		
		public void createCustomDescription(MutableCharacterStatsAPI stats, SkillSpecAPI skill, 
											TooltipMakerAPI info, float width) {
			init(stats, skill);
			float opad = 10f;
			Color c = Misc.getBasePlayerColor();
			info.addPara("Affects: %s", opad + 5f, Misc.getGrayColor(), c, "all colonies");
			info.addSpacer(opad);
			info.addPara("+%s maximum value of custom ship and weapon production per month", 0f, hc, hc,
					"" + (int) CUSTOM_PRODUCTION_BONUS + "%");
		}
		
		public ScopeDescription getScopeDescription() {
			return ScopeDescription.ALL_OUTPOSTS;
		}
	}
	
	public static class Level3 implements CharacterStatsSkillEffect {
		public void apply(MutableCharacterStatsAPI stats, String id, float level) {
			//stats.getDynamic().getMod(Stats.CUSTOM_PRODUCTION_MOD).modifyPercent(id, CUSTOM_PRODUCTION_BONUS, "Industrial planning");
			stats.getDynamic().getMod(Stats.FLEET_QUALITY_MOD).modifyMult(id, 1f + SHIP_QUAL_BONUS/100f, "Industrial planning");
		}

		public void unapply(MutableCharacterStatsAPI stats, String id) {
			stats.getDynamic().getMod(Stats.FLEET_QUALITY_MOD).unmodifyMult(id);
		}
		
		public ScopeDescription getScopeDescription() {
			return ScopeDescription.GOVERNED_OUTPOST;
		}

		public String getEffectDescription(float level) {
			return "+" + SHIP_QUAL_BONUS + "% ship quality of all fleets launched";
		}

		public String getEffectPerLevelDescription() {
			return null;
		}
	}	
}



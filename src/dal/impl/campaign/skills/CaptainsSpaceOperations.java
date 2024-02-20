package dal.impl.campaign.skills;

import com.fs.starfarer.api.campaign.econ.MarketAPI;
import com.fs.starfarer.api.characters.MarketSkillEffect;
import com.fs.starfarer.api.impl.campaign.ids.Stats;

public class CaptainsSpaceOperations {
	
	public static float ACCESS = 0.3f;
	public static float FLEET_SIZE = 25f;
	
	public static float INCOME_MULT = 0.10f;
	public static int PATROLS_BONUS = 1;
	
	
	public static class Level1 implements MarketSkillEffect {
		public void apply(MarketAPI market, String id, float level) {
			market.getAccessibilityMod().modifyFlat(id, ACCESS, "Fleet logistics");
		}

		public void unapply(MarketAPI market, String id) {
			market.getAccessibilityMod().unmodifyFlat(id);
		}
		
		public String getEffectDescription(float level) {
			return "+" + (int)Math.round(ACCESS * 100f) + "% accessibility";
		}
		
		public String getEffectPerLevelDescription() {
			return null;
		}

		public ScopeDescription getScopeDescription() {
			return ScopeDescription.GOVERNED_OUTPOST;
		}
	}
	
	public static class Level2 implements MarketSkillEffect {
		public void apply(MarketAPI market, String id, float level) {
			market.getStats().getDynamic().getMod(Stats.COMBAT_FLEET_SIZE_MULT).modifyFlat(id, FLEET_SIZE / 100f, "Fleet logistics");
		}
		
		public void unapply(MarketAPI market, String id) {
			market.getStats().getDynamic().getMod(Stats.COMBAT_FLEET_SIZE_MULT).unmodifyFlat(id);
		}
		
		public String getEffectDescription(float level) {
			//return "" + (int)Math.round(FLEET_SIZE) + "% larger fleets";
			return "+" + (int)Math.round(FLEET_SIZE) + "% fleet size";
		}
		
		public String getEffectPerLevelDescription() {
			return null;
		}
		
		public ScopeDescription getScopeDescription() {
			return ScopeDescription.GOVERNED_OUTPOST;
		}
	}

	public static class Level3 implements MarketSkillEffect {
		public void apply(MarketAPI market, String id, float level) {
			//market.getIncomeMult().modifyMult(id, 1 + INCOME_MULT);
			market.getStats().getDynamic().getMod(Stats.PATROL_NUM_LIGHT_MOD).modifyFlat(id, 1);
		}

		public void unapply(MarketAPI market, String id) {
			market.getStats().getDynamic().getMod(Stats.PATROL_NUM_LIGHT_MOD).unmodifyFlat(id);
		}
	
		public String getEffectDescription(float level) {
			return "+" + PATROLS_BONUS + " light patrol fleets";
			//return "+" + (int)Math.round((INCOME_MULT) * 100f) + "% income from colonies, including exports";
		}
	
		public String getEffectPerLevelDescription() {
			return null;
		}
		public ScopeDescription getScopeDescription() {
			return ScopeDescription.GOVERNED_OUTPOST;
		}
	}
}



package dal.impl.campaign.skills;

import com.fs.starfarer.api.characters.CharacterStatsSkillEffect;
import com.fs.starfarer.api.characters.MutableCharacterStatsAPI;
import exerelin.utilities.StringHelper;

public class CaptainsNexSkills {
	
	public static boolean hasNex = false;
	public static int BONUS_AGENTS = 1;
	
	public static class AgentBonus implements CharacterStatsSkillEffect {

		
		//@Override
		public void apply(MutableCharacterStatsAPI stats, String id, float level) {
			if (hasNex) stats.getDynamic().getStat("nex_max_agents").modifyFlat(id, BONUS_AGENTS);
		}

		//@Override
		public void unapply(MutableCharacterStatsAPI stats, String id) {
			if (hasNex) stats.getDynamic().getStat("nex_max_agents").unmodify(id);
		}
		
		//@Override
		public String getEffectDescription(float level) {
			if (hasNex) return "+" + (int) BONUS_AGENTS + " " + StringHelper.getString("nex_agents", "skillBonusAgents");
			return "";
		}
		
		//@Override
		public String getEffectPerLevelDescription() {
			return null;
		}

		//@Override
		public ScopeDescription getScopeDescription() {
			return ScopeDescription.NONE;
		}
	}
}

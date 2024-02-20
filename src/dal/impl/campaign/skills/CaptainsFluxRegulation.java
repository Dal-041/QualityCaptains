package dal.impl.campaign.skills;

import com.fs.starfarer.api.characters.CharacterStatsSkillEffect;
import com.fs.starfarer.api.characters.MutableCharacterStatsAPI;
import com.fs.starfarer.api.characters.ShipSkillEffect;
import com.fs.starfarer.api.characters.SkillSpecAPI;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;
import com.fs.starfarer.api.ui.TooltipMakerAPI;

public class CaptainsFluxRegulation {

		public static float DISSIPATION_PERCENT = 10;
		public static float CAPACITY_PERCENT = 10;
		public static int EXTRA_VENTS = 5;
		public static int EXTRA_CAPS = 5;
		
		
		public static class Level1 implements ShipSkillEffect {
			
			public void apply(MutableShipStatsAPI stats, HullSize hullSize, String id, float level) {
				stats.getFluxDissipation().modifyPercent(id, DISSIPATION_PERCENT);
				stats.getFluxCapacity().modifyPercent(id, CAPACITY_PERCENT);
			}
			
			public void unapply(MutableShipStatsAPI stats, HullSize hullSize, String id) {
				stats.getFluxDissipation().unmodifyPercent(id);
				stats.getFluxCapacity().unmodifyPercent(id);
			}
			
			public String getEffectDescription(float level) {
				return "+" + (int)DISSIPATION_PERCENT + "% flux dissipation for combat ships";
			}
			
			public ScopeDescription getScopeDescription() {
				return ScopeDescription.ALL_SHIPS;
			}

			public String getEffectPerLevelDescription() {
				return null;
			}
		}
		
		public static class Level1B implements ShipSkillEffect {
			
			public void apply(MutableShipStatsAPI stats, HullSize hullSize, String id, float level) {

			}
			
			public void unapply(MutableShipStatsAPI stats, HullSize hullSize, String id) {

			}
			
			public String getEffectDescription(float level) {
				return "+" + (int)CAPACITY_PERCENT + "% flux capacity for combat ships";
			}
			
			public ScopeDescription getScopeDescription() {
				return ScopeDescription.ALL_SHIPS;
			}

			public String getEffectPerLevelDescription() {
				return null;
			}
		}
		
		public static class Level2 implements CharacterStatsSkillEffect {
			public void apply(MutableCharacterStatsAPI stats, String id, float level) {
				stats.getMaxCapacitorsBonus().modifyFlat(id, EXTRA_CAPS);
			}

			public void unapply(MutableCharacterStatsAPI stats, String id) {
				stats.getMaxCapacitorsBonus().unmodify(id);
			}

			public String getEffectDescription(float level) {
				return "+" + (int)EXTRA_CAPS + " maximum flux capacitors for each of your ships";
			}

			public String getEffectPerLevelDescription() {
				return null;
			}

			public ScopeDescription getScopeDescription() {
				return ScopeDescription.ALL_SHIPS;
			}
		}

		public static class Level3 implements CharacterStatsSkillEffect {
			public void apply(MutableCharacterStatsAPI stats, String id, float level) {
				//stats.getShipOrdnancePointBonus().modifyPercent(id, 50f);
				stats.getMaxVentsBonus().modifyFlat(id, EXTRA_VENTS);
			}

			public void unapply(MutableCharacterStatsAPI stats, String id) {
				//stats.getShipOrdnancePointBonus().unmodifyPercent(id);
				stats.getMaxVentsBonus().unmodify(id);
			}

			public String getEffectDescription(float level) {
				return "+" + (int)EXTRA_VENTS + " maximum flux vents for each of your ships";
			}

			public String getEffectPerLevelDescription() {
				return null;
			}

			public ScopeDescription getScopeDescription() {
				return ScopeDescription.ALL_SHIPS;
			}
		}

	}






package dal.impl.campaign.skills;

import com.fs.starfarer.api.characters.ShipSkillEffect;
import com.fs.starfarer.api.characters.LevelBasedEffect.ScopeDescription;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;
import com.fs.starfarer.api.impl.campaign.skills.Helmsmanship;

public class CaptainsHelmsmanship {
	public static boolean loadStock = false;
	protected static Helmsmanship.Level1 l1 = new Helmsmanship.Level1();
	protected static Helmsmanship.Level2 l2 = new Helmsmanship.Level2();
	protected static Helmsmanship.Level3 l3 = new Helmsmanship.Level3();
	protected static Helmsmanship.Level4 l4 = new Helmsmanship.Level4();
	
	public static float MANEUVERABILITY_BONUS = 50;
	public static float SPEED_BONUS = 15f;
	public static float ZERO_FLUX_LEVEL = 1f;
	
	public static float ELITE_SPEED_BONUS_FLAT = 10f;
	
	public static boolean USE_FLUX_THRESHOLD = true;
	public static boolean USE_FLUX_NO_USE = false;
	
	public static class Level1 implements ShipSkillEffect {
		public void apply(MutableShipStatsAPI stats, HullSize hullSize, String id, float level) {
			if (!loadStock) {
			stats.getAcceleration().modifyPercent(id, MANEUVERABILITY_BONUS);
			stats.getDeceleration().modifyPercent(id, MANEUVERABILITY_BONUS);
			stats.getTurnAcceleration().modifyPercent(id, MANEUVERABILITY_BONUS * 2f);
			stats.getMaxTurnRate().modifyPercent(id, MANEUVERABILITY_BONUS);
			} else {
				l1.apply(stats, hullSize, id, level);
			}			
		}
		
		public void unapply(MutableShipStatsAPI stats, HullSize hullSize, String id) {
			if (!loadStock) {
			stats.getAcceleration().unmodify(id);
			stats.getDeceleration().unmodify(id);
			stats.getTurnAcceleration().unmodify(id);
			stats.getMaxTurnRate().unmodify(id);
			} else {
				l1.unapply(stats, hullSize, id);
			}
		}
		
		public String getEffectDescription(float level) {
			if (!loadStock) {
				return "+" + (int)(MANEUVERABILITY_BONUS) + "% maneuverability and acceleration";
			} else {
				return l1.getEffectDescription(level);
			}				
		}
		
		public String getEffectPerLevelDescription() {
			if (!loadStock) {
				return null;
			} else { 
				return l1.getEffectPerLevelDescription();
			}
		}
		
		public ScopeDescription getScopeDescription() {
			if (!loadStock) {
				return ScopeDescription.PILOTED_SHIP;
			} else {
				return l1.getScopeDescription();
			}
		}
	}

	public static class Level2 implements ShipSkillEffect {
		public void apply(MutableShipStatsAPI stats, HullSize hullSize, String id, float level) {
			if (!loadStock) {
				stats.getMaxSpeed().modifyPercent(id, SPEED_BONUS);
			} else {
				l2.apply(stats, hullSize, id, level);
			}
		}
		
		public void unapply(MutableShipStatsAPI stats, HullSize hullSize, String id) {
			if (!loadStock) {
				stats.getMaxSpeed().unmodify(id);
			} else {
				l2.unapply(stats, hullSize, id);
			}
		}
		
		public String getEffectDescription(float level) {
			if (!loadStock) {
				return "+" + (int)(SPEED_BONUS) + "% top speed";
			} else {
				return l2.getEffectDescription(level);
			}				
		}
	
		public String getEffectPerLevelDescription() {
			if (!loadStock) {
				return null;
			} else { 
				return l2.getEffectPerLevelDescription();
			}
		}
	
		public ScopeDescription getScopeDescription() {
			if (!loadStock) {
				return ScopeDescription.PILOTED_SHIP;
			} else {
				return l2.getScopeDescription();
			}
		}
	}
	
	public static class Level3 implements ShipSkillEffect {

		public void apply(MutableShipStatsAPI stats, HullSize hullSize, String id, float level) {
			if (!loadStock) {
				if (USE_FLUX_THRESHOLD) {
					stats.getZeroFluxMinimumFluxLevel().modifyFlat(id, ZERO_FLUX_LEVEL * 0.01f);
				}
			} else {
				l3.apply(stats, hullSize, id, level);
			}
		}
		
		public void unapply(MutableShipStatsAPI stats, HullSize hullSize, String id) {
			if (!loadStock) {
				if (USE_FLUX_THRESHOLD) stats.getZeroFluxMinimumFluxLevel().unmodify(id);
			} else {
				l3.unapply(stats, hullSize, id);
			}
		}	
		
		public String getEffectDescription(float level) {
			if (!loadStock) {
				if (USE_FLUX_THRESHOLD) {
					return "The 0-flux speed boost is active at up to " + (int)(ZERO_FLUX_LEVEL) + "% flux";
				}
				return "";
			} else {
				return l3.getEffectDescription(level);
			}
		}
		
		public String getEffectPerLevelDescription() {
			if (!loadStock) {
				return null;
			} else {
				return l3.getEffectPerLevelDescription();
			}
		}
		
		public ScopeDescription getScopeDescription() {
			if (!loadStock) {
				return ScopeDescription.PILOTED_SHIP;
			} else {
				return l3.getScopeDescription();
			}
		}
	}
	
	public static class Level3B implements ShipSkillEffect {

		public void apply(MutableShipStatsAPI stats, HullSize hullSize, String id, float level) {
			if (USE_FLUX_NO_USE) {
				stats.getAllowZeroFluxAtAnyLevel().modifyFlat(id, 1f);
			}
		}
		
		public void unapply(MutableShipStatsAPI stats, HullSize hullSize, String id) {
			if (USE_FLUX_NO_USE) stats.getAllowZeroFluxAtAnyLevel().unmodifyFlat(id);
		}
		
		public String getEffectDescription(float level) {
			if (USE_FLUX_NO_USE) {
				return "The 0-flux speed boost is activated at any flux level, as long as the ship is not generating or venting flux in any way";
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
	
	public static class Level4 implements ShipSkillEffect {
		public void apply(MutableShipStatsAPI stats, HullSize hullSize, String id, float level) {
			stats.getMaxSpeed().modifyFlat(id, ELITE_SPEED_BONUS_FLAT);
		}
		
		public void unapply(MutableShipStatsAPI stats, HullSize hullSize, String id) {
			stats.getMaxSpeed().unmodify(id);
		}
		
		public String getEffectDescription(float level) {
			return "+" + (int)(ELITE_SPEED_BONUS_FLAT) + " su/s to top speed";
		}
		
		public String getEffectPerLevelDescription() {
			return null;
		}

		public ScopeDescription getScopeDescription() {
			return ScopeDescription.PILOTED_SHIP;
		}
	}
}

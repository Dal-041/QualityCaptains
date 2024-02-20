package dal.impl.campaign.skills;

import java.awt.Color;

import com.fs.starfarer.api.campaign.econ.MarketAPI;
import com.fs.starfarer.api.characters.FleetStatsSkillEffect;
import com.fs.starfarer.api.characters.MarketSkillEffect;
import com.fs.starfarer.api.characters.MutableCharacterStatsAPI;
import com.fs.starfarer.api.characters.ShipSkillEffect;
import com.fs.starfarer.api.characters.SkillSpecAPI;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;
import com.fs.starfarer.api.fleet.MutableFleetStatsAPI;
import com.fs.starfarer.api.impl.campaign.ids.Stats;
import com.fs.starfarer.api.impl.campaign.skills.BaseSkillEffectDescription;
import com.fs.starfarer.api.impl.campaign.skills.TacticalDrills;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;

public class CaptainsTacticalDrills {
	
	public static boolean loadStock = false;
	private static TacticalDrills.Level1 lev1 = new TacticalDrills.Level1();
	private static TacticalDrills.Level2 lev2 = new TacticalDrills.Level2();
	private static TacticalDrills.Level3 lev3 = new TacticalDrills.Level3();
	
	public static float WEAPON_ROF_BONUS = 6;
	public static float TURRET_LEADING_PERC = 25;
	public static float WEAPON_RECOIL_NEG_PERC = 20;
	
	public static int GROUND_ATTACK_BONUS = 50;
	public static int GROUND_DEFENSE_BONUS = 50;
	public static float CASUALTIES_MULT = 0.75f;
	
	public static class Level1 implements ShipSkillEffect {
		public void apply(MutableShipStatsAPI stats, HullSize hullSize, String id, float level) {
			if (!loadStock) {
				stats.getMaxRecoilMult().modifyMult(id, 1f - (0.01f * WEAPON_RECOIL_NEG_PERC));
				stats.getRecoilPerShotMult().modifyMult(id, 1f - (0.01f * WEAPON_RECOIL_NEG_PERC));
				stats.getRecoilDecayMult().modifyMult(id, 1f - (0.01f * WEAPON_RECOIL_NEG_PERC));
				stats.getAutofireAimAccuracy().modifyFlat(id, TURRET_LEADING_PERC * 0.01f);
			} else {
				lev1.apply(stats, hullSize, id, level);
			}
		}
		
		public void unapply(MutableShipStatsAPI stats, HullSize hullSize, String id) {
			if (!loadStock) {
				stats.getMaxRecoilMult().unmodify(id);
				stats.getRecoilPerShotMult().unmodify(id);
				stats.getRecoilDecayMult().unmodify(id);
				stats.getAutofireAimAccuracy().unmodify(id);
			} else {
				lev1.unapply(stats, hullSize, id);
			}
		}
		
		public String getEffectDescription(float level) {
			if (!loadStock) {
				return "-" + (int)(WEAPON_RECOIL_NEG_PERC) + "% weapon recoil and +" + (int)(TURRET_LEADING_PERC) + "% target leading accuracy for autofiring weapons";
			} else {
				return lev1.getEffectDescription(level);
			}
		}
		
		public String getEffectPerLevelDescription() {
			if (!loadStock) {
				return "";
			} else {
				return lev1.getEffectPerLevelDescription();
			}
		}

		public ScopeDescription getScopeDescription() {
			if (!loadStock) {
				return ScopeDescription.ALL_SHIPS;
			} else {
				return lev1.getScopeDescription();
			}
		}
	}

	public static class Level2 extends BaseSkillEffectDescription implements FleetStatsSkillEffect {
		public void apply(MutableFleetStatsAPI stats, String id, float level) {
			if (!loadStock) {
				stats.getDynamic().getMod(Stats.PLANETARY_OPERATIONS_MOD).modifyPercent(id, GROUND_ATTACK_BONUS, "Tactical drills");
			} else {
				lev2.apply(stats, id, level);
			}
		}
		
		public void unapply(MutableFleetStatsAPI stats, String id) {
			if (!loadStock) {
				stats.getDynamic().getMod(Stats.PLANETARY_OPERATIONS_MOD).unmodifyPercent(id);
			} else {
				lev2.unapply(stats, id);
			}
		}
		
		public void createCustomDescription(MutableCharacterStatsAPI stats, SkillSpecAPI skill, TooltipMakerAPI info, float width) {
			if (!loadStock) {
				init(stats, skill);

				float opad = 10f;
				Color c = Misc.getBasePlayerColor();
				//info.addPara("Affects: %s", opad + 5f, Misc.getGrayColor(), c, "fleet");
				info.addPara("Affects: %s", opad + 5f, Misc.getGrayColor(), c, "ground operations");
				info.addSpacer(opad);
				info.addPara("+%s effectiveness of ground operations such as raids", 0f, hc, hc,
					"" + (int) GROUND_ATTACK_BONUS + "%");
			} else {
				lev2.createCustomDescription(stats, skill, info, width);
			}
		}
		
		public String getEffectDescription(float level) {
			if (!loadStock) {
				return "+" + (int)(GROUND_ATTACK_BONUS) + "% effectiveness of ground attacks such as raids and +" + (int)(GROUND_DEFENSE_BONUS) + "% effectiveness of ground defenses";
			} else {
				return lev2.getEffectDescription(level);
			}
		}
		
		public String getEffectPerLevelDescription() {
			if (!loadStock) {
				return null;
			} else {
				return lev2.getEffectPerLevelDescription();
			}
		}
		
		public ScopeDescription getScopeDescription() {
			if (!loadStock) {
				return ScopeDescription.FLEET;
			} else {
				return lev2.getScopeDescription();
			}
		}
	}
	
	public static class Level2B implements MarketSkillEffect {
		public void apply(MarketAPI market, String id, float level) {
			market.getStats().getDynamic().getMod(Stats.GROUND_DEFENSES_MOD).modifyMult(id, 1f + GROUND_DEFENSE_BONUS * 0.01f, "Tactical drills");
		}

		public void unapply(MarketAPI market, String id) {
			//market.getStats().getDynamic().getMod(Stats.GROUND_DEFENSES_MOD).unmodifyPercent(id);
			market.getStats().getDynamic().getMod(Stats.GROUND_DEFENSES_MOD).unmodifyMult(id);
		}
		
		public String getEffectDescription(float level) {
			return "+" + (int)(GROUND_DEFENSE_BONUS) + "% more effective ground defenses";
		}
		
		public String getEffectPerLevelDescription() {
			return null;
		}

		public ScopeDescription getScopeDescription() {
			return ScopeDescription.ALL_OUTPOSTS;
		}
	}
	
	public static class Level3 implements FleetStatsSkillEffect {
		public void apply(MutableFleetStatsAPI stats, String id, float level) {
			if (!loadStock) {
				stats.getDynamic().getStat(Stats.PLANETARY_OPERATIONS_CASUALTIES_MULT).modifyMult(id, CASUALTIES_MULT, "Tactical drills");
			} else {
				lev3.apply(stats, id, level);
			}
		}
		
		public void unapply(MutableFleetStatsAPI stats, String id) {
			if (!loadStock) {
				stats.getDynamic().getStat(Stats.PLANETARY_OPERATIONS_CASUALTIES_MULT).unmodifyMult(id);
			} else {
				lev3.unapply(stats, id);
			}
		}
		
		public String getEffectDescription(float level) {
			if (!loadStock) {
			return "-" + (int)Math.round((1f - CASUALTIES_MULT) * 100f) + "% marine casualties suffered during ground operations";
			} else {
				return lev3.getEffectDescription(level);
			}
		}
		
		public String getEffectPerLevelDescription() {
			if (!loadStock) {
			return null;
			} else {
				return lev3.getEffectPerLevelDescription();
			}
		}
		
		public ScopeDescription getScopeDescription() {
			if (!loadStock) {
				return ScopeDescription.FLEET;
			} else {
				return lev3.getScopeDescription();
			}
		}
	}
}






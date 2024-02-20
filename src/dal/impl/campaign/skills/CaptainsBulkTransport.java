package dal.impl.campaign.skills;

import com.fs.starfarer.api.campaign.FleetDataAPI;
import com.fs.starfarer.api.campaign.econ.MarketAPI;
import com.fs.starfarer.api.characters.MarketSkillEffect;
import com.fs.starfarer.api.characters.MutableCharacterStatsAPI;
import com.fs.starfarer.api.characters.ShipSkillEffect;
import com.fs.starfarer.api.characters.SkillSpecAPI;
import com.fs.starfarer.api.characters.LevelBasedEffect.ScopeDescription;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.StatBonus;
import com.fs.starfarer.api.combat.MutableStat.StatMod;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;
import com.fs.starfarer.api.fleet.FleetMemberAPI;
import com.fs.starfarer.api.impl.campaign.ids.Stats;
import com.fs.starfarer.api.impl.campaign.skills.BaseSkillEffectDescription;
import com.fs.starfarer.api.impl.campaign.skills.BulkTransport.BaseCapacityModifierEffect;
import com.fs.starfarer.api.ui.TooltipMakerAPI;

public class CaptainsBulkTransport {

	public static float CARGO_CAPACITY_MAX_PERCENT = 30;
	public static float CARGO_CAPACITY_THRESHOLD = 20000;
	
	public static float FUEL_CAPACITY_MAX_PERCENT = 30;
	public static float FUEL_CAPACITY_THRESHOLD = 20000;
	
	public static float PERSONNEL_CAPACITY_MAX_PERCENT = 30;
	public static float PERSONNEL_CAPACITY_THRESHOLD = 50000;
	
	public static int ACCESS_BONUS_1 = 15;
	public static int ACCESS_BONUS_2 = 15;
	public static int FLEET_SIZE_BONUS = 10;
	
	public static float BURN_BONUS = 2;
	
	public abstract static class BaseCapacityModifierEffect extends BaseSkillEffectDescription implements ShipSkillEffect {
		protected abstract String getModifierId();
		protected abstract String getCacheKey();
		protected abstract String getCapacityString();
		protected abstract float getCapacity(FleetMemberAPI member);
		protected abstract float getMaxPercent();
		protected abstract float getThreshold();
		protected abstract StatBonus getShipStat(MutableShipStatsAPI stats);
		protected abstract boolean withSpacerAfter();
		
		public void apply(MutableShipStatsAPI stats, HullSize hullSize, String id, float level) {
			id = getModifierId();
			float capBonus = getCapacityBonus(id, getFleetData(stats));
			getShipStat(stats).modifyMult(id, 1f + (capBonus / 100f));
		}
		
		public void unapply(MutableShipStatsAPI stats, HullSize hullSize, String id) {
			id = getModifierId();
			getShipStat(stats).unmodifyMult(id);
		}
		
		public String getEffectDescription(float level) {
			return null;
		}
		
		protected float getCapacityBase(String id, FleetDataAPI data) {
			if (data == null) return 0f;
			
			float cap = 0;
			for (FleetMemberAPI curr : data.getMembersListCopy()) {
				cap += getCapacityBase(id, curr);
			}
			return cap;
		}
		
		protected float getCapacityBase(String id, FleetMemberAPI curr) {
			StatBonus stat = getShipStat(curr.getStats());
			StatMod mod = stat.getMultBonus(id);
			if (mod != null) {
				stat.unmodifyMult(mod.source);
			}
			float cap = getCapacity(curr);
			if (mod != null) {
				stat.modifyMult(mod.source, mod.value, mod.desc);
			}
			return cap;
		}
		
		protected float getCapacityBonus(String id, FleetDataAPI data) {
			if (data == null) return getMaxPercent();
			
			String key = getCacheKey();
			Float bonus = (Float) data.getCacheClearedOnSync().get(key);
			if (bonus != null) return bonus;
			
			//float base = getCapacityBase(id, data);
			
			bonus = getMaxPercent();
			//float capMult = 0f;
//			if (base > 0) {
//				float addCapacity = Math.min(base * (getMaxPercent() * 0.01f), getMaxUnits());
//				capMult = 1f + addCapacity / base;
//				capMult = Math.round(capMult * 100f) / 100f;
//			}
			
			data.getCacheClearedOnSync().put(key, bonus);
			return bonus;
		}
		
		public void createCustomDescription(MutableCharacterStatsAPI stats, SkillSpecAPI skill, 
											TooltipMakerAPI info, float width) {
			init(stats, skill);
			
//			//info.addSpacer(5f);
//			info.addPara(getCapacityString() + " increased by %s or %s units, whichever is lower",
//					0f, hc, hc,
//					"" + (int) getMaxPercent() + "%", 
//					"" + (int) getThreshold()
//			);
			
			FleetDataAPI data = getFleetData(null);
			float capBonus = getCapacityBonus(getModifierId(), data);;
						
			info.addPara("+%s " + getCapacityString().toLowerCase() + "", 0f, hc, hc,
					"" + (int)(Math.round(capBonus)) + "%");

			/*
			if (isInCampaign()) {
				float baseCap = getCapacityBase(getModifierId(), data);
				info.addPara(indent + "Maximum at %s or less base " + getCapacityString().toLowerCase() + 
									  " in fleet, your fleet has %s base " + getCapacityString().toLowerCase(),
						0f, tc, hc, 
						"" + (int) getThreshold(),
						"" + (int)Math.round(baseCap));
			} else {
				info.addPara(indent + "Maximum at %s or less base " + getCapacityString().toLowerCase() +
									  " in fleet",
						0f, tc, hc, 
						"" + (int) getThreshold());
			}
			*/
			if (withSpacerAfter()) {
				//info.addSpacer(5f);
			}
			
//			if (isInCampaign()) {
//				FleetDataAPI data = Global.getSector().getPlayerFleet().getFleetData();
//				String id = getModifierId();
//				float baseCap = getCapacityBase(id, data);
//				float capMult = getCapacityMult(id, data);
//				
//				float increase = baseCap * (capMult - 1f);
//				float actual = increase;
//				boolean approximate = false;
//				
////				if (this instanceof Level2) {
////					System.out.println("wefwefwe");
////				}
//				if (data != null) {
//					actual = 0f;
//					
//					for (FleetMemberAPI curr : data.getMembersListCopy()) {
//						float base = getCapacityBase(id, curr);
//						float add = (int)(base * capMult - base);
//						actual += add;
//					}
//					if (actual != increase) {
//						approximate = true;
//					}
//				}
//				
//				boolean has = stats.getSkillLevel(skill.getId()) > 0;
//				String is = "is";
//				if (!has) is = "would be";
//				String by = "by";
//				String units = "%s units";
//				if (approximate) {
//					//units = "approximately %s units";
//					by = "by approximately";
//					increase = actual;
//				}
//				info.addPara(indent + "Your fleet has a base " + getCapacityString().toLowerCase() +
//						" of %s, which " + is + " increased " + by + " %s, or " + units,
//						0f, tc, hc, 
//						"" + Misc.getRoundedValueMaxOneAfterDecimal(baseCap), 
//						"" + (int)(Math.round((capMult - 1f) * 100f)) + "%",
//						"" + (int)increase 
//				);
//				if (withSpacerAfter()) {
//					info.addSpacer(5f);
//				}
//			}
		}
		
		public ScopeDescription getScopeDescription() {
			return ScopeDescription.FLEET;
		}
	}
	
	public static class Market1 implements MarketSkillEffect {
		public void apply(MarketAPI market, String id, float level) {
			if (level == 1) {
				market.getAccessibilityMod().modifyFlat(id, ACCESS_BONUS_1, "Fleet logistics");
			} else if (level > 1) {
				market.getAccessibilityMod().modifyFlat(id, ACCESS_BONUS_1 + ACCESS_BONUS_2, "Fleet logistics");
			}
		}

		public void unapply(MarketAPI market, String id) {
			market.getAccessibilityMod().unmodifyFlat(id);
		}
		
		public String getEffectDescription(float level) {
			return "+" + (int)Math.round(ACCESS_BONUS_1) + "% accessibility at governed colonies";
		}
		
		public String getEffectPerLevelDescription() {
			return null;
		}

		public ScopeDescription getScopeDescription() {
			return ScopeDescription.GOVERNED_OUTPOST;
		}
	}
	
	public static class Market2 implements MarketSkillEffect {
		public void apply(MarketAPI market, String id, float level) {
			//market.getAccessibilityMod().modifyFlat(id, ACCESS_2, "Fleet logistics");
		}

		public void unapply(MarketAPI market, String id) {
			//market.getAccessibilityMod().unmodifyFlat(id);
		}
		
		public String getEffectDescription(float level) {
			return "+" + (int)Math.round(ACCESS_BONUS_2) + "% accessibility at governed colonies";
		}
		
		public String getEffectPerLevelDescription() {
			return null;
		}
		
		public ScopeDescription getScopeDescription() {
			return ScopeDescription.GOVERNED_OUTPOST;
		}
	}
	
	public static class Market3 implements MarketSkillEffect {
		public void apply(MarketAPI market, String id, float level) {
			market.getStats().getDynamic().getMod(Stats.COMBAT_FLEET_SIZE_MULT).modifyFlat(id, FLEET_SIZE_BONUS / 100f, "Fleet logistics");
		}
		
		public void unapply(MarketAPI market, String id) {
			market.getStats().getDynamic().getMod(Stats.COMBAT_FLEET_SIZE_MULT).unmodifyFlat(id);
		}
		
		public String getEffectDescription(float level) {
			return "" + (int)Math.round(FLEET_SIZE_BONUS) + "% larger fleets at governed colonies";
		}
		
		public String getEffectPerLevelDescription() {
			return null;
		}
		
		public ScopeDescription getScopeDescription() {
			return ScopeDescription.GOVERNED_OUTPOST;
		}
	}
	
	public static class Level1 extends BaseCapacityModifierEffect {
		public String getModifierId() {
			return "bt_cargo_cap_mod";
		}
		public String getCacheKey() {
			return "bt_cargo_cap";
		}
		public StatBonus getShipStat(MutableShipStatsAPI stats) {
			return stats.getCargoMod();
		}
		public float getCapacity(FleetMemberAPI member) {
			return member.getCargoCapacity();
		}
		@Override
		public String getCapacityString() {
			return "Cargo capacity";
		}
		@Override
		public float getMaxPercent() {
			return CARGO_CAPACITY_MAX_PERCENT;
		}
		@Override
		public float getThreshold() {
			return CARGO_CAPACITY_THRESHOLD;
		}
		@Override
		public boolean withSpacerAfter() {
			return true;
		}
	}
	
	public static class Level2 extends BaseCapacityModifierEffect {
		public String getModifierId() {
			return "bt_fuel_cap_mod";
		}
		public String getCacheKey() {
			return "bt_fuel_cap";
		}
		public StatBonus getShipStat(MutableShipStatsAPI stats) {
			return stats.getFuelMod();
		}
		public float getCapacity(FleetMemberAPI member) {
			return member.getFuelCapacity();
		}
		@Override
		public String getCapacityString() {
			return "Fuel capacity";
		}
		@Override
		public float getMaxPercent() {
			return FUEL_CAPACITY_MAX_PERCENT;
		}
		@Override
		public float getThreshold() {
			return FUEL_CAPACITY_THRESHOLD;
		}
		@Override
		public boolean withSpacerAfter() {
			return true;
		}
	}
	
	public static class Level3 extends BaseCapacityModifierEffect {
		public String getModifierId() {
			return "bt_crew_cap_mod";
		}
		public String getCacheKey() {
			return "bt_crew_cap";
		}
		public StatBonus getShipStat(MutableShipStatsAPI stats) {
			return stats.getMaxCrewMod();
		}
		public float getCapacity(FleetMemberAPI member) {
			return member.getMaxCrew();
		}
		@Override
		public String getCapacityString() {
			return "Personnel capacity";
		}
		@Override
		public float getMaxPercent() {
			return PERSONNEL_CAPACITY_MAX_PERCENT;
		}
		@Override
		public float getThreshold() {
			return PERSONNEL_CAPACITY_THRESHOLD;
		}
		@Override
		public boolean withSpacerAfter() {
			return true;
		}
	}
	
	public static class Level4 extends BaseSkillEffectDescription implements ShipSkillEffect {
		public void apply(MutableShipStatsAPI stats, HullSize hullSize, String id, float level) {
			if (isCivilian(stats)) {
				stats.getMaxBurnLevel().modifyFlat(id, BURN_BONUS);
			}
		}
			
		public void unapply(MutableShipStatsAPI stats, HullSize hullSize, String id) {
			stats.getMaxBurnLevel().unmodifyFlat(id);
		}
		
		@Override
		public boolean hasCustomDescription() {
			return false;
		}

		public String getEffectDescription(float level) {
			return "Increases the burn level of all non-militarized civilian-grade ships by " + (int) BURN_BONUS;
		}
			
		public ScopeDescription getScopeDescription() {
			return ScopeDescription.ALL_SHIPS;
		}
	}
}




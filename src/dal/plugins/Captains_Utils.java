package dal.plugins;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.listeners.FleetEventListener;
import com.fs.starfarer.api.characters.MutableCharacterStatsAPI;
import com.fs.starfarer.api.characters.MutableCharacterStatsAPI.SkillLevelAPI;
import com.fs.starfarer.api.impl.campaign.AICoreOfficerPluginImpl;
import com.fs.starfarer.api.impl.campaign.skills.BaseSkillEffectDescription;
import com.fs.starfarer.api.impl.campaign.skills.FieldRepairsScript;

import dal.impl.campaign.skills.CaptainsAutomatedShips;
import dal.impl.campaign.skills.CaptainsAuxiliarySupport;
import dal.impl.campaign.skills.CaptainsBallisticMastery;
import dal.impl.campaign.skills.CaptainsBestOfTheBest;
import dal.impl.campaign.skills.CaptainsBulkTransport;
import dal.impl.campaign.skills.CaptainsCarrierGroup;
import dal.impl.campaign.skills.CaptainsColonyManagement;
import dal.impl.campaign.skills.CaptainsCombatEndurance;
import dal.impl.campaign.skills.CaptainsContainmentProcedures;
import dal.impl.campaign.skills.CaptainsCoordinatedManeuvers;
import dal.impl.campaign.skills.CaptainsCoordinatedManeuversScript;
import dal.impl.campaign.skills.CaptainsCrewTraining;
import dal.impl.campaign.skills.CaptainsCyberneticAugmentation;
import dal.impl.campaign.skills.CaptainsDamageControl;
import dal.impl.campaign.skills.CaptainsDerelictContingent;
import dal.impl.campaign.skills.CaptainsElectronicWarfare;
import dal.impl.campaign.skills.CaptainsElectronicWarfareScript;
import dal.impl.campaign.skills.CaptainsEnergyWeaponsMastery;
import dal.impl.campaign.skills.CaptainsFieldModulation;
import dal.impl.campaign.skills.CaptainsFieldRepairs;
import dal.impl.campaign.skills.CaptainsFieldRepairsScript;
import dal.impl.campaign.skills.CaptainsFighterUplink;
import dal.impl.campaign.skills.CaptainsFluxRegulation;
import dal.impl.campaign.skills.CaptainsGunneryImplants;
import dal.impl.campaign.skills.CaptainsHelmsmanship;
import dal.impl.campaign.skills.CaptainsHullRestoration;
import dal.impl.campaign.skills.CaptainsHypercognition;
import dal.impl.campaign.skills.CaptainsImpactMitigation;
import dal.impl.campaign.skills.CaptainsIndustrialPlanning;
import dal.impl.campaign.skills.CaptainsMakeshiftEquipment;
import dal.impl.campaign.skills.CaptainsMissileSpecialization;
import dal.impl.campaign.skills.CaptainsNavigation;
import dal.impl.campaign.skills.CaptainsNeuralLink;
import dal.impl.campaign.skills.CaptainsNeuralLinkScript;
import dal.impl.campaign.skills.CaptainsNexSkills;
import dal.impl.campaign.skills.CaptainsOfficerManagement;
import dal.impl.campaign.skills.CaptainsOfficerTraining;
import dal.impl.campaign.skills.CaptainsOrdnanceExpertise;
import dal.impl.campaign.skills.CaptainsPhaseCorps;
import dal.impl.campaign.skills.CaptainsPhaseMastery;
import dal.impl.campaign.skills.CaptainsPlanetaryOperations;
import dal.impl.campaign.skills.CaptainsPointDefense;
import dal.impl.campaign.skills.CaptainsPolarizedArmor;
import dal.impl.campaign.skills.CaptainsRangedSpecialization;
import dal.impl.campaign.skills.CaptainsReliabilityEngineering;
import dal.impl.campaign.skills.CaptainsSalvaging;
import dal.impl.campaign.skills.CaptainsSensors;
import dal.impl.campaign.skills.CaptainsShieldModulation;
import dal.impl.campaign.skills.CaptainsSpaceOperations;
import dal.impl.campaign.skills.CaptainsSpecialModifications;
import dal.impl.campaign.skills.CaptainsStrikeCommander;
import dal.impl.campaign.skills.CaptainsSupportDoctrine;
import dal.impl.campaign.skills.CaptainsSystemsExpertise;
import dal.impl.campaign.skills.CaptainsTacticalDrills;
import dal.impl.campaign.skills.CaptainsTargetAnalysis;
import dal.impl.campaign.skills.CaptainsVayraSkills;
import dal.impl.campaign.skills.CaptainsWolfpackTactics;
import dal.impl.campaign.skills.milestones.CaptainsAcademician;
import dal.impl.campaign.skills.milestones.CaptainsBabylon;
import dal.impl.campaign.skills.milestones.CaptainsCombatListener;
import dal.impl.campaign.skills.milestones.CaptainsKnight;
import dal.impl.campaign.skills.milestones.CaptainsLucifer;
import dal.impl.campaign.skills.milestones.CaptainsMilestoneEFScript;
import dal.impl.campaign.skills.milestones.CaptainsOmega;
import dal.impl.campaign.skills.milestones.CaptainsPrince;
import dal.impl.campaign.skills.milestones.CaptainsStarfarer;
import dal.impl.campaign.skills.milestones.CaptainsAscendance;
import dal.impl.campaign.skills.milestones.CaptainsUnbound;
import dal.impl.campaign.skills.milestones.CaptainsUnderworld;
import dal.impl.campaign.skills.milestones.CaptainsUsurper;
import dal.impl.intel.IntelQCAchievedMilestone;
import lunalib.lunaSettings.LunaSettings;



public class Captains_Utils {
    private static final Logger LOG = Global.getLogger(Captains_Utils.class);
    private static final String PATH = "data/config/LunaSettings.csv";
    public static boolean initLoad = false;
    public static JSONArray csv;
    //public static ArrayList<String> entry = new ArrayList<String>();
    public static ArrayList<ArrayList<String>> entryList = new ArrayList<ArrayList<String>>();
    
    public static boolean QCMilestonesEnabled = true;
	private static final String[] milestoneSkills = { CaptainsAcademician.skillID, CaptainsBabylon.skillID, CaptainsUnbound.skillID, CaptainsUsurper.skillID,
			CaptainsPrince.skillID, CaptainsKnight.skillID, CaptainsLucifer.skillID, CaptainsOmega.skillID, CaptainsUnderworld.skillID, CaptainsAscendance.skillID };
	
	static boolean hasNexerelin = Global.getSettings().getModManager().isModEnabled("nexerelin");
	static boolean hasVSP = Global.getSettings().getModManager().isModEnabled("vayrashippack");
	static boolean hasRealisticCombat = Global.getSettings().getModManager().isModEnabled("RealisticCombat");
	public static Boolean QCInstalled = true;
	
	
    protected static void parseModSettings() throws JSONException {
    	if (initLoad) return;
        try {
        	csv = Global.getSettings().loadCSV(PATH, Captains_ModPlugin.ModID);
        } catch (IOException | JSONException ex) {
            LOG.fatal("QC: unable to read LunaSettings.csv", ex);
        }

        if (csv == null) return;
        
        for (int i = 0; i < csv.length(); i++) {
        	
        	JSONObject row = csv.getJSONObject(i);

			String id = row.getString("fieldID");
			if (id == "") continue;
			String type = row.getString("fieldType");
			
			String value = row.getString("defaultValue");
			//String valueSec = row.getString("secondaryValue");
			String min = row.getString("minValue");
			String max = row.getString("maxValue");
			//String tab = row.getString("tab");
			
			ArrayList<String> entry = new ArrayList<String>();
			entry.add(id); entry.add(type); entry.add(value); entry.add(min); entry.add(max);
			entryList.add(entry);
			
        }
        initLoad = true;
        return;
    }
    
    public static int getIndex(String id) {
		for (int i = 0; i < entryList.size(); i++) {
			if (entryList.get(i).get(0).equals(id)) {
				return i;
			}
		}
		LOG.warn("QC: Did not find entry for " + id + "!");
    	return 0;
    }
    
    public static boolean getBool(String id) {
    	return getBoolean(id);
    }
    
    public static boolean getBoolean(String id) {
    	int i = getIndex(id);
    	return Boolean.parseBoolean(entryList.get(i).get(2));
    }
    
    public static int getInt(String id) {
    	int i = getIndex(id);
    	int result = Integer.parseInt(entryList.get(i).get(2));
    	if (result < Integer.parseInt(entryList.get(i).get(3))) result = Integer.parseInt(entryList.get(i).get(3));
    	if (result > Integer.parseInt(entryList.get(i).get(4))) result = Integer.parseInt(entryList.get(i).get(4));
    	return result; 
    }
    
    public static float getFloat(String id) {
    	int i = getIndex(id);
    	float result = Float.parseFloat(entryList.get(i).get(2));
    	if (result < Float.parseFloat(entryList.get(i).get(3))) result = Float.parseFloat(entryList.get(i).get(3));
    	if (result > Float.parseFloat(entryList.get(i).get(4))) result = Float.parseFloat(entryList.get(i).get(4));
    	return result;
    }
    
    public static double getDouble(String id) {
    	int i = getIndex(id);
    	double result = Double.parseDouble(entryList.get(i).get(2));
    	if (result < Double.parseDouble(entryList.get(i).get(3))) result = Double.parseDouble(entryList.get(i).get(3));
    	if (result > Double.parseDouble(entryList.get(i).get(4))) result = Double.parseDouble(entryList.get(i).get(4));
    	return result;
    }
    
    public static String getString(String id) {
    	int i = getIndex(id);
    	return entryList.get(i).get(2);
    }
    
	public static void loadQualityConfig() {
		QCInstalled = getBool("qc_installed");
		boolean QCCustomBattleEffects = getBool("qc_useCustomBattleEffects");
		QCMilestonesEnabled = getBool("qc_useMilestones");
		
		if (QCInstalled) {
			LOG.info("QC: Loading skill configs");
			//Now manually validated
			//If you need the depreciated ones, add the entries to LunaSettings.csv and call the load methods via console 
			// "runcode dal.plugins.Captains_Utils.loadQualityCombatV1();" etc
			//loadQualityCombatV1();
			loadQualityCombatV2();
			//loadQualityLeadershipV1();
			loadQualityLeadershipV2();
			//loadQualityTechnologyV1();
			loadQualityTechnologyV2();
			//loadQualityIndustryV1();
			loadQualityIndustryV2();
			if (QCCustomBattleEffects) {
				loadQualityBattleEffectsV1();
			}
			if (QCMilestonesEnabled) {
				loadQualityMilestoneEffectsV1();
			}
			//Mod support
			CaptainsNexSkills.hasNex = hasNexerelin;
			CaptainsVayraSkills.hasVSP = hasVSP;
			if (hasNexerelin) loadNex();
			if (hasVSP) loadVSP();
			if (hasRealisticCombat) handleOverrides(hasRealisticCombat);
			
			CaptainsSupportDoctrine.recalcDPReductionCap();
			//CaptainsAutomatedShips.refreshThreshold();
		} 		
	}
	
	public static void uninstallQC() {
		if (!Global.getSector().hasScript(FieldRepairsScript.class)) {
			Global.getSector().addScript(new FieldRepairsScript());
		}
		if (Global.getSector().hasScript(CaptainsFieldRepairsScript.class)) {
			Global.getSector().removeScriptsOfClass(CaptainsFieldRepairsScript.class);
		}
		if (Global.getSector().hasScript(CaptainsMilestoneEFScript.class)) {
			Global.getSector().removeScriptsOfClass(CaptainsMilestoneEFScript.class);
		}
		if (Global.getSector().getPlayerFleet() != null) {
			for (FleetEventListener listener : Global.getSector().getPlayerFleet().getEventListeners()) {
				if (listener.equals(new CaptainsCombatListener())) {
					Global.getSector().getPlayerFleet().removeEventListener(listener);
				}
			}
		}
		if (Global.getSector().getPlayerStats() != null) {
			for (SkillLevelAPI skill : Global.getSector().getPlayerStats().getSkillsCopy()) {
				for (String ID : milestoneSkills) {
					if (skill.getSkill().getId().equals(ID)) {
						skill.setLevel(0);
					}
				}
			}
		}
		return;
	}
	
	public static void installQC() {
		if (Global.getSector().hasScript(FieldRepairsScript.class)) {
			Global.getSector().removeScriptsOfClass(FieldRepairsScript.class);
		}
		if (!Global.getSector().hasScript(CaptainsFieldRepairsScript.class)) {
			Global.getSector().addScript(new CaptainsFieldRepairsScript());
		}
		if (!Global.getSector().hasScript(CaptainsMilestoneEFScript.class)) {
			Global.getSector().addScript(new CaptainsMilestoneEFScript());
		}
		if (Global.getSector().getPlayerFleet() != null) Global.getSector().getPlayerFleet().addEventListener(new CaptainsCombatListener());
		return;
	}
	
	public static void loadQualityCombatV1() {
		
		try {
		// BACKCOMPAT
		CaptainsStrikeCommander.FIGHTER_REPLACEMENT_RATE_BONUS = getFloat("STRKCOM_REPLACEMENT_BONUS");
		CaptainsStrikeCommander.MISSILE_HITPOINTS_BONUS = getFloat("STRKCOM_ORD_HP_BONUS");
		CaptainsStrikeCommander.SPEED_BONUS = getFloat("STRKCOM_SPEED_BONUS");
		CaptainsStrikeCommander.DAMAGE_TAKEN_REDUCTION = getFloat("STRKCOM_DAMAGE_TAKEN_REDUCTION");
		CaptainsStrikeCommander.TARGET_LEADING_BONUS = getFloat("STRKCOM_TARGET_LEADING");
		CaptainsStrikeCommander.FTR_DAMAGE_TO_FRIGATES = getFloat("STRKCOM_BONUS_DAM_FRIG");
		CaptainsStrikeCommander.FTR_DAMAGE_TO_DESTROYERS = getFloat("STRKCOM_BONUS_DAM_DEST");
		CaptainsStrikeCommander.FTR_DAMAGE_TO_CRUISERS = getFloat("STRKCOM_BONUS_DAM_CRUISER");
		CaptainsStrikeCommander.FTR_DAMAGE_TO_CAPITALS = getFloat("STRKCOM_BONUS_DAM_CAP");
		
		// BACKCOMPAT
		CaptainsRangedSpecialization.RANGED_BONUS = getBool("ST_USE_RANGED_BONUS_SKILL");
		CaptainsRangedSpecialization.MAX_CHANCE_PERCENT = getFloat("ST_RANGED_DAM_BONUS");
		CaptainsRangedSpecialization.MIN_RANGE = getFloat("ST_RANGED_DAM_MIN");
		CaptainsRangedSpecialization.MAX_RANGE = getFloat("ST_RANGED_DAM_MAX");
		CaptainsRangedSpecialization.PROJ_SPEED_BONUS = getFloat("ST_PROJ_SPEED_BONUS");
		CaptainsRangedSpecialization.RANGE_BONUS = getFloat("ST_RANGE_BONUS");
		CaptainsRangedSpecialization.DAMAGE_TO_SHIELDS_BONUS = getFloat("ST_DAMAGE_TO_SHIELD_BONUS");
		
		// BACKCOMPAT
		CaptainsShieldModulation.FLUX_UPKEEP_REDUCTION = getFloat("SM_UPKEEP_REDUCTION");
		CaptainsShieldModulation.FLUX_SHUNT_DISSIPATION = getFloat("SM_HFLUX_DISSIPATION_RATE");
		CaptainsShieldModulation.SHIELD_DAMAGE_REDUCTION = getFloat("SM_DAMAGE_TO_SHIELD_REDUCTION");
		CaptainsShieldModulation.SHIELD_RATE_BONUS = getFloat("SM_SHIELD_RATES_BONUS");
		
		// BACKCOMPAT
		CaptainsPhaseMastery.FLUX_UPKEEP_REDUCTION = getFloat("PHSMR_UPKEEP_REDUCTION");
		CaptainsPhaseMastery.PHASE_COOLDOWN_REDUCTION = getFloat("PHSMR_COOLDOWN_REDUCTION");
		CaptainsPhaseMastery.PHASE_SPEED_BONUS_OTHER = getFloat("PHSMR_SPEED_BONUS_OTHER");
		CaptainsPhaseMastery.PHASE_SPEED_BONUS_CAP = getFloat("PHSMR_SPEED_BONUS_CAP");
		CaptainsPhaseMastery.PHASE_SPEED_BONUS_CRU = getFloat("PHSMR_SPEED_BONUS_CRU");
		CaptainsPhaseMastery.PHASE_SPEED_BONUS_DEST = getFloat("PHSMR_SPEED_BONUS_DEST");
		CaptainsPhaseMastery.PHASE_SPEED_BONUS_FRIG = getFloat("PHSMR_SPEED_BONUS_FRIG");
		} catch (RuntimeException e) {
			//welp
		}
	}
	
	public static void loadQualityCombatV2() {
		//Combat
		CaptainsHelmsmanship.MANEUVERABILITY_BONUS = getFloat("HELM_MANEUVERABILITY_BONUS");
		CaptainsHelmsmanship.SPEED_BONUS = getFloat("HELM_SPEED_BONUS");
		CaptainsHelmsmanship.ZERO_FLUX_LEVEL = getFloat("HELM_FLUX_THRESHOLD");
		CaptainsHelmsmanship.USE_FLUX_NO_USE = getBool("HELM_0FLUXBOOST_ANYDECAYING_ENABLED");
		CaptainsHelmsmanship.USE_FLUX_THRESHOLD = getBool("HELM_0FLUXBOOST_THRESHOLD_ENABLED");
		CaptainsHelmsmanship.ELITE_SPEED_BONUS_FLAT = getInt("HELM_FLAT_SPEED_BONUS");
		
		CaptainsCombatEndurance.PEAK_TIME_BONUS = getInt("CE_PPT_BONUS");
		CaptainsCombatEndurance.MAX_CR_BONUS = getFloat("CE_MAX_CR_BONUS");
		CaptainsCombatEndurance.MALFUNCTION_REDUCTION = getFloat("CE_MALFUNCTION_REDUCTION");
		CaptainsCombatEndurance.DEGRADE_REDUCTION_PERCENT = getFloat("CE_DEGRADATION_RATE_REDUCTION");
		CaptainsCombatEndurance.MAX_REGEN_LEVEL = getFloat("CE_BEGIN_REPAIR_BELOW");
		CaptainsCombatEndurance.TOTAL_REGEN_MAX_POINTS = getInt("CE_HP_REPAIR_LIMIT");
		CaptainsCombatEndurance.REGEN_RATE = getFloat("CE_HP_REPAIR_RATE");
		
		CaptainsImpactMitigation.ARMOR_BONUS = getFloat("IM_ARMOR_BONUS");
		CaptainsImpactMitigation.ARMOR_DAMAGE_REDUCTION = getFloat("IM_ARMOR_DAMAGE_REDUCTION_BONUS");
		//CaptainsImpactMitigation.DAMAGE_TO_COMPONENTS_REDUCTION = getFloat("IM_DAMAGE_TO_COMPONENTS_REDUCTION");
		CaptainsImpactMitigation.ARMOR_EFFECTIVE_BONUS = getFloat("IM_ARMOR_EFFECTIVE_BONUS");
		CaptainsImpactMitigation.MAX_DAMAGE_REDUCTION_BONUS = getFloat("IM_MAX_DAMAGE_REDUCTION_BONUS");
		CaptainsImpactMitigation.MIN_ARMOR_FRACTION_BONUS = getFloat("IM_MIN_ARMOR_FRACTION_BONUS");
		
		CaptainsDamageControl.HULL_DAMAGE_REDUCTION = getFloat("DC_HULL_DAMAGE_REDUCTION");
		CaptainsDamageControl.CREW_LOSS_REDUCTION = getFloat("DC_CREW_LOSS_REDUCTION");
		//CaptainsDamageControl.INSTA_REPAIR_PERC = getInt("DC_SHIP_INSTA_REPAIR");
		CaptainsDamageControl.COMPONENT_REPAIR_BONUS = getFloat("DC_COMPONENT_REPAIR_BONUS");
		CaptainsDamageControl.COMPONENT_DAMAGE_TAKEN = getFloat("DC_COMPONENT_DAMAGE_TAKEN_REDUCTION");
		//CaptainsDamageControl.OVERLOAD_REDUCTION = getFloat("DC_OVERLOAD_REDUCTION");
		CaptainsDamageControl.USE_HIT_NULL = getBool("DC_USE_HIT_NULL");
		CaptainsDamageControl.HIT_NULL_MIN = getFloat("DC_HIT_NULL_MIN");
		CaptainsDamageControl.HIT_NULL_COOLDOWN = getFloat("DC_HIT_NULL_COOLDOWN");
		CaptainsDamageControl.HIT_NULL_PERC = getFloat("DC_HIT_NULL_PERC");
		
		CaptainsFieldModulation.PHASE_FLUX_UPKEEP_REDUCTION = getFloat("FM_PHASE_UPKEEP_REDUCTION");
		CaptainsFieldModulation.FLUX_SHUNT_DISSIPATION = getFloat("FM_SHIELD_HFLUX_DISSIPATION_RATE");
		CaptainsFieldModulation.SHIELD_DAMAGE_REDUCTION = getFloat("FM_DAMAGE_TO_SHIELD_REDUCTION");
		CaptainsFieldModulation.SHIELD_RATE_BONUS = getFloat("FM_SHIELD_RATES_BONUS");
		CaptainsFieldModulation.PHASE_FLUX_THRESHOLD_BONUS = getFloat("FM_PHASE_FLUX_THRESHOLD_BONUS");
		CaptainsFieldModulation.PHASE_COOLDOWN_REDUCTION = getFloat("FM_PHASE_COOLDOWN_REDUCTION");
		
		CaptainsPointDefense.FIGHTER_DAMAGE_BONUS = getFloat("PD_FIGHTER_DAMAGE_BONUS");
		CaptainsPointDefense.MISSILE_DAMAGE_BONUS = getFloat("PD_MISSILE_DAMAGE_BONUS");
		CaptainsPointDefense.TURRET_TURN_BONUS = getFloat("PD_TURRET_TURN_BONUS");
		CaptainsPointDefense.FIGHTER_DAMAGE_BONUS_FTR_MULT = getFloat("PD_FTR_ANTIFIGHTER_BONUS_MULT");
		CaptainsPointDefense.MISSILE_DAMAGE_BONUS_FTR_MULT = getFloat("PD_FTR_ANTIMISSILE_BONUS_MULT");
		CaptainsPointDefense.PD_RANGE_BONUS_FLAT = getFloat("PD_RANGE_BONUS_FLAT");
		CaptainsPointDefense.PD_RANGE_BONUS_PERC = getFloat("PD_RANGE_BONUS_PERC");
		//CaptainsPointDefense.PD_RANGE_BONUS_FTR_MULT = getFloat("PD_RANGE_FTR_BONUS_MULT");

		CaptainsTargetAnalysis.DAMAGE_TO_FRIGATES = getFloat("TA_DAM_TO_FRIG_BONUS");
		CaptainsTargetAnalysis.DAMAGE_TO_DESTROYERS = getFloat("TA_DAM_TO_DEST_BONUS");
		CaptainsTargetAnalysis.DAMAGE_TO_CRUISERS = getFloat("TA_DAM_TO_CRUISER_BONUS");
		CaptainsTargetAnalysis.DAMAGE_TO_CAPITALS = getFloat("TA_DAM_TO_CAP_BONUS");
		CaptainsTargetAnalysis.DAMAGE_TO_COMPONENTS_BONUS = getFloat("TA_DAM_TO_COMP_BONUS");
		CaptainsTargetAnalysis.HIT_STRENGTH_BONUS = getFloat("TA_ARMOR_BREACH_BONUS");
		
		//Fire Control
		CaptainsBallisticMastery.PROJ_SPEED_BONUS = getFloat("FC_PROJ_SPEED_BONUS");
		CaptainsBallisticMastery.RANGE_BONUS = getFloat("FC_RANGE_BONUS");
		CaptainsBallisticMastery.DAMAGE_TO_SHIELDS_BONUS = getFloat("FC_DAMAGE_TO_SHIELD_BONUS");
		
		CaptainsSystemsExpertise.CHARGES_PERCENT = getFloat("SE_CHARGES_PERCENT");
		CaptainsSystemsExpertise.CHARGES_BONUS = getInt("SE_CHARGES_FLAT");
		CaptainsSystemsExpertise.REGEN_PERCENT = getFloat("SE_REGEN_PERCENT");
		CaptainsSystemsExpertise.RANGE_PERCENT = getFloat("SE_RANGE_PERCENT");
		CaptainsSystemsExpertise.SYSTEM_COOLDOWN_REDUCTION_PERCENT = getFloat("SE_COOLDOWN_REDUCTION");
		CaptainsSystemsExpertise.NO_OFFICER_FACTOR = getFloat("SE_NO_OFFICER_MULT");
		
		CaptainsMissileSpecialization.MISSILE_AMMO_BONUS_1 = getFloat("MS_AMMO_BONUS_1");
		CaptainsMissileSpecialization.MISSILE_AMMO_BONUS_2 = getFloat("MS_AMMO_BONUS_2");
		CaptainsMissileSpecialization.MISSILE_SPEC_HEALTH_BONUS = getFloat("MS_HEALTH_BONUS");
		//CaptainsMissileSpecialization.MISSILE_SPEC_DAMAGE_BONUS = getFloat("MS_DAMAGE_BONUS");
		CaptainsMissileSpecialization.MISSILE_SPEC_ROF_BONUS = getFloat("MS_ROF_BONUS");
		CaptainsMissileSpecialization.MISSILE_SPEC_SPEED_BONUS = getFloat("MS_SPEED_BONUS");
		CaptainsMissileSpecialization.MISSILE_SPEC_RANGE_MULT = getFloat("MS_RANGE_MULT");
		CaptainsMissileSpecialization.MISSILE_TURN_ACCEL_BONUS = getFloat("MS_TURN_ACCEL_BONUS");
		CaptainsMissileSpecialization.MISSILE_TURN_RATE_BONUS = getFloat("MS_TURN_RATE_BONUS");
		CaptainsMissileSpecialization.MISSILE_SPEC_ACCEL_BONUS = getFloat("MS_ACCEL_BONUS");
		
	}
	
	public static void loadQualityLeadershipV1() {
		try {
			//0.95
			CaptainsAuxiliarySupport.AUXILIARY_EFFECT_BONUS = getFloat("AUXSP_EFFECT_BONUS");
			CaptainsAuxiliarySupport.BURN_BONUS = getInt("AUXSP_CIV_BURN_BONUS");
			CaptainsAuxiliarySupport.CIVILIAN_UPKEEP_REDUCTION = getFloat("AUXSP_UPKEEP_REDUCTION");
			CaptainsAuxiliarySupport.CIVILIAN_FUEL_REDUCTION = getFloat("AUXSP_FUEL_USE_REDUCTION");
			
			//0.95
			CaptainsSpaceOperations.ACCESS = getFloat("SPOP_MARKET_ACCESS_BONUS");
			CaptainsSpaceOperations.FLEET_SIZE = getFloat("SPOP_MARKET_FLEET_SIZE_BONUS");
			CaptainsSpaceOperations.PATROLS_BONUS = getInt("SPOP_MARKET_PATROLS_BONUS");
			
			//0.95
			CaptainsPlanetaryOperations.ATTACK_BONUS = getInt("PNOP_GROUND_ATTACK_BONUS");
			CaptainsPlanetaryOperations.DEFEND_BONUS = getInt("PNOP_GROUND_DEFENSE_BONUS");
			CaptainsPlanetaryOperations.CASUALTIES_MULT = getFloat("PNOP_GROUND_CASUALTIES_MULT");
			CaptainsPlanetaryOperations.STABILITY_BONUS = getFloat("PNOP_MARKET_STABILITY_BONUS");
			CaptainsPlanetaryOperations.UPKEEP_MULT = getFloat("PNOP_MARKET_UPKEEP_MULT");
			
		} catch (RuntimeException e) {
			
		}
	}
	
	public static void loadQualityLeadershipV2() {
		//Leadership
		CaptainsTacticalDrills.TURRET_LEADING_PERC = getFloat("TD_TURRET_LEADING_PERC");
		CaptainsTacticalDrills.WEAPON_RECOIL_NEG_PERC = getFloat("TD_WEAPON_RECOIL_NEG_PERC");
		CaptainsTacticalDrills.WEAPON_ROF_BONUS = getFloat("TD_WEAPON_ROF_BONUS");
		//CaptainsTacticalDrills.WEAPON_REPAIR_BONUS = getFloat("WD_WEAPON_REPAIR_BONUS");
		CaptainsTacticalDrills.GROUND_ATTACK_BONUS = getInt("TD_GROUND_ATTACK_BONUS");
		CaptainsTacticalDrills.GROUND_DEFENSE_BONUS = getInt("TD_GROUND_DEFENSE_BONUS");
		CaptainsTacticalDrills.CASUALTIES_MULT = getFloat("TD_GROUND_CASUALTIES_MULT");
		
		//Battle nav effects in its script
		CaptainsCoordinatedManeuvers.NAV_BY_LEVELS = getBool("CONAV_PTS_FOR_LEVELS");
		CaptainsCoordinatedManeuvers.NAV_BY_SIZE = getBool("CONAV_PTS_FOR_HULLSIZE");
		CaptainsCoordinatedManeuvers.PER_FRIG = getInt("CONAV_PTS_FRIG");
		CaptainsCoordinatedManeuvers.PER_DEST = getInt("CONAV_PTS_DEST");
		CaptainsCoordinatedManeuvers.PER_CRSR = getInt("CONAV_PTS_CRSR");
		CaptainsCoordinatedManeuvers.PER_CAP = getInt("CONAV_PTS_CAP");
		CaptainsCoordinatedManeuvers.CP_REGEN_FRIGATES = getFloat("CM_REGEN_FRIG_BONUS");
		CaptainsCoordinatedManeuvers.CP_REGEN_DESTROYERS = getFloat("CM_REGEN_DEST_BONUS");
		
		//CaptainsWolfpackTactics.FLAGSHIP_CP_BONUS = getFloat("WP_FLAGSHIP_COMMAND_BONUS");
		CaptainsWolfpackTactics.PEAK_TIME_BONUS = getFloat("WP_PEAK_TIME_BONUS_FRIG");
		CaptainsWolfpackTactics.PEAK_TIME_BONUS_DEST = getFloat("WP_PEAK_TIME_BONUS_DEST");
		CaptainsWolfpackTactics.MANEUVER_BONUS = getInt("WP_MANEUVER_BONUS");
		CaptainsWolfpackTactics.SPEED_BONUS = getInt("WP_SPEED_BONUS");
		CaptainsWolfpackTactics.NFLUX_BOOST = getInt("WP_NFLUX_BOOST");
		CaptainsWolfpackTactics.DAMAGE_TO_LARGER_BONUS = getFloat("WP_DAM_BONUS_FRIG");
		CaptainsWolfpackTactics.DAMAGE_TO_LARGER_BONUS_DEST = getFloat("WP_DAM_BONUS_DEST");
		//CaptainsUsurper.USE_FULL_FLANKING = getBool("WP_ALLOW_ALL_FLANKING");
		
		CaptainsCrewTraining.CR_PERCENT = getInt("CT_CR_BONUS");
		CaptainsCrewTraining.PEAK_SECONDS = getInt("CT_PPT_BONUS");
		
		CaptainsCarrierGroup.FIGHTER_DAMAGE_REDUCTION = getFloat("ALL_FTR_DAMAGE_REDUCTION");
		CaptainsCarrierGroup.FIGHTER_REPLACEMENT_RATE_BONUS = getFloat("ALL_FTR_REPLACEMENT_RATE_BONUS");
		CaptainsCarrierGroup.OFFICER_MULT = getFloat("ALL_FTR_REPLACEMENT_RATE_OFFICER_MULT");
		
		CaptainsFighterUplink.TARGET_LEADING_BONUS = getFloat("FTRUP_TARGET_LEADING_BONUS");
		CaptainsFighterUplink.CREW_LOSS_PERCENT = getFloat("FTRUP_CREW_LOSS_REDUCTION");
		//CaptainsFighterUplink.DAMAGE_TO_FIGHTERS_BONUS = getFloat("FTRUP_PD_BONUS");
		CaptainsFighterUplink.MAX_SPEED_PERCENT = getFloat("FTRUP_MAX_SPEED_BONUS");
		
		CaptainsOfficerTraining.CP_BONUS = getInt("OT_COMPOINT_BONUS");
		CaptainsOfficerTraining.MAX_LEVEL_BONUS = getInt("OT_MAX_LEVEL_BONUS");
		CaptainsOfficerTraining.MAX_ELITE_SKILLS_BONUS = getInt("OT_MAX_ELITE_SKILLS_BONUS");
		
		CaptainsOfficerManagement.NUM_OFFICERS_BONUS = getInt("OM_NUM_OFFICERS_BONUS");
		CaptainsOfficerManagement.CP_BONUS = getInt("OM_COMPOINT_BONUS");
		CaptainsOfficerManagement.MAX_ELITE_SKILLS_BONUS = getInt("OM_MAX_ELITE_SKILLS_BONUS");
		
		CaptainsBestOfTheBest.CPR_BONUS = getFloat("BOTB_COMMAND_REC_BONUS");
		CaptainsBestOfTheBest.DEPLOYMENT_BONUS = getFloat("BOTB_BASE_DP_BONUS");
		CaptainsBestOfTheBest.EXTRA_LMODS = getInt("BOTB_LMOD_CAP_BONUS");
		CaptainsBestOfTheBest.EXTRA_SMODS = getInt("BOTB_SMOD_CAP_BONUS");
		
		//Armada Doctrine
		CaptainsSupportDoctrine.USE_HELM_BONUS = getBool("AD_SPEED_BONUS");
		CaptainsSupportDoctrine.USE_DAMCON_BONUS = getBool("AD_DAM_CONTROL_BONUS");
		CaptainsSupportDoctrine.USE_ORDNANCE_BONUS = getBool("AD_ORDINANCE_BONUS");
		CaptainsSupportDoctrine.USE_ENDURANCE_BONUS = getBool("AD_ENDURANCE_BONUS");
		CaptainsCombatEndurance.NO_OFFICER_MULT = getFloat("AD_ENDURANCE_GEN_MULT");
		CaptainsSupportDoctrine.DP_REDUCTION = getFloat("AD_DP_REDUCTION");
		CaptainsSupportDoctrine.DP_REDUCTION_MAX = getFloat("AD_DP_REDUCTION_MAX");
		CaptainsSupportDoctrine.USE_DP_REDUCTION_SCALING = getBool("AD_USE_BATTLESIZE_SCALING");

	}
	
	public static void loadQualityTechnologyV1() {
		try {
			
			//0.95
			CaptainsSpecialModifications.CAPACITORS_BONUS = getInt("SPMOD_CAPACITORS_BONUS");
			CaptainsSpecialModifications.VENTS_BONUS = getInt("SPMOD_VENTS_BONUS");
			CaptainsSpecialModifications.EXTRA_SMODS = getInt("SPMOD_EXTRA_SMODS");
			CaptainsSpecialModifications.EXTRA_LMODS = getInt("SPMOD_EXTRA_LMODS");
			CaptainsSpecialModifications.BUILD_IN_XP_BONUS = getFloat("SPMOD_BUILD_IN_XP_BONUS");
			
		} catch (RuntimeException e) {
			
		}
	}
	
	public static void loadQualityTechnologyV2() {
		//Technology
		CaptainsNavigation.TERRAIN_PENALTY_REDUCTION = getFloat("TERRAIN_PENALTY_REDUCTION");
		CaptainsNavigation.FLEET_BURN_BONUS = getFloat("FLEET_BURN_BONUS");
		CaptainsNavigation.SB_BURN_BONUS = getFloat("SB_BURN_BONUS");
		
		CaptainsSensors.DETECTED_BONUS = getFloat("DETECTED_AT_REDUCTION");
		CaptainsSensors.SENSOR_BONUS = getFloat("SENSOR_BONUS");
		CaptainsSensors.SLOW_BURN_BONUS = getFloat("SLOW_BURN_BONUS");
		CaptainsSensors.GO_DARK_MULT = getFloat("GO_DARK_PROFILE_MULT");
		
		CaptainsGunneryImplants.RANGE_BONUS = getFloat("GI_RANGE_BONUS");
		CaptainsGunneryImplants.RECOIL_BONUS = getFloat("GI_RECOIL_REDUCTION");
		CaptainsGunneryImplants.TARGET_LEADING_BONUS = getFloat("GI_TARGET_LEADING_BONUS");
		CaptainsGunneryImplants.WEAPON_HP_BONUS = getFloat("GI_WEAPON_HP_BONUS");
		
		CaptainsEnergyWeaponsMastery.ENERGY_DAMAGE_MIN_FLUX_LEVEL = getFloat("EWM_ENERGY_DAMAGE_MIN_FLUX_LEVEL");
		CaptainsEnergyWeaponsMastery.ENERGY_DAMAGE_PERCENT = getFloat("EWM_ENERGY_DAMAGE_PERCENT");
		CaptainsEnergyWeaponsMastery.MIN_RANGE = getFloat("EWM_MIN_RANGE");
		CaptainsEnergyWeaponsMastery.MAX_RANGE = getFloat("EWM_MAX_RANGE");
		CaptainsEnergyWeaponsMastery.FLUX_COST_MULT = getFloat("EWM_FLUX_COST_MULT");
		CaptainsEnergyWeaponsMastery.BEAM_DAM_BONUS = getFloat("EWM_BEAM_DAM_BONUS");
		CaptainsEnergyWeaponsMastery.BEAM_FLUX_MULT = getFloat("EWM_BEAM_FLUX_MULT");
		CaptainsEnergyWeaponsMastery.BEAM_TURN_BONUS = getFloat("EWM_BEAM_TURN_BONUS");
		CaptainsEnergyWeaponsMastery.ENERGY_AMMO_BONUS = getFloat("EWM_ENERGY_AMMO_BONUS");
		CaptainsEnergyWeaponsMastery.EMP_DAM_REDUCTION = getFloat("EWM_EMP_DAM_REDUCTION");
		
		CaptainsElectronicWarfare.ECM_FOR_SKILLS = getBool("ECM_SCORE_SKILLS");
		CaptainsElectronicWarfare.ECM_FOR_LEVELS = getBool("ECM_SCORE_OFFICER_LEVELS");
		CaptainsElectronicWarfare.ECM_FOR_HULLSIZE = getBool("ECM_SCORE_HULLSIZE");
		CaptainsElectronicWarfare.PER_FRIG = getInt("EWAR_VAL_FRIG");
		CaptainsElectronicWarfare.PER_DEST = getInt("EWAR_VAL_DEST");
		CaptainsElectronicWarfare.PER_CRSR = getInt("EWAR_VAL_CRSR");
		CaptainsElectronicWarfare.PER_CAP = getInt("EWAR_VAL_CAP");
		CaptainsElectronicWarfare.CAP_RANGE = getFloat("EWAR_CAP_BONUS_RANGE");
		CaptainsElectronicWarfare.CAP_RATE = getFloat("EWAR_CAP_BONUS_RATE");

		CaptainsFluxRegulation.DISSIPATION_PERCENT = getFloat("FLXREG_FLEET_DISSIPATION_BONUS");
		CaptainsFluxRegulation.CAPACITY_PERCENT = getFloat("FLXREG_FLEET_CAPACITOR_BONUS");
		CaptainsFluxRegulation.EXTRA_CAPS = getInt("FLXREG_CAPACITORS_BONUS");
		CaptainsFluxRegulation.EXTRA_VENTS = getInt("FLXREG_VENTS_BONUS");
		
		CaptainsPhaseCorps.USE_PPT_BONUS = getBool("PFLT_USE_PPT_BONUS");
		CaptainsPhaseCorps.USE_SPEED_BONUS = getBool("PFLT_USE_SPEED_BONUS");
		CaptainsPhaseCorps.FLUX_UPKEEP_REDUCTION = getFloat("PFLT_FLUX_UPKEEP_REDUCTION");
		CaptainsPhaseCorps.PEAK_TIME_BONUS = getFloat("PFLT_PEAK_TIME_BONUS");
		CaptainsPhaseCorps.PHASE_COOLDOWN_REDUCTION = getFloat("PFLT_COOLDOWN_REDUCTION");
		CaptainsPhaseCorps.PHASE_FIELD_BONUS_PERCENT = getFloat("PFLT_PHASE_FIELD_BONUS_SIG_REDUCTION");
		CaptainsPhaseCorps.PHASE_SHIP_SENSOR_BONUS_PERCENT = getFloat("PFLT_PHASE_FIELD_BONUS_SENSOR");
		CaptainsPhaseCorps.PHASE_SPEED_BONUS = getFloat("PFLT_PHASED_SPEED_BONUS");
		
		CaptainsCyberneticAugmentation.USE_GUNNERY_BONUS = getBool("CA_USE_GUNNERY_BONUS");
		CaptainsGunneryImplants.GEN_SKILL_MULT = getFloat("CA_GEN_GUNNERY_MULT");
		CaptainsCyberneticAugmentation.MAX_ELITE_SKILLS_BONUS = getInt("CA_MAX_ELITE_SKILLS_BONUS");
		CaptainsCyberneticAugmentation.USE_SEX_BONUS = getBool("CA_USE_SYSTEMS_BONUS");
		CaptainsSystemsExpertise.NO_OFFICER_FACTOR = getFloat("CA_SEX_MULT");
		
		//Infomorphology
		CaptainsNeuralLink.MAX_ELITE_SKILLS_BONUS = getInt("NRL_MAX_ELITE_SKILLS_BONUS");
		CaptainsNeuralLinkScript.INSTANT_TRANSFER_DP = getFloat("NRL_INSTANT_TRANSFER_PT_MAX");
		CaptainsNeuralLinkScript.TRANSFER_SECONDS_PER_DP = getFloat("NRL_TRANSFER_RATE_PER_DP");
		CaptainsNeuralLinkScript.TRANSFER_MAX_SECONDS = getFloat("NRL_TRANSFER_MAX_TIME");
		CaptainsNeuralLinkScript.ALLOW_ENGINE_CONTROL_DURING_TRANSFER = getBool("NRL_ALLOW_CONTROL_DURING_XFER");
		CaptainsNeuralLinkScript.LINK_TO_ANY = getBool("NRL_UNWISE_HULLMODLESS");
		CaptainsNeuralLinkScript.LINK_ANARCHY = getBool("NRL_REALLY_UNWISE_LINKS_UNBOUND");

		CaptainsAutomatedShips.USE_AUTOMATED_LIMITS = getBool("USE_AUTOMATED_LIMITS");
		CaptainsAutomatedShips.USE_AUTOMATED_BATTLE_SCALING = getBool("USE_AUTOMATED_LIMIT_BATTLE_SCALING");
		CaptainsAutomatedShips.USE_AUTOMATED_LEVEL_SCALING = getBool("USE_AUTOMATED_LEVEL_SCALING");
		//CaptainsAutomatedShips.USE_AUTOMATED_DYNAMIC_SCALING = getBool("USE_AUTOMATED_LIMIT_DYNAMIC_SCALING");
		BaseSkillEffectDescription.AUTOMATED_POINTS_THRESHOLD = getFloat("AUTOMATED_POINTS_THRESHOLD");
		CaptainsAutomatedShips.AUTOMATED_POINTS_THRESHOLD_FULL = getFloat("AUTOMATED_POINTS_THRESHOLD");
		CaptainsAutomatedShips.MAX_CR_BONUS = getFloat("AUTOMATED_MAX_CR");
		CaptainsAutomatedShips.THRESHOLD_DIVISOR = getFloat("AUTOMATED_POINTS_THRESHOLD_DIVISOR");
		
		AICoreOfficerPluginImpl.OMEGA_MULT = getFloat("AUTOMATED_OHM_MULT");
		AICoreOfficerPluginImpl.ALPHA_MULT = getFloat("AUTOMATED_ALPHA_MULT");
		AICoreOfficerPluginImpl.BETA_MULT = getFloat("AUTOMATED_BETA_MULT");
		AICoreOfficerPluginImpl.GAMMA_MULT = getFloat("AUTOMATED_GAMMA_MULT");
		CaptainsAutomatedShips.refreshThreshold();
		
		CaptainsHypercognition.ACCESS = getFloat("HCAI_ACCESS_BONUS");
		CaptainsHypercognition.DEFEND_BONUS = getInt("HCAI_DEFENSE_BONUS");
		CaptainsHypercognition.FLEET_SIZE = getFloat("HCAI_FLEET_BONUS");
		CaptainsHypercognition.STABILITY_BONUS = getFloat("HCAI_STAB_BONUS");
	}
	
	public static void loadQualityIndustryV1() {
		try {
			//0.95
			CaptainsReliabilityEngineering.DEGRADE_REDUCTION_PERCENT = getFloat("RE_DEGRADE_REDUCTION_PERCENT");
			CaptainsReliabilityEngineering.MAX_CR_BONUS = getFloat("RE_MAX_CR_BONUS");
			CaptainsReliabilityEngineering.PEAK_TIME_BONUS = getFloat("RE_PEAK_TIME_BONUS");
			CaptainsReliabilityEngineering.OVERLOAD_REDUCTION = getFloat("RE_OVERLOAD_REDUCTION");
			CaptainsReliabilityEngineering.CRITICAL_MALFUNCTION_REDUCTION = getFloat("RE_CRITICAL_MALFUNCTION_REDUCTION");
			CaptainsReliabilityEngineering.MALFUNCTION_REDUCTION = getFloat("RE_MALFUNCTION_REDUCTION");
			
			CaptainsColonyManagement.ADMINS = getInt("CLMNG_ADMINS_BONUS");
			CaptainsColonyManagement.ADMINS_ELITE = getInt("CLMNG_ADMINS_BONUS_ELITE");
			CaptainsColonyManagement.COLONY_NUM_BONUS = getInt("CLMNG_GOV_BONUS");
			
		} catch (RuntimeException e) {
			
		}
	}
	
	public static void loadQualityIndustryV2() {
		//Industry
		CaptainsBulkTransport.CARGO_CAPACITY_MAX_PERCENT = getFloat("BT_CARGO_CAPACITY_MAX_PERCENT");
		CaptainsBulkTransport.FUEL_CAPACITY_MAX_PERCENT = getFloat("BT_FUEL_CAPACITY_MAX_PERCENT");
		CaptainsBulkTransport.PERSONNEL_CAPACITY_MAX_PERCENT = getFloat("BT_PERSONNEL_CAPACITY_MAX_PERCENT");
		CaptainsBulkTransport.BURN_BONUS = getInt("BT_CIV_BURN_BONUS");
		
		CaptainsSalvaging.COMBAT_SALVAGE = getFloat("SALVAGE_COMBAT_BONUS");
		CaptainsSalvaging.SALVAGE_BONUS = getFloat("SALVAGE_BONUS");
		CaptainsSalvaging.SALVAGE_BONUS_RARE = getFloat("SALVAGE_RARE_BONUS");
		CaptainsSalvaging.SALVAGE_RARE = getBool("SALVAGE_RARES_TOGGLE");
		//CaptainsSalvaging.CREW_LOSS_REDUCTION = getFloat("NC_CREW_LOSS_REDUCTION");
		CaptainsSalvaging.MAX_CR = getFloat("SALV_MAX_CR");
		CaptainsSalvaging.MIN_CR = getFloat("SALV_MIN_CR");
		CaptainsSalvaging.MAX_HULL = getFloat("SALV_MAX_HULL");
		CaptainsSalvaging.MIN_HULL = getFloat("SALV_MIN_HULL");
		
		CaptainsFieldRepairs.REPAIR_RATE_BONUS = getFloat("FR_REPAIR_RATE_BONUS");
		CaptainsFieldRepairs.INSTA_REPAIR_PERCENT = getFloat("FR_AFTER_BATTLE_REPAIR_PERC");
		CaptainsFieldRepairs.MAINTENANCE_COST_REDUCTION = getFloat("FR_MAINTENANCE_REDUCTION_PERC");
		//Field repair script now grouped with Hull Restoration
		
		CaptainsOrdnanceExpertise.USE_DISCOUNT_EFFECT = getBool("OE_SWAP_TO_DISCOUNT");
		CaptainsOrdnanceExpertise.FLUX_PER_OP = getFloat("OE_BONUS_DISSIPATION_PER_OP");
		CaptainsOrdnanceExpertise.CAP_PER_OP = getFloat("OE_BONUS_CAP_PER_OP");
		CaptainsOrdnanceExpertise.FLUX_GEN_REDUCTION_PER_OP_FRIG = getFloat("OE_FLUX_GEN_REDUCTION_PER_OP_FRIG");
		CaptainsOrdnanceExpertise.FLUX_GEN_REDUCTION_PER_OP_DEST = getFloat("OE_FLUX_GEN_REDUCTION_PER_OP_DEST");
		CaptainsOrdnanceExpertise.FLUX_GEN_REDUCTION_PER_OP_CRSR = getFloat("OE_FLUX_GEN_REDUCTION_PER_OP_CRSR");
		CaptainsOrdnanceExpertise.FLUX_GEN_REDUCTION_PER_OP_CAPITAL = getFloat("OE_FLUX_GEN_REDUCTION_PER_OP_CAP");
		CaptainsOrdnanceExpertise.FLUX_GEN_REDUCTION_PER_OP = getFloat("OE_FLUX_GEN_REDUCTION_PER_OP_OTHER");
		CaptainsOrdnanceExpertise.FLUX_GEN_REDUCTION_CAP = getFloat("OE_FLUX_GEN_REDUCTION_CAP");
		
		//CaptainsPolarizedArmor.ARMOR_FRACTION_BONUS = getFloat("PA_ARMOR_FRACTION_BONUS");
		CaptainsPolarizedArmor.MIN_BONUS_PERC = getFloat("PA_MIN_APPLICATION");
		CaptainsPolarizedArmor.MIN_BONUS_SHIELDLESS_PERC = getFloat("PA_MIN_APPLICATION_SHIELDLESS");
		CaptainsPolarizedArmor.EFFECTIVE_ARMOR_BONUS = getFloat("PA_EFFECTIVE_ARMOR_BONUS_CAP");
		CaptainsPolarizedArmor.EMP_BONUS_PERCENT = getFloat("PA_EMP_REDUCTION_CAP");
		//CaptainsPolarizedArmor.HULL_DAM_REDUCTION = getFloat("PA_HULL_DAMAGE_REDUCTION");
		CaptainsPolarizedArmor.OVERLOAD_REDUCTION = getFloat("PA_OVERLOAD_DURATION_REDUCTION");
		
		CaptainsContainmentProcedures.CREW_LOSS_REDUCTION_COMBAT = getFloat("CPRO_CREW_LOSS_REDUCTION_COMBAT");
		CaptainsContainmentProcedures.CREW_LOSS_REDUCTION_NON_COMBAT = getFloat("CPRO_CREW_LOSS_REDUCTION_CAMPAIGN");
		CaptainsContainmentProcedures.FUEL_SALVAGE_BONUS = getFloat("CPRO_FUEL_SALVAGE_BONUS");
		CaptainsContainmentProcedures.FUEL_USE_REDUCTION_PERC = getInt("CPRO_FUEL_USE_REDUCTION_PERC");
		CaptainsContainmentProcedures.CORONA_EFFECT_PERC_MULT = getInt("CPRO_CORONA_EFFECT_PERC_MULT");
		
		//Jury-Rigging
		CaptainsMakeshiftEquipment.DIRECT_JUMP_DISCOUNT = getFloat("JR_DIRECT_JUMP_REDUCTION");
		CaptainsMakeshiftEquipment.SURVEY_COST_MULT_PERC = getFloat("JR_SURVEY_COST_MULT_PERC");
		CaptainsMakeshiftEquipment.DMOD_DISCOUNT_MULT = getFloat("JR_DMOD_SUPPLY_REDUCTION_MULT");
		
		CaptainsIndustrialPlanning.CUSTOM_PRODUCTION_BONUS = getFloat("MRKT_CUSTOM_PRODUCTION_BONUS");
		CaptainsIndustrialPlanning.ACCESS_BONUS = getFloat("MRKT_ACCESSIBILITY_BONUS");
		CaptainsIndustrialPlanning.SUPPLY_BONUS = getInt("MRKT_SUPPLY_BONUS");
		CaptainsIndustrialPlanning.SHIP_QUAL_BONUS = getInt("MRKT_SHIP_QUAL_BONUS");
		
		CaptainsHullRestoration.CR_PER_SMOD = getInt("HR_CR_PER_SMOD");
		CaptainsHullRestoration.DMOD_AVOID_MIN = getFloat("HR_AVOID_DMOD_CHANCE_MIN");
		CaptainsHullRestoration.DMOD_AVOID_MIN_DP = getFloat("HR_AVOID_DMOD_DP_LOW");
		CaptainsHullRestoration.DMOD_AVOID_MAX = getFloat("HR_AVOID_DMOD_CHANCE_MAX");
		CaptainsHullRestoration.DMOD_AVOID_MAX_DP = getFloat("HR_AVOID_DMOD_DP_HIGH");
		
		CaptainsFieldRepairsScript.REMOVE_DMOD_FROM_NEW_SHIPS = getBool("HRS_REMOVE_FROM_NEW");
		CaptainsFieldRepairsScript.MIN_NEW_REMOVE_PROB = getFloat("HRS_NEW_REMOVE_BASE_PROB");
		CaptainsFieldRepairsScript.NEW_REMOVE_PROB_PER_DMOD = getFloat("HRS_NEW_REMOVE_PER_DMOD_PROB");
		CaptainsFieldRepairsScript.MONTHS_PER_DMOD_REMOVAL = getFloat("MONTHS_PER_DMOD_REMOVAL");
		CaptainsFieldRepairsScript.MULT_CAPITAL = getFloat("HRS_MULT_CAPITAL");
		CaptainsFieldRepairsScript.MULT_CRUISER = getFloat("HRS_MULT_CRUISER");
		CaptainsFieldRepairsScript.MULT_DESTROYER = getFloat("HRS_MULT_DEST");
		CaptainsFieldRepairsScript.MULT_FRIGATE = getFloat("HRS_MULT_FRIG");
		CaptainsFieldRepairsScript.MULT_PHASE = getFloat("HRS_MULT_PHASE");
		CaptainsFieldRepairsScript.MULT_CIVILIAN = getFloat("HRS_MULT_CIV");
		
		//Derelict Operations
		CaptainsDerelictContingent.MAX_DMODS = getFloat("DO_MAX_DMODS_TO_COUNT");
		CaptainsDerelictContingent.SHIELDLESS_FLUX_BONUS_PERC = getInt("DO_SHIELDLESS_FLUX_BONUS_PERC");
		CaptainsDerelictContingent.DMOD_EFFECT_PERC_MULT = getFloat("DO_DMOD_EFFECT_PERC_MULT");
		CaptainsDerelictContingent.MINUS_DP_PERCENT_PER_DMOD = getFloat("DO_MINUS_DP_PERCENT_PER_DMOD");
		
	}
	
	public static void loadQualityBattleEffectsV1 () {
		CaptainsCoordinatedManeuversScript.BASE_MAXIMUM = getFloat("BATTLE_NAV_MAX");
		CaptainsCoordinatedManeuversScript.MULT_BUOY = getFloat("BATTLE_NAV_MULT_BUOY");
		CaptainsCoordinatedManeuversScript.AFFECTS_SPEED = getBool("BATTLE_NAV_APPLY_TO_SPEED");
		CaptainsCoordinatedManeuversScript.AFFECTS_MANEUVER = getBool("BATTLE_NAV_APPLY_TO_MANEUVER");
		CaptainsCoordinatedManeuversScript.APPLY_TO_SHIPS = getBool("BATTLE_NAV_SHIPS");
		CaptainsCoordinatedManeuversScript.APPLY_TO_FIGHTERS = getBool("BATTLE_NAV_FIGHTERS");
		CaptainsCoordinatedManeuversScript.APPLY_TO_MISSILES = getBool("BATTLE_NAV_MISSILES");
		CaptainsCoordinatedManeuversScript.MULT_SHIPS = getFloat("BATTLE_NAV_MULT_SHIPS");
		CaptainsCoordinatedManeuversScript.MULT_FIGHTERS = getFloat("BATTLE_NAV_MULT_FIGHTERS");
		CaptainsCoordinatedManeuversScript.MULT_MISSILES = getFloat("BATTLE_NAV_MULT_MISSILES");
		CaptainsCoordinatedManeuversScript.MULT_SPEED = getFloat("BATTLE_NAV_MULT_MISSILES");
		CaptainsCoordinatedManeuversScript.MULT_MANEUVER = getFloat("BATTLE_NAV_MULT_MISSILES");
		
		CaptainsElectronicWarfareScript.BASE_MAXIMUM = getFloat("ECM_MAX");
		CaptainsElectronicWarfareScript.JAMMER_MULT = getFloat("ECM_JAMMER_MULT");
		CaptainsElectronicWarfareScript.AUTOAIM_MULT = getFloat("ECM_AUTOAIM_MULT");
		CaptainsElectronicWarfareScript.RANGE_MULT = getFloat("ECM_RANGE_MULT");
		CaptainsElectronicWarfareScript.RECOIL_MULT = getFloat("ECM_RECOIL_MULT");
		CaptainsElectronicWarfareScript.APPLY_TO_SHIPS = getBool("ECM_SHIPS");
		CaptainsElectronicWarfareScript.APPLY_TO_FIGHTERS = getBool("ECM_FIGHTERS");
		CaptainsElectronicWarfareScript.APPLY_TO_TURRETS = getBool("ECM_TURRETS");
		CaptainsElectronicWarfareScript.APPLY_TO_MISSILES = getBool("ECM_MISSILES");
		CaptainsElectronicWarfareScript.APPLIES_TO_AUTOAIM = getBool("ECM_AUTOAIM");
		CaptainsElectronicWarfareScript.APPLIES_TO_RANGE = getBool("ECM_RANGE");
		CaptainsElectronicWarfareScript.APPLIES_TO_RECOIL = getBool("ECM_RECOIL");
		CaptainsElectronicWarfareScript.APPLIES_TO_TURRET_TURNRATE = getBool("ECM_TURRET_TURNRATE");
		CaptainsElectronicWarfareScript.APPLIES_TO_MISSILE_TURNRATE = getBool("ECM_MISSILE_TURNRATE");
	}
	
	public static void loadQualityMilestoneEffectsV1 () {
		CaptainsAcademician.SENSOR_PERC = getFloat("PERK_SENSOR_RANGE");
		CaptainsBabylon.USE_UNNERF = getBoolean("PERK_PHASE_UNNERF");
		CaptainsBabylon.SPEED_BONUS = getFloat("PERK_PHASE_SPD_BONUS");
		CaptainsUnbound.range = getFloat("PERK_GATE_RANGE");
		CaptainsUsurper.USE_AMBUSH = getBoolean("PERK_USE_AMBUSH");
		CaptainsUsurper.BONUS_CONTACTS = getInt("PERK_BONUS_CONTACTS");
		CaptainsKnight.MIN_OPPONENTS = getInt("PERK_KNT_MIN_OPPONENTS");
		CaptainsKnight.PPT_PERC = getFloat("PERK_KNT_PPT_PERC");
		CaptainsKnight.SECOND_WIND = getBoolean("PERK_KNT_SCDWIND");
		CaptainsLucifer.STABILITY_BONUS = getFloat("PERK_STABILITY");
		CaptainsPrince.BONUS_PTS = getInt("PERK_REP_BONUS");
		CaptainsPrince.MIN_PTS = getInt("PERK_REPCHANGE_MIN");
		CaptainsUnderworld.MAX_REP = getInt("PERK_PIRATES_REP_MAX");
		CaptainsUnderworld.REP_RATE = getInt("PERK_PIRATES_REP_MAG");
		CaptainsOmega.MAG = getFloat("PERK_ECM_FLAT");
		CaptainsAscendance.MAG = getFloat("PERK_NAV_FLAT");
		//CaptainsStarfarer.SP_BONUS = getInt("PERK_SP_BONUS");;
	}
	
	public static void blankMilestones() {
		try {
			Global.getSettings().loadTexture("graphics/icons/skills/blank.png");
		} catch (IOException e) {
			
		}
		for (String ID : milestoneSkills) {
			Global.getSettings().getSkillSpec(ID).setName("");
			Global.getSettings().getSkillSpec(ID).setAuthor("");
			Global.getSettings().getSkillSpec(ID).setDescription("");
			Global.getSettings().getSkillSpec(ID).setSpriteName("graphics/icons/skills/blank.png");
		}
		Global.getSettings().getSkillSpec("captains_starfarer").setName("");
		Global.getSettings().getSkillSpec("captains_starfarer").setAuthor("");
		Global.getSettings().getSkillSpec("captains_starfarer").setDescription("");
		Global.getSettings().getSkillSpec("captains_starfarer").setSpriteName("graphics/icons/skills/blank.png");
	}
	
	public static void refreshMilestones() {
		CaptainsAcademician.enableDisable();
		CaptainsBabylon.enableDisable();
		CaptainsUnbound.enableDisable();
		CaptainsUsurper.enableDisable();
		CaptainsUnderworld.enableDisable();
		CaptainsPrince.enableDisable();
		CaptainsKnight.enableDisable();
		CaptainsLucifer.enableDisable();
		CaptainsOmega.enableDisable();
		CaptainsAscendance.enableDisable();
		CaptainsStarfarer.enableDisable();
	}
	
	public static void checkMilestones() {
		MutableCharacterStatsAPI pc = Global.getSector().getPlayerStats();
		boolean allMilestones = true;
		if (QCMilestonesEnabled) {
			if (Global.getSector().getMemoryWithoutUpdate().contains("$gaKA_missionCompleted")) {
				if (Global.getSector().getPlayerStats().getSkillLevel(CaptainsAcademician.skillID) == 0) {
					IntelQCAchievedMilestone notif = new IntelQCAchievedMilestone(CaptainsAcademician.skillIcon, CaptainsAcademician.skillName);
					Global.getSector().getIntelManager().addIntel(notif);
					notif.endAfterDelay(14);
					pc.setSkillLevel(CaptainsAcademician.skillID, 1);
				}
			} else { allMilestones = false; }
			if (Global.getSector().getMemoryWithoutUpdate().contains("$defeatedZiggurat")) {
				if (Global.getSector().getPlayerStats().getSkillLevel(CaptainsBabylon.skillID) == 0) {
					IntelQCAchievedMilestone notif = new IntelQCAchievedMilestone(CaptainsBabylon.skillIcon, CaptainsBabylon.skillName);
					Global.getSector().getIntelManager().addIntel(notif);
					notif.endAfterDelay(14);
					pc.setSkillLevel(CaptainsBabylon.skillID, 1);
					}
			} else { allMilestones = false; }
			if (Global.getSector().getMemoryWithoutUpdate().contains("$gaATG_missionCompleted")) {
				if (Global.getSector().getPlayerStats().getSkillLevel(CaptainsUnbound.skillID) == 0) {
					IntelQCAchievedMilestone notif = new IntelQCAchievedMilestone(CaptainsUnbound.skillIcon, CaptainsUnbound.skillName);
					Global.getSector().getIntelManager().addIntel(notif);
					notif.endAfterDelay(14);
					pc.setSkillLevel(CaptainsUnbound.skillID, 1);
					}
			} else { allMilestones = false; }
			if (Global.getSector().getMemoryWithoutUpdate().contains("$sdtu_missionCompleted")) {
				if (Global.getSector().getPlayerStats().getSkillLevel(CaptainsUsurper.skillID) == 0) {
					IntelQCAchievedMilestone notif = new IntelQCAchievedMilestone(CaptainsUsurper.skillIcon, CaptainsUsurper.skillName);
					Global.getSector().getIntelManager().addIntel(notif);
					notif.endAfterDelay(14);
					pc.setSkillLevel(CaptainsUsurper.skillID, 1);
				}
			} else { allMilestones = false; }
			if (Global.getSector().getCharacterData().getMemoryWithoutUpdate().contains("$soe_wonDuel")) {
				if (Global.getSector().getPlayerStats().getSkillLevel(CaptainsPrince.skillID) == 0) {
					IntelQCAchievedMilestone notif = new IntelQCAchievedMilestone(CaptainsPrince.skillIcon, CaptainsPrince.skillName);
					Global.getSector().getIntelManager().addIntel(notif);
					notif.endAfterDelay(14);
					pc.setSkillLevel(CaptainsPrince.skillID, 1);
					}
			} else { allMilestones = false; }
			if (Global.getSector().getMemoryWithoutUpdate().contains("$lke_missionCompleted")) {
				if (Global.getSector().getPlayerStats().getSkillLevel(CaptainsKnight.skillID) == 0) {
					IntelQCAchievedMilestone notif = new IntelQCAchievedMilestone(CaptainsKnight.skillIcon, CaptainsKnight.skillName);
					Global.getSector().getIntelManager().addIntel(notif);
					notif.endAfterDelay(14);
					pc.setSkillLevel(CaptainsKnight.skillID, 1);
				}
			} else { allMilestones = false; }
			if (Global.getSector().getMemoryWithoutUpdate().contains("$pk_recovered")) {
				if (Global.getSector().getPlayerStats().getSkillLevel(CaptainsLucifer.skillID) == 0) {
					IntelQCAchievedMilestone notif = new IntelQCAchievedMilestone(CaptainsLucifer.skillIcon, CaptainsLucifer.skillName);
					Global.getSector().getIntelManager().addIntel(notif);
					notif.endAfterDelay(14);
					pc.setSkillLevel(CaptainsLucifer.skillID, 1);
				}
			} else { allMilestones = false; }
			if (Global.getSector().getMemoryWithoutUpdate().contains(CaptainsCombatListener.QC_DEFEATED_OMEGA)) {
				if (Global.getSector().getPlayerStats().getSkillLevel(CaptainsOmega.skillID) == 0) {
					IntelQCAchievedMilestone notif = new IntelQCAchievedMilestone(CaptainsOmega.skillIcon, CaptainsOmega.skillName);
					Global.getSector().getIntelManager().addIntel(notif);
					notif.endAfterDelay(14);
					pc.setSkillLevel(CaptainsOmega.skillID, 1);
				}
			} else { allMilestones = false; }
			if (Global.getSector().getCharacterData().getMemoryWithoutUpdate().contains("$everHadKantaProtection")) {
				if (Global.getSector().getPlayerStats().getSkillLevel(CaptainsUnderworld.skillID) == 0) {
					IntelQCAchievedMilestone notif = new IntelQCAchievedMilestone(CaptainsUnderworld.skillIcon, CaptainsUnderworld.skillName);
					Global.getSector().getIntelManager().addIntel(notif);
					notif.endAfterDelay(14);
					pc.setSkillLevel(CaptainsUnderworld.skillID, 1);
				}
			} else { allMilestones = false; }
			if (Global.getSector().getPlayerStats().getLevel() >= 15) {
				if (Global.getSector().getPlayerStats().getSkillLevel(CaptainsAscendance.skillID) == 0) {
					IntelQCAchievedMilestone notif = new IntelQCAchievedMilestone(CaptainsAscendance.skillIcon, CaptainsAscendance.skillName);
					Global.getSector().getIntelManager().addIntel(notif);
					notif.endAfterDelay(14);
					pc.setSkillLevel(CaptainsAscendance.skillID, 1);
				}
			} else { allMilestones = false; }
			//if (Global.getSector().getCharacterData().getMemoryWithoutUpdate().contains("$IndEvo_HamsterMurderer") && (Global.getSector().getPlayerStats().getSkillLevel(CaptainsAcademician.skillID) == 0) {pc.setSkillLevel(CaptainsIndEvoHamsterHitler.skillID, 1);
			if (allMilestones) {
				if (Global.getSector().getPlayerStats().getSkillLevel(CaptainsStarfarer.skillID) == 0) {
					IntelQCAchievedMilestone notif = new IntelQCAchievedMilestone(CaptainsStarfarer.skillIcon, CaptainsStarfarer.skillName);
					Global.getSector().getIntelManager().addIntel(notif);
					notif.endAfterDelay(14);
					pc.setSkillLevel(CaptainsStarfarer.skillID, 1);
				}
			}
		} else {
			for (String ID : milestoneSkills) {
				pc.setSkillLevel(ID, 0);
			}
			pc.setSkillLevel(CaptainsStarfarer.skillID, 0);
		}
		refreshMilestones();
	}
	
	public static void debugAwardMilestones() {
		Global.getSector().getMemoryWithoutUpdate().set("$gaKA_missionCompleted", true);
		Global.getSector().getMemoryWithoutUpdate().set("$defeatedZiggurat", true);
		Global.getSector().getMemoryWithoutUpdate().set("$gaATG_missionCompleted", true);
		Global.getSector().getMemoryWithoutUpdate().set("$sdtu_missionCompleted", true);
		Global.getSector().getMemoryWithoutUpdate().set("$soe_WonDuel", true);
		Global.getSector().getMemoryWithoutUpdate().set("$lke_completed", true);
		Global.getSector().getMemoryWithoutUpdate().set("$pk_recovered", true);
		Global.getSector().getMemoryWithoutUpdate().set(CaptainsCombatListener.QC_DEFEATED_OMEGA, true);
		Global.getSector().getCharacterData().getMemoryWithoutUpdate().set("$everHadKantaProtection",true);
		//Global.getSector().getCharacterData().getMemoryWithoutUpdate().set("$IndEvo_HamsterMurderer",true);
	}
	
	//Other mod stuff
	public static void loadNex() {
		CaptainsNexSkills.BONUS_AGENTS = getInt("NEX_BONUS_AGENTS");
	}
	public static void loadVSP() {
		//CaptainsVayraSkills.??? = getSettingsFloat("");
	}
	
	@SuppressWarnings("unused")
	public static void handleOverrides(boolean hasRealisticCombat) {
		if (false) {
			CaptainsBallisticMastery.loadStock = true;
			CaptainsDamageControl.loadStock = true;
			CaptainsFighterUplink.loadStock = true;
			CaptainsEnergyWeaponsMastery.loadStock = true;
			CaptainsFieldModulation.loadStock = true;
			CaptainsGunneryImplants.loadStock = true;
			CaptainsImpactMitigation.loadStock = true;
			CaptainsMissileSpecialization.loadStock = true;
			CaptainsPointDefense.loadStock = true;
			CaptainsPolarizedArmor.loadStock = true;
			CaptainsTacticalDrills.loadStock = true;
			CaptainsTargetAnalysis.loadStock = true;
			CaptainsWolfpackTactics.loadStock = true;
		}
	}
}

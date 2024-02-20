package dal.plugins;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.impl.campaign.AICoreOfficerPluginImpl;
import com.fs.starfarer.api.impl.campaign.skills.BaseSkillEffectDescription;

import dal.impl.campaign.skills.CaptainsAutomatedShips;
import dal.impl.campaign.skills.CaptainsBallisticMastery;
import dal.impl.campaign.skills.CaptainsBestOfTheBest;
import dal.impl.campaign.skills.CaptainsBulkTransport;
import dal.impl.campaign.skills.CaptainsCarrierGroup;
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
import dal.impl.campaign.skills.CaptainsPointDefense;
import dal.impl.campaign.skills.CaptainsPolarizedArmor;
import dal.impl.campaign.skills.CaptainsSalvaging;
import dal.impl.campaign.skills.CaptainsSensors;
import dal.impl.campaign.skills.CaptainsSupportDoctrine;
import dal.impl.campaign.skills.CaptainsSystemsExpertise;
import dal.impl.campaign.skills.CaptainsTacticalDrills;
import dal.impl.campaign.skills.CaptainsTargetAnalysis;
import dal.impl.campaign.skills.CaptainsWolfpackTactics;
import dal.impl.campaign.skills.milestones.CaptainsAcademician;
import dal.impl.campaign.skills.milestones.CaptainsAscendance;
import dal.impl.campaign.skills.milestones.CaptainsBabylon;
import dal.impl.campaign.skills.milestones.CaptainsKnight;
import dal.impl.campaign.skills.milestones.CaptainsLucifer;
import dal.impl.campaign.skills.milestones.CaptainsOmega;
import dal.impl.campaign.skills.milestones.CaptainsPrince;
import dal.impl.campaign.skills.milestones.CaptainsUnbound;
import dal.impl.campaign.skills.milestones.CaptainsUnderworld;
import dal.impl.campaign.skills.milestones.CaptainsUsurper;
import lunalib.lunaSettings.LunaSettings;
import lunalib.lunaSettings.LunaSettingsListener;

public class qcDynamicSettings implements LunaSettingsListener {
	
	protected void reloadQualityConfig() {
		Captains_Utils.QCInstalled = LunaSettings.getBoolean("QualityCaptains", "qc_installed");
		boolean QCCustomBattleEffects = LunaSettings.getBoolean("QualityCaptains", "qc_useCustomBattleEffects");
		Captains_Utils.QCMilestonesEnabled = LunaSettings.getBoolean("QualityCaptains", "qc_useMilestones");
		
		if (Captains_Utils.QCInstalled) {
			if (Global.getSector() != null) Captains_Utils.installQC();
			reloadQualityCombat();
			reloadQualityLeadership();
			reloadQualityTechnology();
			reloadQualityIndustry();
			if (QCCustomBattleEffects) {
				reloadQualityBattleEffects();
			}
			if (Captains_Utils.QCMilestonesEnabled) {
				reloadQualityMilestoneEffects();
			}
			//Mod support
			if(Global.getSettings().getModManager().isModEnabled("nexerelin")) {
				reloadQualityNex();
			}
			if(Global.getSettings().getModManager().isModEnabled("vayrashippack")) {
				reloadQualityVSP();
			}
			if(Global.getSettings().getModManager().isModEnabled("RealisticCombat")) {
				reloadQualityRealisticCombat();
			}
		} else {
			if (Global.getSector() != null) Captains_Utils.uninstallQC();
		}
		
	}
	
	protected void reloadQualityCombat() {
		//Combat
		CaptainsHelmsmanship.MANEUVERABILITY_BONUS = LunaSettings.getFloat("QualityCaptains", "HELM_MANEUVERABILITY_BONUS");
		CaptainsHelmsmanship.SPEED_BONUS = LunaSettings.getFloat("QualityCaptains", "HELM_SPEED_BONUS");
		CaptainsHelmsmanship.ZERO_FLUX_LEVEL = LunaSettings.getFloat("QualityCaptains", "HELM_FLUX_THRESHOLD");
		CaptainsHelmsmanship.USE_FLUX_NO_USE = LunaSettings.getBoolean("QualityCaptains", "HELM_0FLUXBOOST_ANYDECAYING_ENABLED");
		CaptainsHelmsmanship.USE_FLUX_THRESHOLD = LunaSettings.getBoolean("QualityCaptains", "HELM_0FLUXBOOST_THRESHOLD_ENABLED");
		CaptainsHelmsmanship.ELITE_SPEED_BONUS_FLAT = LunaSettings.getInt("QualityCaptains", "HELM_FLAT_SPEED_BONUS");
		
		CaptainsCombatEndurance.PEAK_TIME_BONUS = LunaSettings.getInt("QualityCaptains", "CE_PPT_BONUS");
		CaptainsCombatEndurance.MAX_CR_BONUS = LunaSettings.getFloat("QualityCaptains", "CE_MAX_CR_BONUS");
		CaptainsCombatEndurance.MALFUNCTION_REDUCTION = LunaSettings.getFloat("QualityCaptains", "CE_MALFUNCTION_REDUCTION");
		CaptainsCombatEndurance.DEGRADE_REDUCTION_PERCENT = LunaSettings.getFloat("QualityCaptains", "CE_DEGRADATION_RATE_REDUCTION");
		CaptainsCombatEndurance.MAX_REGEN_LEVEL = LunaSettings.getFloat("QualityCaptains", "CE_BEGIN_REPAIR_BELOW");
		CaptainsCombatEndurance.TOTAL_REGEN_MAX_POINTS = LunaSettings.getInt("QualityCaptains", "CE_HP_REPAIR_LIMIT");
		CaptainsCombatEndurance.REGEN_RATE = LunaSettings.getFloat("QualityCaptains", "CE_HP_REPAIR_RATE");
		
		CaptainsImpactMitigation.ARMOR_BONUS = LunaSettings.getFloat("QualityCaptains", "IM_ARMOR_BONUS");
		CaptainsImpactMitigation.ARMOR_DAMAGE_REDUCTION = LunaSettings.getFloat("QualityCaptains", "IM_ARMOR_DAMAGE_REDUCTION_BONUS");
		//CaptainsImpactMitigation.DAMAGE_TO_COMPONENTS_REDUCTION = LunaSettings.getFloat("QualityCaptains", "IM_DAMAGE_TO_COMPONENTS_REDUCTION");
		CaptainsImpactMitigation.ARMOR_EFFECTIVE_BONUS = LunaSettings.getFloat("QualityCaptains", "IM_ARMOR_EFFECTIVE_BONUS");
		CaptainsImpactMitigation.MAX_DAMAGE_REDUCTION_BONUS = LunaSettings.getFloat("QualityCaptains", "IM_MAX_DAMAGE_REDUCTION_BONUS");
		CaptainsImpactMitigation.MIN_ARMOR_FRACTION_BONUS = LunaSettings.getFloat("QualityCaptains", "IM_MIN_ARMOR_FRACTION_BONUS");
		
		CaptainsDamageControl.HULL_DAMAGE_REDUCTION = LunaSettings.getFloat("QualityCaptains", "DC_HULL_DAMAGE_REDUCTION");
		CaptainsDamageControl.CREW_LOSS_REDUCTION = LunaSettings.getFloat("QualityCaptains", "DC_CREW_LOSS_REDUCTION");
		//CaptainsDamageControl.INSTA_REPAIR_PERC = LunaSettings.getInt("QualityCaptains", "DC_SHIP_INSTA_REPAIR");
		CaptainsDamageControl.COMPONENT_REPAIR_BONUS = LunaSettings.getFloat("QualityCaptains", "DC_COMPONENT_REPAIR_BONUS");
		CaptainsDamageControl.COMPONENT_DAMAGE_TAKEN = LunaSettings.getFloat("QualityCaptains", "DC_COMPONENT_DAMAGE_TAKEN_REDUCTION");
		//CaptainsDamageControl.OVERLOAD_REDUCTION = LunaSettings.getFloat("QualityCaptains", "DC_OVERLOAD_REDUCTION");
		CaptainsDamageControl.USE_HIT_NULL = LunaSettings.getBoolean("QualityCaptains", "DC_USE_HIT_NULL");
		CaptainsDamageControl.HIT_NULL_MIN = LunaSettings.getFloat("QualityCaptains", "DC_HIT_NULL_MIN");
		CaptainsDamageControl.HIT_NULL_COOLDOWN = LunaSettings.getFloat("QualityCaptains", "DC_HIT_NULL_COOLDOWN");
		CaptainsDamageControl.HIT_NULL_PERC = LunaSettings.getFloat("QualityCaptains", "DC_HIT_NULL_PERC");
		
		CaptainsFieldModulation.PHASE_FLUX_UPKEEP_REDUCTION = LunaSettings.getFloat("QualityCaptains", "FM_PHASE_UPKEEP_REDUCTION");
		CaptainsFieldModulation.FLUX_SHUNT_DISSIPATION = LunaSettings.getFloat("QualityCaptains", "FM_SHIELD_HFLUX_DISSIPATION_RATE");
		CaptainsFieldModulation.SHIELD_DAMAGE_REDUCTION = LunaSettings.getFloat("QualityCaptains", "FM_DAMAGE_TO_SHIELD_REDUCTION");
		CaptainsFieldModulation.SHIELD_RATE_BONUS = LunaSettings.getFloat("QualityCaptains", "FM_SHIELD_RATES_BONUS");
		CaptainsFieldModulation.PHASE_FLUX_THRESHOLD_BONUS = LunaSettings.getFloat("QualityCaptains", "FM_PHASE_FLUX_THRESHOLD_BONUS");
		CaptainsFieldModulation.PHASE_COOLDOWN_REDUCTION = LunaSettings.getFloat("QualityCaptains", "FM_PHASE_COOLDOWN_REDUCTION");
		
		CaptainsPointDefense.FIGHTER_DAMAGE_BONUS = LunaSettings.getFloat("QualityCaptains", "PD_FIGHTER_DAMAGE_BONUS");
		CaptainsPointDefense.MISSILE_DAMAGE_BONUS = LunaSettings.getFloat("QualityCaptains", "PD_MISSILE_DAMAGE_BONUS");
		CaptainsPointDefense.TURRET_TURN_BONUS = LunaSettings.getFloat("QualityCaptains", "PD_TURRET_TURN_BONUS");
		CaptainsPointDefense.FIGHTER_DAMAGE_BONUS_FTR_MULT = LunaSettings.getFloat("QualityCaptains", "PD_FTR_ANTIFIGHTER_BONUS_MULT");
		CaptainsPointDefense.MISSILE_DAMAGE_BONUS_FTR_MULT = LunaSettings.getFloat("QualityCaptains", "PD_FTR_ANTIMISSILE_BONUS_MULT");
		CaptainsPointDefense.PD_RANGE_BONUS_FLAT = LunaSettings.getFloat("QualityCaptains", "PD_RANGE_BONUS_FLAT");
		CaptainsPointDefense.PD_RANGE_BONUS_PERC = LunaSettings.getFloat("QualityCaptains", "PD_RANGE_BONUS_PERC");
		//CaptainsPointDefense.PD_RANGE_BONUS_FTR_MULT = LunaSettings.getFloat("QualityCaptains", "PD_RANGE_FTR_BONUS_MULT");

		CaptainsTargetAnalysis.DAMAGE_TO_FRIGATES = LunaSettings.getFloat("QualityCaptains", "TA_DAM_TO_FRIG_BONUS");
		CaptainsTargetAnalysis.DAMAGE_TO_DESTROYERS = LunaSettings.getFloat("QualityCaptains", "TA_DAM_TO_DEST_BONUS");
		CaptainsTargetAnalysis.DAMAGE_TO_CRUISERS = LunaSettings.getFloat("QualityCaptains", "TA_DAM_TO_CRUISER_BONUS");
		CaptainsTargetAnalysis.DAMAGE_TO_CAPITALS = LunaSettings.getFloat("QualityCaptains", "TA_DAM_TO_CAP_BONUS");
		CaptainsTargetAnalysis.DAMAGE_TO_COMPONENTS_BONUS = LunaSettings.getFloat("QualityCaptains", "TA_DAM_TO_COMP_BONUS");
		CaptainsTargetAnalysis.HIT_STRENGTH_BONUS = LunaSettings.getFloat("QualityCaptains", "TA_ARMOR_BREACH_BONUS");
		
		//Fire Control
		CaptainsBallisticMastery.PROJ_SPEED_BONUS = LunaSettings.getFloat("QualityCaptains", "FC_PROJ_SPEED_BONUS");
		CaptainsBallisticMastery.RANGE_BONUS = LunaSettings.getFloat("QualityCaptains", "FC_RANGE_BONUS");
		CaptainsBallisticMastery.DAMAGE_TO_SHIELDS_BONUS = LunaSettings.getFloat("QualityCaptains", "FC_DAMAGE_TO_SHIELD_BONUS");
		
		CaptainsSystemsExpertise.CHARGES_PERCENT = LunaSettings.getFloat("QualityCaptains", "SE_CHARGES_PERCENT");
		CaptainsSystemsExpertise.CHARGES_BONUS = LunaSettings.getInt("QualityCaptains", "SE_CHARGES_FLAT");
		CaptainsSystemsExpertise.REGEN_PERCENT = LunaSettings.getFloat("QualityCaptains", "SE_REGEN_PERCENT");
		CaptainsSystemsExpertise.RANGE_PERCENT = LunaSettings.getFloat("QualityCaptains", "SE_RANGE_PERCENT");
		CaptainsSystemsExpertise.SYSTEM_COOLDOWN_REDUCTION_PERCENT = LunaSettings.getFloat("QualityCaptains", "SE_COOLDOWN_REDUCTION");
		CaptainsSystemsExpertise.NO_OFFICER_FACTOR = LunaSettings.getFloat("QualityCaptains", "SE_NO_OFFICER_MULT");
		
		CaptainsMissileSpecialization.MISSILE_AMMO_BONUS_1 = LunaSettings.getFloat("QualityCaptains", "MS_AMMO_BONUS_1");
		CaptainsMissileSpecialization.MISSILE_AMMO_BONUS_2 = LunaSettings.getFloat("QualityCaptains", "MS_AMMO_BONUS_2");
		CaptainsMissileSpecialization.MISSILE_SPEC_HEALTH_BONUS = LunaSettings.getFloat("QualityCaptains", "MS_HEALTH_BONUS");
		//CaptainsMissileSpecialization.MISSILE_SPEC_DAMAGE_BONUS = LunaSettings.getFloat("QualityCaptains", "MS_DAMAGE_BONUS");
		CaptainsMissileSpecialization.MISSILE_SPEC_ROF_BONUS = LunaSettings.getFloat("QualityCaptains", "MS_ROF_BONUS");
		CaptainsMissileSpecialization.MISSILE_SPEC_SPEED_BONUS = LunaSettings.getFloat("QualityCaptains", "MS_SPEED_BONUS");
		CaptainsMissileSpecialization.MISSILE_SPEC_RANGE_MULT = LunaSettings.getFloat("QualityCaptains", "MS_RANGE_MULT");
		CaptainsMissileSpecialization.MISSILE_TURN_ACCEL_BONUS = LunaSettings.getFloat("QualityCaptains", "MS_TURN_ACCEL_BONUS");
		CaptainsMissileSpecialization.MISSILE_TURN_RATE_BONUS = LunaSettings.getFloat("QualityCaptains", "MS_TURN_RATE_BONUS");
		CaptainsMissileSpecialization.MISSILE_SPEC_ACCEL_BONUS = LunaSettings.getFloat("QualityCaptains", "MS_ACCEL_BONUS");
	}
	
	protected void reloadQualityLeadership() {
		//Leadership
		CaptainsTacticalDrills.TURRET_LEADING_PERC = LunaSettings.getFloat("QualityCaptains", "TD_TURRET_LEADING_PERC");
		CaptainsTacticalDrills.WEAPON_RECOIL_NEG_PERC = LunaSettings.getFloat("QualityCaptains", "TD_WEAPON_RECOIL_NEG_PERC");
		CaptainsTacticalDrills.WEAPON_ROF_BONUS = LunaSettings.getFloat("QualityCaptains", "TD_WEAPON_ROF_BONUS");
		//CaptainsTacticalDrills.WEAPON_REPAIR_BONUS = LunaSettings.getFloat("QualityCaptains", "WD_WEAPON_REPAIR_BONUS");
		CaptainsTacticalDrills.GROUND_ATTACK_BONUS = LunaSettings.getInt("QualityCaptains", "TD_GROUND_ATTACK_BONUS");
		CaptainsTacticalDrills.GROUND_DEFENSE_BONUS = LunaSettings.getInt("QualityCaptains", "TD_GROUND_DEFENSE_BONUS");
		CaptainsTacticalDrills.CASUALTIES_MULT = LunaSettings.getFloat("QualityCaptains", "TD_GROUND_CASUALTIES_MULT");
		
		//Battle nav effects in its script
		CaptainsCoordinatedManeuvers.NAV_BY_LEVELS = LunaSettings.getBoolean("QualityCaptains", "CONAV_PTS_FOR_LEVELS");
		CaptainsCoordinatedManeuvers.NAV_BY_SIZE = LunaSettings.getBoolean("QualityCaptains", "CONAV_PTS_FOR_HULLSIZE");
		CaptainsCoordinatedManeuvers.PER_FRIG = LunaSettings.getInt("QualityCaptains", "CONAV_PTS_FRIG");
		CaptainsCoordinatedManeuvers.PER_DEST = LunaSettings.getInt("QualityCaptains", "CONAV_PTS_DEST");
		CaptainsCoordinatedManeuvers.PER_CRSR = LunaSettings.getInt("QualityCaptains", "CONAV_PTS_CRSR");
		CaptainsCoordinatedManeuvers.PER_CAP = LunaSettings.getInt("QualityCaptains", "CONAV_PTS_CAP");
		CaptainsCoordinatedManeuvers.CP_REGEN_FRIGATES = LunaSettings.getFloat("QualityCaptains", "CM_REGEN_FRIG_BONUS");
		CaptainsCoordinatedManeuvers.CP_REGEN_DESTROYERS = LunaSettings.getFloat("QualityCaptains", "CM_REGEN_DEST_BONUS");
		
		//CaptainsWolfpackTactics.FLAGSHIP_CP_BONUS = LunaSettings.getFloat("QualityCaptains", "WP_FLAGSHIP_COMMAND_BONUS");
		CaptainsWolfpackTactics.PEAK_TIME_BONUS = LunaSettings.getFloat("QualityCaptains", "WP_PEAK_TIME_BONUS_FRIG");
		CaptainsWolfpackTactics.PEAK_TIME_BONUS_DEST = LunaSettings.getFloat("QualityCaptains", "WP_PEAK_TIME_BONUS_DEST");
		CaptainsWolfpackTactics.MANEUVER_BONUS = LunaSettings.getInt("QualityCaptains", "WP_MANEUVER_BONUS");
		CaptainsWolfpackTactics.SPEED_BONUS = LunaSettings.getInt("QualityCaptains", "WP_SPEED_BONUS");
		CaptainsWolfpackTactics.NFLUX_BOOST = LunaSettings.getInt("QualityCaptains", "WP_NFLUX_BOOST");
		CaptainsWolfpackTactics.DAMAGE_TO_LARGER_BONUS = LunaSettings.getFloat("QualityCaptains", "WP_DAM_BONUS_FRIG");
		CaptainsWolfpackTactics.DAMAGE_TO_LARGER_BONUS_DEST = LunaSettings.getFloat("QualityCaptains", "WP_DAM_BONUS_DEST");
		//CaptainsUsurper.USE_FULL_FLANKING = LunaSettings.getBoolean("QualityCaptains", "WP_ALLOW_ALL_FLANKING");
		
		CaptainsCrewTraining.CR_PERCENT = LunaSettings.getInt("QualityCaptains", "CT_CR_BONUS");
		CaptainsCrewTraining.PEAK_SECONDS = LunaSettings.getInt("QualityCaptains", "CT_PPT_BONUS");
		
		CaptainsCarrierGroup.FIGHTER_DAMAGE_REDUCTION = LunaSettings.getFloat("QualityCaptains", "ALL_FTR_DAMAGE_REDUCTION");
		CaptainsCarrierGroup.FIGHTER_REPLACEMENT_RATE_BONUS = LunaSettings.getFloat("QualityCaptains", "ALL_FTR_REPLACEMENT_RATE_BONUS");
		CaptainsCarrierGroup.OFFICER_MULT = LunaSettings.getFloat("QualityCaptains", "ALL_FTR_REPLACEMENT_RATE_OFFICER_MULT");
		
		CaptainsFighterUplink.TARGET_LEADING_BONUS = LunaSettings.getFloat("QualityCaptains", "FTRUP_TARGET_LEADING_BONUS");
		CaptainsFighterUplink.CREW_LOSS_PERCENT = LunaSettings.getFloat("QualityCaptains", "FTRUP_CREW_LOSS_REDUCTION");
		//CaptainsFighterUplink.DAMAGE_TO_FIGHTERS_BONUS = LunaSettings.getFloat("QualityCaptains", "FTRUP_PD_BONUS");
		CaptainsFighterUplink.MAX_SPEED_PERCENT = LunaSettings.getFloat("QualityCaptains", "FTRUP_MAX_SPEED_BONUS");
		
		CaptainsOfficerTraining.CP_BONUS = LunaSettings.getInt("QualityCaptains", "OT_COMPOINT_BONUS");
		CaptainsOfficerTraining.MAX_LEVEL_BONUS = LunaSettings.getInt("QualityCaptains", "OT_MAX_LEVEL_BONUS");
		CaptainsOfficerTraining.MAX_ELITE_SKILLS_BONUS = LunaSettings.getInt("QualityCaptains", "OT_MAX_ELITE_SKILLS_BONUS");
		
		CaptainsOfficerManagement.NUM_OFFICERS_BONUS = LunaSettings.getInt("QualityCaptains", "OM_NUM_OFFICERS_BONUS");
		CaptainsOfficerManagement.CP_BONUS = LunaSettings.getInt("QualityCaptains", "OM_COMPOINT_BONUS");
		CaptainsOfficerManagement.MAX_ELITE_SKILLS_BONUS = LunaSettings.getInt("QualityCaptains", "OM_MAX_ELITE_SKILLS_BONUS");
		
		CaptainsBestOfTheBest.CPR_BONUS = LunaSettings.getFloat("QualityCaptains", "BOTB_COMMAND_REC_BONUS");
		CaptainsBestOfTheBest.DEPLOYMENT_BONUS = LunaSettings.getFloat("QualityCaptains", "BOTB_BASE_DP_BONUS");
		CaptainsBestOfTheBest.EXTRA_LMODS = LunaSettings.getInt("QualityCaptains", "BOTB_LMOD_CAP_BONUS");
		CaptainsBestOfTheBest.EXTRA_SMODS = LunaSettings.getInt("QualityCaptains", "BOTB_SMOD_CAP_BONUS");
		
		//Armada Doctrine
		CaptainsSupportDoctrine.USE_HELM_BONUS = LunaSettings.getBoolean("QualityCaptains", "AD_SPEED_BONUS");
		CaptainsSupportDoctrine.USE_DAMCON_BONUS = LunaSettings.getBoolean("QualityCaptains", "AD_DAM_CONTROL_BONUS");
		CaptainsSupportDoctrine.USE_ORDNANCE_BONUS = LunaSettings.getBoolean("QualityCaptains", "AD_ORDINANCE_BONUS");
		CaptainsSupportDoctrine.USE_ENDURANCE_BONUS = LunaSettings.getBoolean("QualityCaptains", "AD_ENDURANCE_BONUS");
		CaptainsCombatEndurance.NO_OFFICER_MULT = LunaSettings.getFloat("QualityCaptains", "AD_ENDURANCE_GEN_MULT");
		CaptainsSupportDoctrine.DP_REDUCTION = LunaSettings.getFloat("QualityCaptains", "AD_DP_REDUCTION");
		CaptainsSupportDoctrine.DP_REDUCTION_MAX = LunaSettings.getFloat("QualityCaptains", "AD_DP_REDUCTION_MAX");
		CaptainsSupportDoctrine.USE_DP_REDUCTION_SCALING = LunaSettings.getBoolean("QualityCaptains", "AD_USE_BATTLESIZE_SCALING");
	}
	
	protected void reloadQualityTechnology() {
		//Technology
		CaptainsNavigation.TERRAIN_PENALTY_REDUCTION = LunaSettings.getFloat("QualityCaptains", "TERRAIN_PENALTY_REDUCTION");
		CaptainsNavigation.FLEET_BURN_BONUS = LunaSettings.getFloat("QualityCaptains", "FLEET_BURN_BONUS");
		CaptainsNavigation.SB_BURN_BONUS = LunaSettings.getFloat("QualityCaptains", "SB_BURN_BONUS");
		
		CaptainsSensors.DETECTED_BONUS = LunaSettings.getFloat("QualityCaptains", "DETECTED_AT_REDUCTION");
		CaptainsSensors.SENSOR_BONUS = LunaSettings.getFloat("QualityCaptains", "SENSOR_BONUS");
		CaptainsSensors.SLOW_BURN_BONUS = LunaSettings.getFloat("QualityCaptains", "SLOW_BURN_BONUS");
		CaptainsSensors.GO_DARK_MULT = LunaSettings.getFloat("QualityCaptains", "GO_DARK_PROFILE_MULT");
		
		CaptainsGunneryImplants.RANGE_BONUS = LunaSettings.getFloat("QualityCaptains", "GI_RANGE_BONUS");
		CaptainsGunneryImplants.RECOIL_BONUS = LunaSettings.getFloat("QualityCaptains", "GI_RECOIL_REDUCTION");
		CaptainsGunneryImplants.TARGET_LEADING_BONUS = LunaSettings.getFloat("QualityCaptains", "GI_TARGET_LEADING_BONUS");
		CaptainsGunneryImplants.WEAPON_HP_BONUS = LunaSettings.getFloat("QualityCaptains", "GI_WEAPON_HP_BONUS");
		
		CaptainsEnergyWeaponsMastery.ENERGY_DAMAGE_MIN_FLUX_LEVEL = LunaSettings.getFloat("QualityCaptains", "EWM_ENERGY_DAMAGE_MIN_FLUX_LEVEL");
		CaptainsEnergyWeaponsMastery.ENERGY_DAMAGE_PERCENT = LunaSettings.getFloat("QualityCaptains", "EWM_ENERGY_DAMAGE_PERCENT");
		CaptainsEnergyWeaponsMastery.MIN_RANGE = LunaSettings.getFloat("QualityCaptains", "EWM_MIN_RANGE");
		CaptainsEnergyWeaponsMastery.MAX_RANGE = LunaSettings.getFloat("QualityCaptains", "EWM_MAX_RANGE");
		CaptainsEnergyWeaponsMastery.FLUX_COST_MULT = LunaSettings.getFloat("QualityCaptains", "EWM_FLUX_COST_MULT");
		CaptainsEnergyWeaponsMastery.BEAM_DAM_BONUS = LunaSettings.getFloat("QualityCaptains", "EWM_BEAM_DAM_BONUS");
		CaptainsEnergyWeaponsMastery.BEAM_FLUX_MULT = LunaSettings.getFloat("QualityCaptains", "EWM_BEAM_FLUX_MULT");
		CaptainsEnergyWeaponsMastery.BEAM_TURN_BONUS = LunaSettings.getFloat("QualityCaptains", "EWM_BEAM_TURN_BONUS");
		CaptainsEnergyWeaponsMastery.ENERGY_AMMO_BONUS = LunaSettings.getFloat("QualityCaptains", "EWM_ENERGY_AMMO_BONUS");
		CaptainsEnergyWeaponsMastery.EMP_DAM_REDUCTION = LunaSettings.getFloat("QualityCaptains", "EWM_EMP_DAM_REDUCTION");
		
		CaptainsElectronicWarfare.ECM_FOR_SKILLS = LunaSettings.getBoolean("QualityCaptains", "ECM_SCORE_SKILLS");
		CaptainsElectronicWarfare.ECM_FOR_LEVELS = LunaSettings.getBoolean("QualityCaptains", "ECM_SCORE_OFFICER_LEVELS");
		CaptainsElectronicWarfare.ECM_FOR_HULLSIZE = LunaSettings.getBoolean("QualityCaptains", "ECM_SCORE_HULLSIZE");
		CaptainsElectronicWarfare.PER_FRIG = LunaSettings.getInt("QualityCaptains", "EWAR_VAL_FRIG");
		CaptainsElectronicWarfare.PER_DEST = LunaSettings.getInt("QualityCaptains", "EWAR_VAL_DEST");
		CaptainsElectronicWarfare.PER_CRSR = LunaSettings.getInt("QualityCaptains", "EWAR_VAL_CRSR");
		CaptainsElectronicWarfare.PER_CAP = LunaSettings.getInt("QualityCaptains", "EWAR_VAL_CAP");
		CaptainsElectronicWarfare.CAP_RANGE = LunaSettings.getFloat("QualityCaptains", "EWAR_CAP_BONUS_RANGE");
		CaptainsElectronicWarfare.CAP_RATE = LunaSettings.getFloat("QualityCaptains", "EWAR_CAP_BONUS_RATE");

		CaptainsFluxRegulation.DISSIPATION_PERCENT = LunaSettings.getFloat("QualityCaptains", "FLXREG_FLEET_DISSIPATION_BONUS");
		CaptainsFluxRegulation.CAPACITY_PERCENT = LunaSettings.getFloat("QualityCaptains", "FLXREG_FLEET_CAPACITOR_BONUS");
		CaptainsFluxRegulation.EXTRA_CAPS = LunaSettings.getInt("QualityCaptains", "FLXREG_CAPACITORS_BONUS");
		CaptainsFluxRegulation.EXTRA_VENTS = LunaSettings.getInt("QualityCaptains", "FLXREG_VENTS_BONUS");
		
		CaptainsPhaseCorps.USE_PPT_BONUS = LunaSettings.getBoolean("QualityCaptains", "PFLT_USE_PPT_BONUS");
		CaptainsPhaseCorps.USE_SPEED_BONUS = LunaSettings.getBoolean("QualityCaptains", "PFLT_USE_SPEED_BONUS");
		CaptainsPhaseCorps.FLUX_UPKEEP_REDUCTION = LunaSettings.getFloat("QualityCaptains", "PFLT_FLUX_UPKEEP_REDUCTION");
		CaptainsPhaseCorps.PEAK_TIME_BONUS = LunaSettings.getFloat("QualityCaptains", "PFLT_PEAK_TIME_BONUS");
		CaptainsPhaseCorps.PHASE_COOLDOWN_REDUCTION = LunaSettings.getFloat("QualityCaptains", "PFLT_COOLDOWN_REDUCTION");
		CaptainsPhaseCorps.PHASE_FIELD_BONUS_PERCENT = LunaSettings.getFloat("QualityCaptains", "PFLT_PHASE_FIELD_BONUS_SIG_REDUCTION");
		CaptainsPhaseCorps.PHASE_SHIP_SENSOR_BONUS_PERCENT = LunaSettings.getFloat("QualityCaptains", "PFLT_PHASE_FIELD_BONUS_SENSOR");
		CaptainsPhaseCorps.PHASE_SPEED_BONUS = LunaSettings.getFloat("QualityCaptains", "PFLT_PHASED_SPEED_BONUS");
		
		CaptainsCyberneticAugmentation.USE_GUNNERY_BONUS = LunaSettings.getBoolean("QualityCaptains", "CA_USE_GUNNERY_BONUS");
		CaptainsGunneryImplants.GEN_SKILL_MULT = LunaSettings.getFloat("QualityCaptains", "CA_GEN_GUNNERY_MULT");
		CaptainsCyberneticAugmentation.MAX_ELITE_SKILLS_BONUS = LunaSettings.getInt("QualityCaptains", "CA_MAX_ELITE_SKILLS_BONUS");
		CaptainsCyberneticAugmentation.USE_SEX_BONUS = LunaSettings.getBoolean("QualityCaptains", "CA_USE_SYSTEMS_BONUS");
		CaptainsSystemsExpertise.NO_OFFICER_FACTOR = LunaSettings.getFloat("QualityCaptains", "CA_SEX_MULT");
		
		//Infomorphology
		CaptainsNeuralLink.MAX_ELITE_SKILLS_BONUS = LunaSettings.getInt("QualityCaptains", "NRL_MAX_ELITE_SKILLS_BONUS");
		CaptainsNeuralLinkScript.INSTANT_TRANSFER_DP = LunaSettings.getFloat("QualityCaptains", "NRL_INSTANT_TRANSFER_PT_MAX");
		CaptainsNeuralLinkScript.TRANSFER_SECONDS_PER_DP = LunaSettings.getFloat("QualityCaptains", "NRL_TRANSFER_RATE_PER_DP");
		CaptainsNeuralLinkScript.TRANSFER_MAX_SECONDS = LunaSettings.getFloat("QualityCaptains", "NRL_TRANSFER_MAX_TIME");
		CaptainsNeuralLinkScript.ALLOW_ENGINE_CONTROL_DURING_TRANSFER = LunaSettings.getBoolean("QualityCaptains", "NRL_ALLOW_CONTROL_DURING_XFER");
		CaptainsNeuralLinkScript.LINK_TO_ANY = LunaSettings.getBoolean("QualityCaptains", "NRL_UNWISE_HULLMODLESS");
		CaptainsNeuralLinkScript.LINK_ANARCHY = LunaSettings.getBoolean("QualityCaptains", "NRL_REALLY_UNWISE_LINKS_UNBOUND");

		CaptainsAutomatedShips.USE_AUTOMATED_LIMITS = LunaSettings.getBoolean("QualityCaptains", "USE_AUTOMATED_LIMITS");
		CaptainsAutomatedShips.USE_AUTOMATED_BATTLE_SCALING = LunaSettings.getBoolean("QualityCaptains", "USE_AUTOMATED_LIMIT_BATTLE_SCALING");
		CaptainsAutomatedShips.USE_AUTOMATED_LEVEL_SCALING = LunaSettings.getBoolean("QualityCaptains", "USE_AUTOMATED_LEVEL_SCALING");
		//CaptainsAutomatedShips.USE_AUTOMATED_DYNAMIC_SCALING = LunaSettings.getBoolean("QualityCaptains", "USE_AUTOMATED_LIMIT_DYNAMIC_SCALING");
		BaseSkillEffectDescription.AUTOMATED_POINTS_THRESHOLD = LunaSettings.getFloat("QualityCaptains", "AUTOMATED_POINTS_THRESHOLD");
		CaptainsAutomatedShips.AUTOMATED_POINTS_THRESHOLD_FULL = LunaSettings.getFloat("QualityCaptains", "AUTOMATED_POINTS_THRESHOLD");
		CaptainsAutomatedShips.MAX_CR_BONUS = LunaSettings.getFloat("QualityCaptains", "AUTOMATED_MAX_CR");
		CaptainsAutomatedShips.THRESHOLD_DIVISOR = LunaSettings.getInt("QualityCaptains", "AUTOMATED_POINTS_THRESHOLD_DIVISOR");
		
		AICoreOfficerPluginImpl.OMEGA_MULT = LunaSettings.getFloat("QualityCaptains", "AUTOMATED_OHM_MULT");
		AICoreOfficerPluginImpl.ALPHA_MULT = LunaSettings.getFloat("QualityCaptains", "AUTOMATED_ALPHA_MULT");
		AICoreOfficerPluginImpl.BETA_MULT = LunaSettings.getFloat("QualityCaptains", "AUTOMATED_BETA_MULT");
		AICoreOfficerPluginImpl.GAMMA_MULT = LunaSettings.getFloat("QualityCaptains", "AUTOMATED_GAMMA_MULT");
		CaptainsAutomatedShips.refreshThreshold();
		
		CaptainsHypercognition.ACCESS = LunaSettings.getFloat("QualityCaptains", "HCAI_ACCESS_BONUS");
		CaptainsHypercognition.DEFEND_BONUS = LunaSettings.getInt("QualityCaptains", "HCAI_DEFENSE_BONUS");
		CaptainsHypercognition.FLEET_SIZE = LunaSettings.getFloat("QualityCaptains", "HCAI_FLEET_BONUS");
		CaptainsHypercognition.STABILITY_BONUS = LunaSettings.getFloat("QualityCaptains", "HCAI_STAB_BONUS");
	}
	
	protected void reloadQualityIndustry() {
		//Industry
		CaptainsBulkTransport.CARGO_CAPACITY_MAX_PERCENT = LunaSettings.getFloat("QualityCaptains", "BT_CARGO_CAPACITY_MAX_PERCENT");
		CaptainsBulkTransport.FUEL_CAPACITY_MAX_PERCENT = LunaSettings.getFloat("QualityCaptains", "BT_FUEL_CAPACITY_MAX_PERCENT");
		CaptainsBulkTransport.PERSONNEL_CAPACITY_MAX_PERCENT = LunaSettings.getFloat("QualityCaptains", "BT_PERSONNEL_CAPACITY_MAX_PERCENT");
		CaptainsBulkTransport.BURN_BONUS = LunaSettings.getInt("QualityCaptains", "BT_CIV_BURN_BONUS");
		
		CaptainsSalvaging.COMBAT_SALVAGE = LunaSettings.getFloat("QualityCaptains", "SALVAGE_COMBAT_BONUS");
		CaptainsSalvaging.SALVAGE_BONUS = LunaSettings.getFloat("QualityCaptains", "SALVAGE_BONUS");
		CaptainsSalvaging.SALVAGE_BONUS_RARE = LunaSettings.getFloat("QualityCaptains", "SALVAGE_RARE_BONUS");
		CaptainsSalvaging.SALVAGE_RARE = LunaSettings.getBoolean("QualityCaptains", "SALVAGE_RARES_TOGGLE");
		//CaptainsSalvaging.CREW_LOSS_REDUCTION = LunaSettings.getFloat("QualityCaptains", "NC_CREW_LOSS_REDUCTION");
		CaptainsSalvaging.MAX_CR = LunaSettings.getFloat("QualityCaptains", "SALV_MAX_CR");
		CaptainsSalvaging.MIN_CR = LunaSettings.getFloat("QualityCaptains", "SALV_MIN_CR");
		CaptainsSalvaging.MAX_HULL = LunaSettings.getFloat("QualityCaptains", "SALV_MAX_HULL");
		CaptainsSalvaging.MIN_HULL = LunaSettings.getFloat("QualityCaptains", "SALV_MIN_HULL");
		
		CaptainsFieldRepairs.REPAIR_RATE_BONUS = LunaSettings.getFloat("QualityCaptains", "FR_REPAIR_RATE_BONUS");
		CaptainsFieldRepairs.INSTA_REPAIR_PERCENT = LunaSettings.getFloat("QualityCaptains", "FR_AFTER_BATTLE_REPAIR_PERC");
		CaptainsFieldRepairs.MAINTENANCE_COST_REDUCTION = LunaSettings.getFloat("QualityCaptains", "FR_MAINTENANCE_REDUCTION_PERC");
		//Field repair script now grouped with Hull Restoration
		
		CaptainsOrdnanceExpertise.USE_DISCOUNT_EFFECT = LunaSettings.getBoolean("QualityCaptains", "OE_SWAP_TO_DISCOUNT");
		CaptainsOrdnanceExpertise.FLUX_PER_OP = LunaSettings.getFloat("QualityCaptains", "OE_BONUS_DISSIPATION_PER_OP");
		CaptainsOrdnanceExpertise.CAP_PER_OP = LunaSettings.getFloat("QualityCaptains", "OE_BONUS_CAP_PER_OP");
		CaptainsOrdnanceExpertise.FLUX_GEN_REDUCTION_PER_OP_FRIG = LunaSettings.getFloat("QualityCaptains", "OE_FLUX_GEN_REDUCTION_PER_OP_FRIG");
		CaptainsOrdnanceExpertise.FLUX_GEN_REDUCTION_PER_OP_DEST = LunaSettings.getFloat("QualityCaptains", "OE_FLUX_GEN_REDUCTION_PER_OP_DEST");
		CaptainsOrdnanceExpertise.FLUX_GEN_REDUCTION_PER_OP_CRSR = LunaSettings.getFloat("QualityCaptains", "OE_FLUX_GEN_REDUCTION_PER_OP_CRSR");
		CaptainsOrdnanceExpertise.FLUX_GEN_REDUCTION_PER_OP_CAPITAL = LunaSettings.getFloat("QualityCaptains", "OE_FLUX_GEN_REDUCTION_PER_OP_CAP");
		CaptainsOrdnanceExpertise.FLUX_GEN_REDUCTION_PER_OP = LunaSettings.getFloat("QualityCaptains", "OE_FLUX_GEN_REDUCTION_PER_OP_OTHER");
		CaptainsOrdnanceExpertise.FLUX_GEN_REDUCTION_CAP = LunaSettings.getFloat("QualityCaptains", "OE_FLUX_GEN_REDUCTION_CAP");
		
		//CaptainsPolarizedArmor.ARMOR_FRACTION_BONUS = LunaSettings.getFloat("QualityCaptains", "PA_ARMOR_FRACTION_BONUS");
		CaptainsPolarizedArmor.MIN_BONUS_PERC = LunaSettings.getFloat("QualityCaptains", "PA_MIN_APPLICATION");
		CaptainsPolarizedArmor.MIN_BONUS_SHIELDLESS_PERC = LunaSettings.getFloat("QualityCaptains", "PA_MIN_APPLICATION_SHIELDLESS");
		CaptainsPolarizedArmor.EFFECTIVE_ARMOR_BONUS = LunaSettings.getFloat("QualityCaptains", "PA_EFFECTIVE_ARMOR_BONUS_CAP");
		CaptainsPolarizedArmor.EMP_BONUS_PERCENT = LunaSettings.getFloat("QualityCaptains", "PA_EMP_REDUCTION_CAP");
		//CaptainsPolarizedArmor.HULL_DAM_REDUCTION = LunaSettings.getFloat("QualityCaptains", "PA_HULL_DAMAGE_REDUCTION");
		CaptainsPolarizedArmor.OVERLOAD_REDUCTION = LunaSettings.getFloat("QualityCaptains", "PA_OVERLOAD_DURATION_REDUCTION");
		
		CaptainsContainmentProcedures.CREW_LOSS_REDUCTION_COMBAT = LunaSettings.getFloat("QualityCaptains", "CPRO_CREW_LOSS_REDUCTION_COMBAT");
		CaptainsContainmentProcedures.CREW_LOSS_REDUCTION_NON_COMBAT = LunaSettings.getFloat("QualityCaptains", "CPRO_CREW_LOSS_REDUCTION_CAMPAIGN");
		CaptainsContainmentProcedures.FUEL_SALVAGE_BONUS = LunaSettings.getFloat("QualityCaptains", "CPRO_FUEL_SALVAGE_BONUS");
		CaptainsContainmentProcedures.FUEL_USE_REDUCTION_PERC = LunaSettings.getInt("QualityCaptains", "CPRO_FUEL_USE_REDUCTION_PERC");
		CaptainsContainmentProcedures.CORONA_EFFECT_PERC_MULT = LunaSettings.getInt("QualityCaptains", "CPRO_CORONA_EFFECT_PERC_MULT");
		
		//Jury-Rigging
		CaptainsMakeshiftEquipment.DIRECT_JUMP_DISCOUNT = LunaSettings.getFloat("QualityCaptains", "JR_DIRECT_JUMP_REDUCTION");
		CaptainsMakeshiftEquipment.SURVEY_COST_MULT_PERC = LunaSettings.getFloat("QualityCaptains", "JR_SURVEY_COST_MULT_PERC");
		CaptainsMakeshiftEquipment.DMOD_DISCOUNT_MULT = LunaSettings.getFloat("QualityCaptains", "JR_DMOD_SUPPLY_REDUCTION_MULT");
		
		CaptainsIndustrialPlanning.CUSTOM_PRODUCTION_BONUS = LunaSettings.getFloat("QualityCaptains", "MRKT_CUSTOM_PRODUCTION_BONUS");
		CaptainsIndustrialPlanning.ACCESS_BONUS = LunaSettings.getFloat("QualityCaptains", "MRKT_ACCESSIBILITY_BONUS");
		CaptainsIndustrialPlanning.SUPPLY_BONUS = LunaSettings.getInt("QualityCaptains", "MRKT_SUPPLY_BONUS");
		CaptainsIndustrialPlanning.SHIP_QUAL_BONUS = LunaSettings.getInt("QualityCaptains", "MRKT_SHIP_QUAL_BONUS");
		
		CaptainsHullRestoration.CR_PER_SMOD = LunaSettings.getInt("QualityCaptains", "HR_CR_PER_SMOD");
		CaptainsHullRestoration.DMOD_AVOID_MIN = LunaSettings.getFloat("QualityCaptains", "HR_AVOID_DMOD_CHANCE_MIN");
		CaptainsHullRestoration.DMOD_AVOID_MIN_DP = LunaSettings.getFloat("QualityCaptains", "HR_AVOID_DMOD_DP_LOW");
		CaptainsHullRestoration.DMOD_AVOID_MAX = LunaSettings.getFloat("QualityCaptains", "HR_AVOID_DMOD_CHANCE_MAX");
		CaptainsHullRestoration.DMOD_AVOID_MAX_DP = LunaSettings.getFloat("QualityCaptains", "HR_AVOID_DMOD_DP_HIGH");
		
		CaptainsFieldRepairsScript.REMOVE_DMOD_FROM_NEW_SHIPS = LunaSettings.getBoolean("QualityCaptains", "HRS_REMOVE_FROM_NEW");
		CaptainsFieldRepairsScript.MIN_NEW_REMOVE_PROB = LunaSettings.getFloat("QualityCaptains", "HRS_NEW_REMOVE_BASE_PROB");
		CaptainsFieldRepairsScript.NEW_REMOVE_PROB_PER_DMOD = LunaSettings.getFloat("QualityCaptains", "HRS_NEW_REMOVE_PER_DMOD_PROB");
		CaptainsFieldRepairsScript.MONTHS_PER_DMOD_REMOVAL = LunaSettings.getFloat("QualityCaptains", "MONTHS_PER_DMOD_REMOVAL");
		CaptainsFieldRepairsScript.MULT_CAPITAL = LunaSettings.getFloat("QualityCaptains", "HRS_MULT_CAPITAL");
		CaptainsFieldRepairsScript.MULT_CRUISER = LunaSettings.getFloat("QualityCaptains", "HRS_MULT_CRUISER");
		CaptainsFieldRepairsScript.MULT_DESTROYER = LunaSettings.getFloat("QualityCaptains", "HRS_MULT_DEST");
		CaptainsFieldRepairsScript.MULT_FRIGATE = LunaSettings.getFloat("QualityCaptains", "HRS_MULT_FRIG");
		CaptainsFieldRepairsScript.MULT_PHASE = LunaSettings.getFloat("QualityCaptains", "HRS_MULT_PHASE");
		CaptainsFieldRepairsScript.MULT_CIVILIAN = LunaSettings.getFloat("QualityCaptains", "HRS_MULT_CIV");
		
		//Derelict Operations
		CaptainsDerelictContingent.MAX_DMODS = LunaSettings.getFloat("QualityCaptains", "DO_MAX_DMODS_TO_COUNT");
		CaptainsDerelictContingent.SHIELDLESS_FLUX_BONUS_PERC = LunaSettings.getInt("QualityCaptains", "DO_SHIELDLESS_FLUX_BONUS_PERC");
		CaptainsDerelictContingent.DMOD_EFFECT_PERC_MULT = LunaSettings.getFloat("QualityCaptains", "DO_DMOD_EFFECT_PERC_MULT");
		CaptainsDerelictContingent.MINUS_DP_PERCENT_PER_DMOD = LunaSettings.getFloat("QualityCaptains", "DO_MINUS_DP_PERCENT_PER_DMOD");
	}

	protected void reloadQualityBattleEffects() {
		CaptainsCoordinatedManeuversScript.BASE_MAXIMUM = LunaSettings.getFloat("QualityCaptains", "BATTLE_NAV_MAX");
		CaptainsCoordinatedManeuversScript.MULT_BUOY = LunaSettings.getFloat("QualityCaptains", "BATTLE_NAV_MULT_BUOY");
		CaptainsCoordinatedManeuversScript.AFFECTS_SPEED = LunaSettings.getBoolean("QualityCaptains", "BATTLE_NAV_APPLY_TO_SPEED");
		CaptainsCoordinatedManeuversScript.AFFECTS_MANEUVER = LunaSettings.getBoolean("QualityCaptains", "BATTLE_NAV_APPLY_TO_MANEUVER");
		CaptainsCoordinatedManeuversScript.APPLY_TO_SHIPS = LunaSettings.getBoolean("QualityCaptains", "BATTLE_NAV_SHIPS");
		CaptainsCoordinatedManeuversScript.APPLY_TO_FIGHTERS = LunaSettings.getBoolean("QualityCaptains", "BATTLE_NAV_FIGHTERS");
		CaptainsCoordinatedManeuversScript.APPLY_TO_MISSILES = LunaSettings.getBoolean("QualityCaptains", "BATTLE_NAV_MISSILES");
		CaptainsCoordinatedManeuversScript.MULT_SHIPS = LunaSettings.getFloat("QualityCaptains", "BATTLE_NAV_MULT_SHIPS");
		CaptainsCoordinatedManeuversScript.MULT_FIGHTERS = LunaSettings.getFloat("QualityCaptains", "BATTLE_NAV_MULT_FIGHTERS");
		CaptainsCoordinatedManeuversScript.MULT_MISSILES = LunaSettings.getFloat("QualityCaptains", "BATTLE_NAV_MULT_MISSILES");
		CaptainsCoordinatedManeuversScript.MULT_SPEED = LunaSettings.getFloat("QualityCaptains", "BATTLE_NAV_MULT_MISSILES");
		CaptainsCoordinatedManeuversScript.MULT_MANEUVER = LunaSettings.getFloat("QualityCaptains", "BATTLE_NAV_MULT_MISSILES");
		
		CaptainsElectronicWarfareScript.BASE_MAXIMUM = LunaSettings.getFloat("QualityCaptains", "ECM_MAX");
		CaptainsElectronicWarfareScript.JAMMER_MULT = LunaSettings.getFloat("QualityCaptains", "ECM_JAMMER_MULT");
		CaptainsElectronicWarfareScript.AUTOAIM_MULT = LunaSettings.getFloat("QualityCaptains", "ECM_AUTOAIM_MULT");
		CaptainsElectronicWarfareScript.RANGE_MULT = LunaSettings.getFloat("QualityCaptains", "ECM_RANGE_MULT");
		CaptainsElectronicWarfareScript.RECOIL_MULT = LunaSettings.getFloat("QualityCaptains", "ECM_RECOIL_MULT");
		CaptainsElectronicWarfareScript.APPLY_TO_SHIPS = LunaSettings.getBoolean("QualityCaptains", "ECM_SHIPS");
		CaptainsElectronicWarfareScript.APPLY_TO_FIGHTERS = LunaSettings.getBoolean("QualityCaptains", "ECM_FIGHTERS");
		CaptainsElectronicWarfareScript.APPLY_TO_TURRETS = LunaSettings.getBoolean("QualityCaptains", "ECM_TURRETS");
		CaptainsElectronicWarfareScript.APPLY_TO_MISSILES = LunaSettings.getBoolean("QualityCaptains", "ECM_MISSILES");
		CaptainsElectronicWarfareScript.APPLIES_TO_AUTOAIM = LunaSettings.getBoolean("QualityCaptains", "ECM_AUTOAIM");
		CaptainsElectronicWarfareScript.APPLIES_TO_RANGE = LunaSettings.getBoolean("QualityCaptains", "ECM_RANGE");
		CaptainsElectronicWarfareScript.APPLIES_TO_RECOIL = LunaSettings.getBoolean("QualityCaptains", "ECM_RECOIL");
		CaptainsElectronicWarfareScript.APPLIES_TO_TURRET_TURNRATE = LunaSettings.getBoolean("QualityCaptains", "ECM_TURRET_TURNRATE");
		CaptainsElectronicWarfareScript.APPLIES_TO_MISSILE_TURNRATE = LunaSettings.getBoolean("QualityCaptains", "ECM_MISSILE_TURNRATE");
	}
	
	public void reloadQualityMilestoneEffects () {
		CaptainsAcademician.SENSOR_PERC = LunaSettings.getFloat("QualityCaptains", "PERK_SENSOR_RANGE");
		CaptainsBabylon.USE_UNNERF = LunaSettings.getBoolean("QualityCaptains", "PERK_PHASE_UNNERF");
		CaptainsBabylon.SPEED_BONUS = LunaSettings.getFloat("QualityCaptains", "PERK_PHASE_SPD_BONUS");
		CaptainsUnbound.range = LunaSettings.getFloat("QualityCaptains", "PERK_GATE_RANGE");
		CaptainsUsurper.USE_AMBUSH = LunaSettings.getBoolean("QualityCaptains", "PERK_USE_AMBUSH");
		CaptainsKnight.MIN_OPPONENTS = LunaSettings.getInt("QualityCaptains", "PERK_KNT_MIN_OPPONENTS");
		CaptainsKnight.PPT_PERC = LunaSettings.getFloat("QualityCaptains", "PERK_KNT_PPT_PERC");
		CaptainsKnight.SECOND_WIND = LunaSettings.getBoolean("QualityCaptains", "PERK_KNT_SCDWIND");
		CaptainsLucifer.STABILITY_BONUS = LunaSettings.getFloat("QualityCaptains", "PERK_STABILITY");
		CaptainsPrince.BONUS_PTS = LunaSettings.getInt("QualityCaptains", "PERK_REP_BONUS");
		CaptainsPrince.MIN_PTS = LunaSettings.getInt("QualityCaptains", "PERK_REPCHANGE_MIN");
		CaptainsUnderworld.MAX_REP = LunaSettings.getInt("QualityCaptains", "PERK_PIRATES_REP_MAX");
		CaptainsUnderworld.REP_RATE = LunaSettings.getInt("QualityCaptains", "PERK_PIRATES_REP_MAG");
		CaptainsOmega.MAG = LunaSettings.getFloat("QualityCaptains", "PERK_ECM_FLAT");
		CaptainsAscendance.MAG = LunaSettings.getFloat("QualityCaptains", "PERK_NAV_FLAT");
	}
	
	protected void reloadQualityNex() {
		CaptainsNexSkills.BONUS_AGENTS = LunaSettings.getInt("QualityCaptains", "NEX_BONUS_AGENTS");
	}
	
	protected void reloadQualityVSP() {
		//
	}
	
	protected void reloadQualityRealisticCombat() {
		//
	}
	
	@Override
	public void settingsChanged(String modID) {
		reloadQualityConfig();
	}

}

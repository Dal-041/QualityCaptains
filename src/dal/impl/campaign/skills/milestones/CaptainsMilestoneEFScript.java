package dal.impl.campaign.skills.milestones;

import com.fs.starfarer.api.EveryFrameScript;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.CampaignFleetAPI;
import com.fs.starfarer.api.campaign.SectorEntityToken;
import com.fs.starfarer.api.campaign.comm.IntelInfoPlugin;
import com.fs.starfarer.api.impl.campaign.GateEntityPlugin;
import com.fs.starfarer.api.impl.campaign.ids.Factions;
import com.fs.starfarer.api.impl.campaign.ids.Tags;
import com.fs.starfarer.api.impl.campaign.intel.misc.GateIntel;
import com.fs.starfarer.api.util.IntervalUtil;
import dal.plugins.Captains_Utils;
import org.lazywizard.lazylib.MathUtils;

public class CaptainsMilestoneEFScript implements EveryFrameScript {
	private float days = 0;
	protected IntervalUtil trackerMain = new IntervalUtil(7f, 7f);
	protected IntervalUtil trackerUnbound = new IntervalUtil(3f, 3f);
	protected IntervalUtil trackerUnderground = new IntervalUtil(30f, 30f);
	
	public boolean isDone() {
		return false;
	}

	public boolean runWhilePaused() {
		return false;
	}

	public void advance(float amount) {
		
		days = Global.getSector().getClock().convertToDays(amount);
		
		trackerMain.advance(days);
		trackerUnbound.advance(days);
		trackerUnderground.advance(days);
		
		if (trackerMain.intervalElapsed()) {
			Captains_Utils.checkMilestones();
		}
		if (trackerUnderground.intervalElapsed()) {
			if (Global.getSector().getPlayerStats().getSkillLevel("captains_underworld") > 0) {
				if (Global.getSector().getPlayerFaction().getRepInt(Factions.PIRATES) <= (CaptainsUnderworld.MAX_REP)) {
					Global.getSector().getPlayerFaction().adjustRelationship(Factions.PIRATES, (CaptainsUnderworld.REP_RATE)/100f);
				}
			}
			if (Global.getSector().getPlayerStats().getSkillLevel("captains_starfarer") > 0) {
				Global.getSector().getPlayerStats().addStoryPoints(1);
			}
		}
		

		
		if (trackerUnbound.intervalElapsed()) {
			if (Global.getSector().getPlayerStats().getSkillLevel("captains_unbound") > 0) {
				CampaignFleetAPI fleet = Global.getSector().getPlayerFleet();
				if (fleet == null) return;
		        for (SectorEntityToken gate : Global.getSector().getCustomEntitiesWithTag(Tags.GATE)) {
		            Boolean gateScanned = gate.getMemoryWithoutUpdate().getBoolean(GateEntityPlugin.GATE_SCANNED);
		            if (!gateScanned && MathUtils.isWithinRange(Global.getSector().getPlayerFleet().getLocationInHyperspace(), gate.getLocationInHyperspace(), CaptainsUnbound.range)) {
		            	if (gateIntelDoesNotExist(gate) && !gate.getStarSystem().hasTag(Tags.THEME_HIDDEN) && !gate.getStarSystem().hasTag(Tags.THEME_SPECIAL)) {
		            		Global.getSector().getIntelManager().addIntel(new GateIntel(gate));
		            	}
		            }
		        }
			}
		}
	}
	
    private boolean gateIntelDoesNotExist(SectorEntityToken gate) {
        for (IntelInfoPlugin intel : Global.getSector().getIntelManager().getIntel(GateIntel.class)) {
            GateIntel entry = (GateIntel)intel;
            if (entry.getGate() == gate) {
                return false;
            }
        }
        return true;
    }
}

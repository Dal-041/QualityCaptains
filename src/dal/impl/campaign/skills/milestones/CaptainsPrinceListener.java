package dal.impl.campaign.skills.milestones;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.BaseCampaignEventListener;

public class CaptainsPrinceListener extends BaseCampaignEventListener {
	
    public CaptainsPrinceListener(boolean permaRegister) {
		super(permaRegister);
	}
    
	@Override
    public void reportPlayerReputationChange(String factionId, float delta) {
		if (Global.getSector().getPlayerStats().hasSkill("captains_prince")) {
			if (delta >= CaptainsPrince.MIN_PTS) {
				Global.getSector().getFaction(factionId).setRelationship(Global.getSector().getPlayerFaction().getId(), (Global.getSector().getFaction(factionId).getRelationship(Global.getSector().getPlayerFaction().getId()) + CaptainsPrince.BONUS_PTS));
			}
		}
	}
}

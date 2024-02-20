package dal.impl.intel;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.characters.FullName;
import com.fs.starfarer.api.characters.PersonAPI;
import com.fs.starfarer.api.impl.campaign.ids.Skills;
import com.fs.starfarer.api.impl.campaign.intel.BaseIntelPlugin;
import com.fs.starfarer.api.ui.Alignment;
import com.fs.starfarer.api.ui.SectorMapAPI;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;

import dal.impl.campaign.skills.milestones.CaptainsUnbound;

import java.awt.*;
import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.Set;

public class IntelQCAchievedMilestone extends BaseIntelPlugin {

	private String name;
	private String icon;

    public IntelQCAchievedMilestone(String skillIcon, String skillName) {
		try {
			Global.getSettings().loadTexture(skillIcon);
		} catch (IOException e) {
			
		}
        this.name = skillName;
        this.icon = skillIcon;
        //runcode dal.plugins.Captains_Utils.debugAwardMilestones();
    }

    @Override
    protected String getName() {
        return "Achieved Milestone: " + name;
    }

    @Override
    protected void addBulletPoints(TooltipMakerAPI info, ListInfoMode mode, boolean isUpdate, Color tc, float initPad) {

        Color gray = Misc.getGrayColor();

        info.addSpacer(3f);
        info.addPara("Open the character screen to view the effects", 0f, gray, gray);
    }

    @Override
    public void createSmallDescription(TooltipMakerAPI info, float width, float height) {
    	Color gray = Misc.getGrayColor();
        info.addSpacer(10f);
        TooltipMakerAPI imageTooltip = info.beginImageWithText(icon, 64f);
        imageTooltip.addPara("Achieving a major milestone has earned you a new, permanent skill.", 0f);
        info.addImageWithText(0f);

        info.addSpacer(10f);

        info.addPara("Perks and all other skills in QC can be configured while playing using LunaLib.", 0f);
        info.addPara("If not using LL, or if you'd like your configuration to load automatically, the same options can be found in data/config/LunaSettings.csv.", 0f, gray, gray);
    }

    @Override
    public String getIcon() {
        return icon;
    }

    @Override
    public Set<String> getIntelTags(SectorMapAPI map) {
        Set<String> tags = new LinkedHashSet<String>();

        //tags.add("Milestones");

        return tags;
    }
}

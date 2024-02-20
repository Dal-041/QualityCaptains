package dal.impl.campaign.skills.milestones;

public interface milestoneSkill {
	public String getSkillID();
	public String getSkillIcon();
	public String getSkillname();
	public String getSkillAuthor();
	public String getSkillDesc();
	public void setEnabled(boolean enabled);
	public void setAwarded(boolean awarded);
	public boolean isEnabled();
	public boolean isAwarded();
	public void award();
}

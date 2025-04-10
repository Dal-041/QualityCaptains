1.0 - First official release
- Feature complete

1.0.1
- Fixed typo in Impact Mitigation
- Fixed a file conflict blocking customization

1.0.2
- Restored bonus damage to destroyers and cruisers to Target Analysis (currently set to 1/3 and 2/3rd the capital damage bonus respectively.)*
- Allowed more customization settings to accept "0" as a valid input.
- Moved the Special Modification skill's S-mod capacity bonus to its base tier, keeping the XP bonus behind elite. 
- Updated the QC ECM script to correctly display configured effects during battle.
* Updates that requite overwriting the settings file will be reserved for point releases, when possible.

1.0.3
- Updated the "lanes" preset - sensors is now T1 and navigation T2.
- Reduced the default bonus XP for installing S-mods with the special modifications elite to 34% (3 installations per refund).
- Added turret turn rate as a configurable ECM penalty type.
- Added missile maneuver rate as a configurable ECM penalty type.
- Added config options for ECM type multipliers, e.g. ECM rating * 2 for recoil effect - which is now default.
- Added config options for the Target Analysis Destroyer and Cruiser damage bonus.
- Restored "Recovered ships have fewer dmods on average" effect to Field Repairs.
- Added config toggle for whether the salvaging skill should provide a bonus to rare-item salvaging and an associated bonus value. 
- Added support for Nexerelin's additional agents.
- Added support for Vayra Ship Pack's Collapsed Cargohold hullmod.
- B: Fixed an errant multiplier.

1.0.4.
- Fixed a -very- errant multiplier - Salvage setting and rare salvage setting no longer interact. 
- Correctly added the Special Modifications SP reduction to defaults
- Bulk transport no longer scales with fleet size, now provides +30% transport stats by default - this will be cleaned up in the next release

1.0.8
- Updated Officer Management, Navigation, and Sensors skills to correctly call the customization code
- Tidied up the Bulk Transport skill's effect
- Fixed a duplicated speed effect in Wolfpack Tactics
- Changed Helsmanship's elite effect to only use the Flux Threshold standard by default. Either option can still be switched to but having both enabled at once may conflict. 
- Added the Field Repair effect's time factors to settings.json and updated the script with RC14 vanilla fix for hull restoration status.
- Coordinated Maneuvers has been given the ECM treatment: now supplies any combination of speed, maneuver, missile, fighter, and ship bonuses. 
- Point Defense now applies only a (configurable) portion of its effects to launched fighters. 
- Phase Mastery's speed bonus now varies between hull sizes
- Automated Ships supports a flat limit, if desired. Simply disable linear scaling. 
- Addressed a vanilla issue where Energy Weapon Mastery dynamic damage bonuses applied to ballistic weapons.
- Added another "+Damage to ships based on hull size" set to fighters under officers that have the Strike Commander skill
- Also added a bonus for Target Analysis against frigates, though it defaults to +0%. It's there in the settings if you want it for some maniacal reason.
- Also hooked up TA's size based damage bonus to the correct config lines. ^^'
- Restored to Wolfpack Tactics the recovery assurance for frigates commanded by officers.
- Nerfed Crew Training slightly, CR bonus from 15 to 10%. 
- Buffed Reliability Engineering with the same 5% max CR, to +20%.
- Nerfed dmod maintenance reduction effect with new multiplier, set to 50% of it was in 0.91a per ship dmod by default. 
- Unified values in the code and default config.
- Resolved an override so that QC can now work alongside mods implementing custom skills. 

1.1.0 - Starsector 0.95.1a
We're back, baby!
- Refreshed Quality Captains to Starsector's 0.95.1 skills.
- Requires MagicLib now! Yay!
- Incidentally, the mod's tweakable settings are now in modSettings.json rather than settings.json. 
- The default skill tree is now partially leveled; 3 tiers total and reaching each tier unlocks all skills on that tier without further restriction.
- The optional skill trees have also been updated.

1.1.1
- Changed the behavior of the ECM skill to count each electronic warfare skill toward ECM score rather than just checking that you had at least 1. 
- Corrected an error in the description of Point Defense that was displaying half the configured value for anti-fighter damage.
- Various fixes to the Automated Ships skill. It should now display and act exactly as configured.
- Restored Bulk Transport's civilian ship burn bonus and added a config for it. 
1.1.1h
- Tweaked EWar descriptions slightly.
- Corrected an oversight that duplicated Hypercognition for AI Cores. Will require a new game when updating. :>

1.2.0 - The Dynamic Settings Update!
- Lunalib support, allowing values to be changed and tested ingame.
- Fixed Armada Doctrine deployment point calculations not being treated as floats.
- Reformatted backcompatibility settings and added them to modSettings.json. These will are only relevant to modified QC installations.

1.5.0
- Full Starsector 0.96a upgrade!
- Added milestones! These are perks that are unlocked as you progress through vanilla storylines. They provide subtle but substantial effects for different play styles. 
	* Milestones have their own configuration settings
	* Milestones take up to week to appear (or disappear)
	* Milestones must be toggled off to reassign skills. Vanilla bug! The LunaLib setting makes this really easy.
	* The last milestone is awarded if you achieve all other 10 milestones for your character. 
- Streamlined some skills and descriptions
	* Removed the Overload Reduction and Instat Repair effects from Damage Control and added the Weapon and Engine damage resistance effect previously on Combat Endurance
	* Added CR degradation rate reduction to Combat Endurance
	* Moved the Systems Expertise for uncaptained ships from Infomorphology to Cybernetic Implants
	* Consolidated the Containment Procedures effect description
	* Reduced default values for armor-tanking and armor-breaking a little
- Technical: moved code classes to dal.impl.campaign.skills and dal.plugins
- Fixed an issue where custom settings for Phase Cloak Cooldowns were loading
- Corrected a cosmetic issue that listed the default Automated Ship limit even when it was set unused. This did not impact stats. 
- Can be added to ongoing games, but skill additions may mean QC can not be removed from ongoing games. Poke me if you're desperate for a vanilla-only toggle.
- Unified QC settings and Lunalib settings. You will only ever need to change them in one place. Wow!
	* Configuration files have changed from all previous versions, including betas
	* For automatically loaded settings, edit the CSV, otherwise load in dynamic settings each startup by opening LunaLib at the main menu and hitting "Save All"
	
1.5.1
- Reverted a change to Navigation that was flipping the reduction to a penalty.
- Rearranged a code check for better performance
- Hotfix: Removed the officer bonus effect for fighter skills because of unanticipated behavior. This bonus will return in a future version. 

1.5.2
- Reintroduced carriers with officers having enhanced fighter skills.
- Fixed an issue where the non-default Second Wind effect had a 100x too powerful multiplier.
- Fixed an issue where toggling milestones off would not remove the final milestone.
- Adjusted the trigger for the [REDACTED] perk. Now you have to win the duel.

1.5.3
- Crossmod compatibility for the Field Repairs effect when also using the Experimental Hull Modifications mod by Lyravega.
- Tweaked some default values:
	* Reduced the accessibility bonus from Industrial Planning from 25% to 15%.
	* Extended the default range of the [REDACTED] perk to 12500. 
	* Swapped Automated Ships to use level scaling by default from battlesize scaling.
- Completed LunaLib integration. !* Custom LunaLib settings will now load automatically. *! 
	* Added some new handling for loading certain settings while on the main menu.
	
1.6.0
- Starsector 0.97 update!
- Fixed behavior of Carrier skill effects with officers
- Fixed LunaLib support such that it is a soft requirement
- Added new 0.97 skill adjustments:
	* Electronic Warfare received the bonus to capturing objectives (half stock strength by default).
	* Helmsmanship speed boosts adopted as default
	* Damage control received "Can repair weapons/engines while taking damage" effect
	* If Ordnance Expertise was using vanilla effects, mirrored the change to the flux dissipation bonus (2->1.5 default)
- Support for 0.97 ECM mechanics.

1.61
- Forgot a fix for the perk that extends sensor range - was unintentionally doubling the base value. 
- A cosmetic typo where "ECM" appeared in settings twice instead of ECM and "NAV"

1.69
- Recompiled against Starsector 0.98a
- Published alternate milestones-only minimod, Quality Perks
- Fix for issue preventing Knight milestone from being awarded

1.70
- Duplicate fix for Carrier Group officer check.
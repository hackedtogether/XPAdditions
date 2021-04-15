package org.hackedtogether.xpadditions.util;

import net.minecraft.entity.player.PlayerEntity;

public class XPUtils {

    public static int getTotalXPOfPlayer(PlayerEntity player) {
        return (int) (XPUtils.convertLevelToXP(player.experienceLevel) + (player.experienceProgress * player.getXpNeededForNextLevel()));
    }

    public static void addXPToPlayer(PlayerEntity player, int amount) {
        int experience = getTotalXPOfPlayer(player) + amount;
        if (experience < 0) {

        }
        player.totalExperience = experience;
        player.experienceLevel = XPUtils.convertXPToLevels(experience);
        int expForLevel = XPUtils.convertLevelToXP(player.experienceLevel);
        player.experienceProgress = (float) (experience - expForLevel) / (float) player.getXpNeededForNextLevel();
    }

    public static int xpForNextLevel(int level) {
        return convertLevelToXP(level + 1) - convertLevelToXP(level);
    }

    public static int convertXPToLevels(int xp) {
        int level;
        if (xp < 315) {
            level = 0;
        } else if ((xp >= 315) && (xp < 1395)) {
            level = 15;
        } else {
            level = 30;
        }
        while (convertLevelToXP(level) <= xp) {
            level++;
        }

        return level - 1;
    }

    public static int convertLevelToXP(int level) {
        int xpForPreviousMilestone;
        int levelsAboveMilestone;
        int factor1;
        int factor2;

        if (level <= 15) {
            xpForPreviousMilestone = 0;
            levelsAboveMilestone = level - 0;
            factor1 = 7;
            factor2 = 2;
        } else if ((level > 15) && (level <= 30)) {
            xpForPreviousMilestone = 315;
            levelsAboveMilestone = level - 15;
            factor1 = 37;
            factor2 = 5;
        } else {
            xpForPreviousMilestone = 1395;
            levelsAboveMilestone = level - 30;
            factor1 = 112;
            factor2 = 9;
        }

        return xpForPreviousMilestone + (levelsAboveMilestone * (2 * factor1 + (levelsAboveMilestone - 1) * factor2) / 2);
    }
}

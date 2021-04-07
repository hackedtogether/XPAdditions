package org.hackedtogether.xpadditions.util;

import net.minecraft.entity.player.PlayerEntity;

public class XPUtils {

    public static int getPlayerXP(PlayerEntity player) {
        return (int)(XPUtils.getXPForLevel(player.experienceLevel) + (player.experienceProgress * player.getXpNeededForNextLevel()));
    }

    public static void addPlayerXP(PlayerEntity player, int amount) {
        int experience = getPlayerXP(player) + amount;
        player.totalExperience = experience;
        player.experienceLevel = XPUtils.getLevelForXP(experience);
        int expForLevel = XPUtils.getXPForLevel(player.experienceLevel);
        player.experienceProgress = (float)(experience - expForLevel) / (float)player.getXpNeededForNextLevel();
    }

    public static int xpBarCap(int level) {
        if (level >= 30)
            return 112 + (level - 30) * 9;

        if (level >= 15)
            return 37 + (level - 15) * 5;

        return 7 + level * 2;
    }

    private static int sum(int n, int a0, int d) {
        return n * (2 * a0 + (n - 1) * d) / 2;
    }

    public static int getLevelForXP(int xp) {
        int level = 0;
        while (true) {
            final int xpToNextLevel = xpBarCap(level);
            if (xp < xpToNextLevel) return level;
            level++;
            xp -= xpToNextLevel;
        }
    }

    public static int getXPForLevel(int level) {
        if (level == 0) return 0;
        if (level <= 15) return sum(level, 7, 2);
        if (level <= 30) return 315 + sum(level - 15, 37, 5);
        return 1395 + sum(level - 30, 112, 9);
    }
}

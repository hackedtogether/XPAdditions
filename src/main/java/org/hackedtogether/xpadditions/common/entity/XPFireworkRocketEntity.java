package org.hackedtogether.xpadditions.common.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.item.ExperienceOrbEntity;
import net.minecraft.entity.projectile.FireworkRocketEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Arrays;

public class XPFireworkRocketEntity extends FireworkRocketEntity {

    private int[] xpOrbs;
    private static final int maxOrbsPerExplosion = 10;
    private static final Logger LOGGER = LogManager.getLogger();

    public XPFireworkRocketEntity(EntityType<? extends XPFireworkRocketEntity> entity, World world) {
        super(entity, world);
    }

    public XPFireworkRocketEntity(World world, double x, double y, double z, ItemStack stack, int xp) {
        super(world, x, y, z, stack);

        LOGGER.debug("Creating XP Firework Entity");

        // If there isn't enough XP for every orb, reduce the number of orbs
        if (xp < maxOrbsPerExplosion) {
            this.xpOrbs = new int[xp];
            Arrays.fill(this.xpOrbs, 1);
        }

        // Distribute the XP between the orbs
        else {
            int base = (int) Math.floor(xp / maxOrbsPerExplosion);
            int remainder = xp % maxOrbsPerExplosion;

            // Evenly distribute the XP
            this.xpOrbs = new int[maxOrbsPerExplosion];
            Arrays.fill(this.xpOrbs, base);

            // Distribute the remainder
            if (remainder != 0) {
                for (int i = 0; i < remainder-1; i++) {
                    this.xpOrbs[i]++;
                }
            }
        }
    }

    @Override
    public void explode() {

        super.explode();

        // If there is any XP to drop
        if (this.xpOrbs.length > 0) {

            // Calculate where the explosion should be
            double x = this.getX();
            double y = this.getY();
            double z = this.getZ();

            // Create the XP orbs
            LOGGER.debug(String.format("XP explosion at [%.1f, %.1f, %.1f]", x, y, z));
            for (int i = 0; i < this.xpOrbs.length; i++) {
                spawnXPOrb(this.level, x, y, z, this.xpOrbs[i]);
            }
        }
    }

    protected void spawnXPOrb(World world, double x, double y, double z, int value) {
        ExperienceOrbEntity experienceOrbEntity = new ExperienceOrbEntity(world, x, y, z, value);
        world.addFreshEntity(experienceOrbEntity);
        LOGGER.debug(String.format("Spawn XP orb at [%.1f, %.1f, %.2f] with value: %d", x, y, z, value));
    }
}


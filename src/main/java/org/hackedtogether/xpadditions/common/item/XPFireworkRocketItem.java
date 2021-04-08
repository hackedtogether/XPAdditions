package org.hackedtogether.xpadditions.common.item;

import net.minecraft.entity.item.ExperienceOrbEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.FireworkRocketEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hackedtogether.xpadditions.util.XPUtils;

import java.util.concurrent.TimeUnit;

public class XPFireworkRocketItem extends Item {

    private static final Logger LOGGER = LogManager.getLogger();
    private static final int maxBoostDuration = 5;
    private static final int xpPerSecondOfBoost = 2;
    private static final int defaultXPPerExplosionOrb = 10;
    private static final int defaultOrbsPerExplosion = 10;

    public XPFireworkRocketItem(Properties properties) {
        super(properties);
    }

    // TODO: Work out how to do an XP firework explosion
    public ActionResultType useOn(ItemUseContext context) {
        World world = context.getLevel();
        if (!world.isClientSide) {
            ItemStack itemstack = context.getItemInHand();
            Vector3d vector3d = context.getClickLocation();
            Direction direction = context.getClickedFace();
            PlayerEntity player = context.getPlayer();

            // Create the firework
            FireworkRocketEntity fireworkrocketentity = new FireworkRocketEntity(world, context.getPlayer(), vector3d.x + (double)direction.getStepX() * 0.15D, vector3d.y + (double)direction.getStepY() * 0.15D, vector3d.z + (double)direction.getStepZ() * 0.15D, itemstack);
            world.addFreshEntity(fireworkrocketentity);

            // Wait for the firework before exploding XP
            // TODO: The sound is being delayed because of this. Needs to be scheduled
            // TODO: How does the regular firework delay its explosion sound?
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            int xpPerExplosionOrb = defaultXPPerExplosionOrb;
            int orbsPerExplosion = defaultOrbsPerExplosion;
            int playerXP = XPUtils.getPlayerXP(player);
            int xpToUse = xpPerExplosionOrb * orbsPerExplosion;
            int remainder = 0;

            // If they don't have enough XP, reduce the explosion size
            if (playerXP < xpToUse) {

                // If there isn't enough XP for every orb, reduce the number of orbs
                if (playerXP < orbsPerExplosion) {
                    orbsPerExplosion = playerXP;
                }

                xpPerExplosionOrb = (int) Math.floor(playerXP / orbsPerExplosion);
                remainder = playerXP % orbsPerExplosion;
                xpToUse = playerXP;
            }

            // Remove the player's XP
            XPUtils.addPlayerXP(player, -xpToUse);
            LOGGER.debug(String.format("Removing %d XP from player", xpToUse));

            // Calculate where the explosion should be
            BlockPos pos = context.getClickedPos().above(10);

            // Create the XP orbs
            LOGGER.debug("XP explosion at " + pos);
            for (int i = 0; i < orbsPerExplosion; i++) {
                spawnXPOrb(world, pos, xpPerExplosionOrb);
            }
            if (remainder > 0) {
                spawnXPOrb(world, pos, remainder);
            }

            // Play the explosion sound
            fireworkrocketentity.playSound(SoundEvents.FIREWORK_ROCKET_BLAST, 1F, 1F);
        }

        return ActionResultType.sidedSuccess(world.isClientSide);
    }

    public ActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {

        if (world.isClientSide) {
            LOGGER.debug("PlayerEntity: '" + player.getUUID() + "' using XP firework rocket");
        }

        ItemStack stack = player.getItemInHand(hand);

        // TODO: Server doesn't detect crouching so runs code for changing boost duration and boosting
        LOGGER.debug("Crouching: " + player.isCrouching());
        LOGGER.debug("Flying:    " + player.isFallFlying());

        // Check if the player is crouching (Change boost duration)
        if (player.isCrouching()) {

            // Change/fetch the boost duration
            int boostDuration = this.changeBoostDuration(stack);

            // Display a message and play a sound
            player.displayClientMessage(new TranslationTextComponent("message.xpadditions.change_boost_duration", boostDuration, maxBoostDuration), true);
            player.playSound(SoundEvents.STONE_BUTTON_CLICK_OFF, 1F, 1F);

            LOGGER.debug(String.format("Set boost duration to %d/%d", boostDuration, maxBoostDuration));
            return ActionResult.pass(player.getItemInHand(hand));
        }

        // Check if the player is gliding (Boost)
        else if (player.isFallFlying()) {
            if (!world.isClientSide) {

                int boostDuration = this.getBoostDuration(stack);
                int xpToUse = xpPerSecondOfBoost * boostDuration;
                int playerXP = XPUtils.getPlayerXP(player);

                // Check if the player has any XP
                if (playerXP == 0) {
                    return ActionResult.fail(player.getItemInHand(hand));
                }

                // If they don't have enough XP, reduce the boost duration
                if (playerXP < xpToUse) {
                    boostDuration = (int) Math.ceil(boostDuration * (playerXP / xpToUse));
                    xpToUse = playerXP;
                }

                // Boost
                stack.getOrCreateTagElement("Fireworks").putByte("Flight", (byte) boostDuration);
                world.addFreshEntity(new FireworkRocketEntity(world, stack, player));
                XPUtils.addPlayerXP(player, -(xpToUse));

                LOGGER.debug(String.format("Boosting for %s seconds (Cost %d XP)", boostDuration, xpToUse));
            }
            return ActionResult.sidedSuccess(player.getItemInHand(hand), world.isClientSide());
        } else {
            return ActionResult.pass(player.getItemInHand(hand));
        }
    }

    protected byte getBoostDuration(ItemStack stack) {
        CompoundNBT tag = stack.getOrCreateTag();
        if (tag.contains("Duration")) {
            return tag.getByte("Duration");
        }
        return 1;
    }

    protected void setBoostDuration(ItemStack stack, byte duration) {
        CompoundNBT tag = stack.getOrCreateTag();
        tag.putByte("Duration", duration);
    }

    protected int changeBoostDuration(ItemStack stack) {
        byte boostDuration = this.getBoostDuration(stack);
        if (boostDuration < this.maxBoostDuration) {
            boostDuration++;
        } else {
            boostDuration = 1;
        }
        setBoostDuration(stack, boostDuration);
        return boostDuration;
    }

    protected void spawnXPOrb(World world, BlockPos pos, int value) {
        ExperienceOrbEntity experienceOrbEntity = new ExperienceOrbEntity(world, pos.getX(), pos.getY(), pos.getZ(), value);
        world.addFreshEntity(experienceOrbEntity);
        LOGGER.debug(String.format("Spawn XP orb at %s with value: %d", pos, value));
    }
}

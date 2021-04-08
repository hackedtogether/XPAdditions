package org.hackedtogether.xpadditions.common.item;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.FireworkRocketEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hackedtogether.xpadditions.util.XPUtils;

public class XPFireworkRocketItem extends Item {

    private static final Logger LOGGER = LogManager.getLogger();
    private static final int maxBoostDuration = 5;
    private static final int xpPerSecond = 2;

    public XPFireworkRocketItem(Properties properties) {
        super(properties);
    }

    public ActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {

        if (world.isClientSide) {
            LOGGER.debug("PlayerEntity: '" + player.getUUID() + "' using XP firework rocket");
        }

        ItemStack stack = player.getItemInHand(hand);

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
                int xpToUse = xpPerSecond * boostDuration;
                int playerXP = XPUtils.getPlayerXP(player);

                // Check if they have any XP
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

    private int changeBoostDuration(ItemStack stack) {
        byte boostDuration = this.getBoostDuration(stack);
        if (boostDuration < this.maxBoostDuration) {
            boostDuration++;
        } else {
            boostDuration = 1;
        }
        setBoostDuration(stack, boostDuration);
        return boostDuration;
    }
}

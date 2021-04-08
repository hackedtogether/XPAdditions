package org.hackedtogether.xpadditions.common.item;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.FireworkRocketEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hackedtogether.xpadditions.util.XPUtils;

public class XPFireworkRocketItem extends Item {

    private static final Logger LOGGER = LogManager.getLogger();
    private static final int boostDuration = 5;
    private static final int xpPerSecond = 2;

    public XPFireworkRocketItem(Properties properties) {
        super(properties);
    }

    public ActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {

        // Check if the player is gliding
        if (player.isFallFlying()) {

            ItemStack stack = player.getItemInHand(hand);
            int xpToUse = xpPerSecond * boostDuration;

            // Check if they have enough XP
            if (XPUtils.getPlayerXP(player) < xpToUse) {
                return ActionResult.fail(player.getItemInHand(hand));
            }

            // Boost
            if (!world.isClientSide) {
                stack.getOrCreateTagElement("Fireworks").putByte("Flight", (byte) boostDuration);
                world.addFreshEntity(new FireworkRocketEntity(world, stack, player));
                XPUtils.addPlayerXP(player, -(xpToUse));
            }

            return ActionResult.sidedSuccess(player.getItemInHand(hand), world.isClientSide());
        } else {
            return ActionResult.pass(player.getItemInHand(hand));
        }
    }
}

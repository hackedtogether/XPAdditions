package org.hackedtogether.xpadditions.common.item;

import com.google.common.collect.Lists;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.FireworkRocketEntity;
import net.minecraft.item.*;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.*;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hackedtogether.xpadditions.common.entity.XPFireworkRocketEntity;
import org.hackedtogether.xpadditions.util.XPUtils;

import javax.annotation.Nullable;
import java.util.List;

public class XPFireworkRocketItem extends Item {

    private static final Logger LOGGER = LogManager.getLogger();
    private static final int maxBoostDuration = 5;
    private static final int xpPerSecondOfBoost = 2;
    private static final int defaultXPPerExplosion = 100;

    private int flightDuration = 1;

    public XPFireworkRocketItem(Properties properties) {
        super(properties);
    }

    public ActionResultType useOn(ItemUseContext context) {

        World world = context.getLevel();
        ItemStack itemstack = context.getItemInHand();
        Vector3d vector3d = context.getClickLocation();
        Direction direction = context.getClickedFace();
        PlayerEntity player = context.getPlayer();

        // Check if the player is crouching (Change boost duration)
        if (player.isCrouching()) {

            // Change/fetch the boost duration
            this.changeFlightDuration(player, itemstack);
        }

        else if (!world.isClientSide) {

            LOGGER.debug("PlayerEntity: '" + player.getUUID() + "' launching XP firework rocket");

            int xpToUse = defaultXPPerExplosion;
            int playerXP = XPUtils.getPlayerXP(player);

            // Creative players can create XP
            if (!player.isCreative()) {

                // If they don't have enough XP, reduce the explosion size
                if (playerXP < xpToUse) {
                    xpToUse = playerXP;
                }

                // Remove the player's XP
                XPUtils.addPlayerXP(player, -xpToUse);
                LOGGER.debug(String.format("Removing %d XP from player", xpToUse));
            }

            // Add the explosion to the firework
            // TODO: Can we add this data earlier?
            this.save(itemstack);

            // Create the firework
            XPFireworkRocketEntity xpFireworkRocketEntity = new XPFireworkRocketEntity(world, vector3d.x + (double) direction.getStepX() * 0.15D, vector3d.y + (double) direction.getStepY() * 0.15D, vector3d.z + (double) direction.getStepZ() * 0.15D, itemstack, xpToUse);
            world.addFreshEntity(xpFireworkRocketEntity);
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

            // TODO: This is being called twice
            // Change/fetch the boost duration
            this.changeFlightDuration(player, stack);

            return ActionResult.pass(player.getItemInHand(hand));
        }

        // Check if the player is gliding (Boost)
        else if (player.isFallFlying()) {
            if (!world.isClientSide) {

                int flightDuration = this.flightDuration;
                int xpToUse = xpPerSecondOfBoost * flightDuration;
                int playerXP = XPUtils.getPlayerXP(player);

                // Check if the player has any XP
                if (playerXP == 0) {
                    return ActionResult.fail(player.getItemInHand(hand));
                }

                // If they don't have enough XP, reduce the boost duration
                if (playerXP < xpToUse) {
                    flightDuration = (int) Math.ceil(flightDuration * (playerXP / xpToUse));
                    xpToUse = playerXP;
                }

                // Boost
                world.addFreshEntity(new FireworkRocketEntity(world, getNonExplosiveCopy(stack, flightDuration), player));
                XPUtils.addPlayerXP(player, -(xpToUse));

                LOGGER.debug(String.format("Boosting for %s seconds (Cost %d XP)", flightDuration, xpToUse));
            }
            return ActionResult.sidedSuccess(player.getItemInHand(hand), world.isClientSide());
        } else {
            return ActionResult.pass(player.getItemInHand(hand));
        }
    }

    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(ItemStack stack, @Nullable World world, List<ITextComponent> list, ITooltipFlag p_77624_4_) {
        CompoundNBT compoundnbt = stack.getTagElement("Fireworks");
        if (compoundnbt != null) {
            if (compoundnbt.contains("Flight", 99)) {
                list.add((new TranslationTextComponent("item.minecraft.firework_rocket.flight")).append(" ").append(String.valueOf((int)compoundnbt.getByte("Flight"))).withStyle(TextFormatting.GRAY));
            }
        }
    }

    protected void save(ItemStack stack) {

        // Colors
        List<Integer> colors = Lists.newArrayList();
        colors.add(DyeColor.LIME.getFireworkColor());
        colors.add(DyeColor.YELLOW.getFireworkColor());

        // Explosion tags
        CompoundNBT explosion = new CompoundNBT();

        // Set explosion values
        explosion.putByte("Type", (byte) FireworkRocketItem.Shape.LARGE_BALL.getId());
        explosion.putBoolean("Flicker", true);
        explosion.putBoolean("Trail", true);
        explosion.putIntArray("Colors", colors);
        explosion.putIntArray("FadeColors", colors);

        // Add explosion tags to NBTList
        ListNBT explosions = new ListNBT();
        explosions.add(explosion);

        // Fireworks
        CompoundNBT fireworks = stack.getOrCreateTagElement("Fireworks");
        fireworks.putByte("Flight", (byte) this.flightDuration);
        fireworks.put("Explosions", explosions);
    }

    protected void load(ItemStack stack) {
        CompoundNBT fireworks = stack.getOrCreateTagElement("Fireworks");
        this.flightDuration = (int) fireworks.getByte("Flight");
    }

    protected void changeFlightDuration(PlayerEntity player, ItemStack stack) {

        this.load(stack);

        // Change the duration
        if (this.flightDuration < this.maxBoostDuration) {
            this.flightDuration++;
        } else {
            this.flightDuration = 1;
        }

        this.save(stack);

        // Display a message and play a sound
        player.displayClientMessage(new TranslationTextComponent("message.xpadditions.change_flight_duration", this.flightDuration, maxBoostDuration), true);
        player.playSound(SoundEvents.STONE_BUTTON_CLICK_OFF, 1F, 1F);

        LOGGER.debug(String.format("Set boost duration to %d/%d", this.flightDuration, maxBoostDuration));
    }

    protected ItemStack getNonExplosiveCopy(ItemStack stack, int flightDuration) {
        ItemStack nonExplosiveStack = stack.copy();
        CompoundNBT fireworks = nonExplosiveStack.getOrCreateTagElement("Fireworks");
        fireworks.remove("Explosions");
        fireworks.putByte("Flight", (byte) flightDuration);
        return nonExplosiveStack;
    }
}

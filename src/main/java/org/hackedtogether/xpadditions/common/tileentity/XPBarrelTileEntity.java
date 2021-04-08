package org.hackedtogether.xpadditions.common.tileentity;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hackedtogether.xpadditions.util.XPUtils;
import org.hackedtogether.xpadditions.common.registries.ModTileEntityTypes;

public class XPBarrelTileEntity extends TileEntity implements ITickableTileEntity {

    protected int storedXP;
    private static int maxStoredXP = 1395;

    private static final Logger LOGGER = LogManager.getLogger();

    public XPBarrelTileEntity() {
        super(ModTileEntityTypes.XP_BARREL.get());
    }

    @Override
    public void tick() {
        // Test code that will delete the block under the barrel.
        // this.level.setBlockAndUpdate(this.worldPosition.below(), Blocks.AIR.defaultBlockState());
    }

    @Override
    public CompoundNBT save(CompoundNBT nbt) {
        super.save(nbt);
        nbt.putInt("xp", this.storedXP);
        return nbt;
    }

    @Override
    public void load(BlockState state, CompoundNBT nbt) {
        super.load(state, nbt);
        this.storedXP = nbt.getInt("xp");
    }

    public int getStoredXP() {
        return this.storedXP;
    }

    public void addXP(int xp) {
        xp = this.storedXP + xp;
        if (xp < 0) {
            throw new NumberFormatException("Stored XP cannot be negative");
        } else if (xp > this.maxStoredXP) {
            throw new NumberFormatException("Stored XP cannot be greater than " + this.maxStoredXP);
        }
        this.storedXP = xp;
    }

    public int getRemainingSpace() {
        return this.maxStoredXP - this.storedXP;
    }

    //
    // Dispensing
    //

    public int getMaxDispensableXP() {
        return this.storedXP;
    }

    public void dispenseXPLevelToPlayer(PlayerEntity player) {

        int xpForNextLevel = XPUtils.getXPForLevel(player.experienceLevel+1);
        int xpToMove = xpForNextLevel - XPUtils.getPlayerXP(player);

        xpToMove = Math.min(this.getStoredXP(), xpToMove);

        dispenseXPToPlayer(player, xpToMove);
    }

    public void dispenseAllXPToPlayer(PlayerEntity player) {
        dispenseXPToPlayer(player, this.getMaxDispensableXP());
    }

    public void dispenseXPToPlayer(PlayerEntity player, int xp) {
        LOGGER.debug(String.format("Dispensing %d XP to '%s'", xp, player.getUUID()));
        if (xp > 0) {
            XPUtils.addPlayerXP(player, xp);
            this.addXP(-xp);
        }
    }

    //
    // Draining
    //

    public int getMaxDrainableXP(PlayerEntity player) {
        return Math.min(this.getRemainingSpace(), player.totalExperience);
    }

    public void drainXPLevelFromPlayer(PlayerEntity player) {

        int xpForCurrentLevel = XPUtils.getXPForLevel(player.experienceLevel);
        int xpToMove = XPUtils.getPlayerXP(player) - xpForCurrentLevel;

        // Player has exactly x > 0 levels (xp bar looks empty)
        if (xpToMove == 0 && player.experienceLevel > 0) {
            xpToMove = xpForCurrentLevel - XPUtils.getXPForLevel(player.experienceLevel - 1);
        }

        xpToMove = Math.min(this.getRemainingSpace(), xpToMove);

        drainXPFromPlayer(player, xpToMove);
    }

    public void drainAllXPFromPlayer(PlayerEntity player) {
        drainXPFromPlayer(player, this.getMaxDrainableXP(player));
    }

    public void drainXPFromPlayer(PlayerEntity player, int xp) {
        LOGGER.debug(String.format("Draining %d XP from '%s'", xp, player.getUUID()));
        if (xp > 0) {
            XPUtils.addPlayerXP(player, -xp);
            this.addXP(xp);
        }
    }
}

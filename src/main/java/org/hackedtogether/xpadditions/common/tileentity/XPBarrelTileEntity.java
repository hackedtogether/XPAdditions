package org.hackedtogether.xpadditions.common.tileentity;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import org.hackedtogether.xpadditions.common.registries.ModTileEntityTypes;
import org.hackedtogether.xpadditions.util.XPUtils;

public class XPBarrelTileEntity extends TileEntity implements ITickableTileEntity {

    protected int storedXP;
    private static int maxStoredXP = 1395;

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
        }
        this.storedXP = xp;
    }

    public int getRemainingSpace() {
        return this.maxStoredXP - this.storedXP;
    }

    public int getMaxTransferableXP(PlayerEntity player) {
        return Math.min(this.getRemainingSpace(), player.totalExperience);
    }

    public void transferXPLevelFromPlayer(PlayerEntity player) {

        int xpForCurrentLevel = XPUtils.getXPForLevel(player.experienceLevel);
        int xpToMove = XPUtils.getPlayerXP(player) - xpForCurrentLevel;

        // Player has exactly x > 0 levels (xp bar looks empty)
        if (xpToMove == 0 && player.experienceLevel > 0) {
            xpToMove = xpForCurrentLevel - XPUtils.getXPForLevel(player.experienceLevel - 1);
        }

        transferXPFromPlayer(player, xpToMove);
    }

    public void transferAllXPFromPlayer(PlayerEntity player) {
        transferXPFromPlayer(player, this.getMaxTransferableXP(player));
    }

    public void transferXPFromPlayer(PlayerEntity player, int xp) {
        if (xp > 0) {
            player.giveExperiencePoints(-xp);
            this.addXP(xp);
        }
    }
}

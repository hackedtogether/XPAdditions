package org.hackedtogether.xpadditions.common.tileentity;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hackedtogether.xpadditions.common.registries.ModTileEntityTypes;
import org.hackedtogether.xpadditions.util.XPUtils;

public class XPBarrelTileEntity extends TileEntity implements ITickableTileEntity {

    protected int storedXP = 0;
    private static final String XP_NBT_KEY = "xp";
    private static final int maxStoredXP = 1395;

    private static final Logger LOGGER = LogManager.getLogger();

    public XPBarrelTileEntity() {
        super(ModTileEntityTypes.XP_BARREL.get());
    }

    @Override
    public void tick() {
    }

    @Override
    public CompoundNBT save(CompoundNBT nbt) {
        super.save(nbt);
        nbt.putInt(XP_NBT_KEY, this.storedXP);
        return nbt;
    }

    @Override
    public void load(BlockState state, CompoundNBT nbt) {
        super.load(state, nbt);
        this.storedXP = nbt.getInt(XP_NBT_KEY);
    }

    //
    // State
    //
    public int getStoredXP() {
        return this.storedXP;
    }

    public int getRemainingSpace() {
        return maxStoredXP - this.storedXP;
    }


    //
    // Action
    //
    public void addXP(int xp) {
        xp = this.storedXP + xp;
        if (xp < 0) {
            throw new NumberFormatException("Stored XP cannot be negative");
        } else if (xp > maxStoredXP) {
            throw new NumberFormatException("Stored XP cannot be greater than " + maxStoredXP);
        }
        this.storedXP = xp;
    }

    //
    // Barrel to Player
    //
    public void barrelLevelToPlayer(PlayerEntity player) {
        int xpTotalForNextLevel = XPUtils.convertLevelToXP(player.experienceLevel + 1);
        int xpToMove = xpTotalForNextLevel - XPUtils.getTotalXPOfPlayer(player);

        barrelXPToPlayer(player, xpToMove);
    }

    public void barrelXPToPlayer(PlayerEntity player, int xp) {
        // Make sure barrel has enough xp to give
        xp = Math.min(this.getStoredXP(), xp);

        LOGGER.debug(String.format("Transferring %d XP to player '%s'", xp, player.getUUID()));
        if (xp > 0) {
            XPUtils.addXPToPlayer(player, xp);
            this.addXP(-xp);
        }
    }

    public void allBarrelXPToPlayer(PlayerEntity player) {
        barrelXPToPlayer(player, this.getStoredXP());
    }

    //
    // Player to Barrel
    //
    public void playerLevelToBarrel(PlayerEntity player) {

        // Does player have XP to move
        int playersXP = XPUtils.getTotalXPOfPlayer(player);
        if (playersXP == 0) {
            return;
        }

        int xpToMove = 0;
        int currentLevelAsXP = XPUtils.convertLevelToXP(player.experienceLevel);
        int totalXPOfPlayer = XPUtils.getTotalXPOfPlayer(player);

        if (totalXPOfPlayer > currentLevelAsXP) {
            LOGGER.debug("Player has partial level, so draining to exact level amount");
            xpToMove = totalXPOfPlayer - currentLevelAsXP;
        }
        if (totalXPOfPlayer == currentLevelAsXP) {
            LOGGER.debug("Player has exact level amount, so draining a complete level");
            xpToMove = currentLevelAsXP - XPUtils.convertLevelToXP(player.experienceLevel - 1);
        }

        playerXPtoBarrel(player, xpToMove);
    }

    public void playerXPtoBarrel(PlayerEntity player, int xp) {
        //Make sure barrel can store the amount of XP
        xp = Math.min(this.getRemainingSpace(), xp);
        LOGGER.debug(String.format("Draining %d XP from '%s'", xp, player.getUUID()));
        if (xp > 0) {
            XPUtils.addXPToPlayer(player, -xp);
            this.addXP(xp);
        }
    }

    public void allPlayerXPToBarrel(PlayerEntity player) {
        playerXPtoBarrel(player, player.totalExperience);
    }
}

package org.hackedtogether.xpadditions.common.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hackedtogether.xpadditions.common.tileentity.XPBarrelTileEntity;
import org.hackedtogether.xpadditions.util.XPUtils;

public class XPBarrelBlock extends Block {

    private static final Logger LOGGER = LogManager.getLogger();

    public XPBarrelBlock(Properties properties) {
        super(properties);
    }

    private XPBarrelTileEntity getTileEntity(IBlockReader world, BlockPos pos) {
        return (XPBarrelTileEntity) world.getBlockEntity(pos);
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new XPBarrelTileEntity();
    }

    @Override
    public int getExpDrop(BlockState state, IWorldReader world, BlockPos pos, int fortune, int silktouch) {
        XPBarrelTileEntity te = this.getTileEntity(world, pos);
        return te.getStoredXP();
    }

    @Override
    public void attack(BlockState state, World world, BlockPos pos, PlayerEntity player) {

        if (world.isClientSide()) {
            return;
        }

        LOGGER.debug("PlayerEntity: '{}' attacking barrel", player.getUUID());

        // Fetch the XP barrel tile entity
        XPBarrelTileEntity te = this.getTileEntity(world, pos);

        // Get the barrel's total XP
        int barrelXP = te.getStoredXP();

        LOGGER.debug(String.format("[Before] Player XP: %d (Level %d)", XPUtils.getTotalXPOfPlayer(player), player.experienceLevel));
        LOGGER.debug(String.format("[Before] Barrel XP: %d", barrelXP));

        // If the barrel has any XP
        if (barrelXP > 0) {

            // Should we dispense all XP or just 1 level
            if (player.isCrouching()) {
                te.allBarrelXPToPlayer(player);
            } else {
                te.barrelLevelToPlayer(player);
            }
        }

        LOGGER.debug(String.format("[After] Player XP: %d (Level %d)", XPUtils.getTotalXPOfPlayer(player), player.experienceLevel));
        LOGGER.debug(String.format("[After] Barrel XP: %d", te.getStoredXP()));

        super.attack(state, world, pos, player);
    }

    @Override
    public ActionResultType use(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit) {

        if (world.isClientSide()) {
            return ActionResultType.SUCCESS;
        }

        LOGGER.debug("PlayerEntity: '{}' using barrel", player.getUUID());

        XPBarrelTileEntity te = this.getTileEntity(world, pos);

        // Get the player's total XP
        int playerXP = XPUtils.getTotalXPOfPlayer(player);

        LOGGER.debug(String.format("[Before] Player XP: %d (Level %d)", XPUtils.getTotalXPOfPlayer(player), player.experienceLevel));
        LOGGER.debug(String.format("[Before] Barrel XP: %d", te.getStoredXP()));

        // If the player has any XP
        if (playerXP > 0) {

            // Should we drain all XP or just 1 level
            if (player.isCrouching()) {
                te.allPlayerXPToBarrel(player);
            } else {
                te.playerLevelToBarrel(player);
            }
        }

        LOGGER.debug(String.format("[After] Player XP: %d (Level %d)", XPUtils.getTotalXPOfPlayer(player), player.experienceLevel));
        LOGGER.debug(String.format("[After] Barrel XP: %d", te.getStoredXP()));

        return super.use(state, world, pos, player, hand, hit);
    }
}

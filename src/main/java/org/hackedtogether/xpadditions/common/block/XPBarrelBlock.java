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

public class XPBarrelBlock extends Block {

    private static final Logger LOGGER = LogManager.getLogger();

    public XPBarrelBlock(Properties properties) {
        super(properties);
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
        XPBarrelTileEntity te = (XPBarrelTileEntity) world.getBlockEntity(pos);
        return te.getStoredXP();
    }

    @Override
    public ActionResultType use(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit) {

        if (world.isClientSide()) {
            return ActionResultType.SUCCESS;
        }

        int playerXP = player.totalExperience;
        int playerLevel = player.experienceLevel;
        float playerLevelProgress = player.experienceProgress;

        LOGGER.debug("PlayerEntity: '" + player.getUUID() + "' using barrel");
        LOGGER.debug("before player xp level: " + playerLevel);
        LOGGER.debug("before player xp total: " + playerXP);

        if (playerXP > 0) {

            // Fetch the tile entity
            XPBarrelTileEntity te = (XPBarrelTileEntity) world.getBlockEntity(pos);
            LOGGER.debug("before barrel xp: " + te.getStoredXP());
            LOGGER.debug("xp progress: " + playerLevelProgress);

            // Should we move all XP or just 1 level
            if (player.isCrouching()) {
                te.transferAllXPFromPlayer(player);
            } else {
                te.transferXPLevelFromPlayer(player);
            }

            LOGGER.debug("after player xp level: " + player.experienceLevel);
            LOGGER.debug("after player xp total: " + player.totalExperience);
            LOGGER.debug("after barrel xp: " + te.getStoredXP());
        }

        return super.use(state, world, pos, player, hand, hit);
    }
}

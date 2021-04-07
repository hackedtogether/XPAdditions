package org.hackedtogether.xpadditions.common.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;
import org.hackedtogether.xpadditions.common.tileentity.XPBarrelTileEntity;

public class XPBarrelBlock extends Block {

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
}

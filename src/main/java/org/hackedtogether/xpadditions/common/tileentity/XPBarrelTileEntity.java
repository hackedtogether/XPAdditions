package org.hackedtogether.xpadditions.common.tileentity;

import net.minecraft.block.Blocks;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import org.hackedtogether.xpadditions.setup.ModTileEntityTypes;

public class XPBarrelTileEntity extends TileEntity implements ITickableTileEntity {

    public XPBarrelTileEntity() {
        super(ModTileEntityTypes.XP_BARREL.get());
    }

    @Override
    public void tick() {
        // Test code that will delete the block under the barrel.
        this.level.setBlockAndUpdate(this.worldPosition.below(), Blocks.AIR.defaultBlockState());
    }
}

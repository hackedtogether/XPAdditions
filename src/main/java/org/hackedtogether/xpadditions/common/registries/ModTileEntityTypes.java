package org.hackedtogether.xpadditions.common.registries;

import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fml.RegistryObject;
import org.hackedtogether.xpadditions.common.registration.ModTileEntityRegister;
import org.hackedtogether.xpadditions.common.tileentity.XPBarrelTileEntity;

public class ModTileEntityTypes {
    public static final RegistryObject<TileEntityType<XPBarrelTileEntity>> XP_BARREL = ModTileEntityRegister.register("xp_barrel", XPBarrelTileEntity::new, ModBlocks.XP_BARREL);

    public static void register() {
    }
}

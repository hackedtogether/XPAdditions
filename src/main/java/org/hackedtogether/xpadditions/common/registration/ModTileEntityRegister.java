package org.hackedtogether.xpadditions.common.registration;

import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import org.hackedtogether.xpadditions.XPAdditions;
import org.hackedtogether.xpadditions.common.registries.ModTileEntityTypes;

import java.util.function.Supplier;

public class ModTileEntityRegister {

    public static final DeferredRegister<TileEntityType<?>> TILE_ENTITIES = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, XPAdditions.MOD_ID);

    public ModTileEntityRegister(IEventBus modEventBus) {
        TILE_ENTITIES.register(modEventBus);
        ModTileEntityTypes.register();
    }

    public static <T extends TileEntity> RegistryObject<TileEntityType<T>> register(String name, Supplier<T> factory, RegistryObject<? extends Block> block) {
        return TILE_ENTITIES.register(name, () -> {
            return TileEntityType.Builder.of(factory, block.get()).build(null);
        });
    }
}

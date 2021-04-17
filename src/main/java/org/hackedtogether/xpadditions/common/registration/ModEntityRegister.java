package org.hackedtogether.xpadditions.common.registration;

import net.minecraft.entity.EntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import org.hackedtogether.xpadditions.XPAdditions;
import org.hackedtogether.xpadditions.common.registries.ModEntities;

public class ModEntityRegister {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITIES, XPAdditions.MOD_ID);

    public ModEntityRegister(IEventBus modEventBus) {
        ENTITY_TYPES.register(modEventBus);
        ModEntities.register();
    }
// TODO:
//    private static RegistryObject<EntityType<?>> register(String name, Supplier<EntityType> entity) {
//        return ENTITY_TYPES.register(name, entity);
//    }
}

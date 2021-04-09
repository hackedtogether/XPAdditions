package org.hackedtogether.xpadditions.common.registration;

import net.minecraft.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import org.hackedtogether.xpadditions.XPAdditions;
import org.hackedtogether.xpadditions.common.registries.ModItems;

import java.util.function.Supplier;

public class ModItemRegister {

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, XPAdditions.MOD_ID);

    public ModItemRegister(IEventBus modEventBus) {
        ITEMS.register(modEventBus);
        ModItems.register();
    }

    public static RegistryObject<Item> register(String name, Supplier<Item> item) {
        return ITEMS.register(name, item);
    }
}

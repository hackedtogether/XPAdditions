package org.hackedtogether.xpadditions.common.registration;

import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

public class Registration {

    public static void register() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        new ModBlockRegister(modEventBus);
        new ModTileEntityRegister(modEventBus);
        new ModItemRegister(modEventBus);
        new ModEntityRegister(modEventBus);
    }
}

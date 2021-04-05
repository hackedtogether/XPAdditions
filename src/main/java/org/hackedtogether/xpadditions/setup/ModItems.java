package org.hackedtogether.xpadditions.setup;

import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.fml.RegistryObject;

import java.util.function.Supplier;

public class ModItems {

    public static final RegistryObject<Item> XP_INGOT = register("xp_ingot", () -> new Item(new Item.Properties().tab(ItemGroup.TAB_MATERIALS)));

    private static RegistryObject<Item> register(String name, Supplier<Item> item) {
        return Registration.ITEMS.register(name, item);
    }

    static void register() {}
}

package org.hackedtogether.xpadditions.common.registries;

import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.fml.RegistryObject;
import org.hackedtogether.xpadditions.common.registration.ItemRegister;

public class ModItems {
    public static final RegistryObject<Item> XP_INGOT = ItemRegister.register("xp_ingot", () -> new Item(new Item.Properties().tab(ItemGroup.TAB_MATERIALS)));

    public static void register () {}
}

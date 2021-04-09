package org.hackedtogether.xpadditions.common.tags;


import net.minecraft.item.Item;
import net.minecraft.tags.ITag;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;
import org.hackedtogether.xpadditions.XPAdditions;

public class ModItemTags {
    public static final ITag.INamedTag<Item> STORAGE_BLOCKS_XP = forge("storage_blocks/xp");
    public static final ITag.INamedTag<Item> INGOTS_XP = forge("ingots/xp");

    private static ITag.INamedTag<Item> forge(String path) {
        return ItemTags.bind(new ResourceLocation("forge", path).toString());
    }

    private static ITag.INamedTag<Item> mod(String path) {
        return ItemTags.bind(new ResourceLocation(XPAdditions.MOD_ID, path).toString());
    }
}

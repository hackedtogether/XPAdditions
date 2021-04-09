package org.hackedtogether.xpadditions.common.tags;

import net.minecraft.block.Block;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ITag;
import net.minecraft.util.ResourceLocation;
import org.hackedtogether.xpadditions.XPAdditions;

public class ModBlockTags {
    public static final ITag.INamedTag<Block> STORAGE_BLOCKS_XP = forge("storage_blocks/xp");

    private static ITag.INamedTag<Block> forge(String path) {
        return BlockTags.bind(new ResourceLocation("forge", path).toString());
    }

    private static ITag.INamedTag<Block> mod(String path) {
        return BlockTags.bind(new ResourceLocation(XPAdditions.MOD_ID, path).toString());
    }
}

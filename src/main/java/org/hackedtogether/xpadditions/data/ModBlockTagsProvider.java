package org.hackedtogether.xpadditions.data;

import net.minecraft.data.BlockTagsProvider;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.hackedtogether.xpadditions.XPAdditions;
import org.hackedtogether.xpadditions.common.registries.ModBlocks;
import org.hackedtogether.xpadditions.common.tags.ModBlockTags;

public class ModBlockTagsProvider extends BlockTagsProvider {
    public ModBlockTagsProvider(DataGenerator generatorIn, ExistingFileHelper existingFileHelper) {
        super(generatorIn, XPAdditions.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags() {
        tag(ModBlockTags.STORAGE_BLOCKS_XP).add(ModBlocks.XP_BLOCK.get());
        tag(Tags.Blocks.STORAGE_BLOCKS).addTag(ModBlockTags.STORAGE_BLOCKS_XP);
    }
}

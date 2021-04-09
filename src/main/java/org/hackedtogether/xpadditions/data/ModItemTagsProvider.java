package org.hackedtogether.xpadditions.data;

import net.minecraft.data.BlockTagsProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.ItemTagsProvider;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.hackedtogether.xpadditions.XPAdditions;
import org.hackedtogether.xpadditions.common.registries.ModItems;
import org.hackedtogether.xpadditions.common.tags.ModItemTags;
import org.hackedtogether.xpadditions.common.tags.ModBlockTags;

public class ModItemTagsProvider extends ItemTagsProvider {
    public ModItemTagsProvider(DataGenerator dataGenerator, BlockTagsProvider blockTagProvider, ExistingFileHelper existingFileHelper) {
        super(dataGenerator, blockTagProvider, XPAdditions.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags() {
        copy(ModBlockTags.STORAGE_BLOCKS_XP, ModItemTags.STORAGE_BLOCKS_XP);
        copy(Tags.Blocks.STORAGE_BLOCKS, Tags.Items.STORAGE_BLOCKS);

        tag(ModItemTags.INGOTS_XP).add(ModItems.XP_INGOT.get());
        tag(Tags.Items.INGOTS).addTag(ModItemTags.INGOTS_XP);
    }
}

package org.hackedtogether.xpadditions.data;

import net.minecraft.data.BlockTagsProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.ItemTagsProvider;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.hackedtogether.xpadditions.XPAdditions;
import org.hackedtogether.xpadditions.setup.ModItems;
import org.hackedtogether.xpadditions.setup.ModTags;

public class ModItemTagsProvider extends ItemTagsProvider {
    public ModItemTagsProvider(DataGenerator dataGenerator, BlockTagsProvider blockTagProvider, ExistingFileHelper existingFileHelper) {
        super(dataGenerator, blockTagProvider, XPAdditions.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags() {
        copy(ModTags.Blocks.STORAGE_BLOCKS_XP, ModTags.Items.STORAGE_BLOCKS_XP);
        copy(Tags.Blocks.STORAGE_BLOCKS, Tags.Items.STORAGE_BLOCKS);

        tag(ModTags.Items.INGOTS_XP).add(ModItems.XP_INGOT.get());
        tag(Tags.Items.INGOTS).addTag(ModTags.Items.INGOTS_XP);
    }
}
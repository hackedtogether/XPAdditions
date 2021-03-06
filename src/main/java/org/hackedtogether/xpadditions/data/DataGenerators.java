package org.hackedtogether.xpadditions.data;

import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;
import org.hackedtogether.xpadditions.XPAdditions;
import org.hackedtogether.xpadditions.data.client.ModBlockStateProvider;
import org.hackedtogether.xpadditions.data.client.ModItemModelProvider;

@Mod.EventBusSubscriber(modid = XPAdditions.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class DataGenerators {
    private DataGenerators() {}

    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator gen = event.getGenerator();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();

        gen.addProvider(new ModBlockStateProvider(gen, existingFileHelper));
        gen.addProvider(new ModItemModelProvider(gen, existingFileHelper));

        ModBlockTagsProvider blockTags = new ModBlockTagsProvider(gen, existingFileHelper);
        ModItemTagsProvider itemTags = new ModItemTagsProvider(gen, blockTags, existingFileHelper);

        gen.addProvider(blockTags);
        gen.addProvider(itemTags);

        gen.addProvider(new ModLootTableProvider(gen));
        gen.addProvider(new ModRecipeProvider(gen));
    }
}

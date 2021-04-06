package org.hackedtogether.xpadditions.data.client;

import net.minecraft.data.DataGenerator;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.hackedtogether.xpadditions.XPAdditions;
import org.hackedtogether.xpadditions.common.registries.ModBlocks;

public class ModBlockStateProvider extends BlockStateProvider {
    public ModBlockStateProvider(DataGenerator gen, ExistingFileHelper exFileHelper) {
        super(gen, XPAdditions.MOD_ID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        simpleBlock(ModBlocks.XP_BLOCK.get());

        getVariantBuilder(ModBlocks.XP_BARREL.get()).forAllStates(state ->
            ConfiguredModel.builder()
                    .modelFile(models().cubeBottomTop("xp_barrel",
                            modLoc("block/xp_barrel_side"),
                            modLoc("block/xp_barrel_bottom"),
                            modLoc("block/xp_barrel_top")))
                    .build()
        );
    }
}

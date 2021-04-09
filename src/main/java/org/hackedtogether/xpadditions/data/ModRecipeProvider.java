package org.hackedtogether.xpadditions.data;

import net.minecraft.data.*;
import org.hackedtogether.xpadditions.common.registries.ModBlocks;
import org.hackedtogether.xpadditions.common.registries.ModItems;

import java.util.function.Consumer;

public class ModRecipeProvider extends RecipeProvider {
    public ModRecipeProvider(DataGenerator generatorIn) {
        super(generatorIn);
    }

    @Override
    protected void buildShapelessRecipes(Consumer<IFinishedRecipe> consumer) {
        ShapelessRecipeBuilder.shapeless(ModItems.XP_INGOT.get(), 9)
                .requires(ModBlocks.XP_BLOCK.get())
                .unlockedBy("has_item", has(ModItems.XP_INGOT.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(ModBlocks.XP_BLOCK.get())
                .define('#', ModItems.XP_INGOT.get())
                .pattern("###")
                .pattern("###")
                .pattern("###")
                .unlockedBy("has_item", has(ModItems.XP_INGOT.get()))
                .save(consumer);
    }
}

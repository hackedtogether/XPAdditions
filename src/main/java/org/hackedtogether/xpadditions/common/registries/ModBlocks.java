package org.hackedtogether.xpadditions.common.registries;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraftforge.fml.RegistryObject;
import org.hackedtogether.xpadditions.common.registration.BlockRegister;

public class ModBlocks {
    public static final RegistryObject<Block> XP_BLOCK = BlockRegister.register("xp_block", () -> new Block(AbstractBlock.Properties.of(Material.METAL).strength(3, 10).sound(SoundType.METAL)));

    public static void register () {}
}

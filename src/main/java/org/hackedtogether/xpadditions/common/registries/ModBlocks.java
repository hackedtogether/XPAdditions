package org.hackedtogether.xpadditions.common.registries;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraftforge.fml.RegistryObject;
import org.hackedtogether.xpadditions.block.barrel.XPBarrelBlock;
import org.hackedtogether.xpadditions.common.registration.ModBlockRegister;

public class ModBlocks {
    public static final RegistryObject<Block> XP_BLOCK = ModBlockRegister.register("xp_block", () -> new Block(AbstractBlock.Properties.of(Material.METAL).strength(3, 10).sound(SoundType.METAL)));
    public static final RegistryObject<XPBarrelBlock> XP_BARREL = ModBlockRegister.register("xp_barrel", () -> new XPBarrelBlock(AbstractBlock.Properties.of(Material.WOOD).strength(2, 10).sound(SoundType.WOOD)));

    public static void register () {}
}

package org.hackedtogether.xpadditions.common.registries;

import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraftforge.fml.RegistryObject;
import org.hackedtogether.xpadditions.common.entity.XPFireworkRocketEntity;
import org.hackedtogether.xpadditions.common.registration.ModEntityRegister;

public class ModEntities {

    // TODO: We shouldn't directly call ModEntityRegister.ENTITY_TYPES. Instead, we should be calling ModEntityRegister.register()
    public static final RegistryObject<EntityType<XPFireworkRocketEntity>> XP_FIREWORK_ROCKET = ModEntityRegister.ENTITY_TYPES.register(
            "xp_firework_rocket",
            () -> EntityType.Builder.<XPFireworkRocketEntity>of(XPFireworkRocketEntity::new, EntityClassification.MISC)
                .sized(0.25F, 0.25F)
                .clientTrackingRange(4)
                .updateInterval(10)
                .build("xp_firework_rocket")
    );

    public static void register () {}
}

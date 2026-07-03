package net.motikun.individualdrop.register;

import net.minecraft.core.UUIDUtil;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.motikun.individualdrop.IndividualDrop;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.UUID;

public class ModDataComponents {
    public static final DeferredRegister.DataComponents REGISTRAR =
            DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE, IndividualDrop.MOD_ID);

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<UUID>> ITEM_OWNER = REGISTRAR.registerComponentType(
            "item_owner",
            builder -> builder
                    .persistent(UUIDUtil.CODEC)
                    .networkSynchronized(UUIDUtil.STREAM_CODEC)
    );
}

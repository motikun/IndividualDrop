package net.motikun.individualdrop;

import net.motikun.individualdrop.register.ModDataComponents;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;

@Mod(IndividualDrop.MOD_ID)
public class IndividualDrop {
    public static final String MOD_ID = "individualdrop";

    public IndividualDrop(IEventBus modEventBus) {
        ModDataComponents.REGISTRAR.register(modEventBus);
    }
}

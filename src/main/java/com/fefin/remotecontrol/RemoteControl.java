package com.fefin.remotecontrol;

import com.fefin.remotecontrol.blocks.ModBlocks;
import com.fefin.remotecontrol.registry.ModDataComponents;
import com.fefin.remotecontrol.registry.ModItemGroups;
import com.fefin.remotecontrol.registry.ModItems;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;

/**
 * Clase principal de RemoteControl (NeoForge 1.21.1).
 * Solo se encarga de enganchar los registros diferidos al bus del mod.
 */
@Mod(RemoteControl.MOD_ID)
public class RemoteControl {

    public static final String MOD_ID = "remotecontrol";

    public RemoteControl(IEventBus modEventBus) {
        ModDataComponents.register(modEventBus);
        ModBlocks.register(modEventBus);
        ModItems.register(modEventBus);
        ModItemGroups.register(modEventBus);
    }
}

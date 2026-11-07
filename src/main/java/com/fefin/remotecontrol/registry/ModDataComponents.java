package com.fefin.remotecontrol.registry;

import com.fefin.remotecontrol.RemoteControl;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponentType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

/**
 * Componentes de datos personalizados (sustituyen al NBT de los ItemStack en 1.21).
 */
public final class ModDataComponents {

    private static final DeferredRegister.DataComponents DATA_COMPONENTS =
            DeferredRegister.createDataComponents(RemoteControl.MOD_ID);

    /**
     * Posicion guardada por el Remote Control.
     * persistent: se guarda en disco (mundo/inventario). networkSynchronized: se envia al cliente (tooltip).
     */
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<BlockPos>> LINKED_POS =
            DATA_COMPONENTS.registerComponentType("linked_pos", builder -> builder
                    .persistent(BlockPos.CODEC)
                    .networkSynchronized(BlockPos.STREAM_CODEC));

    private ModDataComponents() {
    }

    public static void register(IEventBus modEventBus) {
        DATA_COMPONENTS.register(modEventBus);
    }
}

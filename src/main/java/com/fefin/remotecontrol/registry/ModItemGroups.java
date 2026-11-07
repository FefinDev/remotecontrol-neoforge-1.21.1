package com.fefin.remotecontrol.registry;

import com.fefin.remotecontrol.RemoteControl;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

/** Registro de pestanas del inventario creativo. */
public final class ModItemGroups {

    private static final DeferredRegister<CreativeModeTab> TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, RemoteControl.MOD_ID);

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> REMOTE_CONTROL_TAB = TABS.register(
            "remotecontrol",
            () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.remotecontrol"))
                    .icon(() -> new ItemStack(ModItems.REMOTE_CONTROL.get()))
                    .displayItems((parameters, output) -> {
                        output.accept(ModItems.REMOTE_CONTROL.get());
                        output.accept(ModItems.CONTROL_BLOCK_ITEM.get());
                    })
                    .build());

    private ModItemGroups() {
    }

    public static void register(IEventBus modEventBus) {
        TABS.register(modEventBus);
    }
}

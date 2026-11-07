package com.fefin.remotecontrol.registry;

import com.fefin.remotecontrol.RemoteControl;
import com.fefin.remotecontrol.blocks.ModBlocks;
import com.fefin.remotecontrol.items.RemoteControlItem;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

/** Registro de objetos. */
public final class ModItems {

    private static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(RemoteControl.MOD_ID);

    public static final DeferredItem<RemoteControlItem> REMOTE_CONTROL = ITEMS.registerItem(
            "remote_control",
            RemoteControlItem::new,
            new Item.Properties().stacksTo(1));

    public static final DeferredItem<BlockItem> CONTROL_BLOCK_ITEM =
            ITEMS.registerSimpleBlockItem("control_block", ModBlocks.CONTROL_BLOCK);

    private ModItems() {
    }

    public static void register(IEventBus modEventBus) {
        ITEMS.register(modEventBus);
    }
}

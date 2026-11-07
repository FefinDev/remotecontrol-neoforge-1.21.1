package com.fefin.remotecontrol.blocks;

import com.fefin.remotecontrol.RemoteControl;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

/** Registro de bloques. */
public final class ModBlocks {

    private static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(RemoteControl.MOD_ID);

    public static final DeferredBlock<ControlBlock> CONTROL_BLOCK = BLOCKS.registerBlock(
            "control_block",
            ControlBlock::new,
            BlockBehaviour.Properties.of()
                    .mapColor(MapColor.METAL)
                    .strength(3.0F, 6.0F)
                    .sound(SoundType.METAL)
                    .requiresCorrectToolForDrops()
                    // Brilla un poco cuando esta encendido (coherente con la textura ON).
                    .lightLevel(state -> state.getValue(ControlBlock.POWERED) ? 7 : 0));

    private ModBlocks() {
    }

    public static void register(IEventBus modEventBus) {
        BLOCKS.register(modEventBus);
    }
}

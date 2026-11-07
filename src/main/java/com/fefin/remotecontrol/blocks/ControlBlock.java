package com.fefin.remotecontrol.blocks;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

/**
 * Bloque que emite redstone nivel 15 en todas las direcciones mientras POWERED = true.
 * No lee redstone entrante ni programa ticks: solo cambia de estado mediante {@link #toggle}.
 */
public class ControlBlock extends Block {

    public static final MapCodec<ControlBlock> CODEC = simpleCodec(ControlBlock::new);
    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;

    public ControlBlock(Properties properties) {
        super(properties);
        registerDefaultState(stateDefinition.any().setValue(POWERED, false));
    }

    @Override
    protected MapCodec<? extends Block> codec() {
        return CODEC;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(POWERED);
    }

    /** Invierte POWERED y avisa a los vecinos. Llamar solo desde el servidor. */
    public void toggle(Level level, BlockPos pos, BlockState state) {
        level.setBlock(pos, state.setValue(POWERED, !state.getValue(POWERED)), Block.UPDATE_ALL);
        updateNeighbours(level, pos);
    }

    /**
     * Notifica a los bloques vecinos y a los vecinos de estos (necesario para
     * la senal fuerte que atraviesa bloques solidos, igual que palancas y botones).
     */
    private void updateNeighbours(Level level, BlockPos pos) {
        level.updateNeighborsAt(pos, this);
        for (Direction direction : Direction.values()) {
            level.updateNeighborsAt(pos.relative(direction), this);
        }
    }

    @Override
    protected boolean isSignalSource(BlockState state) {
        return true;
    }

    @Override
    protected int getSignal(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
        return state.getValue(POWERED) ? 15 : 0;
    }

    @Override
    protected int getDirectSignal(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
        return state.getValue(POWERED) ? 15 : 0;
    }

    @Override
    protected void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
        // Si se rompe encendido, hay que apagar la senal de los vecinos.
        if (!movedByPiston && !state.is(newState.getBlock()) && state.getValue(POWERED)) {
            updateNeighbours(level, pos);
        }
        super.onRemove(state, level, pos, newState, movedByPiston);
    }
}

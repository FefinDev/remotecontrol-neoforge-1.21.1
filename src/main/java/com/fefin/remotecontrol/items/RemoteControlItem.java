package com.fefin.remotecontrol.items;

import com.fefin.remotecontrol.blocks.ControlBlock;
import com.fefin.remotecontrol.registry.ModDataComponents;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

/**
 * Control remoto.
 * - Shift + clic derecho sobre un bloque: guarda su posicion.
 * - Clic derecho normal (aire o bloque): alterna el Control Block guardado.
 *
 * Toda la logica que toca el mundo corre solo en el servidor; el cliente unicamente
 * devuelve el resultado "sidedSuccess" para animar el brazo. Asi no hay doble activacion.
 */
public class RemoteControlItem extends Item {

    public RemoteControlItem(Properties properties) {
        super(properties);
    }

    /**
     * Se ejecuta ANTES de la interaccion del bloque clicado. Por eso el mando funciona
     * igual sobre cofres, puertas, palancas, etc., y nunca guarda/alterna dos veces.
     */
    @Override
    public InteractionResult onItemUseFirst(ItemStack stack, UseOnContext context) {
        Level level = context.getLevel();
        Player player = context.getPlayer();

        if (!level.isClientSide) {
            if (player != null && player.isShiftKeyDown()) {
                savePosition(level, context.getClickedPos(), stack, player);
            } else {
                toggleLinkedBlock(level, stack);
            }
        }
        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    /** Clic derecho en el aire. */
    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        // Shift en el aire no hace nada: guardar requiere apuntar a un bloque.
        if (player.isShiftKeyDown()) {
            return InteractionResultHolder.pass(stack);
        }
        if (!level.isClientSide) {
            toggleLinkedBlock(level, stack);
        }
        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide);
    }

    private static void savePosition(Level level, BlockPos pos, ItemStack stack, Player player) {
        BlockPos saved = pos.immutable();
        stack.set(ModDataComponents.LINKED_POS.get(), saved);
        // Sonido unico: lo emite solo el servidor (player = null para que lo oiga tambien quien hizo clic).
        level.playSound(null, saved, SoundEvents.EXPERIENCE_ORB_PICKUP, SoundSource.PLAYERS, 1.0F, 1.0F);
        player.displayClientMessage(
                Component.translatable("message.remotecontrol.position_saved", saved.getX(), saved.getY(), saved.getZ()),
                true);
    }

    private static void toggleLinkedBlock(Level level, ItemStack stack) {
        BlockPos pos = stack.get(ModDataComponents.LINKED_POS.get());
        if (pos == null || !level.isLoaded(pos)) {
            return; // sin posicion guardada o chunk descargado: no hacer nada
        }
        BlockState state = level.getBlockState(pos);
        if (state.getBlock() instanceof ControlBlock controlBlock) {
            controlBlock.toggle(level, pos, state);
        }
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.translatable("tooltip.remotecontrol.save_position"));
        tooltip.add(Component.translatable("tooltip.remotecontrol.toggle"));
        BlockPos pos = stack.get(ModDataComponents.LINKED_POS.get());
        if (pos != null) {
            tooltip.add(Component.translatable("tooltip.remotecontrol.saved_position",
                    pos.getX(), pos.getY(), pos.getZ()));
        }
    }
}

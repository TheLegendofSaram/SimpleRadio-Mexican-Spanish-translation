package com.codinglitch.simpleradio.core.central;

import com.codinglitch.simpleradio.CompatCore;
import com.codinglitch.simpleradio.radio.CommonRadioPlugin;
import com.codinglitch.simpleradio.radio.RadioChannel;
import com.codinglitch.simpleradio.radio.RadioListener;
import de.maxhenkel.voicechat.api.VoicechatConnection;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public interface Transmitting extends Frequencing {

    static boolean validateTransmitter(BlockEntity block, @Nullable Frequency frequency) {
        BlockState state = block.getBlockState();
        if (state.isAir()) return false;

        if (state.getBlock().asItem() instanceof Transmitting transmitting)
            return frequency == null || transmitting.getFrequency(block) == frequency;
        return false;
    }
    static boolean validateTransmitter(WorldlyPosition position, @Nullable Frequency frequency) {
        BlockPos pos = position.realLocation();

        if (!position.level.isLoaded(pos)) return false;

        BlockState state = position.level.getBlockState(pos);
        if (state.isAir()) return false;

        BlockEntity blockEntity = position.level.getBlockEntity(pos);

        if (state.getBlock().asItem() instanceof Transmitting transmitting)
            return frequency == null || transmitting.getFrequency(blockEntity) == frequency;
        return false;
    }
    static boolean validateTransmitter(UUID uuid, @Nullable Frequency frequency) {
        VoicechatConnection connection = CommonRadioPlugin.serverApi.getConnectionOf(uuid);
        if (connection != null) return validateTransmitter(connection, frequency);
        return false;
    }
    static boolean validateTransmitter(VoicechatConnection connection, @Nullable Frequency frequency) {
        ServerPlayer player = (ServerPlayer) connection.getPlayer().getPlayer();
        if (player == null) return false;
        return validateTransmitter(player, frequency);
    }
    static boolean validateTransmitter(ServerPlayer player, @Nullable Frequency frequency) { //TODO: find way to support entities
        return player.getInventory().hasAnyMatching(stack -> {
            if (stack.getItem() instanceof Transmitting transmitting)
                return frequency == null || transmitting.getFrequency(stack) == frequency;
            return false;
        });
    }

    /**
     * Start listening in the world.
     * @param owner the Entity that will listen
     * @return The listener created.
     */
    default RadioListener startListening(Entity owner) {
        return RadioListener.getOrCreateListener(owner);
    }
    /**
     * Start listening in the world.
     * @param location the location to listen to
     * @return The listener created.
     */
    default RadioListener startListening(WorldlyPosition location) {
        return RadioListener.getOrCreateListener(location);
    }

    /**
     * Stop listening in the world.
     * @param owner the Entity that will stop listening
     */
    default void stopListening(Entity owner) {
        RadioListener.removeListener(owner);
    }
    /**
     * Stop listening in the world.
     * @param location the location of the listener to remove
     */
    default void stopListening(WorldlyPosition location) {
        RadioListener.removeListener(location);
    }

    default void setState(Entity entity, State state) {
        switch (state) {
            case TRANSMITTING -> entity.addTag("transmissionActive");
            case INACTIVE -> entity.removeTag("transmissionActive");
        }
    }

    enum State {
        TRANSMITTING,
        INACTIVE
    }
}

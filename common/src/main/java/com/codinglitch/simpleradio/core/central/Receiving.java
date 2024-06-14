package com.codinglitch.simpleradio.core.central;

import com.codinglitch.simpleradio.radio.CommonRadioPlugin;
import com.codinglitch.simpleradio.radio.RadioChannel;
import de.maxhenkel.voicechat.api.VoicechatConnection;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public interface Receiving extends Frequencing {

    static boolean validateReceiver(WorldlyPosition position, @Nullable Frequency frequency) {
        if (!position.level.isLoaded(position.blockPos())) return false;

        BlockState state = position.level.getBlockState(position.blockPos());
        if (state.isAir()) return false;

        BlockEntity blockEntity = position.level.getBlockEntity(position.blockPos());

        if (state.getBlock().asItem() instanceof Receiving receiving)
            return frequency == null || receiving.getFrequency(blockEntity) == frequency;
        return false;
    }
    static boolean validateReceiver(UUID uuid, @Nullable Frequency frequency) {
        VoicechatConnection connection = CommonRadioPlugin.serverApi.getConnectionOf(uuid);
        if (connection != null) return validateReceiver(connection, frequency);
        return false;
    }
    static boolean validateReceiver(VoicechatConnection connection, @Nullable Frequency frequency) {
        ServerPlayer player = (ServerPlayer) connection.getPlayer().getPlayer();
        if (player == null) return false;
        return validateReceiver(player, frequency);
    }
    static boolean validateReceiver(ServerPlayer player, @Nullable Frequency frequency) {
        return player.getInventory().hasAnyMatching(stack -> {
            if (stack.getItem() instanceof Receiving receiving)
                return frequency == null || receiving.getFrequency(stack) == frequency;
            return false;
        });
    }

    /**
     * Start receiving in a certain frequency.
     * @param frequencyName the frequency to listen to
     * @param modulation the modulation type of the frequency
     * @param owner the UUID that will listen
     * @return The channel created from the listener.
     */
    default RadioChannel startReceiving(String frequencyName, Frequency.Modulation modulation, UUID owner) {
        Frequency frequency = Frequency.getOrCreateFrequency(frequencyName, modulation);
        return frequency.tryAddReceiver(owner);
    }

    /**
     * Stop listening in a certain frequency
     * @param frequencyName the frequency to stop listening to
     * @param modulation the modulation type of the frequency
     * @param owner the UUID to remove
     */
    default void stopReceiving(String frequencyName, Frequency.Modulation modulation, UUID owner) {
        Frequency frequency = Frequency.getOrCreateFrequency(frequencyName, modulation);
        frequency.removeReceiver(owner);
    }
}

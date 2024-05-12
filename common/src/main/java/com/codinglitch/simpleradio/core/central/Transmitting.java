package com.codinglitch.simpleradio.core.central;

import com.codinglitch.simpleradio.radio.CommonRadioPlugin;
import com.codinglitch.simpleradio.radio.RadioChannel;
import de.maxhenkel.voicechat.api.VoicechatConnection;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;
import java.util.Random;
import java.util.UUID;

public interface Transmitting extends Frequencing {

    static boolean validateTransmitter(WorldlyPosition position, Frequency frequency) {
        BlockState state = position.level.getBlockState(position.blockPos());
        if (state.isAir()) return false;

        BlockEntity blockEntity = position.level.getBlockEntity(position.blockPos());

        if (state.getBlock().asItem() instanceof Transmitting transmitting)
            return transmitting.getFrequency(blockEntity) == frequency;
        return false;
    }
    static boolean validateTransmitter(UUID uuid, Frequency frequency) {
        VoicechatConnection connection = CommonRadioPlugin.serverApi.getConnectionOf(uuid);
        if (connection != null) return validateTransmitter(connection, frequency);
        return false;
    }
    static boolean validateTransmitter(VoicechatConnection connection, Frequency frequency) {
        ServerPlayer player = (ServerPlayer) connection.getPlayer().getPlayer();
        if (player == null) return false;

        return player.getInventory().hasAnyMatching(stack -> {
            if (stack.getItem() instanceof Transmitting transmitting)
                return transmitting.getFrequency(stack) == frequency;
            return false;
        });
    }
}

package com.codinglitch.simpleradio.core.registry.blocks;

import com.codinglitch.simpleradio.core.central.*;
import com.codinglitch.simpleradio.core.registry.SimpleRadioBlockEntities;
import com.codinglitch.simpleradio.core.registry.SimpleRadioSounds;
import com.codinglitch.simpleradio.radio.RadioChannel;
import com.codinglitch.simpleradio.radio.RadioListener;
import com.codinglitch.simpleradio.radio.RadioManager;
import com.codinglitch.simpleradio.radio.RadioSource;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import java.util.UUID;

public class MicrophoneBlockEntity extends FrequencyBlockEntity implements Transmitting {
    public boolean isListening = false;
    public UUID listenerID;

    public MicrophoneBlockEntity(BlockPos pos, BlockState state) {
        super(SimpleRadioBlockEntities.MICROPHONE, pos, state);

        this.listenerID = UUID.randomUUID();
    }

    @Override
    public void setRemoved() {
        if (level != null) {
            level.playSound(
                    null, this.worldPosition,
                    SimpleRadioSounds.RADIO_CLOSE,
                    SoundSource.PLAYERS,
                    1f,1f
            );
        }


        if (this.frequency != null)
            stopListening(WorldlyPosition.of(getBlockPos(), level));
        super.setRemoved();
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        loadTag(tag);
    }

    @Override
    public void saveAdditional(CompoundTag tag) {
        saveTag(tag);
        super.saveAdditional(tag);
    }

    @Override
    public void saveToItem(ItemStack stack) {
        saveTag(stack.getOrCreateTag());
        super.saveToItem(stack);
    }

    public static void tick(Level level, BlockPos pos, BlockState blockState, MicrophoneBlockEntity blockEntity) {
        if (!level.isClientSide) {
            if (blockEntity.frequency != null && !blockEntity.isListening) {
                blockEntity.listen();
            }
        }
    }

    public void listen() {
        RadioListener listener = startListening(WorldlyPosition.of(getBlockPos(), level));
        listener.range = 12;
        listener.acceptor(source -> {
            source.type = RadioSource.Type.TRANSMITTER;
            source.delegate(listenerID);

            Frequency frequency = getFrequency(this);
            if (frequency != null) RadioManager.transmit(source, frequency);
        });

        this.frequency.tryAddTransmitter(listener);

        level.playSound(
                null, this.worldPosition,
                SimpleRadioSounds.RADIO_OPEN,
                SoundSource.PLAYERS,
                1f,1f
        );

        this.isListening = true;
    }

    public void loadFromItem(ItemStack stack) {
        loadTag(stack.getOrCreateTag());
    }

    public void loadTag(CompoundTag tag) {
        if (this.frequency != null) {
            stopListening(WorldlyPosition.of(getBlockPos(), level));
            this.isListening = false;
        }

        String frequencyName = tag.getString("frequency");
        Frequency.Modulation modulation = Frequency.modulationOf(tag.getString("modulation"));
        this.frequency = Frequency.getOrCreateFrequency(frequencyName, modulation);
    }

    public void saveTag(CompoundTag tag) {
        if (this.frequency == null) return;

        tag.putString("frequency", this.frequency.frequency);
        tag.putString("modulation", this.frequency.modulation.shorthand);
    }
}

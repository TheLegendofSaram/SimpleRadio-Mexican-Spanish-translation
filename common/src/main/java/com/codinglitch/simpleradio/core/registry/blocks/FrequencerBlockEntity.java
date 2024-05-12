package com.codinglitch.simpleradio.core.registry.blocks;

import com.codinglitch.simpleradio.core.central.Frequency;
import com.codinglitch.simpleradio.core.central.Receiving;
import com.codinglitch.simpleradio.core.central.WorldlyPosition;
import com.codinglitch.simpleradio.core.registry.SimpleRadioBlockEntities;
import com.codinglitch.simpleradio.core.registry.SimpleRadioSounds;
import com.codinglitch.simpleradio.radio.CommonRadioPlugin;
import com.codinglitch.simpleradio.radio.RadioChannel;
import com.codinglitch.simpleradio.radio.RadioManager;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class FrequencerBlockEntity extends BlockEntity {
    public Frequency frequency;
    public List<String> listeners = new ArrayList<>();
    public List<String> frequencies = new ArrayList<>();


    public FrequencerBlockEntity(BlockPos pos, BlockState state) {
        super(SimpleRadioBlockEntities.FREQUENCER, pos, state);
    }

    public static void tick(Level level, BlockPos pos, BlockState blockState, FrequencerBlockEntity blockEntity) {
        if (Math.round(level.getGameTime()) % 20 == 0 && !level.isClientSide) {
            blockEntity.frequencies.clear();
            blockEntity.listeners.clear();

            //---- Revalidation ----\\
            if (blockEntity.frequency != null) {
                Frequency frequency = Frequency.getFrequency(blockEntity.frequency.frequency, blockEntity.frequency.modulation);
                if (frequency != blockEntity.frequency && frequency != null)
                    blockEntity.frequency = frequency;
            }

            if (blockEntity.frequency != null) {
                //---- Listener gathering and parsing ----\\
                for (RadioChannel listener : blockEntity.frequency.listeners) {
                    Player player = level.getPlayerByUUID(listener.owner);
                    if (player == null) {
                        BlockPos blockPos = listener.location.blockPos();
                        BlockEntity listenerBlock = level.getBlockEntity(blockPos);
                        if (listenerBlock != null) {
                            blockEntity.listeners.add(
                                    listenerBlock.getBlockState().getBlock().getName().getString()+" at "+blockPos.toShortString()
                            );
                        }
                    } else {
                        String playerName = player.getDisplayName().getString();
                        blockEntity.listeners.add(playerName);
                    }
                }

                level.sendBlockUpdated(pos, blockState, blockState, 2);
            } else {
                //---- Frequency gathering ----\\
                List<Frequency> frequencies = Frequency.getFrequencies();
                for (Frequency frequency : frequencies) {
                    blockEntity.frequencies.add(frequency.frequency + frequency.modulation.shorthand);
                }

                level.sendBlockUpdated(pos, blockState, blockState, 2);
            }
        }
    }

    public void setFrequency(Frequency frequency) {
        this.frequency = frequency;
        level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 2);
    }

    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag tag = new CompoundTag();
        saveTag(tag);
        return tag;
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        CompoundTag tag = new CompoundTag();
        saveTag(tag);
        return ClientboundBlockEntityDataPacket.create(this, blockEntity -> tag);
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

    public void loadTag(CompoundTag tag) {
        if (tag.contains("frequency")) {
            String frequencyName = tag.getString("frequency");
            Frequency.Modulation modulation = Frequency.modulationOf(tag.getString("modulation"));
            this.frequency = Frequency.getOrCreateFrequency(frequencyName, modulation);
        } else {
            this.frequency = null;
        }

        CompoundTag listeners = tag.getCompound("listeners");
        this.listeners.clear();
        for (String key : listeners.getAllKeys()) {
            this.listeners.add(listeners.getString(key));
        }

        CompoundTag frequencies = tag.getCompound("frequencies");
        this.frequencies.clear();
        for (String key : frequencies.getAllKeys()) {
            this.frequencies.add(frequencies.getString(key));
        }
    }

    public void saveTag(CompoundTag tag) {
        if (this.frequency != null) {
            tag.putString("frequency", this.frequency.frequency);
            tag.putString("modulation", this.frequency.modulation.shorthand);
        }

        CompoundTag listeners = new CompoundTag();
        for (int i = 0; i < this.listeners.size(); i++) {
            listeners.putString(String.valueOf(i), this.listeners.get(i));
        }
        tag.put("listeners", listeners);

        CompoundTag frequencies = new CompoundTag();
        for (int i = 0; i < this.frequencies.size(); i++) {
            frequencies.putString(String.valueOf(i), this.frequencies.get(i));
        }
        tag.put("frequencies", frequencies);
    }
}

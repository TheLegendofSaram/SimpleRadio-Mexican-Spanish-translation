package com.codinglitch.simpleradio.core.registry.items;

import com.codinglitch.simpleradio.CommonSimpleRadio;
import com.codinglitch.simpleradio.core.central.Frequency;
import com.codinglitch.simpleradio.core.central.Receiving;
import com.codinglitch.simpleradio.core.central.Transmitting;
import com.codinglitch.simpleradio.core.networking.packets.ClientboundRadioPacket;
import com.codinglitch.simpleradio.core.registry.SimpleRadioSounds;
import com.codinglitch.simpleradio.platform.Services;
import com.codinglitch.simpleradio.radio.RadioListener;
import com.codinglitch.simpleradio.radio.RadioManager;
import com.codinglitch.simpleradio.radio.RadioSource;
import net.minecraft.client.particle.Particle;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.VibrationParticleOption;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.BlockPositionSource;
import net.minecraft.world.level.gameevent.EntityPositionSource;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Random;
import java.util.UUID;

public class WalkieTalkieItem extends TransceiverItem {
    public WalkieTalkieItem(Properties settings) {
        super(settings);
    }

    private Random RANDOM = new Random();

    @Override
    public String getDefaultFrequency() {
        StringBuilder frequency = new StringBuilder();

        for (int i = 0; i < CommonSimpleRadio.SERVER_CONFIG.frequency.wholePlaces; i++) {
            frequency.append(RANDOM.nextInt(0, 9));
        }
        frequency.append(".");
        for (int i = 0; i < CommonSimpleRadio.SERVER_CONFIG.frequency.decimalPlaces; i++) {
            frequency.append(RANDOM.nextInt(0, 9));
        }

        return frequency.toString();
    }

    public void worldTick(ItemEntity item, Level level) {
        ItemStack myStack = item.getItem();
        this.tick(myStack, level);

        if (item.tickCount > 60 && item.tickCount % 10 == 0) {
            CompoundTag myTag = myStack.getOrCreateTag();

            for (Entity entity : level.getEntities(item, item.getBoundingBox().inflate(1.0d))) {
                if (entity instanceof ItemEntity otherItem) {
                    if (!(otherItem.getItem().getItem() instanceof WalkieTalkieItem)) continue;

                    ItemStack theirStack = otherItem.getItem();
                    CompoundTag theirTag = theirStack.getOrCreateTag();
                    if (!theirTag.contains("frequency")) continue;
                    if (!theirTag.contains("modulation")) continue;
                    if (theirTag.getString("frequency").equals(myTag.getString("frequency")) &&
                            theirTag.getString("modulation").equals(myTag.getString("modulation"))) continue;

                    myTag.putString("frequency", theirTag.getString("frequency"));
                    myTag.putString("modulation", theirTag.getString("modulation"));

                    level.playSound(null, item, SoundEvents.ALLAY_ITEM_TAKEN, SoundSource.MASTER, 1, 1);

                    for (int i = 0; i < 3; i++) {
                        level.addParticle(ParticleTypes.ELECTRIC_SPARK,
                                item.getRandomX(0.5D), 0.25D + item.getRandomY(), item.getRandomZ(0.5D),
                                RANDOM.nextDouble(-0.2, 0.2), RANDOM.nextDouble(-0.2, 0.2), RANDOM.nextDouble(-0.2, 0.2)
                        );
                    }

                    level.addParticle(new VibrationParticleOption(new EntityPositionSource(item, 0.25f), 5), otherItem.getX(), otherItem.getY() + 0.25f, otherItem.getZ(), 0.0, 0.0, 0.0);
                }
            }
        }
    }

    @Override
    public int getCooldown() {
        return 60;
    }
}

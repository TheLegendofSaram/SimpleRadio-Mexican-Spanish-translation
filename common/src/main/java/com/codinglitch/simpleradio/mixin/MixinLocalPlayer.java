package com.codinglitch.simpleradio.mixin;

import com.codinglitch.simpleradio.SimpleRadioLibrary;
import com.codinglitch.simpleradio.core.registry.items.TransceiverItem;
import com.codinglitch.simpleradio.core.registry.items.WalkieTalkieItem;
import com.mojang.authlib.GameProfile;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin(value = LocalPlayer.class)
public abstract class MixinLocalPlayer extends AbstractClientPlayer {

    public MixinLocalPlayer(ClientLevel level, GameProfile profile) {
        super(level, profile);
    }

    @Shadow public abstract boolean isUsingItem();

    @Shadow @Nullable private InteractionHand usingItemHand;

    @Unique
    private boolean willSlow() {
        if (this.isUsingItem()) {
            ItemStack stack = this.getItemInHand(this.usingItemHand);
            if (stack.getItem().getClass() == TransceiverItem.class) {
                return SimpleRadioLibrary.SERVER_CONFIG.transceiver.transceiverSlow;
            } else if (stack.getItem().getClass() == WalkieTalkieItem.class) {
                return SimpleRadioLibrary.SERVER_CONFIG.walkie_talkie.walkieTalkieSlow;
            }
        }
        return this.isUsingItem();
    }

    // temporarily removed redirects due to Iron's Spellbooks conflict
    // TODO: find an alternative for the slowdown config
}
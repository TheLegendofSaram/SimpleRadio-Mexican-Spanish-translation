package com.codinglitch.simpleradio.mixin;

import com.codinglitch.simpleradio.CommonSimpleRadio;
import com.codinglitch.simpleradio.core.registry.items.TransceiverItem;
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
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(LocalPlayer.class)
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
            if (stack.getItem() instanceof TransceiverItem) {
                return CommonSimpleRadio.SERVER_CONFIG.transceiver.transceiverSlow;
            }
        }
        return this.isUsingItem();
    }

    @Redirect(method = "aiStep()V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;isUsingItem()Z"))
    private boolean simpleradio$aiStep(LocalPlayer instance) {
        return willSlow();
    }

    @Redirect(method = "canStartSprinting", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;isUsingItem()Z"))
    private boolean simpleradio$canStartSprinting(LocalPlayer instance) {
        return willSlow();
    }
}
package com.codinglitch.simpleradio.mixin;

import com.codinglitch.simpleradio.CommonSimpleRadio;
import com.codinglitch.simpleradio.core.central.Frequency;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.ChatType;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;

@Mixin(MinecraftServer.class)
public class MixinMinecraftServer {

    @Shadow private int tickCount;

    @Inject(at = @At("TAIL"), method = "tickServer(Ljava/util/function/BooleanSupplier;)V")
    private void simpleradio$tickServer(CallbackInfo info) {
        if (this.tickCount % 20 == 0) {
            CommonSimpleRadio.garbageCollect();
        }
    }
}
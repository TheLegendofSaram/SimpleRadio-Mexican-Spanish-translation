package com.codinglitch.simpleradio.platform;

import com.codinglitch.simpleradio.CommonSimpleRadio;
import com.codinglitch.simpleradio.compat.InteractionCompat;
import com.codinglitch.simpleradio.core.central.Packeter;
import com.codinglitch.simpleradio.platform.services.CompatPlatform;
import com.codinglitch.simpleradio.platform.services.NetworkingHelper;
import com.codinglitch.simpleradio.platform.services.PlatformHelper;
import com.codinglitch.simpleradio.radio.RadioChannel;
import com.codinglitch.simpleradio.radio.RadioSource;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.level.ServerPlayer;

public class FabricCompatPlatform implements CompatPlatform {
    @Override
    public void onData(RadioChannel channel, RadioSource source, short[] decoded) {

        // ---- Voice Chat Interaction ---- \\
        if (Services.PLATFORM.isModLoaded("vcinteraction") && CommonSimpleRadio.SERVER_CONFIG.compatibilities.voice_chat_interaction.enabled) {
            InteractionCompat.onData(channel, source, decoded);
        }
    }
}

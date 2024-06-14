package com.codinglitch.simpleradio.platform;

import com.codinglitch.simpleradio.CommonSimpleRadio;
import com.codinglitch.simpleradio.compat.InteractionCompat;
import com.codinglitch.simpleradio.compat.ValkyrienCompat;
import com.codinglitch.simpleradio.core.central.WorldlyPosition;
import com.codinglitch.simpleradio.platform.services.CompatPlatform;
import com.codinglitch.simpleradio.radio.RadioChannel;
import com.codinglitch.simpleradio.radio.RadioSource;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import org.joml.Vector3f;

public class FabricCompatPlatform implements CompatPlatform {
    @Override
    public void onData(RadioChannel channel, RadioSource source, short[] decoded) {

        // ---- Voice Chat Interaction ---- \\
        if (Services.PLATFORM.isModLoaded("vcinteraction") && CommonSimpleRadio.SERVER_CONFIG.compatibilities.voice_chat_interaction.enabled) {
            InteractionCompat.onData(channel, source, decoded);
        }
    }

    @Override
    public WorldlyPosition modifyPosition(BlockPos originalBlockPos, Level level) {

        // ---- Valkyrien Skies ---- \\
        return ValkyrienCompat.modifyPosition(originalBlockPos, level);
    }

    @Override
    public Vector3f modifyPosition(Level level, BlockPos originalBlockPos) {

        // ---- Valkyrien Skies ---- \\
        return ValkyrienCompat.modifyPosition(level, originalBlockPos);
    }
}

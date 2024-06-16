package com.codinglitch.simpleradio.platform.services;

import com.codinglitch.simpleradio.core.central.WorldlyPosition;
import com.codinglitch.simpleradio.radio.RadioChannel;
import com.codinglitch.simpleradio.radio.RadioSource;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import org.joml.Vector3f;

public interface CompatPlatform {
    void onData(RadioChannel channel, RadioSource source, short[] decoded);

    WorldlyPosition modifyPosition(WorldlyPosition position);
}
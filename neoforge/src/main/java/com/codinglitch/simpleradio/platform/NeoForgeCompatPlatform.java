package com.codinglitch.simpleradio.platform;

import com.codinglitch.simpleradio.core.central.WorldlyPosition;
import com.codinglitch.simpleradio.platform.services.CompatPlatform;
import com.codinglitch.simpleradio.radio.RadioChannel;
import com.codinglitch.simpleradio.radio.RadioSource;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import org.joml.Matrix4dc;
import org.joml.Vector3d;
import org.joml.Vector3f;

public class NeoForgeCompatPlatform implements CompatPlatform {
    @Override
    public void onData(RadioChannel channel, RadioSource source, short[] decoded) {

    }

    @Override
    public WorldlyPosition modifyPosition(BlockPos originalBlockPos, Level level) {
        return WorldlyPosition.of(originalBlockPos, level);
    }

    @Override
    public Vector3f modifyPosition(Level level, BlockPos originalBlockPos) {
        return new Vector3f(originalBlockPos.getX() + 0.5F, originalBlockPos.getY() + 0.5F, originalBlockPos.getZ() + 0.5F);
    }
}
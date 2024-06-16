package com.codinglitch.simpleradio.compat;

import com.codinglitch.simpleradio.core.central.WorldlyPosition;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import org.joml.Vector3f;
import org.valkyrienskies.mod.common.VSGameUtilsKt;

public class ValkyrienCompat {
    public static WorldlyPosition modifyPosition(WorldlyPosition position) {
        return CommonValkyrienCompat.modifyPosition(
                VSGameUtilsKt.getShipObjectManagingPos((ServerLevel) position.level, position.realLocation()), position
        );
    }
}

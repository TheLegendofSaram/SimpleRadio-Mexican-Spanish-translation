package com.codinglitch.simpleradio.compat;

import com.codinglitch.simpleradio.core.central.WorldlyPosition;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import org.joml.Matrix4dc;
import org.joml.Vector3d;
import org.joml.Vector3f;
import org.valkyrienskies.core.api.ships.Ship;
import org.valkyrienskies.mod.common.VSGameUtilsKt;

public class ValkyrienCompat {
    public static WorldlyPosition modifyPosition(BlockPos originalBlockPos, Level level) {
        Ship ship = VSGameUtilsKt.getShipObjectManagingPos((ServerLevel) level, originalBlockPos);
        if (ship != null) {
            WorldlyPosition pos;

            Matrix4dc shipToWorld = ship.getTransform().getShipToWorld();
            Vector3d blockPosVec = new Vector3d(originalBlockPos.getX() + 0.5D, originalBlockPos.getY() + 0.5D, originalBlockPos.getZ() + 0.5D);
            Vector3d blockOnShip = shipToWorld.transformPosition(blockPosVec);

            pos = WorldlyPosition.of(new Vector3f((float) blockOnShip.x, (float) blockOnShip.y, (float) blockOnShip.z), level, originalBlockPos);

            return pos;
        } else {
            return WorldlyPosition.of(originalBlockPos, level);
        }
    }

    public static Vector3f modifyPosition(Level level, BlockPos originalBlockPos) {
        Ship ship = VSGameUtilsKt.getShipObjectManagingPos((ServerLevel) level, originalBlockPos);
        if (ship != null) {
            Vector3f pos;

            Matrix4dc shipToWorld = ship.getTransform().getShipToWorld();
            Vector3d blockPosVec = new Vector3d(originalBlockPos.getX() + 0.5D, originalBlockPos.getY() + 0.5D, originalBlockPos.getZ() + 0.5D);
            Vector3d blockOnShip = shipToWorld.transformPosition(blockPosVec);

            pos = new Vector3f((float) blockOnShip.x, (float) blockOnShip.y, (float) blockOnShip.z);

            return pos;
        } else {
            return new Vector3f(originalBlockPos.getX() + 0.5F, originalBlockPos.getY() + 0.5F, originalBlockPos.getZ() + 0.5F);
        }
    }
}

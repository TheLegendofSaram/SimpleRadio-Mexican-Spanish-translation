package com.codinglitch.simpleradio.core.central;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import org.joml.Vector3f;

import javax.annotation.Nullable;

public class WorldlyPosition extends Vector3f {
    public Level level;

    // immutable, set on creation
    private final BlockPos realLocation;

    public WorldlyPosition(float x, float y, float z, Level level, BlockPos realLocation) {
        super(x, y, z);
        this.level = level;
        this.realLocation = realLocation;
    }
    public WorldlyPosition(float x, float y, float z, Level level) {
        this(x, y, z, level, null);
    }
    public WorldlyPosition() {
        this(0, 0, 0, null, null);
    }

    public static WorldlyPosition of(BlockPos pos, Level level, BlockPos realLocation) { // use this upon creation to save the 'real' location
        return new WorldlyPosition(pos.getX(), pos.getY(), pos.getZ(), level, realLocation);
    }
    public static WorldlyPosition of(BlockPos pos, Level level) {
        return new WorldlyPosition(pos.getX(), pos.getY(), pos.getZ(), level);
    }
    public static WorldlyPosition of(Vector3f pos, Level level, BlockPos realLocation) { // use this upon creation to save the 'real' location
        return new WorldlyPosition(pos.x, pos.y, pos.z, level, realLocation);
    }
    public static WorldlyPosition of(Vector3f pos, Level level) {
        return new WorldlyPosition(pos.x, pos.y, pos.z, level);
    }

    public Vector3f position() {
        return this;
    }

    public boolean equals(WorldlyPosition location) {
        return location.level == this.level && location.position() == this.position();
    }

    public BlockPos blockPos() {
        return new BlockPos(Math.round(this.x), Math.round(this.y), Math.round(this.z));
    }
    public BlockPos realLocation() { // used in garbage collection pretty much exclusively for VS and maybe Create: Aeronautics when released
        return this.realLocation == null ? this.blockPos() : this.realLocation;
    }

    public Vector3f dimensionScaled() {
        return this.position().mul((float) level.dimensionType().coordinateScale());
    }

    public float distance(WorldlyPosition other) {
        return this.dimensionScaled().distance(other.dimensionScaled());
    }
}

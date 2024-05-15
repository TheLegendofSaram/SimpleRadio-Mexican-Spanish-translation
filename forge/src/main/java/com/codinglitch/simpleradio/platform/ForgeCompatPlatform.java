package com.codinglitch.simpleradio.platform;

import com.codinglitch.simpleradio.platform.services.CompatPlatform;
import com.codinglitch.simpleradio.platform.services.PlatformHelper;
import com.codinglitch.simpleradio.radio.RadioChannel;
import com.codinglitch.simpleradio.radio.RadioSource;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.loading.FMLLoader;
import net.minecraftforge.fml.loading.FMLPaths;

import java.nio.file.Path;

public class ForgeCompatPlatform implements CompatPlatform {
    @Override
    public void onData(RadioChannel channel, RadioSource source, short[] decoded) {

    }
}
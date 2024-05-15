package com.codinglitch.simpleradio.platform.services;

import com.codinglitch.simpleradio.radio.RadioChannel;
import com.codinglitch.simpleradio.radio.RadioSource;

public interface CompatPlatform {
    void onData(RadioChannel channel, RadioSource source, short[] decoded);
}
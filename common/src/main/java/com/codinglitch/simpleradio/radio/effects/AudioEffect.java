package com.codinglitch.simpleradio.radio.effects;

public abstract class AudioEffect {
    public float severity;
    public float volume;

    public short[] apply(short[] data) {
        for (int i = 0; i < data.length; i++) {
            data[i] *= volume;
        }

        return data;
    }
}

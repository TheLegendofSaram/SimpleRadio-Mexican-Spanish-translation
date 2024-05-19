package com.codinglitch.simpleradio.radio;

import com.codinglitch.lexiconfig.classes.LexiconPageData;
import com.codinglitch.simpleradio.CommonSimpleRadio;
import com.codinglitch.simpleradio.core.central.Frequency;
import com.codinglitch.simpleradio.core.central.WorldlyPosition;
import org.joml.Math;

import java.util.UUID;

public class RadioSource {
    public enum Type {
        TRANSCEIVER,
        WALKIE_TALKIE,
        TRANSMITTER
    }

    public UUID owner;
    public UUID originalOwner;
    public WorldlyPosition location;
    public byte[] data;
    public Type type;

    public float volume;

    public RadioSource(UUID owner, WorldlyPosition location, byte[] data, float volume) {
        this.owner = owner;
        this.location = location;
        this.data = data;
        this.volume = volume;
    }

    public UUID getRealOwner() {
        return originalOwner == null ? owner : originalOwner;
    }

    public void delegate(UUID owner) {
        this.originalOwner = this.owner;
        this.owner = owner;
    }

    public int getMaxDistance(Frequency.Modulation modulation) {
        String pageName = this.type.toString().toLowerCase();
        LexiconPageData pageData = CommonSimpleRadio.SERVER_CONFIG.getPage(pageName);
        if (pageData == null) {
            CommonSimpleRadio.warn("Could not find page {}!", pageName);
            return 0;
        }

        return modulation == Frequency.Modulation.FREQUENCY ?
                (int) pageData.getEntry("maxFMDistance") :
                (int) pageData.getEntry("maxAMDistance");
    }

    public int getFalloff(Frequency.Modulation modulation) {
        String pageName = this.type.toString().toLowerCase();
        LexiconPageData pageData = CommonSimpleRadio.SERVER_CONFIG.getPage(pageName);
        if (pageData == null) {
            CommonSimpleRadio.warn("Could not find page {}!", pageName);
            return 0;
        }

        return modulation == Frequency.Modulation.FREQUENCY ?
                (int) pageData.getEntry("falloffFM") :
                (int) pageData.getEntry("falloffAM");
    }

    public float computeSeverity(WorldlyPosition destination, Frequency destinationFrequency) {
        int maxDistance = this.getMaxDistance(destinationFrequency.modulation);
        int falloff = this.getFalloff(destinationFrequency.modulation);
        float distance = location.distance(destination);
        float base = destinationFrequency.modulation == Frequency.Modulation.FREQUENCY ? 2 : 15;

        if (location.level.dimensionType() != destination.level.dimensionType()) {
            if (CommonSimpleRadio.SERVER_CONFIG.frequency.crossDimensional)
                base += destinationFrequency.modulation == Frequency.Modulation.FREQUENCY ? CommonSimpleRadio.SERVER_CONFIG.frequency.dimensionalInterference : 0;
            else return 100;
        }

        return Math.clamp(
                0, 100,
                base + (Math.max(0, distance - falloff) / (maxDistance - falloff)) * (100 - base)
        );
    }
}

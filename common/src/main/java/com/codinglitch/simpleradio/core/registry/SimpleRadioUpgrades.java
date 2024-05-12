package com.codinglitch.simpleradio.core.registry;

import com.codinglitch.simpleradio.CommonSimpleRadio;
import com.codinglitch.simpleradio.core.central.Upgrade;
import com.mojang.datafixers.util.Pair;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;

public class SimpleRadioUpgrades {
    private static final HashMap<ResourceLocation, Upgrade> UPGRADES = new HashMap<>();

    public static final Upgrade RANGE = register(CommonSimpleRadio.id("range"), new Upgrade(
            CommonSimpleRadio.id("range"),
            Upgrade.Type.TRANSMITTING, Upgrade.Type.RECEIVING
    ));
    public static final Upgrade CLARITY = register(CommonSimpleRadio.id("clarity"), new Upgrade(
            CommonSimpleRadio.id("clarity"),
            Upgrade.Type.EMITTING
    ));
    public static final Upgrade BATTERY = register(CommonSimpleRadio.id("battery"), new Upgrade(
            CommonSimpleRadio.id("battery"),
            Upgrade.Type.POWERING
    ));
    public static final Upgrade LATCH = register(CommonSimpleRadio.id("latch"), new Upgrade(
            CommonSimpleRadio.id("latch"),
            Upgrade.Type.LISTENING
    ));
    public static final Upgrade SOLAR = register(CommonSimpleRadio.id("solar"), new Upgrade(
            CommonSimpleRadio.id("solar"),
            Upgrade.Type.POWERING
    ));

    public static Upgrade register(ResourceLocation location, Upgrade upgrade) {
        UPGRADES.put(location, upgrade);
        return upgrade;
    }

    public static Upgrade get(ResourceLocation identifier) {
        return UPGRADES.get(identifier);
    }

    public static Upgrade get(String name) {
        return UPGRADES.get(CommonSimpleRadio.id(name));
    }

    public static void load() {}
}

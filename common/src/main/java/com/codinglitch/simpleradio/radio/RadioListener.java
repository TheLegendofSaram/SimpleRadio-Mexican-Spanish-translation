package com.codinglitch.simpleradio.radio;

import com.codinglitch.simpleradio.CommonSimpleRadio;
import com.codinglitch.simpleradio.core.central.Frequency;
import com.codinglitch.simpleradio.core.central.Receiving;
import com.codinglitch.simpleradio.core.central.Transmitting;
import com.codinglitch.simpleradio.core.central.WorldlyPosition;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import org.joml.Math;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

public class RadioListener {
    private static final List<RadioListener> listeners = new ArrayList<>();

    public static List<RadioListener> getListeners() {
        return listeners;
    }

    public static void removeListener(RadioListener listener) {
        listeners.remove(listener);
    }
    public static void removeListener(Entity owner) {
        listeners.removeIf(listener -> listener.owner == owner);
    }
    public static void removeListener(WorldlyPosition location) {
        listeners.removeIf(listener -> listener.location != null && listener.location.equals(location));
    }

    public static RadioListener getListener(Entity owner) {
        Optional<RadioListener> radioListener = listeners.stream().filter(listener -> listener.owner == owner).findFirst();
        return radioListener.orElse(null);
    }
    public static RadioListener getListener(WorldlyPosition location) {
        Optional<RadioListener> radioListener = listeners.stream().filter(listener -> listener.location == location).findFirst();
        return radioListener.orElse(null);
    }

    public static RadioListener getOrCreateListener(Entity owner) {
        RadioListener listener = getListener(owner);
        return listener != null ? listener : new RadioListener(owner);
    }
    public static RadioListener getOrCreateListener(WorldlyPosition location) {
        RadioListener listener = getListener(location);
        return listener != null ? listener : new RadioListener(location);
    }

    public static void garbageCollect() {
        listeners.removeIf(Predicate.not(RadioListener::validate));
        listeners.removeIf(listener -> listener.owner == null && listener.location == null);
    }

    public Entity owner;
    public WorldlyPosition location;

    private Consumer<RadioSource> dataAcceptor;

    public float range = 8;

    public RadioListener(Entity owner) {
        this.owner = owner;

        listeners.add(this);
    }

    public RadioListener(WorldlyPosition location) {
        this.location = location;

        listeners.add(this);
    }

    public void acceptor(Consumer<RadioSource> acceptor) {
        this.dataAcceptor = acceptor;
    }

    public void onData(RadioSource source) {
        dataAcceptor.accept(source);
    }

    public boolean validate() {
        if (owner == null) {
            if (Transmitting.)
            if (location == null || !Transmitting.validateTransmitter(location, null)) {
                invalidate();
                return false;
            }
        } else {
            if (!Transmitting.validateTransmitter((ServerPlayer) owner, null)) {
                invalidate();
                return false;
            }
        }
        return true;
    }

    public void invalidate() {
        //removeListener(this);
    }
}

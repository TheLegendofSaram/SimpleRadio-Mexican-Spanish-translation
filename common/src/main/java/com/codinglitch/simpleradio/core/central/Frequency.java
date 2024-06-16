package com.codinglitch.simpleradio.core.central;

import com.codinglitch.simpleradio.CommonSimpleRadio;
import com.codinglitch.simpleradio.SimpleRadioLibrary;
import com.codinglitch.simpleradio.radio.RadioChannel;
import com.codinglitch.simpleradio.radio.RadioListener;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Nullable;
import org.joml.Math;

import java.util.*;
import java.util.function.Predicate;

public class Frequency {
    public enum Modulation {
        FREQUENCY("FM"),
        AMPLITUDE("AM");

        public final String shorthand;

        Modulation(String shorthand) {
            this.shorthand = shorthand;
        }
    }

    private static final List<Frequency> frequencies = new ArrayList<>();

    public static String DEFAULT_FREQUENCY;
    public static Modulation DEFAULT_MODULATION = Modulation.FREQUENCY;
    public static int FREQUENCY_DIGITS;
    public static int MAX_FREQUENCY;
    public static String FREQUENCY_PATTERN;

    public boolean isValid = true;

    public final Modulation modulation;
    public final String frequency;
    public final List<RadioChannel> receivers;
    public final List<RadioListener> transmitters; //TODO: create transmitter class and replace this

    public Frequency(String frequency, Modulation modulation) {
        if (!check(frequency)) {
            CommonSimpleRadio.warn("{} does not follow frequency pattern! Replacing with default pattern {}", frequency, DEFAULT_FREQUENCY);
            frequency = DEFAULT_FREQUENCY;
        }

        this.frequency = frequency;
        this.modulation = modulation;
        this.receivers = new ArrayList<>();
        this.transmitters = new ArrayList<>();

        frequencies.add(this);
    }

    public static Frequency tryParse(String string) {
        Modulation modulation = modulationOf(string.substring(string.length() - 2));
        return getOrCreateFrequency(string.substring(0, string.length() - 2), modulation);
    }

    public static void onLexiconRevision() {
        FREQUENCY_DIGITS = SimpleRadioLibrary.SERVER_CONFIG.frequency.wholePlaces + SimpleRadioLibrary.SERVER_CONFIG.frequency.decimalPlaces;
        MAX_FREQUENCY = (int) java.lang.Math.pow(10, FREQUENCY_DIGITS);
        FREQUENCY_PATTERN = "^\\d{"+ SimpleRadioLibrary.SERVER_CONFIG.frequency.wholePlaces+"}.\\d{"+ SimpleRadioLibrary.SERVER_CONFIG.frequency.decimalPlaces+"}$";

        if (SimpleRadioLibrary.SERVER_CONFIG.frequency.defaultFrequency.equals("auto-generate")) {
            DEFAULT_FREQUENCY = "0".repeat(SimpleRadioLibrary.SERVER_CONFIG.frequency.wholePlaces)+"."+"0".repeat(SimpleRadioLibrary.SERVER_CONFIG.frequency.decimalPlaces);
        } else {
            DEFAULT_FREQUENCY = SimpleRadioLibrary.SERVER_CONFIG.frequency.defaultFrequency;
        }
    }

    public static void garbageCollect() {
        for (int i = 0; i < frequencies.size(); i++) {
            Frequency frequency = frequencies.get(i);
            frequency.receivers.removeIf(Predicate.not(RadioChannel::validate));
            frequency.transmitters.removeIf(Predicate.not(RadioListener::validate));
        }

        frequencies.removeIf(Predicate.not(Frequency::validate));
    }

    @Nullable
    public static Modulation modulationOf(String shorthand) {
        for (Modulation modulation : Modulation.values())
            if (modulation.shorthand.equals(shorthand)) return modulation;
        return null;
    }

    public static boolean check(String frequency) {
        return frequency.matches(FREQUENCY_PATTERN);
    }

    public static String incrementFrequency(String frequency, int amount) {
        int rawFrequency = Integer.parseInt(frequency.replaceAll("[.]", ""));
        String str = String.format("%0"+FREQUENCY_DIGITS+"d", Math.clamp(0, MAX_FREQUENCY-1, rawFrequency + amount));
        return new StringBuilder(str).insert(str.length() - SimpleRadioLibrary.SERVER_CONFIG.frequency.decimalPlaces, ".").toString();
    }

    public static List<Frequency> getFrequencies() {
        return frequencies;
    }

    public static int getFrequencyIndex(String string, Modulation modulation) {
        for (int i = 0; i < frequencies.size(); i++) {
            Frequency frequency = frequencies.get(i);
            if (frequency.frequency.equals(string) && frequency.modulation.equals(modulation))
                return i;
        }

        return -1;
    }

    public static Frequency getFrequency(String string, Modulation modulation) {
        for (int i = 0; i < frequencies.size(); i++) {
            Frequency frequency = frequencies.get(i);
            if (frequency.frequency.equals(string) && frequency.modulation.equals(modulation))
                return frequency;
        }

        return null;
    }

    public RadioChannel getChannel(Player player) {
        return getChannel(player.getUUID());
    }
    public RadioChannel getChannel(UUID player) {
        for (RadioChannel listener : receivers)
            if (listener.owner.equals(player)) return listener;

        return null;
    }

    @Nullable
    public RadioChannel tryAddReceiver(UUID owner) {
        if (getChannel(owner) == null)
            return addReceiver(owner);

        CommonSimpleRadio.info("Failed to add receiver {} to frequency {} as they already exist", owner, this.frequency);
        return null;
    }
    public RadioChannel addReceiver(UUID owner) {
        RadioChannel channel = new RadioChannel(owner, this);
        receivers.add(channel);

        CommonSimpleRadio.info("Added receiver {} to frequency {}", owner, this.frequency);

        return channel;
    }

    public void removeReceiver(Player player) {
        removeReceiver(player.getUUID());
    }
    public void removeReceiver(UUID player) {
        receivers.removeIf(channel -> channel.owner.equals(player));

        if (!this.validate())
            frequencies.remove(this);
    }

    @Nullable
    public RadioListener tryAddTransmitter(RadioListener transmitter) {
        return addTransmitter(transmitter);
    }
    public RadioListener addTransmitter(RadioListener transmitter) {
        transmitters.add(transmitter);

        return transmitter;
    }

    public void removeTransmitter(RadioListener transmitter) {
        transmitters.removeIf(otherTransmitter -> {
            return otherTransmitter.owner == null ?
                    otherTransmitter.location.equals(transmitter.location) :
                    otherTransmitter.owner.equals(transmitter.owner);
        });

        if (!this.validate())
            frequencies.remove(this);
    }

    public void serverTick(int tickCount) {}

    public boolean validate() {
        if (this.receivers.isEmpty() && this.transmitters.isEmpty()) {
            this.invalidate();
            return false;
        }

        return true;
    }

    public void invalidate() {
        this.isValid = false;
    }

    public Frequency revalidate() {
        return Frequency.getFrequency(this.frequency, this.modulation);
    }

    public static Frequency getOrCreateFrequency(String frequency, Modulation modulation) {
        if (frequency.isEmpty()) frequency = DEFAULT_FREQUENCY;
        if (modulation == null) modulation = DEFAULT_MODULATION;

        int index = getFrequencyIndex(frequency, modulation);
        if (index != -1) return frequencies.get(index);

        return new Frequency(frequency, modulation);
    }

    @Nullable
    public static Frequency fromTag(CompoundTag tag) {
        if (!tag.contains("frequency") || !tag.contains("modulation")) return null;

        Modulation modulation = modulationOf(tag.getString("modulation"));
        if (modulation == null) return null;

        return Frequency.getOrCreateFrequency(tag.getString("frequency"), modulation);
    }
}

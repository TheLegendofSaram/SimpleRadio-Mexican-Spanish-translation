package com.codinglitch.simpleradio;

import com.codinglitch.lexiconfig.annotations.Lexicon;
import com.codinglitch.lexiconfig.annotations.LexiconEntry;
import com.codinglitch.lexiconfig.annotations.LexiconPage;
import com.codinglitch.lexiconfig.classes.LexiconData;
import com.codinglitch.lexiconfig.classes.LexiconPageData;

@Lexicon(name = CommonSimpleRadio.ID+"-server")
public class SimpleRadioServerConfig extends LexiconData {
    @LexiconPage(comment = "These are the configurations for the transceiver item.")
    public Transceiver transceiver = new Transceiver();

    @LexiconPage(comment = "These are the configurations for the walkie talkie item.")
    public WalkieTalkie walkie_talkie = new WalkieTalkie();

    @LexiconPage(comment = "These are the configurations for the transmitter block. (IN DEVELOPMENT)")
    public Transmitter transmitter = new Transmitter();

    @LexiconPage(comment = "These are the configurations for the radio block.")
    public Radio radio = new Radio();

    @LexiconPage(comment = "These are the configurations for the microphone block.")
    public Microphone microphone = new Microphone();

    @LexiconPage(comment = "These are the configurations for the speaker block.")
    public Speaker speaker = new Speaker();

    @LexiconPage(comment = "These are the configurations for the antenna block.")
    public Antenna antenna = new Antenna();

    @LexiconPage(comment = "These are the general configurations for frequencies.")
    public Frequency frequency = new Frequency();

    @LexiconPage(comment = "These are the general configurations for compatibilities.")
    public Compatibilities compatibilities = new Compatibilities();

    public static class Transceiver extends LexiconPageData {
        @LexiconEntry(comment = "This is the range after which players can no longer be heard for frequency modulation. Defaults to 1000.")
        public Integer maxFMDistance = 1000;

        @LexiconEntry(comment = "This is the range after which audio begins to decay for frequency modulation. Defaults to 900.")
        public Integer falloffFM = 900;

        @LexiconEntry(comment = "This is the range after which players can no longer be heard for amplitude modulation. Defaults to 1800.")
        public Integer maxAMDistance = 1800;

        @LexiconEntry(comment = "This is the range after which audio begins to decay for amplitude modulation. Defaults to 1700.")
        public Integer falloffAM = 1700;

        @LexiconEntry(comment = "This is whether or not using the transceiver slows the player. Defaults to true.")
        public Boolean transceiverSlow = true;

        @LexiconEntry(comment = "When false, removes the transceiver recipe. Defaults to true.")
        public Boolean enabled = true;
    }

    public static class WalkieTalkie extends LexiconPageData {
        @LexiconEntry(comment = "This is the range after which players can no longer be heard for frequency modulation. Defaults to 500.")
        public Integer maxFMDistance = 500;

        @LexiconEntry(comment = "This is the range after which audio begins to decay for frequency modulation. Defaults to 400.")
        public Integer falloffFM = 400;

        @LexiconEntry(comment = "This is the range after which players can no longer be heard for amplitude modulation. Defaults to 900.")
        public Integer maxAMDistance = 900;

        @LexiconEntry(comment = "This is the range after which audio begins to decay for amplitude modulation. Defaults to 800.")
        public Integer falloffAM = 800;

        @LexiconEntry(comment = "This is whether or not using the walkie talkie slows the player. Defaults to true.")
        public Boolean walkieTalkieSlow = true;

        @LexiconEntry(comment = "When true, replaces the walkie talkie with the spuddie talkie. Defaults to true.")
        public Boolean spuddieTalkie = true;

        @LexiconEntry(comment = "When false, removes both the spuddie and walkie recipe. Defaults to true.")
        public Boolean enabled = true;
    }

    public static class Transmitter extends LexiconPageData {
        @LexiconEntry(comment = "This is the range after which players can no longer be heard for frequency modulation. Defaults to 2200.")
        public Integer maxFMDistance = 2200;

        @LexiconEntry(comment = "This is the range after which audio begins to decay for frequency modulation. Defaults to 2000.")
        public Integer falloffFM = 2000;

        @LexiconEntry(comment = "This is the range after which players can no longer be heard for amplitude modulation. Defaults to 4400.")
        public Integer maxAMDistance = 4400;

        @LexiconEntry(comment = "This is the range after which audio begins to decay for amplitude modulation. Defaults to 4000.")
        public Integer falloffAM = 4000;

        @LexiconEntry(comment = "When false, removes the transmitter (will not disable speaker) recipe. Defaults to true.")
        public Boolean enabled = true;
    }

    public static class Radio extends LexiconPageData {
        @LexiconEntry(comment = "This is the range for the radio in which the audio transmitted from it can be heard. Defaults to 24.")
        public Integer range = 24;

        @LexiconEntry(comment = "When false, removes the radio recipe. Defaults to true.")
        public Boolean enabled = true;
    }

    public static class Microphone extends LexiconPageData {
        @LexiconEntry(comment = "This is the range for the microphone that it can hear from. Defaults to 8.")
        public Integer range = 8;

        @LexiconEntry(comment = "When false, removes the microphone recipe. Defaults to true.")
        public Boolean enabled = true;
    }

    public static class Speaker extends LexiconPageData {
        @LexiconEntry(comment = "When false, removes the speaker recipe. Defaults to true.")
        public Boolean enabled = true;
    }

    public static class Antenna extends LexiconPageData {
        @LexiconEntry(comment = "When false, removes the antenna recipe. Defaults to true.")
        public Boolean enabled = true;
    }

    public static class Frequency extends LexiconPageData {
        @LexiconEntry(comment = "This is how many whole places (digits before the period) can exist in a frequency. Defaults to 3.")
        public Integer wholePlaces = 3;

        @LexiconEntry(comment = "This is how many decimal places (digits after the period) can exist in a frequency. Defaults to 2.")
        public Integer decimalPlaces = 2;

        @LexiconEntry(comment = "This is the default frequency to be provided to frequency-holding items. When set to auto-generate, will generate a pattern of zeros equal to the wholePlaces and decimalPlaces configurations, i.e. '000.00' by default. Defaults to auto-generate.")
        public String defaultFrequency = "auto-generate";

        @LexiconEntry(comment = "Whether or not the radios work across dimensions. Defaults to false.")
        public Boolean crossDimensional = false;

        @LexiconEntry(comment = "The base amount of interference to give to radio transmission across dimensions. Defaults to 10.")
        public Integer dimensionalInterference = 10;

        @LexiconEntry(comment = "The packet buffer for packet transmission. You likely won't need to worry about this. Defaults to 2.")
        public Integer packetBuffer = 2;

        @LexiconEntry(comment = "How many listeners should be able to receive a single players audio? Defaults to 2.")
        public Integer listenerBuffer = 2;
    }

    public static class Compatibilities extends LexiconPageData {
        @LexiconPage(comment = "These are the configurations for the optional dependency Voice Chat Interaction.")
        public VoiceChatInteraction voice_chat_interaction = new VoiceChatInteraction();

        public static class VoiceChatInteraction extends LexiconPageData {
            @LexiconEntry(comment = "When false, removes compatibility for Voice Chat Interaction. Defaults to false. (NON-FUNCTIONAL)")
            public Boolean enabled = false;
        }

        //----

        @LexiconPage(comment = "These are the configurations for the optional dependency Vibrative Voice.")
        public VibrativeVoice vibrative_voice = new VibrativeVoice();

        public static class VibrativeVoice extends LexiconPageData {
            @LexiconEntry(comment = "When false, removes compatibility for Vibrative Voice. Defaults to true.")
            public Boolean enabled = true;
        }
    }
}
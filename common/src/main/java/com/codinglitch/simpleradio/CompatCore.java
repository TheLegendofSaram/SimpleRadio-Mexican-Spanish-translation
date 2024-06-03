package com.codinglitch.simpleradio;

import com.codinglitch.simpleradio.platform.Services;

public class CompatCore {
    public static void spoutCompatibilities() {
        //---- Voice Chat Interaction ----\\
        if (Services.PLATFORM.isModLoaded("vcinteraction")) {
            CommonSimpleRadio.info("Voice Chat Interaction is present!");
            if (CommonSimpleRadio.SERVER_CONFIG.compatibilities.voice_chat_interaction.enabled) {
                CommonSimpleRadio.info("..and compat is enabled!");
            } else {
                CommonSimpleRadio.info("..but compat is disabled");
            }
        }

        //---- Vibrative Voice ----\\
        if (Services.PLATFORM.isModLoaded("vibrativevoice")) {
            CommonSimpleRadio.info("Vibrative Voice is present!");
            if (Services.PLATFORM.isModLoaded("vcinteraction")) {
                CommonSimpleRadio.info("..but so is Voice Chat Interaction?!");
            } else {
                if (CommonSimpleRadio.SERVER_CONFIG.compatibilities.vibrative_voice.enabled) {
                    CommonSimpleRadio.info("..and compat is enabled!");
                } else {
                    CommonSimpleRadio.info("..but compat is disabled");
                }
            }
        }
    }
}

package com.codinglitch.simpleradio.core.registry;

import com.codinglitch.simpleradio.CommonSimpleRadio;
import com.codinglitch.simpleradio.core.central.ItemHolder;
import com.codinglitch.simpleradio.core.registry.items.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.*;

import java.util.*;

import static com.codinglitch.simpleradio.CommonSimpleRadio.id;

public class SimpleRadioItems {
    public static final Map<ResourceLocation, ItemHolder<Item>> ITEMS = new HashMap<>();
    public static final Map<ResourceLocation, List<Item>> TAB_ITEMS = new HashMap<>();

    public static TransceiverItem TRANSCEIVER = register(id("transceiver"), new TransceiverItem(new Item.Properties().stacksTo(1)));
    public static WalkieTalkieItem WALKIE_TALKIE = register(id("walkie_talkie"), new WalkieTalkieItem(new Item.Properties().stacksTo(1)), !CommonSimpleRadio.SERVER_CONFIG.walkie_talkie.spuddieTalkie);
    public static WalkieTalkieItem SPUDDIE_TALKIE = register(id("spuddie_talkie"), new WalkieTalkieItem(new Item.Properties().stacksTo(1)), CommonSimpleRadio.SERVER_CONFIG.walkie_talkie.spuddieTalkie);
    public static Item RADIOSMITHER = register(id("radiosmither"), new BlockItem(SimpleRadioBlocks.RADIOSMITHER, new Item.Properties()));
    public static RadioItem RADIO = register(id("radio"), new RadioItem(new Item.Properties().stacksTo(1)));
    public static SpeakerItem SPEAKER = register(id("speaker"), new SpeakerItem(new Item.Properties().stacksTo(1)));
    public static MicrophoneItem MICROPHONE = register(id("microphone"), new MicrophoneItem(new Item.Properties().stacksTo(1)));

    public static Item FREQUENCER = register(id("frequencer"), new BlockItem(SimpleRadioBlocks.FREQUENCER, new Item.Properties().stacksTo(1)));

    public static Item ANTENNA = register(id("antenna"), new BlockItem(SimpleRadioBlocks.ANTENNA, new Item.Properties().stacksTo(16)));

    // ---- Modules ---- \\
    public static Item TRANSMITTING_MODULE = register(id("transmitting_module"), new Item(new Item.Properties()));
    public static Item RECEIVING_MODULE = register(id("receiving_module"), new Item(new Item.Properties()));
    public static Item SPEAKER_MODULE = register(id("speaker_module"), new Item(new Item.Properties()));
    public static Item LISTENER_MODULE = register(id("listener_module"), new Item(new Item.Properties()));

    // -- Upgrades -- \\
    public static ModuleItem IRON_MODULE = register(id("iron_module"), new ModuleItem(Tiers.IRON, new Item.Properties()));
    public static ModuleItem GOLD_MODULE = register(id("gold_module"), new ModuleItem(Tiers.GOLD, new Item.Properties()));
    public static ModuleItem DIAMOND_MODULE = register(id("diamond_module"), new ModuleItem(Tiers.DIAMOND, new Item.Properties()));
    public static ModuleItem NETHERITE_MODULE = register(id("netherite_module"), new ModuleItem(Tiers.NETHERITE, new Item.Properties()));

    public static ItemHolder<Item> getByName(String name) {
        Optional<Map.Entry<ResourceLocation, ItemHolder<Item>>> optional = ITEMS.entrySet().stream().filter(entry -> entry.getKey().getPath().equals(name)).findFirst();
        return optional.map(Map.Entry::getValue).orElse(null);
    }

    private static <I extends Item> I register(ResourceLocation location, I item) {
        return register(location, item, SimpleRadioMenus.RADIO_TAB_LOCATION, true);
    }

    private static <I extends Item> I register(ResourceLocation location, I item, boolean state) {
        return register(location, item, SimpleRadioMenus.RADIO_TAB_LOCATION, state);
    }

    private static <I extends Item> I register(ResourceLocation location, I item, ResourceLocation tab, boolean state) {
        ItemHolder<I> holder = ItemHolder.of(item, location, state);
        if (tab != null && holder.enabled) {
            TAB_ITEMS.computeIfAbsent(tab, key -> new ArrayList<>());
            TAB_ITEMS.get(tab).add(item);
        }

        ITEMS.put(location, (ItemHolder<Item>) holder);
        return item;
    }
}

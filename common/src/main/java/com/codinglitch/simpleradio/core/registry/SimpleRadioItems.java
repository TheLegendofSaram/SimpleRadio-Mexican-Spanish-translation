package com.codinglitch.simpleradio.core.registry;

import com.codinglitch.simpleradio.CommonSimpleRadio;
import com.codinglitch.simpleradio.core.registry.items.RadioItem;
import com.codinglitch.simpleradio.core.registry.items.TransceiverItem;
import com.codinglitch.simpleradio.core.registry.items.UpgradeModuleItem;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.Block;

import java.util.*;

import static com.codinglitch.simpleradio.CommonSimpleRadio.id;

public class SimpleRadioItems {
    public static final Map<ResourceLocation, Item> ITEMS = new HashMap<>();
    public static final Map<ResourceLocation, List<Item>> TAB_ITEMS = new HashMap<>();

    public static Item TRANSCEIVER = register(id("transceiver"), new TransceiverItem(new Item.Properties().stacksTo(1)));
    public static Item RADIOSMITHER = register(id("radiosmither"), new BlockItem(SimpleRadioBlocks.RADIOSMITHER, new Item.Properties()));
    public static Item RADIO = register(id("radio"), new RadioItem(new Item.Properties().stacksTo(1)));

    public static Item ANTENNA = register(id("antenna"), new BlockItem(SimpleRadioBlocks.ANTENNA, new Item.Properties().stacksTo(16)));


    // ---- Modules ---- \\
    public static Item TRANSMITTING_MODULE = register(id("transmitting_module"), new Item(new Item.Properties()));
    public static Item RECEIVING_MODULE = register(id("receiving_module"), new Item(new Item.Properties()));
    public static Item SPEAKER_MODULE = register(id("speaker_module"), new Item(new Item.Properties()));

    // -- Upgrades -- \\
    public static Item IRON_UPGRADE_MODULE = register(id("iron_upgrade_module"), new UpgradeModuleItem(Tiers.IRON, new Item.Properties()));
    public static Item GOLD_UPGRADE_MODULE = register(id("gold_upgrade_module"), new UpgradeModuleItem(Tiers.GOLD, new Item.Properties()));
    public static Item DIAMOND_UPGRADE_MODULE = register(id("diamond_upgrade_module"), new UpgradeModuleItem(Tiers.DIAMOND, new Item.Properties()));
    public static Item NETHERITE_UPGRADE_MODULE = register(id("netherite_upgrade_module"), new UpgradeModuleItem(Tiers.NETHERITE, new Item.Properties()));

    private static Item register(ResourceLocation location, Item item) {
        return register(location, item, SimpleRadioMenus.RADIO_TAB_LOCATION);
    }

    private static Item register(ResourceLocation location, Item item, ResourceLocation tab) {
        if (tab != null) {
            TAB_ITEMS.computeIfAbsent(tab, key -> new ArrayList<>());
            TAB_ITEMS.get(tab).add(item);
        }

        ITEMS.put(location, item);
        return item;
    }
}

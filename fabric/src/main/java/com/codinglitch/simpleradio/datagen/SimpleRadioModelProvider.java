package com.codinglitch.simpleradio.datagen;

import com.codinglitch.simpleradio.core.registry.SimpleRadioItems;
import com.codinglitch.simpleradio.core.registry.items.ModuleItem;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.data.models.BlockModelGenerators;
import net.minecraft.data.models.ItemModelGenerators;

public class SimpleRadioModelProvider extends FabricModelProvider {
    public SimpleRadioModelProvider(FabricDataOutput generator) {
        super(generator);
    }

    @Override
    public void generateBlockStateModels(BlockModelGenerators blockStateModelGenerator) {

    }

    @Override
    public void generateItemModels(ItemModelGenerators itemModelGenerator) {

    }
}

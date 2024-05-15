package com.codinglitch.simpleradio.datagen;

import com.codinglitch.simpleradio.CommonSimpleRadio;
import com.codinglitch.simpleradio.core.ItemsEnabledCondition;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraftforge.common.crafting.ConditionalRecipe;
import net.minecraftforge.common.crafting.conditions.IConditionBuilder;

import java.util.function.Consumer;

public class SimpleRadioRecipeProvider extends RecipeProvider implements IConditionBuilder {
    public SimpleRadioRecipeProvider(PackOutput output) {
        super(output);
    }

    @Override
    protected void buildRecipes(Consumer<FinishedRecipe> output) {
        ConditionalRecipe.builder()
                .addCondition(new ItemsEnabledCondition("microphone"))
                .build(output, CommonSimpleRadio.id("test"));
    }
}

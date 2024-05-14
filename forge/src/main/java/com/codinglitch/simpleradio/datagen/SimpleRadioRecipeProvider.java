package com.codinglitch.simpleradio.datagen;

import com.codinglitch.simpleradio.CommonSimpleRadio;
import com.codinglitch.simpleradio.core.ItemsEnabledCondition;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraftforge.common.crafting.ConditionalRecipe;
import net.minecraftforge.common.crafting.conditions.IConditionBuilder;

public class SimpleRadioRecipeProvider extends RecipeProvider implements IConditionBuilder {
    public SimpleRadioRecipeProvider(PackOutput output) {
        super(output);
    }

    @Override
    protected void buildRecipes(RecipeOutput output) {
        ConditionalRecipe.builder()
                .condition(new ItemsEnabledCondition("microphone"))
                .save(output, CommonSimpleRadio.id("test"));
    }
}

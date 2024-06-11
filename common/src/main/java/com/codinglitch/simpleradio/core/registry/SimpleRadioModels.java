package com.codinglitch.simpleradio.core.registry;

import com.codinglitch.simpleradio.CommonSimpleRadio;
import com.codinglitch.simpleradio.client.models.ModuleModel;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class SimpleRadioModels {
    public static final ArrayList<ModelHolder<BakedModel>> MODELS = new ArrayList<>();

    public static ModelHolder<ModuleModel> UPGRADE_MODULE = register(new ModelHolder<>(ModuleModel.class,
            new ModelResourceLocation(CommonSimpleRadio.ID, "iron_upgrade_module", "inventory"),
            new ModelResourceLocation(CommonSimpleRadio.ID, "gold_upgrade_module", "inventory"),
            new ModelResourceLocation(CommonSimpleRadio.ID, "diamond_upgrade_module", "inventory"),
            new ModelResourceLocation(CommonSimpleRadio.ID, "netherite_upgrade_module", "inventory")
    ));

    public static <M extends BakedModel> ModelHolder<M> register(ModelHolder<M> model) {
        MODELS.add((ModelHolder<BakedModel>) model);
        return model;
    }

    public static void onModelsLoad(Map<ResourceLocation, BakedModel> bakedRegistry) {
        for (ModelHolder<BakedModel> model : MODELS) {
            for (ModelResourceLocation location : model.locations) {
                BakedModel existingModel = bakedRegistry.get(location);
                if (existingModel == null) {
                    CommonSimpleRadio.warn("Could not find model {}", location);
                } else {
                    CommonSimpleRadio.info("Replacing model for {}", location);

                    try {
                        BakedModel newModel = model.bakedModel.getDeclaredConstructor(BakedModel.class).newInstance(existingModel);
                        
                        if (newModel instanceof LocationHolder locationHolder)
                            locationHolder.location = location;
                        
                        bakedRegistry.put(location, newModel);
                    } catch (NoSuchMethodException e) {
                        throw new RuntimeException("Model "+location+" has not declared an appropriate constructor", e);
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException("Model "+location+" has a declared constructor that is inaccessible", e);
                    } catch (InvocationTargetException | InstantiationException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
    }

    public static class LocationHolder {
        public ModelResourceLocation location = null;
    }
    
    public static class ModelHolder<M extends BakedModel> {
        public final Class<M> bakedModel;
        public final List<ModelResourceLocation> locations;

        public ModelHolder(Class<M> bakedModel, ModelResourceLocation... locations) {
            this.bakedModel = bakedModel;
            this.locations = Arrays.stream(locations).toList();
        }
    }
}

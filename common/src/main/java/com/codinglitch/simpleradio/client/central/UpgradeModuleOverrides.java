package com.codinglitch.simpleradio.client.central;

import com.codinglitch.simpleradio.client.models.LayeredUpgradeModuleModel;
import com.codinglitch.simpleradio.client.models.UpgradeModuleModel;
import com.codinglitch.simpleradio.core.central.Upgrade;
import com.codinglitch.simpleradio.core.registry.items.UpgradeModuleItem;
import com.google.common.collect.Maps;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.client.renderer.block.model.ItemOverride;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelBaker;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;

public class UpgradeModuleOverrides extends ItemOverrides {
    private final Map<Upgrade, BakedModel> cache = Maps.newHashMap();

    public UpgradeModuleOverrides() {
        super();
    }

    @Nullable
    @Override
    public BakedModel resolve(BakedModel model, ItemStack stack, @Nullable ClientLevel level, @Nullable LivingEntity entity, int i) {
        Upgrade upgrade = UpgradeModuleItem.getUpgrade(stack);

        if (!cache.containsKey(upgrade))
            cache.put(upgrade, new LayeredUpgradeModuleModel((UpgradeModuleModel) model, upgrade));

        return cache.get(upgrade);
    }
}

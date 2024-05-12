package com.codinglitch.simpleradio.core.registry.items;

import com.codinglitch.simpleradio.CommonSimpleRadio;
import com.codinglitch.simpleradio.core.central.Receiving;
import com.codinglitch.simpleradio.core.central.Upgrade;
import com.codinglitch.simpleradio.core.registry.SimpleRadioBlocks;
import com.codinglitch.simpleradio.core.registry.SimpleRadioUpgrades;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class UpgradeModuleItem extends TieredItem {

    public UpgradeModuleItem(Tier tier, Properties properties) {
        super(tier, properties);
    }

    public static Upgrade getUpgrade(ItemStack stack) {
        CompoundTag tag = stack.getOrCreateTag();

        ResourceLocation type = tag.contains("type") ? ResourceLocation.tryParse(tag.getString("type")) : CommonSimpleRadio.id("range");
        return SimpleRadioUpgrades.get(type);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> components, TooltipFlag tooltip) {
        CompoundTag tag = stack.getOrCreateTag();

        if (tag.contains("type")) {
            Upgrade upgrade = getUpgrade(stack);
            String namespace = upgrade.identifier.getNamespace();
            String upgradeName = upgrade.identifier.getPath();
            String upgradePath = "upgrade."+namespace+"."+upgradeName;

            components.add(Component.translatable(upgradePath).withStyle(ChatFormatting.AQUA));

            components.add(CommonComponents.EMPTY);
            for (Upgrade.Type type : upgrade.types) {
                components.add(Component.translatable("item.modifiers." + type.getName()).withStyle(ChatFormatting.GRAY));

                if (I18n.exists(upgradePath + "." + type.getName() + ".effects")) {
                    components.add(CommonComponents.space().append(Component.translatable(upgradePath + "." + type.getName() + ".effects").withStyle(ChatFormatting.DARK_GREEN)));
                } else {
                    components.add(CommonComponents.space().append(Component.translatable(upgradePath + ".effects").withStyle(ChatFormatting.DARK_GREEN)));
                }
            }
        }

        if (Screen.hasShiftDown() && tag.contains("user")) {
            /*components.add(Component.translatable(
                    "tooltip.simpleradio.receiver_user",
                    tag.getUUID("user")
            ).withStyle(ChatFormatting.DARK_GRAY));*/
        }

        super.appendHoverText(stack, level, components, tooltip);
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int $$3, boolean $$4) {
        CompoundTag tag = stack.getOrCreateTag();

        if (!tag.contains("type"))
            tag.putString("type", CommonSimpleRadio.id("range").toString());

        super.inventoryTick(stack, level, entity, $$3, $$4);
    }
}

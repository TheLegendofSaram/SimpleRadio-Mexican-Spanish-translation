package com.codinglitch.simpleradio.core.registry.items;

import com.codinglitch.simpleradio.core.central.Receiving;
import com.codinglitch.simpleradio.core.central.Transmitting;
import com.codinglitch.simpleradio.core.central.Upgradable;
import com.codinglitch.simpleradio.core.central.Upgrade;
import com.codinglitch.simpleradio.core.registry.SimpleRadioBlocks;
import com.codinglitch.simpleradio.core.registry.SimpleRadioUpgrades;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class MicrophoneItem extends BlockItem implements Transmitting, Upgradable {
    public MicrophoneItem(Properties settings) {
        super(SimpleRadioBlocks.MICROPHONE, settings);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> components, TooltipFlag tooltip) {
        appendTooltip(stack, components);
        super.appendHoverText(stack, level, components, tooltip);
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slot, boolean b) {
        super.inventoryTick(stack, level, entity, slot, b);

        tick(stack, level);
    }

    @Override
    public InteractionResult place(BlockPlaceContext context) {
        return super.place(context);
    }

    @Override
    public boolean canAcceptUpgrade(Upgrade upgrade) {
        return upgrade == SimpleRadioUpgrades.RANGE;
    }
}

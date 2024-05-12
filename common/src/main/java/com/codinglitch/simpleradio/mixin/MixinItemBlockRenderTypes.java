package com.codinglitch.simpleradio.mixin;

import com.codinglitch.simpleradio.core.central.Upgrade;
import com.codinglitch.simpleradio.core.registry.items.UpgradeModuleItem;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemBlockRenderTypes.class)
public class MixinItemBlockRenderTypes {
    
    @Inject(at = @At("HEAD"), method = "getRenderType(Lnet/minecraft/world/item/ItemStack;Z)Lnet/minecraft/client/renderer/RenderType;", cancellable = true)
    private static void simpleradio$getRenderType(ItemStack stack, boolean direct, CallbackInfoReturnable<RenderType> cir) {
        if (stack.getItem() instanceof UpgradeModuleItem upgradeModuleItem) {
            Upgrade upgrade = upgradeModuleItem.getUpgrade(stack);
            if (upgrade == null) return;

            //cir.setReturnValue(RenderType.entityTranslucentCull(upgrade.getTexture()));
        }
    }
}
package com.codinglitch.simpleradio.mixin;

import com.codinglitch.simpleradio.client.CommonSimpleRadioClient;
import com.codinglitch.simpleradio.core.central.Upgrade;
import com.codinglitch.simpleradio.core.registry.items.UpgradeModuleItem;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemRenderer.class)
public class MixinItemRenderer {
    
    @Inject(at = @At("HEAD"), method = "getModel", cancellable = true)
    private void simpleradio$getRenderType(ItemStack stack, Level level, LivingEntity entity, int i, CallbackInfoReturnable<BakedModel> cir) {

    }
}
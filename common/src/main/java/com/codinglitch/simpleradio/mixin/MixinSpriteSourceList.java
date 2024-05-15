package com.codinglitch.simpleradio.mixin;

import com.codinglitch.simpleradio.CommonSimpleRadio;
import net.minecraft.client.renderer.texture.atlas.SpriteResourceLoader;
import net.minecraft.client.renderer.texture.atlas.SpriteSource;
import net.minecraft.client.renderer.texture.atlas.sources.DirectoryLister;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.List;

@Mixin(SpriteResourceLoader.class)
public abstract class MixinSpriteSourceList {

    @Inject(
            at = @At(value = "TAIL"),
            method = "load(Lnet/minecraft/server/packs/resources/ResourceManager;Lnet/minecraft/resources/ResourceLocation;)Lnet/minecraft/client/renderer/texture/atlas/SpriteResourceLoader;",
            locals = LocalCapture.CAPTURE_FAILHARD
    )
    private static void simpleradio$load(
            ResourceManager resourceManager,
            ResourceLocation atlasLocation,
            CallbackInfoReturnable<SpriteResourceLoader> cir,
            ResourceLocation location,
            List<SpriteSource> spriteSources
    ) {
        if (atlasLocation.equals(new ResourceLocation("minecraft:blocks"))) {
            DirectoryLister source = new DirectoryLister("upgrade", "upgrade/");
            CommonSimpleRadio.info(source.toString());
            spriteSources.add(source);
        }
    }
}
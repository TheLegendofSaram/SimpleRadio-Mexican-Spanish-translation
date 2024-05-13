package com.codinglitch.simpleradio.client.renderers;

import com.codinglitch.simpleradio.client.models.MicrophoneModel;
import com.codinglitch.simpleradio.client.models.RadioModel;
import com.codinglitch.simpleradio.core.registry.blocks.MicrophoneBlock;
import com.codinglitch.simpleradio.core.registry.blocks.MicrophoneBlockEntity;
import com.codinglitch.simpleradio.core.registry.blocks.RadioBlock;
import com.codinglitch.simpleradio.core.registry.blocks.RadioBlockEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class MicrophoneRenderer implements BlockEntityRenderer<MicrophoneBlockEntity> {
    private MicrophoneModel model;

    public MicrophoneRenderer(BlockEntityRendererProvider.Context context) {
        this.model = new MicrophoneModel(context.bakeLayer(MicrophoneModel.LAYER_LOCATION));
    }

    @Override
    public void render(MicrophoneBlockEntity blockEntity, float tickDelta, PoseStack poseStack, MultiBufferSource bufferSource, int light, int overlay) {
        BlockState state = blockEntity.getBlockState();
        Block block = state.getBlock();

        if (block instanceof MicrophoneBlock microphoneBlock) {
            poseStack.pushPose();
            poseStack.translate(0.5f, 1.5f, 0.5f);
            poseStack.mulPose(Axis.XP.rotationDegrees(180));
            poseStack.mulPose(Axis.YP.rotationDegrees(microphoneBlock.getYRotationDegrees(state)));

            VertexConsumer vertexConsumer = bufferSource.getBuffer(model.renderType(MicrophoneModel.TEXTURE_LOCATION));
            model.renderToBuffer(poseStack, vertexConsumer, light, overlay, 1, 1, 1, 1);

            poseStack.popPose();
        }
    }
}

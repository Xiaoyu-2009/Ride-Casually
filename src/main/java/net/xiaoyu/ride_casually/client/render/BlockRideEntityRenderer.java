package net.xiaoyu.ride_casually.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.xiaoyu.ride_casually.entity.BlockRideEntity;

public class BlockRideEntityRenderer extends EntityRenderer<BlockRideEntity> {
    public BlockRideEntityRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public ResourceLocation getTextureLocation(BlockRideEntity entity) {
        return null;
    }

    @Override
    public void render(BlockRideEntity entity, float entityYaw, float partialTicks, PoseStack matrixStack, MultiBufferSource buffer, int packedLight) {
        super.render(entity, entityYaw, partialTicks, matrixStack, buffer, packedLight);
    }
}
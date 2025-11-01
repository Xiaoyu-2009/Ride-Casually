package net.xiaoyu.ride_casually.client;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.xiaoyu.ride_casually.RideCasually;
import net.xiaoyu.ride_casually.client.render.BlockRideEntityRenderer;
import net.xiaoyu.ride_casually.entity.BlockRideEntity;

@EventBusSubscriber(modid = RideCasually.MOD_ID)
public class ClientSetup {
    @SubscribeEvent
    public static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(BlockRideEntity.BLOCK_RIDE_ENTITY.get(), BlockRideEntityRenderer::new);
    }
}
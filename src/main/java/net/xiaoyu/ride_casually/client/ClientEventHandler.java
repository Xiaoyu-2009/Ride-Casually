package net.xiaoyu.ride_casually.client;

import net.minecraft.client.Minecraft;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.InputEvent;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import net.xiaoyu.ride_casually.ModKeyBindings;
import net.xiaoyu.ride_casually.RideCasually;
import net.xiaoyu.ride_casually.network.RidePacket;

@EventBusSubscriber(modid = RideCasually.MOD_ID)
public class ClientEventHandler {

    @SubscribeEvent
    public static void onKeyInput(InputEvent.Key event) {
        Minecraft mc = Minecraft.getInstance();

        if (ModKeyBindings.RIDE_KEY.consumeClick() && mc.hitResult != null && mc.hitResult.getType() == HitResult.Type.ENTITY) {
            EntityHitResult entityHitResult = (EntityHitResult) mc.hitResult;
            Entity targetEntity = entityHitResult.getEntity();
            
            mc.level.playSound(
                mc.player, targetEntity.getX(), targetEntity.getY(), targetEntity.getZ(),
                SoundEvents.HORSE_SADDLE, SoundSource.NEUTRAL, 3.0F, 1.0F
            );

            PacketDistributor.sendToServer(new RidePacket(targetEntity.getId(), true));
        }
    }
    
    @SubscribeEvent
    public static void onRenderTick(RenderLevelStageEvent event) {
        Minecraft mc = Minecraft.getInstance();
        
        if (mc.player != null && mc.player.isPassenger() && mc.options.keyShift.isDown()) {
            PacketDistributor.sendToServer(new RidePacket(0, false));
        }
    }
}
package net.xiaoyu.ride_casually.client;

import net.minecraft.client.Minecraft;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.InputEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import net.xiaoyu.ride_casually.ModKeyBindings;
import net.xiaoyu.ride_casually.RideCasually;
import net.xiaoyu.ride_casually.network.RideEntityPacket;

@EventBusSubscriber(modid = RideCasually.MOD_ID)
public class ClientEventHandler {

    @SubscribeEvent
    public static void onKeyInput(InputEvent.Key event) {
        if (ModKeyBindings.RIDE_KEY.consumeClick()) {
            Minecraft mc = Minecraft.getInstance();
            if (mc.hitResult != null && mc.hitResult.getType() == HitResult.Type.ENTITY) {
                EntityHitResult entityHitResult = (EntityHitResult) mc.hitResult;
                Entity targetEntity = entityHitResult.getEntity();

                mc.player.playSound(SoundEvents.HORSE_SADDLE, 1.0F, 1.0F);
                
                PacketDistributor.sendToServer(new RideEntityPacket(targetEntity.getId()));
            }
        }
    }
}
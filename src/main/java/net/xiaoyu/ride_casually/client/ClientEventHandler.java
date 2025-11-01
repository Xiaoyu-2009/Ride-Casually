package net.xiaoyu.ride_casually.client;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.InputEvent;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import net.xiaoyu.ride_casually.ModKeyBindings;
import net.xiaoyu.ride_casually.RideCasually;
import net.xiaoyu.ride_casually.RideConfig;
import net.xiaoyu.ride_casually.network.RidePacket;
import net.xiaoyu.ride_casually.util.RidePermissionUtil;
import net.xiaoyu.ride_casually.util.RideUtil;

@EventBusSubscriber(modid = RideCasually.MOD_ID)
public class ClientEventHandler {

    @SubscribeEvent
    public static void onKeyInput(InputEvent.Key event) {
        Minecraft mc = Minecraft.getInstance();

        if (ModKeyBindings.RIDE_KEY.consumeClick() && mc.hitResult != null) {
            if (mc.hitResult.getType() == HitResult.Type.ENTITY) {
                if (RideConfig.ALLOW_ENTITY_RIDING.get()) {
                    EntityHitResult entityHitResult = (EntityHitResult) mc.hitResult;
                    Entity targetEntity = entityHitResult.getEntity();

                    if (RidePermissionUtil.isEntityRidingAllowed(targetEntity)) {
                        mc.level.playSound(
                            mc.player, targetEntity.getX(), targetEntity.getY(), targetEntity.getZ(),
                            SoundEvents.HORSE_SADDLE, SoundSource.NEUTRAL, 3.0F, 1.0F
                        );

                        PacketDistributor.sendToServer(new RidePacket(targetEntity.getId(), true, BlockPos.ZERO, false));
                        RideUtil.addModRidingPlayer(mc.player);
                    }
                }
            } else if (mc.hitResult.getType() == HitResult.Type.BLOCK) {
                if (RideConfig.ALLOW_BLOCK_RIDING.get()) {
                    BlockHitResult blockHitResult = (BlockHitResult) mc.hitResult;
                    BlockPos pos = blockHitResult.getBlockPos();

                    BlockState blockState = mc.level.getBlockState(pos);
                    if (RidePermissionUtil.isBlockRidingAllowed(blockState)) {
                        PacketDistributor.sendToServer(new RidePacket(0, false, pos, true));
                    }
                }
            }
        }
    }
    
    @SubscribeEvent
    public static void onRenderTick(RenderLevelStageEvent event) {
        Minecraft mc = Minecraft.getInstance();
        
        if (mc.player != null && mc.player.isPassenger() && mc.options.keyShift.isDown()) {
            PacketDistributor.sendToServer(new RidePacket(0, false, BlockPos.ZERO, false));
            RideUtil.removeModRidingPlayer(mc.player);
        }
    }
}
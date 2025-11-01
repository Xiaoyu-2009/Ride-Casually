package net.xiaoyu.ride_casually.event;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.level.BlockEvent.BreakEvent;
import net.xiaoyu.ride_casually.RideCasually;
import net.xiaoyu.ride_casually.entity.BlockRideEntity;
import net.xiaoyu.ride_casually.util.BlockRideUtil;

@EventBusSubscriber(modid = RideCasually.MOD_ID)
public class BlockRideHandler {
	private BlockRideHandler() {}

	@SubscribeEvent
	public static void onBreak(BreakEvent event) {
		if (!event.getLevel().isClientSide()) {
			BlockRideEntity entity = BlockRideUtil.getBlockRideEntity((Level) event.getLevel(), event.getPos());

			if (entity != null) {
				BlockRideUtil.removeBlockRideEntity((Level) event.getLevel(), event.getPos());
				entity.ejectPassengers();
			}
		}
	}

	public static void handleBlockRideRequest(Player player, BlockPos pos) {
		BlockRideUtil.handleBlockRideRequest(player, pos);
	}
}
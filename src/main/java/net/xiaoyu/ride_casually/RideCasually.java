package net.xiaoyu.ride_casually;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.AddReloadListenerEvent;
import net.xiaoyu.ride_casually.data.BlockOffsetManager;
import net.xiaoyu.ride_casually.entity.BlockRideEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Mod(RideCasually.MOD_ID)
public class RideCasually {
    public static final String MOD_ID = "ride_casually";
    public static final Logger LOGGER = LoggerFactory.getLogger(RideCasually.MOD_ID);

    public RideCasually(IEventBus modEventBus) {
        BlockRideEntity.register(modEventBus);
        NeoForge.EVENT_BUS.addListener(this::addReloadListeners);
    }
    
    private void addReloadListeners(AddReloadListenerEvent event) {
        event.addListener(BlockOffsetManager.getInstance());
    }
}
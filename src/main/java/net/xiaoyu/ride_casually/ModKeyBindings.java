package net.xiaoyu.ride_casually;

import net.minecraft.client.KeyMapping;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import org.lwjgl.glfw.GLFW;

@EventBusSubscriber(modid = RideCasually.MOD_ID)
public class ModKeyBindings {
    public static final KeyMapping RIDE_KEY = new KeyMapping(
        "key.ride_casually.ride",
        GLFW.GLFW_KEY_R,
        "key.categories.gameplay"
    );

    @SubscribeEvent
    public static void registerKeyBindings(RegisterKeyMappingsEvent event) {
        event.register(RIDE_KEY);
    }
}
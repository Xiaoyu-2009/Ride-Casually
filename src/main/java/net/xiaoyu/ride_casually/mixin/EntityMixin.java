package net.xiaoyu.ride_casually.mixin;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.xiaoyu.ride_casually.util.RideUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public class EntityMixin {
    
    @Inject(method = "makeBoundingBox", at = @At("HEAD"), cancellable = true)
    private void onMakeBoundingBox(CallbackInfoReturnable<AABB> cir) {
        Entity entity = (Entity) (Object) this;

        if (entity instanceof Player player /* && player.isPassenger() */ && RideUtil.isModRidingPlayer(player)) {
            EntityDimensions dimensions = entity.getDimensions(entity.getPose());
            AABB box = new AABB(
                entity.position().x - (double)(dimensions.width() / 2.0F),
                entity.position().y + (double)0.7F,
                entity.position().z - (double)(dimensions.width() / 2.0F),
                entity.position().x + (double)(dimensions.width() / 2.0F),
                entity.position().y + (double)1.8F,
                entity.position().z + (double)(dimensions.width() / 2.0F)
            );
            
            cir.setReturnValue(box);
        }
    }
}
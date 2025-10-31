package net.xiaoyu.ride_casually.mixin;

import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EnderDragon.class)
public class RideCasuallyEntityMixin {

    @Inject(method = "isPickable", at = @At("HEAD"), cancellable = true)
    private void onIsPickable(CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(true);
    }
}
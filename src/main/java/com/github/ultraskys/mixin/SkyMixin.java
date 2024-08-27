package com.github.ultraskys.mixin;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.github.ultraskys.DaySky;
import com.github.ultraskys.SharedData;
import finalforeach.cosmicreach.world.Sky;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(Sky.class)
public class SkyMixin {
    @Redirect(method = "<clinit>", at = @At(value = "FIELD", target = "Lcom/badlogic/gdx/graphics/Color;BLACK:Lcom/badlogic/gdx/graphics/Color;"))
    private static Color fixBlackColors() {
        // DO NOT use `Color.BLACK`: it is MUTABLE!!!
        return new Color(0.0F, 0.0F, 0.0F, 1.0F);
    }

    @Inject(method = "drawSky", at = @At("HEAD"))
    private void drawClouds(Camera worldCamera, CallbackInfo ci) {
        //noinspection ConstantValue
        if (this != (Object) Sky.SPACE_DAY || SharedData.isDay()) {
            DaySky.renderClouds(worldCamera);
        }
    }

    @Inject(method = "drawStars", at = @At("HEAD"))
    private void invalidateStarMesh(CallbackInfo ci) {
        if (SharedData.Updated()) {
            SharedData.setUpdated(false);

            Sky sky = (Sky) (Object) this;
            sky.starMesh.dispose();
            sky.starMesh = null;
        }
    }

    @ModifyConstant(method = "drawStars", constant = @Constant(intValue = 1000, ordinal = 0))
    private int modifyNumStars(int original) {
        return SharedData.getNumStars();
    }
}


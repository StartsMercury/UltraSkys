package com.github.ultraskys.mixin;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.github.ultraskys.DaySky;
import com.github.ultraskys.SharedData;
import com.github.ultraskys.SkyExtensions;
import finalforeach.cosmicreach.gamestates.InGame;
import finalforeach.cosmicreach.world.Sky;
import finalforeach.cosmicreach.world.World;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Implements(@Interface(iface = SkyExtensions.class, prefix = "ultraSkys$"))
@Mixin(Sky.class)
public class SkyMixin implements SkyExtensions {
    @Shadow
    long seed;

    @Redirect(method = "<clinit>", at = @At(value = "FIELD", target = "Lcom/badlogic/gdx/graphics/Color;BLACK:Lcom/badlogic/gdx/graphics/Color;"))
    private static Color fixBlackColors() {
        // DO NOT use `Color.BLACK`: it is MUTABLE!!!
        return new Color(0.0F, 0.0F, 0.0F, 1.0F);
    }

    @Inject(method = "drawSky", at = @At("HEAD"))
    private void drawClouds(Camera worldCamera, CallbackInfo ci) {
        World world = InGame.world;
        if (world != null) {
            DaySky.renderClouds(worldCamera);
        }
    }

    @ModifyConstant(method = "drawStars", constant = @Constant(intValue = 1000, ordinal = 0))
    private int modifyNumStars(int original) {
        return SharedData.getNumStars();
    }

    // Star mesh rebuild is triggered when `this.seed != world.worldSeed`
    @Override
    public void setSeed(long seed) {
        this.seed = seed;
    }
}


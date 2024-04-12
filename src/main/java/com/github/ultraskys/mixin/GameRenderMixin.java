package com.github.ultraskys.mixin;


import com.github.ultraskys.Cloudshader;
import finalforeach.cosmicreach.rendering.shaders.GameShader;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameShader.class)
public class GameRenderMixin {
    @Inject(method = "initShaders", at = @At("TAIL"))
    private static void initRender(CallbackInfo ci){
        Cloudshader.initCloudShader();
    }
}

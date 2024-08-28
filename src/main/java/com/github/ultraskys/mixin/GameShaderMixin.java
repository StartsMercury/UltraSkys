package com.github.ultraskys.mixin;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.github.ultraskys.Cloudshader;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import finalforeach.cosmicreach.rendering.shaders.GameShader;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(GameShader.class)
public class GameShaderMixin {
    @WrapOperation(method = "loadShaderFile", at = @At(value = "INVOKE", target = "Lfinalforeach/cosmicreach/GameAssetLoader;loadAsset(Ljava/lang/String;)Lcom/badlogic/gdx/files/FileHandle;"))
    private FileHandle loadCustomAsset(String fileName, Operation<FileHandle> original) {
        if ((Object) this instanceof Cloudshader) {
            return Gdx.files.classpath("assets/ultraskys/" + fileName);
        } else {
            return original.call(fileName);
        }
    }
}

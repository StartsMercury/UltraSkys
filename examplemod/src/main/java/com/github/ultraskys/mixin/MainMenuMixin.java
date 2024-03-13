package com.github.ultraskys.mixin;

import com.github.ultraskys.SharedData;
import finalforeach.cosmicreach.gamestates.MainMenu;

import finalforeach.cosmicreach.world.Sky;
import org.spongepowered.asm.mixin.Dynamic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static com.github.ultraskys.UltraSkys.LOGGER;

@Mixin(MainMenu.class)
public class MainMenuMixin {
    @Inject(method = "create", at = @At("HEAD"))
    private void mainmenu$init(CallbackInfo ci) {
        LOGGER.info("Loaded UltraSkys");



    }




}

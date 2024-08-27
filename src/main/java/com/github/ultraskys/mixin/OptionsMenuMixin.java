package com.github.ultraskys.mixin;

import com.github.ultraskys.IntsSlider;
import com.github.ultraskys.SharedData;
import finalforeach.cosmicreach.gamestates.GameState;
import finalforeach.cosmicreach.gamestates.OptionsMenu;
import finalforeach.cosmicreach.ui.UIElement;
import finalforeach.cosmicreach.world.Sky;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(OptionsMenu.class)

public abstract class OptionsMenuMixin extends GameState {
    @Unique
    private static final int[] SORTED_SUPPORTED_STAR_COUNTS = new int[]{50, 100, 200, 400, 600, 1000, 2000, 2500, 3000, 5000};

    @Unique
    private IntsSlider starsCountSlider;

    @Inject(method = "<init>", at = @At("RETURN"))
    private void onInit(GameState previousState, CallbackInfo ci) {
        starsCountSlider = new IntsSlider(SORTED_SUPPORTED_STAR_COUNTS, 400.0F, -200.0F, 250.0F, 50.0F) {
            protected void onValueUpdate(int value) {
                this.setText("Stars Count: " + value);
            }

            public void updateText() {
                this.setText("Stars Count: " + this.getValue());
            }
        };

        starsCountSlider.setIndexAtOrBefore(SharedData.getNumStars());
        starsCountSlider.show();
        OptionsMenu uiElements = (OptionsMenu)(Object) this;
        uiElements.uiObjects.add(starsCountSlider);

        UIElement VibrentDay = new UIElement(400.0F, -125.0F, 250.0F, 50.0F) {
            public void onCreate() {
                super.onCreate();
                this.updateText();
            }

            public void onClick() {

                super.onClick();
                SharedData.isDayUpdate(!SharedData.isDay());
                this.updateText();

                if (SharedData.isDay()){
                    Sky.SPACE_DAY.currentSkyColor.set(0.1F, 0.1F, 0.2F, 0.2F);
                } else {
                    Sky.SPACE_DAY.currentSkyColor.set(0.0F, 0.0F, 0.0F, 1.0F);
                }

            }

            public void updateText() {
                this.setText("Vibrant Day: " + (SharedData.isDay() ? "On" : "Off"));
            }

        };
        VibrentDay.show();
        uiElements.uiObjects.add(VibrentDay);



    }

    @Override
    public void switchAwayTo(GameState gameState) {
        super.switchAwayTo(gameState);

        final var numStars = this.starsCountSlider.getValue();
        if (numStars == SharedData.getNumStars()) {
            return;
        }

        SharedData.setNumStars(numStars);
        SharedData.setUpdated(true);
    }
}
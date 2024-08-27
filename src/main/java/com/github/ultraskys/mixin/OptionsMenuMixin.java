package com.github.ultraskys.mixin;

import com.badlogic.gdx.graphics.Color;
import com.github.ultraskys.SharedData;
import com.github.ultraskys.SkyExtensions;
import finalforeach.cosmicreach.gamestates.GameState;
import finalforeach.cosmicreach.gamestates.InGame;
import finalforeach.cosmicreach.gamestates.OptionsMenu;
import finalforeach.cosmicreach.ui.UIElement;
import finalforeach.cosmicreach.world.Sky;
import finalforeach.cosmicreach.world.World;
import java.util.Arrays;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(OptionsMenu.class)

public abstract class OptionsMenuMixin extends GameState {
    @Unique
    private int numStars = SharedData.getNumStars();

    @Inject(method = "<init>", at = @At("RETURN"))
    private void onInit(GameState previousState, CallbackInfo ci) {
        UIElement StarsButton = new UIElement(400.0F, -200.0F, 250.0F, 50.0F) {
            private static final int[] SORTED_DISTANCES = new int[]{50, 100, 200, 400, 600, 1000, 2000, 2500, 3000, 5000};
            private int index;
            public void onCreate() {
                super.onCreate();

                // See docs about return
                int index = Arrays.binarySearch(SORTED_DISTANCES, OptionsMenuMixin.this.numStars);
                // RHS: min(~a, N - 1) = min(-a - 1, N - 1) = -1 + min(-a, N)
                this.index = index >= 0 ? index : -1 + Math.min(-index, SORTED_DISTANCES.length);

                this.updateText();
            }
            public void onClick() {
                super.onClick();
                this.index = (this.index + 1) % SORTED_DISTANCES.length;
                this.updateText();
            }
            public void updateText() {
                int numStars = OptionsMenuMixin.this.numStars = SORTED_DISTANCES[this.index];
                SharedData.setUpdated(true);
                this.setText("Stars Count: " + numStars);
            }
        };

        StarsButton.show();
        OptionsMenu uiElements = (OptionsMenu)(Object) this;
        uiElements.uiObjects.add(StarsButton);

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
                    Sky.SPACE_DAY.currentAmbientColor.set(Color.WHITE);
                } else {
                    Sky.SPACE_DAY.currentSkyColor.set(Color.BLACK);
                    Sky.SPACE_DAY.currentSkyColor.set(0.1F, 0.1F, 0.2F, 1.0F);
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

        final var numStars = this.numStars;
        if (numStars == SharedData.getNumStars()) {
            return;
        }

        SharedData.setNumStars(numStars);

        World world = InGame.world;
        if (world == null) {
            return;
        }

        Sky sky = Sky.currentSky;
        if (!sky.shouldDrawStars) {
            return;
        }

        // Invalidates the seed to indirectly trigger star mesh rebuild
        long unequalSeed = ~world.worldSeed;
        ((SkyExtensions) sky).setSeed(unequalSeed);
    }
}
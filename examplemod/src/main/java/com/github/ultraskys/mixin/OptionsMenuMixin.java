package com.github.ultraskys.mixin;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.github.ultraskys.SharedData;
import finalforeach.cosmicreach.gamestates.GameState;
import finalforeach.cosmicreach.gamestates.MainMenu;

import finalforeach.cosmicreach.gamestates.OptionsMenu;
import finalforeach.cosmicreach.settings.ControlSettings;
import finalforeach.cosmicreach.settings.GraphicsSettings;
import finalforeach.cosmicreach.ui.UIElement;
import finalforeach.cosmicreach.world.Sky;
import org.spongepowered.asm.mixin.Dynamic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import finalforeach.cosmicreach.ui.UIElement;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static com.github.ultraskys.UltraSkys.LOGGER;

@Mixin(OptionsMenu.class)

public abstract class OptionsMenuMixin {


    @Inject(method = "<init>", at = @At("RETURN"))
    private void onInit(GameState previousState, CallbackInfo ci) {



        UIElement StarsButton = new UIElement(300.0F, -200.0F, 250.0F, 50.0F) {
            public void onCreate() {
                super.onCreate();
                this.updateText();
            }

            public void onClick() {
                super.onClick();
                int[] distances = new int[]{50, 100, 200, 400, 600, 1000, 2000, 2500, 3000, 5000};
                int oldDist = SharedData.getNumStars();

                for(int i = 0; i < distances.length; ++i) {
                    int d = distances[i];
                    SharedData.setNumStars(d);
                    if (oldDist < d) {
                        break;
                    }

                    if (i == distances.length - 1) {
                        SharedData.setNumStars(distances[0]);
                    }
                }

                this.updateText();



            }

            public void updateText() {
                SharedData sharedData = SharedData.getInstance();

                sharedData.setUpdated(true);

                this.setText("Stars Count: " + SharedData.getNumStars());

            }
        };
        StarsButton.show();
        OptionsMenu uiElements = (OptionsMenu)(Object) this;
        uiElements.uiElements.add(StarsButton);

        UIElement VibrentDay = new UIElement(-300.0F, -200.0F, 250.0F, 50.0F) {
            public void onCreate() {
                super.onCreate();
                this.updateText();
            }

            public void onClick() {
                super.onClick();

                if (!SharedData.isDay()){
                    SharedData.isDayUpdate(true);
                } else {
                    SharedData.isDayUpdate(false);
                }


                this.updateText();


                SharedData sharedData = SharedData.getInstance();

                if (sharedData.isDay()){

                    Sky.skyColor.set(0.1F, 0.1F, 0.2F, 0.2F);
                } else {
                    Sky.skyColor.set(0.0F, 0.0F, 0.0F, 1.0F);
                }

            }

            public void updateText() {

                this.setText("Vibrant Day: " + (SharedData.isDay() ? "On" : "Off"));




            }

        };
        VibrentDay.show();
        uiElements.uiElements.add(VibrentDay);



    }
}
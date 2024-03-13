package com.github.ultraskys;

import finalforeach.cosmicreach.world.Sky;

public class SharedData {
    private static final SharedData instance = new SharedData();

    private static int numStars = 200;
    private static boolean isUpdated = false;

    private static boolean VibratenDay = false;

    private SharedData() {

    }

    public static SharedData getInstance() {
        return instance;
    }


    public static boolean isDay(){

        return VibratenDay;
    }

    public static void isDayUpdate(boolean value){
        VibratenDay = value;

    }

    public static boolean Updated(){
        return isUpdated;
    }

    public static void setUpdated(boolean value){
        isUpdated = value;
    }
    public static int getNumStars() {
        return numStars;
    }

    public static void setNumStars(int value) {
        numStars = value;
    }
}

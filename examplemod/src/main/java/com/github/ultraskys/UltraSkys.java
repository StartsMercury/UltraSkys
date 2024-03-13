package com.github.ultraskys;

import net.fabricmc.api.ModInitializer;

import java.util.logging.Logger;

public class UltraSkys implements ModInitializer {
    public static final String MOD_ID = "UltraSkys";
    public static final Logger LOGGER = Logger.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        LOGGER.info("Loading UltraSkys");
    }
}

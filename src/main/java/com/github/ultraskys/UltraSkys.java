package com.github.ultraskys;

import org.coolcosmos.cosmicquilt.api.entrypoint.ModInitializer;
import org.quiltmc.loader.api.ModContainer;

import java.util.logging.Logger;

public class UltraSkys implements ModInitializer {
    public static final String MOD_ID = "ultraskys";
    public static final Logger LOGGER = Logger.getLogger(MOD_ID);
    @Override
    public void onInitialize(ModContainer modContainer) {
        LOGGER.info("Loading.. UltraSkys");
    }
}

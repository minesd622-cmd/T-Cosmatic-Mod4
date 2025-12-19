package com.example.customskin;

import com.example.customskin.command.SkinCommand;
import com.example.customskin.config.SkinConfig;
import com.example.customskin.util.SkinLoader;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CustomSkinClient implements ClientModInitializer {
    public static final String MOD_ID = "customskinloader";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitializeClient() {
        LOGGER.info("Initializing Custom Skin Loader...");
        
        // Load Config
        SkinConfig.load();
        
        // Initialize Skin Loader logic
        SkinLoader.init();

        // Register Commands
        ClientCommandRegistrationCallback.EVENT.register(SkinCommand::register);
    }
}
package com.example.customskin.util;

import com.example.customskin.CustomSkinClient;
import com.example.customskin.config.SkinConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.NativeImageBackedTexture;
import net.minecraft.util.Identifier;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.concurrent.CompletableFuture;

public class SkinLoader {
    private static Identifier customSkinIdentifier = null;
    private static boolean isLoading = false;

    public static void init() {
        String url = SkinConfig.getInstance().skinUrl;
        if (url != null && !url.isEmpty()) {
            loadSkinFromUrl(url);
        }
    }

    public static void refresh() {
        init();
    }

    public static void loadSkinFromUrl(String urlString) {
        if (isLoading) return;
        isLoading = true;

        CompletableFuture.runAsync(() -> {
            try {
                URL url = new URI(urlString).toURL();
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setDoInput(true);
                connection.connect();

                if (connection.getResponseCode() == 200) {
                    try (InputStream stream = connection.getInputStream()) {
                        NativeImage image = NativeImage.read(stream);
                        
                        // Execute on Render Thread to register texture
                        MinecraftClient.getInstance().execute(() -> {
                            NativeImageBackedTexture texture = new NativeImageBackedTexture(image);
                            Identifier id = Identifier.of(CustomSkinClient.MOD_ID, "custom_skin_" + System.currentTimeMillis());
                            MinecraftClient.getInstance().getTextureManager().registerTexture(id, texture);
                            customSkinIdentifier = id;
                            CustomSkinClient.LOGGER.info("Custom skin loaded successfully.");
                        });
                    }
                } else {
                     CustomSkinClient.LOGGER.error("Failed to download skin: HTTP " + connection.getResponseCode());
                }
            } catch (Exception e) {
                CustomSkinClient.LOGGER.error("Error loading skin", e);
            } finally {
                isLoading = false;
            }
        });
    }

    public static Identifier getCustomSkinIdentifier() {
        return customSkinIdentifier;
    }
    
    public static void clear() {
        customSkinIdentifier = null;
    }
}
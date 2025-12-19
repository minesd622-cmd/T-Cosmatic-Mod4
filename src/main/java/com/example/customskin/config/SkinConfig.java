package com.example.customskin.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class SkinConfig {
    private static final File CONFIG_FILE = FabricLoader.getInstance().getConfigDir().resolve("customskinloader.json").toFile();
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static SkinConfig instance;

    public String skinUrl = "";
    public String modelType = "default"; // "default" or "slim"

    public static void load() {
        if (CONFIG_FILE.exists()) {
            try (FileReader reader = new FileReader(CONFIG_FILE)) {
                instance = GSON.fromJson(reader, SkinConfig.class);
            } catch (IOException e) {
                e.printStackTrace();
                instance = new SkinConfig();
            }
        } else {
            instance = new SkinConfig();
            save();
        }
    }

    public static void save() {
        try (FileWriter writer = new FileWriter(CONFIG_FILE)) {
            GSON.toJson(instance, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static SkinConfig getInstance() {
        if (instance == null) load();
        return instance;
    }
}
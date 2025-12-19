package com.example.skinmod.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.minecraft.text.Text;
import com.mojang.brigadier.arguments.StringArgumentType;

public class SkinModClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        // Register client-side command /setskin <url>
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
            dispatcher.register(ClientCommandManager.literal("setskin")
                .then(ClientCommandManager.argument("url", StringArgumentType.string())
                .executes(context -> {
                    String url = StringArgumentType.getString(context, "url");
                    context.getSource().sendFeedback(Text.of("Applying skin from: " + url + " (Logic placeholder)"));
                    // Actual skin rendering replacement logic would require complex Mixins into PlayerSkinTexture
                    return 1;
                })));
        });
    }
}
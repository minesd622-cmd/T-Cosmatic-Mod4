package com.example.customskin.command;

import com.example.customskin.config.SkinConfig;
import com.example.customskin.util.SkinLoader;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.text.Text;

import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.argument;
import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.literal;

public class SkinCommand {
    public static void register(CommandDispatcher<FabricClientCommandSource> dispatcher, com.mojang.brigadier.tree.CommandRegistryAccess registryAccess) {
        dispatcher.register(literal("skin")
            .then(literal("set")
                .then(argument("url", StringArgumentType.greedyString())
                    .executes(context -> {
                        String url = StringArgumentType.getString(context, "url");
                        SkinConfig.getInstance().skinUrl = url;
                        SkinConfig.save();
                        SkinLoader.refresh();
                        context.getSource().sendFeedback(Text.literal("§aCustom skin URL set! Downloading..."));
                        return 1;
                    })
                )
            )
            .then(literal("clear")
                .executes(context -> {
                    SkinConfig.getInstance().skinUrl = "";
                    SkinConfig.save();
                    SkinLoader.clear();
                    context.getSource().sendFeedback(Text.literal("§eCustom skin reset to default."));
                    return 1;
                })
            )
            .then(literal("model")
                .then(literal("slim")
                    .executes(context -> {
                        SkinConfig.getInstance().modelType = "slim";
                        SkinConfig.save();
                        context.getSource().sendFeedback(Text.literal("§aModel set to SLIM (Alex)."));
                        return 1;
                    })
                )
                .then(literal("wide")
                    .executes(context -> {
                        SkinConfig.getInstance().modelType = "default";
                        SkinConfig.save();
                        context.getSource().sendFeedback(Text.literal("§aModel set to WIDE (Steve)."));
                        return 1;
                    })
                )
            )
        );
    }
}
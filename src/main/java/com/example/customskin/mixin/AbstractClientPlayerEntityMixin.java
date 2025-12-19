package com.example.customskin.mixin;

import com.example.customskin.config.SkinConfig;
import com.example.customskin.util.SkinLoader;
import com.mojang.authlib.GameProfile;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.util.SkinTextures;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractClientPlayerEntity.class)
public abstract class AbstractClientPlayerEntityMixin extends PlayerEntity {

    public AbstractClientPlayerEntityMixin(World world, BlockPos pos, float yaw, GameProfile gameProfile) {
        super(world, pos, yaw, gameProfile);
    }

    @Inject(method = "getSkinTextures", at = @At("HEAD"), cancellable = true)
    private void injectCustomSkin(CallbackInfoReturnable<SkinTextures> cir) {
        // Only replace skin for the local client player
        if (this.equals(MinecraftClient.getInstance().player)) {
            Identifier customSkin = SkinLoader.getCustomSkinIdentifier();
            if (customSkin != null) {
                String model = SkinConfig.getInstance().modelType;
                boolean isSlim = "slim".equalsIgnoreCase(model);
                
                // Create a generic SkinTextures record with our custom texture
                // Using null for cape/elytra for simplicity, or we could fallback to original
                SkinTextures textures = new SkinTextures(
                        customSkin,
                        null, // textureUrl (nullable)
                        null, // capeTexture (nullable)
                        null, // elytraTexture (nullable)
                        isSlim ? SkinTextures.Model.SLIM : SkinTextures.Model.WIDE,
                        true // secure
                );
                cir.setReturnValue(textures);
            }
        }
    }
}
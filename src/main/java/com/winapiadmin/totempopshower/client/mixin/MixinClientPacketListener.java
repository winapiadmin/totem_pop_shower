package com.winapiadmin.totempopshower.client.mixin;


import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.toasts.SystemToast;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundEntityEventPacket;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPacketListener.class)
public class MixinClientPacketListener {
    @Shadow private ClientLevel level;
    private static final Minecraft client = Minecraft.getInstance();
    @Inject(method = "handleEntityEvent", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/particle/ParticleEngine;createTrackingEmitter(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/core/particles/ParticleOptions;I)V"))
    public void updateCounter(ClientboundEntityEventPacket packet, CallbackInfo ci) {
        Entity entity = packet.getEntity(level);
        if (entity instanceof Player player) {
            Minecraft client = Minecraft.getInstance();
            if (client.player == null) return;

            // get a plain name string from the player's display Component
            String playerName = player.getName().getString();

            // build the toast title/description
            Component title = Component.literal("Totem Counter");
            Component desc  = Component.literal(playerName + " popped a totem");

            // create and add the toast (TutorialToast is the official-mapping toast)
            SystemToast toast = new SystemToast(
                    SystemToast.SystemToastId.PERIODIC_NOTIFICATION, // choose a suitable icon/type
                    title,
                    desc
            );

            client.getToastManager().addToast(toast);
        }
    }
}
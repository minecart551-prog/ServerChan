package net.himeki.serverchan.fabric;

import net.himeki.serverchan.MessageBroadcaster;
import net.himeki.serverchan.i18n.I18n;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;

/**
 * Fabric implementation of MessageBroadcaster
 * Uses /say command so Discord bridge mods pick up the messages.
 */
public class FabricMessageBroadcaster implements MessageBroadcaster {
    private MinecraftServer server;

    public FabricMessageBroadcaster(MinecraftServer server) {
        this.server = server;
    }

    public void setServer(MinecraftServer server) {
        this.server = server;
    }

    @Override
    public void broadcastMessage(String message) {
        if (server != null) {
            server.execute(() -> {
                if (server.getPlayerList() != null) {
                    try {
                        String botName = I18n.get("bot.name");
                        Component nameComponent = Component.literal(botName);
                        CommandSourceStack source = new CommandSourceStack(
                                server, Vec3.ZERO, Vec2.ZERO,
                                server.overworld(), 2,
                                botName, nameComponent, server, null
                        );
                        server.getCommands().getDispatcher().execute("say " + message, source);
                    } catch (Exception e) {
                        // fallback
                    }
                }
            });
        }
    }

    @Override
    public boolean isReady() {
        return server != null && server.getPlayerList() != null;
    }
}

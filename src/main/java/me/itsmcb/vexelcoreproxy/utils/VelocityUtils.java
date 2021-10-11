package me.itsmcb.vexelcoreproxy.utils;

import com.velocitypowered.api.command.Command;
import com.velocitypowered.api.command.CommandMeta;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import me.itsmcb.vexelcoreproxy.VexelCoreProxy;

import java.util.ArrayList;

public class VelocityUtils {

    public static void registerCommand(ArrayList<String> aliases, Command command, VexelCoreProxy instance) {
        ProxyServer server = instance.getProxyServer();
        CommandMeta.Builder builder = server.getCommandManager().metaBuilder(aliases.get(0));
        if (aliases.size() > 1) {
            String[] extra_aliases = aliases.toArray(aliases.toArray(new String[1]));
            builder.aliases(extra_aliases);
        }
        try {
            server.getCommandManager().register(builder.build(),command);
        } catch (Exception e) {
            e.printStackTrace();
            instance.getLogger().error("An error occurred while registering the command \"" + aliases.get(0) + "\"");
        }
    }

    public static Player getPlayer(ProxyServer server, String playerName) {
        return server.getAllPlayers().stream().filter(serverPlayer -> serverPlayer.getUsername().equalsIgnoreCase(playerName)).findAny().orElse(null);
    }
}

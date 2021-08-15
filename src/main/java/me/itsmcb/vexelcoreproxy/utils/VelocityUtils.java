package me.itsmcb.vexelcoreproxy.utils;

import com.velocitypowered.api.command.Command;
import com.velocitypowered.api.command.CommandMeta;
import com.velocitypowered.api.proxy.ProxyServer;
import me.itsmcb.vexelcoreproxy.VexelCoreProxy;

import java.util.Arrays;

public class VelocityUtils {

    public static void registerCommand(String alises[], Command command, VexelCoreProxy VCP) {
        ProxyServer server = VCP.getProxyServer();
        CommandMeta.Builder builder = server.getCommandManager().metaBuilder(alises[0]);
        if (alises.length > 1) {
            String[] extra_aliases = Arrays.copyOfRange(alises, 1, alises.length);
            builder.aliases(extra_aliases);
        }
        try {
            server.getCommandManager().register(builder.build(),command);
        } catch (Exception e) {
            e.printStackTrace();
            VCP.getLogger().error("An error occurred while registering the command \"" + alises[0] + "\"");
        }
    }
}

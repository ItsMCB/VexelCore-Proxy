package me.itsmcb.vexelcoreproxy.utils;

import com.velocitypowered.api.proxy.ProxyServer;

import java.util.ArrayList;
import java.util.List;

public class TabUtils {
    public static List<String> getEmpty() {
        return new ArrayList();
    }

    public static List<String> getAllPlayers(ProxyServer server) {
        List<String> tabComplete = new ArrayList();
        server.getAllPlayers().forEach(player -> tabComplete.add(player.getUsername()));
        return tabComplete;
    }
    public static List<String> getAllServers(ProxyServer server) {
        List<String> tabComplete = new ArrayList();
        server.getAllServers().forEach(_server -> tabComplete.add(_server.getServerInfo().getName()));
        return tabComplete;
    }

    public static List<String> getAllPluginIDs(ProxyServer server) {
        List<String> tabComplete = new ArrayList();
        server.getPluginManager().getPlugins().forEach(plugin -> tabComplete.add(plugin.getDescription().getId()));
        return tabComplete;
    }

    public static List<String> returnTab(String[] args, List<String>...tabComplete) {
        // Improve in the future
        if (args.length == 0) {
            try {
                return tabComplete[0];
            } catch (Exception e) {
                return getEmpty();
            }
        }
        if (args.length > tabComplete.length) {
            return getEmpty();
        } else {
            return tabComplete[args.length-1];
        }
    }
}

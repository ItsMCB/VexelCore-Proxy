package me.itsmcb.vexelcoreproxy.commands;

import com.google.common.collect.ImmutableList;
import com.moandjiezana.toml.Toml;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import me.itsmcb.vexelcoreproxy.VexelCoreProxy;
import me.itsmcb.vexelcoreproxy.utils.ChatUtils;
import me.itsmcb.vexelcoreproxy.utils.MessageUtils;
import me.itsmcb.vexelcoreproxy.utils.PermissionUtils;

import java.util.List;

public class Glist implements SimpleCommand {

    private final ProxyServer server;
    public Glist(ProxyServer server) {
        this.server = server;
    }

    public static Toml special = VexelCoreProxy.getConfig().getTable("special");

    @Override
    public void execute(Invocation invocation) {
        CommandSource source = invocation.source();
        //String[] args = invocation.arguments(); # Maybe do something with this later, such as /glist <server name> for more detailed info
        if (source instanceof Player) {
            Player p = (Player) source;
            if (!p.hasPermission(PermissionUtils.get("glist"))) {
                p.sendMessage(MessageUtils.get("noPermission"));
                return;
            }
        }
        source.sendMessage(ChatUtils.parseLegacy(special.getString("formatting") + special.getString("serverName") + special.getString("formatting")));
        for (RegisteredServer server : server.getAllServers()) {
            sendServerPlayers(source, server);
        }

    }

    private void sendServerPlayers(CommandSource target, RegisteredServer server) {
        List<Player> onServer = ImmutableList.copyOf(server.getPlayersConnected());
        StringBuilder msg = new StringBuilder();
        msg.append("&3").append(server.getServerInfo().getName()).append(" &7(").append(onServer.size()).append(") &8>> &a");

        for (int i = 0; i < onServer.size(); i++) {
            Player player = onServer.get(i);
            msg.append(player.getUsername());

            if (i + 1 < onServer.size()) {
                msg.append(", ");
            }
        }
        target.sendMessage(ChatUtils.parseLegacy(msg.toString()));
    }
}

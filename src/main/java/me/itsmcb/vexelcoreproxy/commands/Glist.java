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
import net.kyori.adventure.text.TextComponent;

import java.util.List;
import java.util.Optional;

public class Glist implements SimpleCommand {

    private final ProxyServer server;
    private Toml config;
    private Toml special;
    private Toml language;
    public Glist(VexelCoreProxy VCP) {
        this.server = VCP.getProxyServer();
        this.config = VCP.getConfig();
        this.special = config.getTable("special");
        this.language = config.getTable("language");
    }

    @Override
    public void execute(Invocation invocation) {
        CommandSource source = invocation.source();
        //String[] args = invocation.arguments(); # Maybe do something with this later, such as /glist <server name> for more detailed info
        if (source instanceof Player) {
            Player p = (Player) source;
            if (!p.hasPermission(config.getTable("permissions").getString("glist"))) {
                p.sendMessage(MessageUtils.toComponent(new String[] {config.getString("prefix"),language.getString("noPermission")}));
                return;
            }
        }
        source.sendMessage(ChatUtils.parseLegacy(special.getString("formatting") + special.getString("serverName") + special.getString("formatting")));
        for (RegisteredServer server : server.getAllServers()) {
            sendServerPlayers(source, server);
        }

    }

    private void sendServerPlayers(CommandSource target, RegisteredServer regserver) {
        List<Player> onServer = ImmutableList.copyOf(regserver.getPlayersConnected());
        StringBuilder msg = new StringBuilder();
        Optional<RegisteredServer> regServerFix = server.getServer(regserver.getServerInfo().getName());
        TextComponent hoverMsg = ChatUtils.parseLegacy("&3Online: &a" + regServerFix.isPresent()); // do later
        msg.append("&3").append(regserver.getServerInfo().getName()).append(" &7(").append(onServer.size()).append(") &8>> &a");

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

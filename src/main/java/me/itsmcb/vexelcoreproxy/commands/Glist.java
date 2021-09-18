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
import me.itsmcb.vexelcoreproxy.utils.TabUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class Glist implements SimpleCommand {

    private final ProxyServer server;
    private final Toml config;
    private final Toml language;

    public Glist(VexelCoreProxy VCP) {
        this.server = VCP.getProxyServer();
        this.config = VCP.getConfig();
        this.language = config.getTable("language");
    }

    @Override
    public void execute(Invocation invocation) {
        CommandSource source = invocation.source();
        String[] args = invocation.arguments();
        if (source instanceof Player p) {
            if (!p.hasPermission(config.getTable("permissions").getString("glist"))) {
                p.sendMessage(ChatUtils.toComponent(new String[] {config.getString("prefix"),language.getString("noPermission")}));
                return;
            }
        }
        source.sendMessage(ChatUtils.parseLegacy(language.getString("bar") + language.getString("serverName") + language.getString("bar")));
        boolean showAllServers = false;
        if (args.length > 0) {
            if (args[0].equalsIgnoreCase("-all")) {
                showAllServers = true;
            }
        }
        for (RegisteredServer regserver : server.getAllServers()) {
            if (regserver.getPlayersConnected().size() == 0 && showAllServers) {
                sendServerPlayers(source, regserver);
            }
            if (regserver.getPlayersConnected().size() > 0) {
                sendServerPlayers(source, regserver);
            }
        }
        
        if (!showAllServers) {
            source.sendMessage(ChatUtils.parseLegacy("&7To view all servers despite player count, do &3/glist -all"));
        }
    }

    @Override
    public List<String> suggest(Invocation invocation) {
        String[] args = invocation.arguments();
        return TabUtils.returnTab(args, List.of("-all"));
    }

    private void sendServerPlayers(CommandSource target, RegisteredServer regserver) {
        String serverName = regserver.getServerInfo().getName();
        List<Player> onServer = ImmutableList.copyOf(regserver.getPlayersConnected());
        StringBuilder playerList = new StringBuilder();

        for (int i = 0; i < onServer.size(); i++) {
            Player player = onServer.get(i);
            playerList.append(player.getUsername());

            if (i + 1 < onServer.size()) {
                playerList.append(", ");
            }
        }

        AtomicReference<String> serverInfo = new AtomicReference<>("&cServer Unavailable");
        AtomicReference<String> serverStatusColor = new AtomicReference<>("&c");
        AtomicReference<String> MOTD = new AtomicReference<>("");
        regserver.ping().exceptionally(e -> null).thenAcceptAsync(serverPing -> {
            if (serverPing != null) {
                String versionName = serverPing.getVersion().getName();
                int protocol = serverPing.getVersion().getProtocol();
                MOTD.set(LegacyComponentSerializer.legacyAmpersand().serialize(serverPing.getDescriptionComponent().asComponent()));

                serverInfo.set("&7Version: &3" + versionName + " &8(&7" + protocol + "&8)\n&7MOTD: &3" + MOTD);
                serverStatusColor.set("&a");
            }
        }).join();
        Component server = ChatUtils.parseLegacy(serverStatusColor.get() + serverName + " &7(" + onServer.size() + ")")
                .hoverEvent(HoverEvent.showText(ChatUtils.parseLegacy(serverInfo.get())))
                .clickEvent(ClickEvent.suggestCommand("/server " + serverName));
        Component stylingArrow = ChatUtils.parseLegacy(" &8>> ");
        Component players = ChatUtils.parseLegacy("&7" + playerList);
        target.sendMessage(Component.empty().append(server).append(stylingArrow).append(players));
    }
}

package me.itsmcb.vexelcoreproxy.commands;

import com.moandjiezana.toml.Toml;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import me.itsmcb.vexelcoreproxy.VexelCoreProxy;
import me.itsmcb.vexelcoreproxy.utils.ChatUtils;
import me.itsmcb.vexelcoreproxy.utils.TabUtils;

import java.util.List;

public class Jump implements SimpleCommand {

    private final ProxyServer server;
    private final Toml config;
    private final Toml language;

    public Jump(VexelCoreProxy VCP) {
        this.server = VCP.getProxyServer();
        this.config = VCP.getConfig();
        this.language = config.getTable("language");
    }

    @Override
    public void execute(Invocation invocation) {
        CommandSource source = invocation.source();
        String[] args = invocation.arguments();
        if (source instanceof Player) {
            Player player = (Player) source;
            if (!player.hasPermission(config.getTable("permissions").getString("jump"))) {
                player.sendMessage(ChatUtils.toComponent(new String[] {config.getString("prefix"),language.getString("noPermission")}));
                return;
            }
            if (args.length == 1) {
                server.getAllPlayers().forEach(networkPlayer -> {
                    if (networkPlayer.getUsername().equalsIgnoreCase(args[0])) {
                        String target_server_name = networkPlayer.getCurrentServer().get().getServer().getServerInfo().getName();
                        player.sendMessage(ChatUtils.parseLegacy(language.getString("creatingConnectionRequest").replace("%server%",target_server_name)));
                        if (player.getCurrentServer().get().getServerInfo().getName().equals(target_server_name)) {
                            player.sendMessage(ChatUtils.parseLegacy(language.getString("alreadyConnected")));
                        } else {
                            player.createConnectionRequest(networkPlayer.getCurrentServer().get().getServer()).connect().join();
                        }
                    }
                });
                return;
            }
            player.sendMessage(ChatUtils.toComponent(new String[] {language.getString("usage"),"&7/jump <player name>"}));
        } else {
            source.sendMessage(ChatUtils.parseLegacy(language.getString("canOnlyBeExecutedByAPlayer")));
        }
    }

    @Override
    public List<String> suggest(Invocation invocation) {
        return TabUtils.returnTab(invocation.arguments(), TabUtils.getAllPlayers(server));
    }
}

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
        if (!source.hasPermission(config.getTable("permissions").getString("jump"))) {
            ChatUtils.sendMsg(source, config.getString("prefix"), language.getString("noPermission"));
            return;
        }
        if (source instanceof Player player) {
            if (args.length == 1) {
                server.getAllPlayers().forEach(networkPlayer -> {
                    if (networkPlayer.getUsername().equalsIgnoreCase(args[0])) {
                        String target_server_name = networkPlayer.getCurrentServer().get().getServer().getServerInfo().getName();
                        if (player.getCurrentServer().get().getServerInfo().getName().equals(target_server_name)) {
                            ChatUtils.sendMsg(source, language.getString("alreadyConnected"));
                        } else {
                            ChatUtils.sendMsg(source,language.getString("creatingConnectionRequest").replace("%server%",target_server_name));
                            player.createConnectionRequest(networkPlayer.getCurrentServer().get().getServer()).connect().join();
                        }
                    }
                });
                return;
            }
            ChatUtils.sendMsg(source,language.getString("invalidUsage"), "/jump <player name>");
        } else {
            ChatUtils.sendMsg(source,language.getString("canOnlyBeExecutedByAPlayer"));
        }
    }

    @Override
    public List<String> suggest(Invocation invocation) {
        return TabUtils.returnTab(invocation.arguments(), TabUtils.getAllPlayers(server));
    }
}

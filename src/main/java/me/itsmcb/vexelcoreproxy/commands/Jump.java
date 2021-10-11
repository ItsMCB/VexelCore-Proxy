package me.itsmcb.vexelcoreproxy.commands;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import me.itsmcb.vexelcoreproxy.VexelCoreProxy;
import me.itsmcb.vexelcoreproxy.utils.ChatUtils;
import me.itsmcb.vexelcoreproxy.utils.TabUtils;
import org.spongepowered.configurate.CommentedConfigurationNode;

import java.util.List;

public class Jump implements SimpleCommand {

    private final ProxyServer server;
    private final CommentedConfigurationNode config;
    private final CommentedConfigurationNode language;

    public Jump(VexelCoreProxy VCP) {
        this.server = VCP.getProxyServer();
        this.config = VCP.getConfig().get();
        this.language = VCP.getLang().get();
    }

    @Override
    public void execute(Invocation invocation) {
        CommandSource source = invocation.source();
        String[] args = invocation.arguments();
        if (!source.hasPermission(config.node("jump").node("permission").getString())) {
            ChatUtils.sendMsg(source, language.node("general").node("prefix").getString(), language.node("error").node("noPermission").getString());
            return;
        }
        if (source instanceof Player player) {
            if (args.length == 1) {
                server.getAllPlayers().forEach(networkPlayer -> {
                    if (networkPlayer.getUsername().equalsIgnoreCase(args[0])) {
                        String target_server_name = networkPlayer.getCurrentServer().get().getServer().getServerInfo().getName();
                        if (player.getCurrentServer().get().getServerInfo().getName().equals(target_server_name)) {
                            ChatUtils.sendMsg(source, language.node("error").node("alreadyConnected").getString());
                        } else {
                            ChatUtils.sendMsg(source,language.node("confirmation").node("creatingConnectionRequest").getString().replace("[serverName]",target_server_name));
                            player.createConnectionRequest(networkPlayer.getCurrentServer().get().getServer()).connect().join();
                        }
                    }
                });
                return;
            }
            ChatUtils.sendMsg(source, language.node("error").node("invalidUsage").getString(), "/jump <player name>");
        } else {
            ChatUtils.sendMsg(source, language.node("error").node("canOnlyBeExecutedByAPlayer").getString());
        }
    }

    @Override
    public List<String> suggest(Invocation invocation) {
        return TabUtils.returnTab(invocation.arguments(), TabUtils.getAllPlayers(server));
    }
}

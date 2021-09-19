package me.itsmcb.vexelcoreproxy.commands;

import com.moandjiezana.toml.Toml;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import me.itsmcb.vexelcoreproxy.VexelCoreProxy;
import me.itsmcb.vexelcoreproxy.utils.ChatUtils;
import me.itsmcb.vexelcoreproxy.utils.TabUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.title.Title;

import java.util.Arrays;
import java.util.List;

public class Broadcast implements SimpleCommand {

    private final ProxyServer server;
    private final Toml config;
    private final Toml language;
    private final Toml broadcast;

    public Broadcast(VexelCoreProxy VCP) {
        this.server = VCP.getProxyServer();
        this.config = VCP.getConfig();
        this.language = config.getTable("language");
        this.broadcast = config.getTable("broadcast");
    }

    @Override
    public void execute(Invocation invocation) {
        CommandSource source = invocation.source();
        String[] args = invocation.arguments();
        String sender = broadcast.getString("consoleName");
        if (!source.hasPermission(config.getTable("permissions").getString("broadcast"))) {
            ChatUtils.sendMsg(source, config.getString("prefix"), language.getString("noPermission"));
            return;
        }
        if (source instanceof Player player) {
            sender = player.getUsername();
        }
        if (args.length > 2) {
            StringBuilder bcmsg = new StringBuilder("");
            String[] msg_args = Arrays.copyOfRange(args, 2, args.length);
            TextComponent prefix = ChatUtils.parseLegacy(broadcast.getString("prefix"));
            for (String temp : msg_args) {
                bcmsg.append(temp).append(" ");
            }
            final TextComponent bkcontent = prefix.append(ChatUtils.parseLegacy(" " + bcmsg));
            TextComponent bkInfo = prefix.hoverEvent(HoverEvent.showText(ChatUtils.parseLegacy(broadcast.getString("hoverInfo").replace("%sender%", sender))));
            if (args[0].equalsIgnoreCase("title")) {
                final Title title = Title.title(prefix, ChatUtils.parseLegacy(bcmsg.toString()));
                if (args[1].equalsIgnoreCase("all")) {
                    server.showTitle(title);
                } else {
                    server.getAllServers().forEach(connectedServer -> {
                        if (args[1].equalsIgnoreCase(connectedServer.getServerInfo().getName())) {
                            connectedServer.getPlayersConnected().forEach(connectedPlayer -> connectedPlayer.showTitle(title));
                        }
                    });
                }
            }
            if (args[0].equalsIgnoreCase("actionbar")) {
                if (args[1].equalsIgnoreCase("all")) {
                    server.sendActionBar(bkcontent);
                } else {
                    server.getAllServers().forEach(connectedServer -> {
                        if (args[1].equalsIgnoreCase(connectedServer.getServerInfo().getName())) {
                            connectedServer.getPlayersConnected().forEach(connectedPlayer -> connectedPlayer.sendActionBar(bkcontent));
                        }
                    });
                }
            }
            if (args[0].equalsIgnoreCase("chat") || broadcast.getBoolean("alwaysSendChatBroadcast")) {
                TextComponent final_msg = Component.text().append(bkInfo).append(ChatUtils.parseLegacy(" " + bcmsg)).build();
                if (args[1].equalsIgnoreCase("all")) {
                    server.sendMessage(final_msg);
                } else {
                    server.getAllServers().forEach(connectedServer -> {
                        if (args[1].equalsIgnoreCase(connectedServer.getServerInfo().getName())) {
                            connectedServer.getPlayersConnected().forEach(connectedPlayer -> connectedPlayer.sendMessage(final_msg));
                        }
                    });
                }
                ChatUtils.sendComponentMsg(source, config.getString("prefix"), Component.text(language.getString("broadcastSentSuccessfully")).hoverEvent(HoverEvent.showText(bkcontent)));
            }
        }
        if (args.length < 2) {
            ChatUtils.sendMsg(source, language.getString("invalidUsage"), "/broadcast <title/actionbar/chat> <server name> <message>");
        }
    }


    @Override
    public List<String> suggest(Invocation invocation) {
        List<String> types = Arrays.asList("title", "actionbar", "chat");
        List<String> targets = TabUtils.getAllServers(server);
        targets.add("all");
        return TabUtils.returnTab(invocation.arguments(), types, targets);
        // Make it so multiple tabs can be inputted
    }
}

package me.itsmcb.vexelcoreproxy.commands;

import com.moandjiezana.toml.Toml;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import me.itsmcb.vexelcoreproxy.VexelCoreProxy;
import me.itsmcb.vexelcoreproxy.utils.ChatUtils;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;

public class HelpOp implements SimpleCommand {

    private final ProxyServer server;
    private final Toml config;
    private final Toml language;
    private final Toml permissions;

    public HelpOp(VexelCoreProxy VCP) {
        this.server = VCP.getProxyServer();
        this.config = VCP.getConfig();
        this.language = config.getTable("language");
        this.permissions = config.getTable("permissions");
    }

    @Override
    public void execute(Invocation invocation) {
        CommandSource source = invocation.source();
        String[] args = invocation.arguments();
        if (!source.hasPermission(config.getTable("permissions").getString("helpop"))) {
            ChatUtils.sendMsg(source, config.getString("prefix"), language.getString("noPermission"));
            return;
        }
        if (args.length == 0) {
            ChatUtils.sendMsg(source, language.getString("invalidUsage"), "/helpop <message to staff>");
            return;
        }
        if (source instanceof Player requester) {
            String reason = String.join(" ",args);
            String requesterServer = requester.getCurrentServer().get().getServerInfo().getName();
            // Show requester their message and assure them that online staff will see it.
            // If they are staff who see the help requests, they don't get shown the confirmation.
            if (!requester.hasPermission(permissions.getString("seeHelpOp"))) {
                ChatUtils.sendMsg(source, language.getString("helpOpStaffAlert")
                        .replace("%requester%", requester.getUsername())
                        .replace("%server%", requesterServer)
                        .replace("%reason%", reason)
                );
                ChatUtils.sendMsg(source, language.getString("helpOpConfirmation"));
            }
            // Send to online staff
            server.getAllPlayers().stream().filter(player -> player.hasPermission(permissions.getString("seeHelpOp"))).allMatch(player -> {
                TextComponent alert = ChatUtils.parseLegacy(language.getString("helpOpStaffAlert")
                        .replace("%requester%", requester.getUsername())
                        .replace("%server%", requesterServer)
                        .replace("%reason%", reason))
                        .hoverEvent(HoverEvent.showText(ChatUtils.parseLegacy(language.getString("clickToTeleport").replace("%server%", requesterServer))))
                        .clickEvent(ClickEvent.runCommand("/server " + requesterServer));
                player.sendMessage(alert);
                return true;
            });
        } else {
            ChatUtils.sendMsg(source, language.getString("canOnlyBeExecutedByAPlayer"));
        }
    }
}

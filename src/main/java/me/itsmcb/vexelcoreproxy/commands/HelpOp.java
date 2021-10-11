package me.itsmcb.vexelcoreproxy.commands;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import me.itsmcb.vexelcoreproxy.VexelCoreProxy;
import me.itsmcb.vexelcoreproxy.utils.ChatUtils;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import org.spongepowered.configurate.CommentedConfigurationNode;

public class HelpOp implements SimpleCommand {

    private final ProxyServer server;
    private final CommentedConfigurationNode config;
    private final CommentedConfigurationNode language;

    public HelpOp(VexelCoreProxy VCP) {
        this.server = VCP.getProxyServer();
        this.config = VCP.getConfig().get();
        this.language = VCP.getLang().get();
    }

    @Override
    public void execute(Invocation invocation) {
        CommandSource source = invocation.source();
        String[] args = invocation.arguments();
        if (!source.hasPermission(config.node("helpop").node("use-permission").getString())) {
            ChatUtils.sendMsg(source, language.node("general").node("prefix").getString(), language.node("error").node("noPermission").getString());
            return;
        }
        if (args.length == 0) {
            ChatUtils.sendMsg(source, language.node("error").node("invalidUsage").getString(), "/helpop <message to staff>");
            return;
        }
        if (source instanceof Player requester) {
            String reason = String.join(" ",args);
            String requesterServer = requester.getCurrentServer().get().getServerInfo().getName();
            // Show requester their message and assure them that online staff will see it.
            // If they are staff who see the help requests, they don't get shown the confirmation.
            if (!requester.hasPermission(config.node("helpop").node("see-help-op-permission").getString())) {
                ChatUtils.sendMsg(source, language.node("helpop").node("staffAlert").getString()
                        .replace("[requester]", requester.getUsername())
                        .replace("[serverName]", requesterServer)
                        .replace("[reason]", reason)
                );
                ChatUtils.sendMsg(source, language.node("confirmation").node("helpop").getString());
            }
            // Send to online staff
            server.getAllPlayers().stream().filter(player -> player.hasPermission(config.node("helpop").node("see-help-op-permission").getString())).allMatch(player -> {
                TextComponent alert = ChatUtils.parseLegacy(language.node("helpop").node("staffAlert").getString()
                        .replace("[requester]", requester.getUsername())
                        .replace("[serverName]", requesterServer)
                        .replace("[reason]", reason))
                        .hoverEvent(HoverEvent.showText(ChatUtils.parseLegacy(language.node("helpop").node("clickToTeleport").getString().replace("[serverName]", requesterServer))))
                        .clickEvent(ClickEvent.runCommand("/server " + requesterServer));
                player.sendMessage(alert);
                return true;
            });
        } else {
            ChatUtils.sendMsg(source, language.node("error").node("canOnlyBeExecutedByAPlayer").getString());
        }
    }
}

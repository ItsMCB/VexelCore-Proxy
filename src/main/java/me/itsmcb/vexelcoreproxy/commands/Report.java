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

public class Report implements SimpleCommand {

    private final ProxyServer server;
    private final CommentedConfigurationNode config;
    private final CommentedConfigurationNode language;

    public FeatureName(VexelCoreProxy VCP) {
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
            ChatUtils.sendMsg(source, language.node("error").node("invalidUsage").getString(), "/report <player> <reason> to staff");
            return;
        }
        if (source instanceof Player reporter) {
            String reporter = String.join(" ", args);
            Player reported = VelocityUtils.getPlayer(server, args[0]); //TODO: Not sure if this works with offlinePlayers, will need to test to see
            String reason = String.join(" ",args);
            String reporterServer = requester.getCurrentServer().get().getServerInfo().getName();
            // Show reporter their message
            if (!requester.hasPermission(config.node("report").node("see-report-permission").getString())) {
                ChatUtils.sendMsg(source, language.node("report").node("reportAlert").getString()
                        .replace("[requester]", requester.getUsername())
                        .replace("[serverName]", reporterServer)
                        .replace("[reason]", reason)
                );
                ChatUtils.sendMsg(source, language.node("confirmation").node("report").getString());
            }
            // Send to online staff
            server.getAllPlayers().stream().filter(player -> player.hasPermission(config.node("report").node("see-report-permission").getString())).allMatch(player -> {
                TextComponent alert = ChatUtils.parseLegacy(language.node("helpop").node("reportAlert").getString()
                                .replace("[reporter]", reporter.getUsername())
                                .replace("[reported]", reported)
                                .replace("[serverName]", reporterServer)
                                .replace("[reason]", reason))
                        .hoverEvent(HoverEvent.showText(ChatUtils.parseLegacy(language.node("report").node("clickToGo").getString().replace("[serverName]", reporterServer))))
                        .clickEvent(ClickEvent.runCommand("/server " + reporterServer));
                player.sendMessage(alert);
                return true;
            });
        } else {
            ChatUtils.sendMsg(source, language.node("error").node("canOnlyBeExecutedByAPlayer").getString());
        }
    }
}
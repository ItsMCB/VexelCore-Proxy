package me.itsmcb.vexelcoreproxy.commands;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import me.itsmcb.vexelcoreproxy.VexelCoreProxy;
import me.itsmcb.vexelcoreproxy.utils.ChatUtils;
import me.itsmcb.vexelcoreproxy.utils.TabUtils;
import me.itsmcb.vexelcoreproxy.utils.VelocityUtils;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import org.spongepowered.configurate.CommentedConfigurationNode;

import java.util.Arrays;
import java.util.List;

public class Report implements SimpleCommand {

    private final ProxyServer server;
    private final CommentedConfigurationNode config;
    private final CommentedConfigurationNode language;

    public Report(VexelCoreProxy VCP) {
        this.server = VCP.getProxyServer();
        this.config = VCP.getConfig().get();
        this.language = VCP.getLang().get();
    }

    @Override
    public void execute(Invocation invocation) {
        CommandSource source = invocation.source();
        String[] args = invocation.arguments();
        if (!source.hasPermission(config.node("report").node("permission").getString())) {
            ChatUtils.sendMsg(source, language.node("general").node("prefix").getString(), language.node("error").node("noPermission").getString());
            return;
        }
        if (args.length <= 1) {
            ChatUtils.sendMsg(source, language.node("error").node("invalidUsage").getString(), "/report <player> <reason>");
            return;
        }
        if (source instanceof Player reportingPlayer) {
            StringBuilder reason = new StringBuilder("");
            String[] reasonData = Arrays.copyOfRange(args, 1, args.length);
            for (String temp : reasonData) {
                reason.append(temp).append(" ");
            }
            String reporterServer = reportingPlayer.getCurrentServer().get().getServerInfo().getName();

            // Send to online staff
            TextComponent alert = ChatUtils.parseLegacy(language.node("report").node("reportAlert").getString()
                            .replace("[reporter]", reportingPlayer.getUsername())
                            .replace("[reported]", args[0])
                            .replace("[serverName]", reporterServer)
                            .replace("[reason]", reason))
                    .hoverEvent(HoverEvent.showText(ChatUtils.parseLegacy(language.node("report").node("clickToGo").getString().replace("[serverName]", reporterServer))))
                    .clickEvent(ClickEvent.runCommand("/server " + reporterServer));

            server.getAllPlayers().stream().filter(player -> player.hasPermission(config.node("report").node("see-report-permission").getString())).allMatch(player -> {
                player.sendMessage(alert);
                return true;
            });
            // Show reporter their message
            if (!reportingPlayer.hasPermission(config.node("report").node("see-report-permission").getString())) { // If this check wasn't here, staff would see the report two times.
                reportingPlayer.sendMessage(alert);
            }
        } else {
            ChatUtils.sendMsg(source, language.node("error").node("canOnlyBeExecutedByAPlayer").getString());
        }
    }
}
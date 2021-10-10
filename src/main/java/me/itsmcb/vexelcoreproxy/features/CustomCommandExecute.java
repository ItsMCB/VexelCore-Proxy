package me.itsmcb.vexelcoreproxy.features;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import me.itsmcb.vexelcoreproxy.VexelCoreProxy;
import me.itsmcb.vexelcoreproxy.config.main.cc.CCMDExec;
import me.itsmcb.vexelcoreproxy.utils.ChatUtils;
import org.spongepowered.configurate.CommentedConfigurationNode;

import java.util.Arrays;
import java.util.List;

public class CustomCommandExecute implements SimpleCommand {

    private final ProxyServer server;
    private final CCMDExec data;
    private final CommentedConfigurationNode language;

    public CustomCommandExecute(VexelCoreProxy VCP, CCMDExec data) {
        this.server = VCP.getProxyServer();
        this.data = data;
        this.language = VCP.getLang().get();
    }

    @Override
    public void execute(Invocation invocation) {
        CommandSource source = invocation.source();
        List<String> execute = data.getExecutions();
        execute.forEach(execution -> {
            String command = execution.replace("[consoleProxyExecute]","").replace("[playerProxyExecute]","").replace("[playerServerExecute]","");
            if (execution.contains("[playerName]") && !(source instanceof Player)) {
                source.sendMessage(ChatUtils.parseLegacy(language.node("error").node("customCommandExecute").getString(),command, "&7-", language.node("error").node("canOnlyBeExecutedByAPlayer").getString()));
                return;
            }
            if (source instanceof Player) {
                command = command.replace("[playerName]", ((Player) source).getUsername());
            }
            if (execution.startsWith("[consoleProxyExecute]")) {
                server.getCommandManager().executeAsync(server.getConsoleCommandSource(), command);
                return;
            }
            if (execution.startsWith("[playerProxyExecute]")) {
                server.getCommandManager().executeAsync(source, command);
                return;
            }
            if (execution.startsWith("[playerServerExecute]")) {
                if (source instanceof Player p) {
                    p.spoofChatInput("/" + command);
                } else {
                    source.sendMessage(ChatUtils.parseLegacy(language.node("error").node("canOnlyBeExecutedByAPlayer").getString()));
                }
                return;
            }
            source.sendMessage(ChatUtils.parseLegacy(language.node("error").node("customCommandExecuteContext").getString().replace("[execution]",execution)));

        });
    }
}

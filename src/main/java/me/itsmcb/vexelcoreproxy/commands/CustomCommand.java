package me.itsmcb.vexelcoreproxy.commands;

import com.moandjiezana.toml.Toml;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import me.itsmcb.vexelcoreproxy.VexelCoreProxy;
import me.itsmcb.vexelcoreproxy.utils.ChatUtils;
import net.kyori.adventure.text.TextComponent;

import java.util.ArrayList;
import java.util.Arrays;

public class CustomCommand implements SimpleCommand {

    private final ProxyServer server;
    private final Toml language;
    private final Toml data;

    public CustomCommand(VexelCoreProxy VCP, Toml data) {
        Toml config = VCP.getConfig();
        this.language = config.getTable("language");
        this.server = VCP.getProxyServer();
        this.data = data;
    }

    @Override
    public void execute(Invocation invocation) {
        CommandSource source = invocation.source();
        String[] args = invocation.arguments();
        StringBuilder passedArgs = new StringBuilder();
        Arrays.stream(args).forEach(passedArgs::append);
        String type = data.getString("type");


        //
        // Executor
        //

        if (type.equalsIgnoreCase("playerProxyExecute") || type.equalsIgnoreCase("playerServerExecute")) {
            String execute = data.getString("execute");
            if (data.getString("passArgs").equalsIgnoreCase("yes")) {
                execute = execute + " " + passedArgs;
            }
            if (type.equalsIgnoreCase("playerProxyExecute")) {
                server.getCommandManager().executeAsync(source,execute);
                return;
            }
            // Not a ppe cc, execute through server
            if (source instanceof Player) {
                Player p = (Player) source;
                p.spoofChatInput("/" + execute);
                return;
            }
            source.sendMessage(ChatUtils.parseLegacy(language.getString("canOnlyBeExecutedByAPlayer"))); // Improve later
        }

        //
        // Message
        //

        if (type.equalsIgnoreCase("message")) {
            ArrayList<TextComponent> tcList = new ArrayList<>();
            data.getTables("components").forEach(msgData -> {
                String action = msgData.getString("action");
                if (action != null) {
                    if (action.equalsIgnoreCase("open_url") || action.equalsIgnoreCase("run_command") || action.equalsIgnoreCase("suggest_command") || action.equalsIgnoreCase("copy_to_clipboard"))
                        tcList.add(ChatUtils.clickableComponent(
                                msgData.getString("content"),
                                msgData.getString("hover"),
                                action,
                                msgData.getString("actionValue")));
                } else {
                    tcList.add(ChatUtils.parseLegacy(msgData.getString("content")));
                }
            });
            tcList.forEach(source::sendMessage);
        }
    }
}

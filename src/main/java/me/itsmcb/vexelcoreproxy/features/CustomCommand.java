package me.itsmcb.vexelcoreproxy.features;

import com.moandjiezana.toml.Toml;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import me.itsmcb.vexelcoreproxy.VexelCoreProxy;
import me.itsmcb.vexelcoreproxy.utils.ChatUtils;

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
            if (source instanceof Player p) {
                p.spoofChatInput("/" + execute);
                return;
            }
            source.sendMessage(ChatUtils.parseLegacy(language.getString("canOnlyBeExecutedByAPlayer"))); // Improve later
        }

        //
        // Message
        //

        if (type.equalsIgnoreCase("message")) {
            ChatUtils.sendCCMessage(data).forEach(source::sendMessage);
        }
    }
}

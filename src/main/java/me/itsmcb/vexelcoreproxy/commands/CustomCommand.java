package me.itsmcb.vexelcoreproxy.commands;

import com.moandjiezana.toml.Toml;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import me.itsmcb.vexelcoreproxy.VexelCoreProxy;
import me.itsmcb.vexelcoreproxy.utils.ChatUtils;

import java.util.Arrays;
import java.util.HashMap;

public class CustomCommand implements SimpleCommand {

    private final ProxyServer server;
    private final HashMap<String, String> map;
    private final Toml language;

    public CustomCommand(VexelCoreProxy VCP, HashMap<String, String>  map) {
        Toml config = VCP.getConfig();
        this.language = config.getTable("language");
        this.server = VCP.getProxyServer();
        this.map = map;
    }

    @Override
    public void execute(Invocation invocation) {
        CommandSource source = invocation.source();
        String[] args = invocation.arguments();
        StringBuilder passedArgs = new StringBuilder();
        Arrays.stream(args).forEach(passedArgs::append);
        String type = map.get("type");
        if (type.equalsIgnoreCase("playerProxyExecute") || type.equalsIgnoreCase("playerServerExecute")) {
            String execute = map.get("execute");
            if (map.get("passArgs").equalsIgnoreCase("yes")) {
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
    }
}

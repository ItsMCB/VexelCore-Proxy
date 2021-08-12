package me.itsmcb.vexelcoreproxy.commands;

import com.moandjiezana.toml.Toml;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.plugin.PluginDescription;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import me.itsmcb.vexelcoreproxy.VexelCoreProxy;
import me.itsmcb.vexelcoreproxy.utils.ChatUtils;
import me.itsmcb.vexelcoreproxy.utils.MessageUtils;
import me.itsmcb.vexelcoreproxy.utils.PermissionUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.NamedTextColor;

import java.util.HashMap;
import java.util.List;

public class VCP implements SimpleCommand {

    private final ProxyServer server;
    public VCP(ProxyServer server) {
        this.server = server;
    }

    public Toml language = VexelCoreProxy.getConfig().getTable("language");
    public Toml special = VexelCoreProxy.getConfig().getTable("special");
    public Toml features = VexelCoreProxy.getConfig().getTable("features");
    public static PluginDescription vcp = VexelCoreProxy.getProxyServer().getPluginManager().getPlugin("vexelcore").get().getDescription();
    public static String pluginName = vcp.getName().get();
    public static String pluginVersion = vcp.getVersion().get();
    @Override
    public void execute(Invocation invocation) {
        CommandSource source = invocation.source();
        String[] args = invocation.arguments();
        if (source instanceof Player) {
            Player p = (Player) source;
            if (args.length == 1) {
                if (args[0].equalsIgnoreCase("version")) {
                    showVersion(source);
                    return;
                }
            }
            if (!p.hasPermission(PermissionUtils.get("staff"))) {
                p.sendMessage(MessageUtils.get("noPermission"));
                p.sendMessage(ChatUtils.parseLegacy("&7Plugin information can be viewed with &3/vcp version"));
                return;
            }
        }
        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("reload")) {
                VexelCoreProxy.loadConfigs(server, true);
                source.sendMessage(MessageUtils.getWithPrefix("reloadedConfig"));
                return;
            }
            if (args[0].equalsIgnoreCase("version")) {
                showVersion(source);
                return;
            }
        }
        showHelp(source);
        return;
    }

    public static void showVersion(CommandSource source) {
        PluginDescription vcp = VexelCoreProxy.getProxyServer().getPluginManager().getPlugin("vexelcore").get().getDescription();
        source.sendMessage(ChatUtils.parseLegacy("&7Running &a" + pluginName + " v" + pluginVersion));
        Component updateTXT = Component.text("https://github.com/Vexelosity/VexelCore-Proxy").color(NamedTextColor.DARK_AQUA).clickEvent(ClickEvent.openUrl("https://github.com/Vexelosity/VexelCore-Proxy"));
        source.sendMessage(ChatUtils.parseLegacy("&7The latest version can be downloaded at ").append(updateTXT));
        Component setupGuideTXT = Component.text("https://github.com/Vexelosity/VexelCore-Proxy/wiki/Setup-Guide").color(NamedTextColor.DARK_AQUA).clickEvent(ClickEvent.openUrl("https://github.com/Vexelosity/VexelCore-Proxy/wiki/Setup-Guide"));
        source.sendMessage(ChatUtils.parseLegacy("&7The setup guide can be found at ").append(setupGuideTXT));
        source.sendMessage(ChatUtils.parseLegacy("&7Use &3/vcp help &7to view available commands."));
    }

    public void showHelp(CommandSource source) {
        source.sendMessage(ChatUtils.parseLegacy(special.getString("formatting") + "&a" + pluginName + " v" + pluginVersion + special.getString("formatting") ));
        source.sendMessage(MessageUtils.get("helpL1"));
        source.sendMessage(MessageUtils.get("helpL2"));
        source.sendMessage(MessageUtils.get("helpL3"));
        source.sendMessage(ChatUtils.parseLegacy("\n&3VCP Features Status:"));
        if (features.getBoolean("customGlist")) {
            source.sendMessage(ChatUtils.parseLegacy("&8[&aEnabled&8] &7Custom Glist"));
        } else {
            source.sendMessage(ChatUtils.parseLegacy("&8[&cDisabled&8] &3Custom Glist"));
        }
        source.sendMessage(ChatUtils.parseLegacy("\n&3VCP Custom Commands:"));
        List<HashMap<String, String>> data = VexelCoreProxy.getConfig().getList("customCommand");
        data.forEach((hashMap) -> {
            source.sendMessage(ChatUtils.parseLegacy("&8[&7" + hashMap.get("type") + "&8] &3" + hashMap.get("newCommand") + " &8-> &7/" + hashMap.get("execute")));
        });
        source.sendMessage(ChatUtils.parseLegacy("\n" +language.getString("helpL4")));
    }
}

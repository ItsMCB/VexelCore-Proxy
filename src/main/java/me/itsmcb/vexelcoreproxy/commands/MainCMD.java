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
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.NamedTextColor;

import java.util.HashMap;
import java.util.List;

public class MainCMD implements SimpleCommand {

    private final ProxyServer server;
    private final Toml config;
    private final VexelCoreProxy VCP;
    private final Toml language;
    private final Toml features;
    private final Toml special;
    private final String pluginName;
    private final String pluginVersion;
    public MainCMD(VexelCoreProxy VCP) {
        this.VCP = VCP;
        this.server = VCP.getProxyServer();
        this.config = VCP.getConfig();
        this.language = config.getTable("language");
        this.features = config.getTable("features");
        this.special = config.getTable("special");
        PluginDescription pluginDescription = VCP.getProxyServer().getPluginManager().getPlugin("vexelcore").get().getDescription();
        this.pluginName = pluginDescription.getName().get();
        this.pluginVersion = pluginDescription.getVersion().get();
    }

    @Override
    public void execute(Invocation invocation) {
        CommandSource source = invocation.source();
        String[] args = invocation.arguments();
        if (source instanceof Player) {
            Player p = (Player) source;
            if (!p.hasPermission(config.getTable("permissions").getString("staff"))) {
                p.sendMessage(MessageUtils.toComponent(new String[] {config.getString("prefix"),language.getString("noPermission")}));
                return;
            }
        }
        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("reload")) {
                VexelCoreProxy.loadConfigs(VCP, true);
                source.sendMessage(MessageUtils.toComponent(new String[] {config.getString("prefix"),language.getString("reloadedConfig")}));
                return;
            }
            if (args[0].equalsIgnoreCase("version")) {
                showVersion(source);
                return;
            }
        }
        showHelp(source);
    }

    public void showVersion(CommandSource source) {
        source.sendMessage(ChatUtils.parseLegacy("&7Running &a" + pluginName + " v" + pluginVersion));
        Component updateTXT = Component.text("https://github.com/Vexelosity/VexelCore-Proxy").color(NamedTextColor.DARK_AQUA).clickEvent(ClickEvent.openUrl("https://github.com/Vexelosity/VexelCore-Proxy"));
        source.sendMessage(ChatUtils.parseLegacy("&7The latest version can be downloaded at ").append(updateTXT));
        Component setupGuideTXT = Component.text("https://github.com/Vexelosity/VexelCore-Proxy/wiki/Setup-Guide").color(NamedTextColor.DARK_AQUA).clickEvent(ClickEvent.openUrl("https://github.com/Vexelosity/VexelCore-Proxy/wiki/Setup-Guide"));
        source.sendMessage(ChatUtils.parseLegacy("&7The setup guide can be found at ").append(setupGuideTXT));
        source.sendMessage(ChatUtils.parseLegacy("&7Use &3/vcp help &7to view available commands."));
    }

    public void showHelp(CommandSource source) {
        source.sendMessage(ChatUtils.parseLegacy(special.getString("formatting") + "&a" + pluginName + " v" + pluginVersion + special.getString("formatting") ));
        source.sendMessage(MessageUtils.toComponent(language.getString("helpL1")));
        source.sendMessage(MessageUtils.toComponent(language.getString("helpL2")));
        source.sendMessage(MessageUtils.toComponent(language.getString("helpL3")));
        source.sendMessage(ChatUtils.parseLegacy("\n&3VCP Features Status:"));
        if (features.getBoolean("customGlist")) {
            source.sendMessage(ChatUtils.parseLegacy("&8[&aEnabled&8] &7Custom Glist"));
        } else {
            source.sendMessage(ChatUtils.parseLegacy("&8[&cDisabled&8] &3Custom Glist"));
        }
        source.sendMessage(ChatUtils.parseLegacy("\n&3VCP Custom Commands:"));
        List<HashMap<String, String>> data = config.getList("customCommand");
        data.forEach((hashMap) -> {
            source.sendMessage(ChatUtils.parseLegacy("&8[&7" + hashMap.get("type") + "&8] &3" + hashMap.get("newCommand") + " &8-> &7/" + hashMap.get("execute")));
        });
        source.sendMessage(ChatUtils.parseLegacy("\n" +language.getString("helpL4")));
    }
}

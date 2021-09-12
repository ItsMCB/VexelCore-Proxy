package me.itsmcb.vexelcoreproxy.commands;

import com.moandjiezana.toml.Toml;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.plugin.PluginDescription;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import me.itsmcb.vexelcoreproxy.VexelCoreProxy;
import me.itsmcb.vexelcoreproxy.utils.ChatUtils;
import me.itsmcb.vexelcoreproxy.utils.ConfigUtils;
import me.itsmcb.vexelcoreproxy.utils.TabUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.NamedTextColor;

import java.util.List;

public class MainCMD implements SimpleCommand {

    private final Toml config;
    private final VexelCoreProxy instance;
    private final Toml language;
    private final Toml glist;
    private final String pluginName;
    private final String pluginVersion;
    public MainCMD(VexelCoreProxy instance) {
        this.instance = instance;
        ProxyServer server = instance.getProxyServer();
        this.config = instance.getConfig();
        this.language = config.getTable("language");
        PluginDescription pluginDescription = instance.getProxyServer().getPluginManager().getPlugin("vexelcore").get().getDescription();
        this.glist = config.getTable("glist");
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
                p.sendMessage(ChatUtils.toComponent(new String[] {config.getString("prefix"),language.getString("noPermission")}));
                return;
            }
        }
        if (args.length >= 1) {
            if (args[0].equalsIgnoreCase("reload")) {
                ConfigUtils.loadConfigs(instance, true);
                source.sendMessage(ChatUtils.toComponent(new String[] {config.getString("prefix"),language.getString("reloadedConfig")}));
                return;
            }
            if (args[0].equalsIgnoreCase("version")) {
                showVersion(source);
                return;
            }
            if (args[0].equalsIgnoreCase("features")) {
                showEnabledFeatures(source);
                return;
            }
            if (args[0].equalsIgnoreCase("cc") || args[0].equalsIgnoreCase("customcommand")) {
                if (args.length >= 2) {
                    if (args[1].equalsIgnoreCase("list")) {
                        showCustomCommands(source);
                        return;
                    }
                }
                source.sendMessage(ChatUtils.toComponent(language.getString("helpC1")));
                return;
            }
        }
        showHelp(source);
    }

    @Override
    public List<String> suggest(Invocation invocation) {
        String[] args = invocation.arguments();
        return TabUtils.returnTab(args, List.of("reload","version","features","customcommand"));
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
        source.sendMessage(ChatUtils.parseLegacy(language.getString("bar") + "&a" + pluginName + " v" + pluginVersion + language.getString("bar") ));
        source.sendMessage(ChatUtils.toComponent(language.getString("helpL1")));
        source.sendMessage(ChatUtils.toComponent(language.getString("helpL2")));
        source.sendMessage(ChatUtils.toComponent(language.getString("helpL3")));
        source.sendMessage(ChatUtils.toComponent(language.getString("helpL4")));
        source.sendMessage(ChatUtils.toComponent(language.getString("helpL5")));
        source.sendMessage(ChatUtils.toComponent(language.getString("helpL6")));
    }

    public void showEnabledFeatures(CommandSource source) {
        if (glist.getBoolean("enabled")) {
            source.sendMessage(ChatUtils.parseLegacy("&8[&aEnabled&8] &7Custom Glist"));
        } else {
            source.sendMessage(ChatUtils.parseLegacy("&8[&cDisabled&8] &3Custom Glist"));
        }
    }

    public void showCustomCommands(CommandSource source) {
        config.getTable("customCommand").getTables("data").forEach(data -> {
            String type = data.getString("type");
            switch (type) {
                case "playerProxyExecute", "playerServerExecute" -> source.sendMessage(ChatUtils.parseLegacy("&8[&7" + type + "&8] &3/" + data.getString("newCommand") + " &7-> " + data.getString("execute")));
                case "message" -> {
                    source.sendMessage(ChatUtils.parseLegacy("&8[&7" + type + "&8] &3/" + data.getString("newCommand") + " &7-> "));
                    ChatUtils.sendCCMessage(data).forEach(source::sendMessage);
                }
                default -> source.sendMessage(ChatUtils.parseLegacy("&cError: unknown CC type"));
            }
            data.getString("newCommand");
        });
    }
}

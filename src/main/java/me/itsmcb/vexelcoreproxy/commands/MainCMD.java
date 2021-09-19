package me.itsmcb.vexelcoreproxy.commands;

import com.moandjiezana.toml.Toml;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.plugin.PluginDescription;
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
    private final Toml customCommand;
    private final Toml jump;
    private final Toml broadcast;
    private final Toml playerinformation;
    private final String pluginName;
    private final String pluginVersion;
    public MainCMD(VexelCoreProxy instance) {
        this.instance = instance;
        this.config = instance.getConfig();
        this.language = config.getTable("language");
        PluginDescription pluginDescription = instance.getProxyServer().getPluginManager().getPlugin("vexelcore").get().getDescription();
        this.glist = config.getTable("glist");
        this.customCommand = config.getTable("customCommand");
        this.jump = config.getTable("jump");
        this.broadcast = config.getTable("broadcast");
        this.playerinformation = config.getTable("playerInformation");
        this.pluginName = pluginDescription.getName().get();
        this.pluginVersion = pluginDescription.getVersion().get();
    }

    @Override
    public void execute(Invocation invocation) {
        CommandSource source = invocation.source();
        String[] args = invocation.arguments();
        if (!source.hasPermission(config.getTable("permissions").getString("staff"))) {
            ChatUtils.sendMsg(source, config.getString("prefix"), language.getString("noPermission"));
            return;
        }
        if (args.length >= 1) {
            if (args[0].equalsIgnoreCase("reload")) {
                ConfigUtils.loadConfigs(instance, true);
                ChatUtils.sendMsg(source,config.getString("prefix"),language.getString("reloadedConfig"));
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
                ChatUtils.sendMsg(source,language.getString("helpC1"));
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
        ChatUtils.sendMsg(source, config.getString("prefix"), "&7Running &a" + pluginName + " v" + pluginVersion);
        Component updateTXT = Component.text("https://github.com/Vexelosity/VexelCore-Proxy").color(NamedTextColor.DARK_AQUA).clickEvent(ClickEvent.openUrl("https://github.com/Vexelosity/VexelCore-Proxy"));
        source.sendMessage(ChatUtils.parseLegacy("&7The latest version can be downloaded at").append(updateTXT));
        Component setupGuideTXT = Component.text("https://github.com/Vexelosity/VexelCore-Proxy/wiki/Setup-Guide").color(NamedTextColor.DARK_AQUA).clickEvent(ClickEvent.openUrl("https://github.com/Vexelosity/VexelCore-Proxy/wiki/Setup-Guide"));
        source.sendMessage(ChatUtils.parseLegacy("&7The setup guide can be found at").append(setupGuideTXT));
        ChatUtils.sendMsg(source, "&7Use &3/vcp help &7to view available commands.");
    }

    public void showHelp(CommandSource source) {
        ChatUtils.sendMsg(source,language.getString("bar") + "&a" + pluginName + " v" + pluginVersion + language.getString("bar"));
        ChatUtils.sendMsg(source,language.getString("helpL1"));
        ChatUtils.sendMsg(source,language.getString("helpL2"));
        ChatUtils.sendMsg(source,language.getString("helpL3"));
        ChatUtils.sendMsg(source,language.getString("helpL4"));
        ChatUtils.sendMsg(source,language.getString("helpL5"));
        ChatUtils.sendMsg(source,language.getString("helpL6"));
    }

    public void showEnabledFeatures(CommandSource source) {
        if (customCommand.getBoolean("enabled")) {
            ChatUtils.sendMsg(source, "&8[&aEnabled&8] &7Custom Commands");
        } else {
            ChatUtils.sendMsg(source, "&8[&cDisabled&8] &3Custom Commands");
        }
        if (glist.getBoolean("enabled")) {
            ChatUtils.sendMsg(source, "&8[&aEnabled&8] &7Better Glist");
        } else {
            ChatUtils.sendMsg(source, "&8[&cDisabled&8] &3Better Glist");
        }
        if (jump.getBoolean("enabled")) {
            ChatUtils.sendMsg(source, "&8[&aEnabled&8] &7Jump");
        } else {
            ChatUtils.sendMsg(source, "&8[&cDisabled&8] &3Jump");
        }
        if (broadcast.getBoolean("enabled")) {
            ChatUtils.sendMsg(source, "&8[&aEnabled&8] &7Broadcast");
        } else {
            ChatUtils.sendMsg(source,"&8[&cDisabled&8] &3Broadcast");
        }
        if (playerinformation.getBoolean("enabled")) {
            ChatUtils.sendMsg(source, "&8[&aEnabled&8] &7Player Information");
        } else {
            ChatUtils.sendMsg(source,"&8[&cDisabled&8] &3Player Information");
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

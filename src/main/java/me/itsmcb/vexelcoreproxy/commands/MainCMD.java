package me.itsmcb.vexelcoreproxy.commands;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.plugin.PluginDescription;
import me.itsmcb.vexelcoreproxy.VexelCoreProxy;
import me.itsmcb.vexelcoreproxy.config.main.cc.CCMDExec;
import me.itsmcb.vexelcoreproxy.config.main.cc.CCMDMsg;
import me.itsmcb.vexelcoreproxy.utils.*;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MainCMD implements SimpleCommand {

    private final CommentedConfigurationNode config;
    private final VexelCoreProxy instance;
    private final CommentedConfigurationNode language;
    private final String pluginName;
    private final String pluginVersion;

    public MainCMD(VexelCoreProxy instance) {
        this.instance = instance;
        this.config = instance.getConfig().get();
        this.language = instance.getLang().get();
        PluginDescription pluginDescription = instance.getProxyServer().getPluginManager().getPlugin("vexelcore").get().getDescription();
        this.pluginName = pluginDescription.getName().get();
        this.pluginVersion = pluginDescription.getVersion().get();
    }

    @Override
    public void execute(Invocation invocation) {
        CommandSource source = invocation.source();
        String[] args = invocation.arguments();
        if (!source.hasPermission(config.node("main").node("permission").getString())) {
            ChatUtils.sendMsg(source, language.node("general").node("prefix").getString(), language.node("error").node("noPermission").getString());
            return;
        }
        if (args.length >= 1) {
            if (args[0].equalsIgnoreCase("reload")) {
                long startTime = System.nanoTime();
                ConfigUtils.unloadFeatures(instance);
                instance.getConfig().load();
                ConfigUtils.loadFeatures(instance);
                long endTime = System.nanoTime();
                instance.getLogger().info("All configs have been loaded! Took " + TimeUtils.convertDurationToMs(startTime,endTime));
                ChatUtils.sendMsg(source, language.node("general").node("prefix").getString(), language.node("confirmation").node("reloadedConfig").getString());
                return;
            }
            if (args[0].equalsIgnoreCase("feature") || args[0].equalsIgnoreCase("features")) {
                if (args.length >= 3) {
                    if (config.node(args[2]).isNull()) {
                        ChatUtils.sendMsg(source, language.node("error").node("featureNotFound").getString());
                        return;
                    } else {
                        boolean enableFeature = !args[1].equalsIgnoreCase("disable");
                        try {
                            ConfigUtils.unloadFeatures(instance);
                            instance.getConfig().get().node(args[2]).node("enabled").set(enableFeature);
                            instance.getConfig().save();
                            instance.getConfig().load();
                            ConfigUtils.loadFeatures(instance);
                            ChatUtils.sendMsg(source, language.node("confirmation").node("featureStateChangeSuccess").getString().replace("[feature]",args[2]).replace("[state]",Boolean.toString(enableFeature)));
                            showEnabledFeatures(source, instance);
                        } catch (SerializationException e) {
                            e.printStackTrace();
                        }
                    }
                    return;
                }
                showEnabledFeatures(source, instance);
                return;
            }
            if (args[0].equalsIgnoreCase("cc") || args[0].equalsIgnoreCase("customcommand")) {
                if (args.length >= 2) {
                    if (args[1].equalsIgnoreCase("list")) {
                        showCustomCommands(source);
                        return;
                    }
                }
                // Temp
                try { ChatUtils.sendMsg(source, language.node("helpcc").getList(String.class).get(0)); } catch (Exception ignored) {}
                return;
            }
        }
        showHelp(source);
    }

    @Override
    public List<String> suggest(Invocation invocation) {
        String[] args = invocation.arguments();
        return TabUtils.returnTab(args, List.of("features","customcommand","reload"), List.of("enable","disable"), instance.getFeatureHandler().getAllFeatureIds());
    }

    public void showHelp(CommandSource source) {
        ChatUtils.sendMsg(source, language.node("general").node("bar").getString(), "&a" + pluginName + " v" + pluginVersion, language.node("general").node("bar").getString());
        try { language.node("help").getList(String.class).forEach(msg -> ChatUtils.sendMsg(source, msg)); } catch (Exception ignored) {}
        Component updateTXT = Component.text("https://github.com/Vexelosity/VexelCore-Proxy").color(NamedTextColor.DARK_AQUA).clickEvent(ClickEvent.openUrl("https://github.com/Vexelosity/VexelCore-Proxy"));
        source.sendMessage(ChatUtils.parseLegacy("&7The latest version can be downloaded at ").append(updateTXT));
        Component setupGuideTXT = Component.text("https://github.com/Vexelosity/VexelCore-Proxy/wiki/Setup-Guide").color(NamedTextColor.DARK_AQUA).clickEvent(ClickEvent.openUrl("https://github.com/Vexelosity/VexelCore-Proxy/wiki/Setup-Guide"));
        source.sendMessage(ChatUtils.parseLegacy("&7The setup guide can be found at ").append(setupGuideTXT));
        Component supportTXT = Component.text("https://discord.vexelosity.com").color(NamedTextColor.DARK_AQUA).clickEvent(ClickEvent.openUrl("https://discord.vexelosity.com"));
        source.sendMessage(ChatUtils.parseLegacy("&7Join our Discord for support and more at ").append(supportTXT));
    }

    public void showEnabledFeatures(CommandSource source, VexelCoreProxy instance) {
        instance.getFeatureHandler().getVcpFeatures().forEach(feature -> {
            source.sendMessage(ChatUtils.parseLegacy(getFeatureDisplay(feature.getFeatureId(),feature.getStatus())).clickEvent(ClickEvent.runCommand("/vcp feature " + FeatureUtils.getOppositeStatus(feature.getStatus()) + " " + feature.getFeatureId())).hoverEvent(HoverEvent.showText(ChatUtils.parseLegacy(language.node("general").node("toggleFeature").getString()))));
        });
    }

    public String getFeatureDisplay(String featureName, Boolean enabled) {
        if (enabled) {
            return "&a"+featureName + "&7: true";
        }
        return "&c"+featureName + "&7: false";
    }

    public void showCustomCommands(CommandSource source) {
        try {
            config.node("custom-commands").node("execute-commands").getList(CCMDExec.class).forEach(ccmdExec -> {
                ChatUtils.sendMsg(source, "&3",ccmdExec.getNewCommand(),"&7->&3",ccmdExec.getExecutions().stream().collect(Collectors.joining(", ")));
            });
            config.node("custom-commands").node("send-messages").getList(CCMDMsg.class).forEach(ccmdExec -> {
                ArrayList<TextComponent> tcFinal = new ArrayList<>();
                ccmdExec.getComponents().forEach(ccmdComponent -> tcFinal.add(ChatUtils.getCCMessage(ccmdComponent)));
                ChatUtils.sendMsg(source, "&3 " + ccmdExec.getNewCommand() + " &7->&3 ");
                tcFinal.forEach(source::sendMessage);
            });
        } catch (Exception ignored) {}
    }
}

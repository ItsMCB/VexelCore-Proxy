package me.itsmcb.vexelcoreproxy.utils;

import me.itsmcb.vexelcoreproxy.VexelCoreProxy;
import me.itsmcb.vexelcoreproxy.commands.*;
import me.itsmcb.vexelcoreproxy.config.main.cc.CCMDExec;
import me.itsmcb.vexelcoreproxy.config.main.cc.CCMDMsg;
import me.itsmcb.vexelcoreproxy.features.CustomCommandExecute;
import me.itsmcb.vexelcoreproxy.features.CustomCommandMessage;
import org.spongepowered.configurate.CommentedConfigurationNode;

public class ConfigUtils {

    public static void loadCC(VexelCoreProxy instance) {
        CommentedConfigurationNode config = instance.getYamlConfig().get();
        try {
            config.node("custom-command").node("execute-commands").getList(CCMDExec.class).forEach(cc -> {
                VelocityUtils.registerCommand(new String[] {cc.getNewCommand()},new CustomCommandExecute(instance,cc),instance);
            });
            config.node("custom-command").node("send-messages").getList(CCMDMsg.class).forEach(cc -> {
                VelocityUtils.registerCommand(new String[] {cc.getNewCommand()},new CustomCommandMessage(cc),instance);
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void unloadCC(VexelCoreProxy instance) {
        CommentedConfigurationNode config = instance.getYamlConfig().get();
        try {
            config.node("custom-command").node("execute-commands").getList(CCMDExec.class).forEach(cc -> {
                instance.getProxyServer().getCommandManager().unregister(cc.getNewCommand());
            });
            config.node("custom-command").node("send-messages").getList(CCMDExec.class).forEach(cc -> {
                instance.getProxyServer().getCommandManager().unregister(cc.getNewCommand());
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static int getCCAmount(VexelCoreProxy instance) {
        return getExecuteCCAmount(instance)+getMessageCCAmount(instance);
    }

    public static int getMessageCCAmount(VexelCoreProxy instance) {
        try {
            return instance.getYamlConfig().get().node("custom-command").node("send-messages").getList(CCMDMsg.class).size();
        } catch (Exception ignored) { return 0; }
    }

    public static int getExecuteCCAmount(VexelCoreProxy instance) {
        try {
            return instance.getYamlConfig().get().node("custom-command").node("execute-commands").getList(CCMDExec.class).size();
        } catch (Exception ignored) { return 0; }
    }

    public static void loadFeatures(VexelCoreProxy instance) {
        if (instance.getYamlConfig().get().node("main").node("enabled").getBoolean()) {
            VelocityUtils.registerCommand(new String[] {"vcp","vexelcoreproxy"},new MainCMD(instance),instance);
        }
        if (instance.getYamlConfig().get().node("glist").node("enabled").getBoolean()) {
            VelocityUtils.registerCommand(new String[] {"glist"},new Glist(instance),instance);
        }
        if (instance.getYamlConfig().get().node("jump").node("enabled").getBoolean()) {
            VelocityUtils.registerCommand(new String[] {"jump"},new Jump(instance),instance);
        }
        if (instance.getYamlConfig().get().node("broadcast").node("enabled").getBoolean()) {
            VelocityUtils.registerCommand(new String[] {"broadcast"},new Broadcast(instance),instance);
        }
        if (instance.getYamlConfig().get().node("player-information").node("enabled").getBoolean()) {
            VelocityUtils.registerCommand(new String[] {"playerInformation"},new PlayerInformation(instance),instance);
        }
        if (instance.getYamlConfig().get().node("helpop").node("enabled").getBoolean()) {
            VelocityUtils.registerCommand(new String[] {"helpop"},new HelpOp(instance),instance);
        }
        if (instance.getYamlConfig().get().node("custom-command").node("enabled").getBoolean()) {
            loadCC(instance);
        }
    }

    public static void unloadFeatures(VexelCoreProxy instance) {
        if (instance.getYamlConfig().get().node("main").node("enabled").getBoolean()) {
            instance.getProxyServer().getCommandManager().unregister("vcp");
        }
        if (instance.getYamlConfig().get().node("glist").node("enabled").getBoolean()) {
            instance.getProxyServer().getCommandManager().unregister("glist");
        }
        if (instance.getYamlConfig().get().node("jump").node("enabled").getBoolean()) {
            instance.getProxyServer().getCommandManager().unregister("jump");
        }
        if (instance.getYamlConfig().get().node("broadcast").node("enabled").getBoolean()) {
            instance.getProxyServer().getCommandManager().unregister("broadcast");
        }
        if (instance.getYamlConfig().get().node("player-information").node("enabled").getBoolean()) {
            instance.getProxyServer().getCommandManager().unregister("playerinformation");
        }
        if (instance.getYamlConfig().get().node("helpop").node("enabled").getBoolean()) {
            instance.getProxyServer().getCommandManager().unregister("helpop");
        }
        if (instance.getYamlConfig().get().node("custom-command").node("enabled").getBoolean()) {
            unloadCC(instance);
        }
    }
}

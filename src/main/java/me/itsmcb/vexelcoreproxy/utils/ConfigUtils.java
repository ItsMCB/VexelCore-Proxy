package me.itsmcb.vexelcoreproxy.utils;

import me.itsmcb.vexelcoreproxy.VexelCoreProxy;
import me.itsmcb.vexelcoreproxy.commands.*;
import me.itsmcb.vexelcoreproxy.config.main.cc.CCMDExec;
import me.itsmcb.vexelcoreproxy.config.main.cc.CCMDMsg;
import me.itsmcb.vexelcoreproxy.features.CustomCommandExecute;
import me.itsmcb.vexelcoreproxy.features.CustomCommandMessage;
import me.itsmcb.vexelcoreproxy.features.VCPFeature;
import org.spongepowered.configurate.CommentedConfigurationNode;

import java.util.ArrayList;
import java.util.List;

public class ConfigUtils {

    public static void loadCC(VexelCoreProxy instance) {
        CommentedConfigurationNode config = instance.getConfig().get();
        try {
            config.node("custom-commands").node("execute-commands").getList(CCMDExec.class).forEach(cc -> {
                VelocityUtils.registerCommand(new ArrayList<>(List.of(cc.getNewCommand())),new CustomCommandExecute(instance,cc),instance);
            });
            config.node("custom-commands").node("send-messages").getList(CCMDMsg.class).forEach(cc -> {
                VelocityUtils.registerCommand(new ArrayList<>(List.of(cc.getNewCommand())),new CustomCommandMessage(cc),instance);
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void unloadCC(VexelCoreProxy instance) {
        CommentedConfigurationNode config = instance.getConfig().get();
        try {
            config.node("custom-commands").node("execute-commands").getList(CCMDExec.class).forEach(cc -> {
                instance.getProxyServer().getCommandManager().unregister(cc.getNewCommand());
            });
            config.node("custom-commands").node("send-messages").getList(CCMDExec.class).forEach(cc -> {
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
            return instance.getConfig().get().node("custom-commands").node("send-messages").getList(CCMDMsg.class).size();
        } catch (Exception ignored) { return 0; }
    }

    public static int getExecuteCCAmount(VexelCoreProxy instance) {
        try {
            return instance.getConfig().get().node("custom-commands").node("execute-commands").getList(CCMDExec.class).size();
        } catch (Exception ignored) { return 0; }
    }

    public static void loadFeatures(VexelCoreProxy instance) {
        instance.getFeatureHandler().registerFeature(new VCPFeature(instance, "main", new MainCMD(instance)), true);
        instance.getFeatureHandler().registerFeature(new VCPFeature(instance, "glist", new Glist(instance)), true);
        instance.getFeatureHandler().registerFeature(new VCPFeature(instance, "jump", new Jump(instance)), true);
        instance.getFeatureHandler().registerFeature(new VCPFeature(instance, "broadcast", new Broadcast(instance)), true);
        instance.getFeatureHandler().registerFeature(new VCPFeature(instance, "player-information", new PlayerInformation(instance)), true);
        instance.getFeatureHandler().registerFeature(new VCPFeature(instance, "helpop", new HelpOp(instance)), true);
        instance.getFeatureHandler().registerFeature(new VCPFeature(instance, "report", new Report(instance)), true);
        if (instance.getConfig().get().node("custom-commands").node("enabled").getBoolean()) {
            loadCC(instance);
        }
    }

    public static void unloadFeatures(VexelCoreProxy instance) {
        instance.getFeatureHandler().disableAndUnregisterAllFeatures();
        if (instance.getConfig().get().node("custom-commands").node("enabled").getBoolean()) {
            unloadCC(instance);
        }
    }
}

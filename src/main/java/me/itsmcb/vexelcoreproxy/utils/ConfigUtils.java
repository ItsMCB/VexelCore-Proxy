package me.itsmcb.vexelcoreproxy.utils;

import me.itsmcb.vexelcoreproxy.VexelCoreProxy;
import me.itsmcb.vexelcoreproxy.commands.Broadcast;
import me.itsmcb.vexelcoreproxy.commands.Glist;
import me.itsmcb.vexelcoreproxy.commands.Jump;
import me.itsmcb.vexelcoreproxy.commands.MainCMD;
import me.itsmcb.vexelcoreproxy.features.CustomCommand;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class ConfigUtils {

    public static void loadConfigs(VexelCoreProxy instance, Boolean reload) {
        long startTime = System.nanoTime();
        if (reload) {
            unloadFeatures(instance);
        }
        instance.setConfig(FileUtils.getTomlConfig(instance.getDataDirectory(),"config.toml",instance));
        loadFeatures(instance);
        long endTime = System.nanoTime();
        instance.getLogger().info("All configs have been loaded! Took " + TimeUtils.convertDurationToMs(startTime,endTime));
    }

    public static void setCustomCommands(VexelCoreProxy instance) {
        long startTime = System.nanoTime();
        AtomicInteger amountOfRegisteredCC = new AtomicInteger();
        instance.getConfig().getTable("customCommand").getTables("data").forEach(data -> {
            try {
                VelocityUtils.registerCommand(new String[] {data.getString("newCommand")},new CustomCommand(instance,data),instance);
                amountOfRegisteredCC.getAndIncrement();
            } catch (Exception e) {
                instance.getLogger().warn("Failed to register custom command \"" + data.getString("newCommand") + "\"");
            }
        });
        long endTime = System.nanoTime();
        instance.getLogger().info("Loaded " + amountOfRegisteredCC + " custom command(s) into memory in " + TimeUtils.convertDurationToMs(startTime,endTime));
    }

    public static void unsetCustomCommands(VexelCoreProxy instance) {
        List<HashMap<String, String>> data = instance.getConfig().getTable("customCommand").getList("data");
        data.forEach((hashMap) -> {
            instance.getProxyServer().getCommandManager().unregister(hashMap.get("newCommand"));
        });
    }

    public static void loadFeatures(VexelCoreProxy instance) {
        if (instance.getConfig().getTable("vcp-main").getBoolean("enabled")) {
            VelocityUtils.registerCommand(new String[] {"vcp","vexelcoreproxy"},new MainCMD(instance),instance);
        }
        if (instance.getConfig().getTable("customCommand").getBoolean("enabled"))  {
            setCustomCommands(instance);
        }
        if (instance.getConfig().getTable("glist").getBoolean("enabled")) {
            VelocityUtils.registerCommand(new String[] {"glist"},new Glist(instance),instance);
        }
        if (instance.getConfig().getTable("jump").getBoolean("enabled")) {
            VelocityUtils.registerCommand(new String[] {"jump"},new Jump(instance),instance);
        }
        if (instance.getConfig().getTable("broadcast").getBoolean("enabled")) {
            VelocityUtils.registerCommand(new String[] {"broadcast"},new Broadcast(instance),instance);
        }
    }

    public static void unloadFeatures(VexelCoreProxy instance) {
        if (instance.getConfig().getTable("vcp-main").getBoolean("enabled")) {
            instance.getProxyServer().getCommandManager().unregister("vcp");
        }
        if (instance.getConfig().getTable("customCommand").getBoolean("enabled")) {
            unsetCustomCommands(instance);
        }
        if (instance.getConfig().getTable("glist").getBoolean("enabled")) {
            instance.getProxyServer().getCommandManager().unregister("glist");
        }
        if (instance.getConfig().getTable("jump").getBoolean("enabled")) {
            instance.getProxyServer().getCommandManager().unregister("jump");
        }
        if (instance.getConfig().getTable("broadcast").getBoolean("enabled")) {
            instance.getProxyServer().getCommandManager().unregister("broadcast");
        }
    }
}

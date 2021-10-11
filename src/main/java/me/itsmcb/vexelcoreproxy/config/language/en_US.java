package me.itsmcb.vexelcoreproxy.config.language;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;

import java.util.HashMap;
import java.util.List;

@ConfigSerializable
public class en_US {

    // English - United States
    // Maintained by ItsMCB

    HashMap<String, String> general = new HashMap<String, String>()  {{
        put("prefix", "&3VCP &8>>");
        put("serverName", "&aSouthHollow Network");
        put("bar", "&r&8&m                 &r");
        put("onlinePlayers", "&7- &3[amountOfOnlinePlayers]");
        put("toggleFeature", "&7Click to toggle feature.");
    }};

    private List<String> help = List.of(
            "&3/vcp &7features&8- &7Displays the state of VCP features",
            "&3/vcp &7features [enable/disable] [feature name] &8- &7Enables or disables a plugin feature",
            "&3/vcp &7cc &8- &7Displays help for custom commands",
            "&3/vcp &7reload &8- &7Reloads plugin config.",
            "&7This plugin is modular. Edit the &3config.yml&7 to customize and enable/disable features."
    );

    private List<String> helpcc = List.of(
            "&7CC help coming soon... Use &3/vcp cc list &7to preview currently loaded custom commands."
    );

    HashMap<String, String> error = new HashMap<String, String>()  {{
        put("noPermission", "&c(!) &7You do not have the required permission for this command!");
        put("invalidUsage", "&7Usage:&3");
        put("canOnlyBeExecutedByAPlayer", "&c(!) &7This can only be executed by a player!");
        put("alreadyConnected", "&cYou're already connected to that server!");
        put("playerNotOnline","&c(!) &7That player isn't online!");
        put("customCommandExecute","&c(!) &7An error occurred while running&3");
        put("customCommandExecuteContext","&7Oops! Execution of \"[execution]\" failed because either [playerProxyExecute] or [playerServerExecute] wasn't put before the command.");
        put("featureNotFound","&c(!) &7Feature not found! Ensure you're typing it the same was as it displays in the config.");
    }};

    HashMap<String, String> confirmation = new HashMap<String, String>()  {{
        put("reloadedConfig", "&7The configuration has been reloaded.");
        put("creatingConnectionRequest", "&7Creating connection request to &3[serverName]&7...");
        put("broadcastSentSuccessfully", "&3The broadcast sent successfully!");
        put("canOnlyBeExecutedByAPlayer", "&c(!) &7This can only be executed by a player!");
        put("helpop","&7Online staff have been alerted to your help request. If you still need support, visit &3#support &7in our &3/discord");
        put("featureStateChangeSuccess","&7Successfully set &3[feature] &7to &3[state]");
    }};

    HashMap<String, String> playerInfo = new HashMap<String, String>()  {{
        put("connectionProfileHeader", "&7---==== &3[playerName]'s Connection &7====---");
        put("pinfoUserL0", "&71) &3User Information");
        put("pinfoUserL1", "&7Username:&3");
        put("pinfoUserL2", "&7Locale:&3");
        put("pinfoUserL3", "&7UUID:&3");
        put("pinfoUserL4", "&7Client:&3");
        put("pinfoUserL5", "&7View Distance:&3");
        put("pinfoUserL6", "&7Chat Mode:&3");
        put("pinfoUserL7", "&7IP Address:&3");
        put("pinfoUserL8", "&7Click to paste the player UUID");
        put("pinfoServerL0", "&72) &3Server Information");
        put("pinfoServerL1", "&7Name:&3");
        put("pinfoServerL2", "&7Ping:&3");
        put("pinfoServerL3", "&7Click to connect");
        put("pinfoRPL0", "&73) &3Applied Resource Pack Information");
        put("pinfoRPL1", "&7URL:&3");
        put("pinfoRPL2", "&7Hash:&3");
        put("pinfoRPL3", "&7Origin:&3");
        put("pinfoRPL4", "&7Click to open the pack URL");
        put("playerResourcePackNotFound", "&7Player doesn't have a resource pack applied");
    }};

    HashMap<String, String> helpop = new HashMap<String, String>()  {{
        put("staffAlert", "&3[requester] &7made a help request in &3[serverName] &7for reason: &3[reason]");
        put("clickToTeleport", "&7Click to join [serverName]");
    }};
}

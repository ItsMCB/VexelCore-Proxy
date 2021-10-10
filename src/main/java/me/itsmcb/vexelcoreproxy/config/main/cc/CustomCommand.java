package me.itsmcb.vexelcoreproxy.config.main.cc;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;

import java.util.List;

@ConfigSerializable
public class CustomCommand {
    private Boolean enabled = true;

    private List<CCMDExec> executeCommands = List.of(
            new CCMDExec("hub", List.of("[playerProxyExecute]server hub")),
            new CCMDExec("lobby", List.of("[playerProxyExecute]server hub")),
            new CCMDExec("survival", List.of("[playerProxyExecute]server survival")),
            new CCMDExec("nds", List.of("[playerProxyExecute]server natural-disasters")),
            new CCMDExec("ndsdev", List.of("[playerProxyExecute]server Natural-Disasters-Dev")),
            new CCMDExec("creative", List.of("[playerProxyExecute]server creative")),
            new CCMDExec("tnt", List.of("[playerProxyExecute]server tntrunv2")),
            new CCMDExec("kr", List.of("[playerProxyExecute]server kingdomrush")),
            new CCMDExec("snapshot", List.of("[playerProxyExecute]server snapshot")),
            new CCMDExec("events", List.of("[playerProxyExecute]server events")),
            new CCMDExec("walls", List.of("[playerProxyExecute]server walls")),
            new CCMDExec("buy", List.of("[playerProxyExecute]store")),
            new CCMDExec("donate", List.of("[playerProxyExecute]store"))
    );

    private List<CCMDMsg> sendMessages = List.of(
            new CCMDMsg("discord", List.of(
                    new CCMDComponent("&7Click to join our &3Discord", "&7Click me!", "OPEN_URL", "https://discord.gg/V4ukMbe")
            )),
            new CCMDMsg("help", List.of(
                    new CCMDComponent("&7Need support? Use &3/helop <message to staff>&7 to message online staff.", "&7Click me!", "SUGGEST_COMMAND", "/helpop "),
                    new CCMDComponent("&7Additional support can be found on our &3Discord&7.", "&7Click me!", "OPEN_URL", "https://discord.gg/V4ukMbe")
            )),
            new CCMDMsg("website", List.of(
                    new CCMDComponent("&7Click to view our &3website", "&7Click me!", "OPEN_URL", "https://southhollow.net/")
            )),
            new CCMDMsg("store", List.of(
                    new CCMDComponent("&7Click to view our &3store", "&7Click me!", "OPEN_URL", "https://store.southhollow.net/")
            )),
            new CCMDMsg("secretmessage", List.of(
                    new CCMDComponent("&7Hmmm. What's this?", "&7Click to copy a secret message!", "COPY_TO_CLIPBOARD", "You're a nerd!")
            ))
    );
}

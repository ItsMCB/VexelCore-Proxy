package me.itsmcb.vexelcoreproxy.config.main;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;

import java.util.List;

@ConfigSerializable
public class Broadcast {

    private Boolean enabled = true;

    private String permission = "vcp.broadcast";

    private List<String> aliases = List.of("broadcast");

    private String prefix = "&7[&aBroadcast&7]";

    private String hoverInfo = "&7Sender: &3%sender%";

    private Boolean alwaysSendChatBroadcast = true;

    private String consoleName = "CONSOLE";

}

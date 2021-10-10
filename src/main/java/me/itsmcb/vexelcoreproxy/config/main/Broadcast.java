package me.itsmcb.vexelcoreproxy.config.main;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;

@ConfigSerializable
public class Broadcast {

    private Boolean enabled = true;

    private String permission = "vcp.broadcast";

    private String prefix = "&7[&aBroadcast&7]";

    private String hoverInfo = "&7Sender: &3%sender%";

    private Boolean alwaysSendChatBroadcast = true;

    private String consoleName = "CONSOLE";

}

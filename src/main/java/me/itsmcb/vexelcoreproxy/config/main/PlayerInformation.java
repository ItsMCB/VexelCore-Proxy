package me.itsmcb.vexelcoreproxy.config.main;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;

import java.util.List;

@ConfigSerializable
public class PlayerInformation {

    private Boolean enabled = true;
    private String permission = "vcp.playerinformation";
    private List<String> aliases = List.of("pinfo","playerinformation");

}

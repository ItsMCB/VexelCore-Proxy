package me.itsmcb.vexelcoreproxy.config.main;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;

import java.util.List;

@ConfigSerializable
public class Glist {

    private Boolean enabled = true;
    private String permission = "vcp.glist";
    private List<String> aliases = List.of("glist");

}

package me.itsmcb.vexelcoreproxy.config.main;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;

import java.util.List;

@ConfigSerializable
public class Main {

    private Boolean enabled = true;
    private String permission = "vcp.staff";
    private List<String> aliases = List.of("vcp","vexelcoreproxy");

}

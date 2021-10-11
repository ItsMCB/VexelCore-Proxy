package me.itsmcb.vexelcoreproxy.config.main;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;

import java.util.List;

@ConfigSerializable
public class Jump {

    private Boolean enabled = true;
    private String permission = "vcp.jump";
    private List<String> aliases = List.of("jump");
}

package me.itsmcb.vexelcoreproxy.config.main;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;

@ConfigSerializable
public class Jump {

    private Boolean enabled = true;
    private String permission = "vcp.jump";
}

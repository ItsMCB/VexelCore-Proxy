package me.itsmcb.vexelcoreproxy.config.main.cc;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;

@ConfigSerializable
public class CCMDComponent {

    private String content;
    private String hover;
    private String action;
    private String actionValue;

    CCMDComponent() {}
    CCMDComponent(String content, String hover, String action, String actionValue) {
        this.content = content;
        this.hover = hover;
        this.action = action;
        this.actionValue = actionValue;
    }

    public String content() {
        return this.content;
    }
    public String hover() {
        return this.hover;
    }
    public String action() {
        return this.action;
    }
    public String actionValue() {
        return this.actionValue;
    }
}

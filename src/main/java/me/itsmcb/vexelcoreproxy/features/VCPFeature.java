package me.itsmcb.vexelcoreproxy.features;

import com.velocitypowered.api.command.Command;
import me.itsmcb.vexelcoreproxy.VexelCoreProxy;
import me.itsmcb.vexelcoreproxy.utils.VelocityUtils;

import java.util.ArrayList;
import java.util.List;

public class VCPFeature {

    private VexelCoreProxy instance;
    private String featureId;
    private String firstAlias;
    private Boolean status;
    private List<String> aliases;
    private Command command;

    public VCPFeature(VexelCoreProxy instance, String featureId, Command command) {
        this.instance = instance;
        this.featureId = featureId;
        this.command = command;
        try {
            List<String> aliases = instance.getConfig().get().node(featureId).node("aliases").getList(String.class);
            this.firstAlias = aliases.get(0);
            if (aliases.size() > 1) {
                this.aliases = aliases.subList(1,aliases.size());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getFirstAlias() {
        return this.firstAlias;
    }

    public List<String> getAdditionAliases() { return this.aliases; }

    public Command getCommand() { return command; }

    public String getFeatureId() {
        return this.featureId;
    }

    public Boolean getStatus() {
        return this.status;
    }

    public void setStatus(Boolean newStatus) {
        this.status = newStatus;
    }
}

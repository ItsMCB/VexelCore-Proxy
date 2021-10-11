package me.itsmcb.vexelcoreproxy.features;

import com.velocitypowered.api.command.Command;
import me.itsmcb.vexelcoreproxy.VexelCoreProxy;
import me.itsmcb.vexelcoreproxy.utils.VelocityUtils;

import java.util.ArrayList;
import java.util.List;

public class VCPFeature {

    private VexelCoreProxy instance;
    private String featureId;
    private String newCommand;
    private Boolean status;
    private List<String> aliases;
    private Command command;

    public VCPFeature(VexelCoreProxy instance, String featureId, Command command) {
        this.instance = instance;
        this.featureId = featureId;
        this.command = command;
        try {
            List<String> aliases = instance.getConfig().get().node(featureId).node("aliases").getList(String.class);
            this.newCommand = aliases.get(0);
            if (aliases.size() > 1) {
                this.aliases = aliases.subList(1,aliases.size());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getNewCommand() {
        return this.newCommand;
    }

    public String getFeatureId() {
        return this.featureId;
    }

    public Boolean getStatus() {
        return this.status;
    }

    public String getOppositeStatusText() {
        if (status) { return "disable"; }
        return "enable";
    }

    public void enableIfAble() {
        if (instance.getConfig().get().node(featureId).node("enabled").getBoolean()) {
            ArrayList<String> commandAliases = new ArrayList<>();
            commandAliases.add(newCommand);
            if (aliases != null) {
                commandAliases.addAll(aliases);
            }
            VelocityUtils.registerCommand(commandAliases,command,instance);
            status = true;
            return;
        }
        status = false;
    }

    public void disableIfEnabled() {
        if (instance.getConfig().get().node(featureId).node("enabled").getBoolean()) {
            instance.getProxyServer().getCommandManager().unregister(newCommand);
            status = false;
        }
    }
}

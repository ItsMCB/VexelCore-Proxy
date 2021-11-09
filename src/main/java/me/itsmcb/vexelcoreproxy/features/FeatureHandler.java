package me.itsmcb.vexelcoreproxy.features;

import me.itsmcb.vexelcoreproxy.VexelCoreProxy;
import me.itsmcb.vexelcoreproxy.utils.VelocityUtils;

import java.util.ArrayList;
import java.util.List;

public class FeatureHandler {

    private VexelCoreProxy instance;
    private ArrayList<VCPFeature> vcpFeatures = new ArrayList<>();

    public FeatureHandler(VexelCoreProxy instance) {
        this.instance = instance;
    }

    public VCPFeature registerFeature(VCPFeature vcpFeature, Boolean enable) {
        if (enable) {
            if (instance.getConfig().get().node(vcpFeature.getFeatureId()).node("enabled").getBoolean()) {
                ArrayList<String> commandAliases = new ArrayList<>();
                commandAliases.add(vcpFeature.getFirstAlias());
                if (vcpFeature.getAdditionAliases() != null) {
                    commandAliases.addAll(vcpFeature.getAdditionAliases());
                }
                VelocityUtils.registerCommand(commandAliases,vcpFeature.getCommand(),instance);
                vcpFeature.setStatus(true);
            } else {
                vcpFeature.setStatus(false);
            }
        }
        vcpFeatures.add(vcpFeature);
        return vcpFeature;
    }

    public void unregisterFeature(String featureId) {
        VCPFeature vcpFeature = vcpFeatures.stream().filter(feature -> feature.getFeatureId().equalsIgnoreCase(featureId)).findAny().orElseThrow();
        vcpFeatures.remove(vcpFeature);
    }
    public void disableAndUnregisterAllFeatures() {
        vcpFeatures.forEach(vcpFeature -> {
            if (instance.getConfig().get().node(vcpFeature.getFeatureId()).node("enabled").getBoolean()) {
                instance.getProxyServer().getCommandManager().unregister(vcpFeature.getFirstAlias());
                if (vcpFeature.getAdditionAliases() != null) {
                    vcpFeature.getAdditionAliases().forEach(alias -> instance.getProxyServer().getCommandManager().unregister(alias));
                }
                vcpFeature.setStatus(false);
            }
        });
        vcpFeatures.clear();
    }

    public ArrayList<VCPFeature> getVcpFeatures() {
        return vcpFeatures;
    }

    public List<String> getAllFeatureIds() {
        List<String> VCPFeatureIds = new ArrayList<>();
        getVcpFeatures().forEach(feature -> VCPFeatureIds.add(feature.getFeatureId()));
        return VCPFeatureIds;
    }
}

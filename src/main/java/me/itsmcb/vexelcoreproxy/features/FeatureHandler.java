package me.itsmcb.vexelcoreproxy.features;

import java.util.ArrayList;
import java.util.List;

public class FeatureHandler {

    private ArrayList<VCPFeature> vcpFeatures = new ArrayList<>();

    public VCPFeature registerFeature(VCPFeature vcpFeature, Boolean enable) {
        if (enable) {
            vcpFeature.enableIfAble();
        }
        vcpFeatures.add(vcpFeature);
        return vcpFeature;
    }

    public void unregisterFeature(String featureId) {
        VCPFeature vcpFeature = vcpFeatures.stream().filter(feature -> feature.getFeatureId().equalsIgnoreCase(featureId)).findAny().orElseThrow();
        vcpFeatures.remove(vcpFeature);
    }
    public void disableAndUnregisterAllFeatures() {
        vcpFeatures.forEach(VCPFeature::disableIfEnabled);
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

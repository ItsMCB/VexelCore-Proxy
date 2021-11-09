package me.itsmcb.vexelcoreproxy.utils;

public class FeatureUtils {

    public static String getOppositeStatus(Boolean featureStatus) {
        if (featureStatus) { return "disable"; }
        return "enable";
    }
}

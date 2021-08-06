package me.itsmcb.vexelcoreproxy.utils;

import com.moandjiezana.toml.Toml;
import me.itsmcb.vexelcoreproxy.VexelCoreProxy;

public class PermissionUtils {
    public static Toml permissions = VexelCoreProxy.getConfig().getTable("permissions");

    public static String get(String key) {
        return permissions.getString(key);
    }
}

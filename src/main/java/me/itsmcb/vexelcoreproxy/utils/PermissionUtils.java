package me.itsmcb.vexelcoreproxy.utils;

import com.moandjiezana.toml.Toml;
import me.itsmcb.vexelcoreproxy.VexelCoreProxy;

public class PermissionUtils {

    public static String get(String key) {
        Toml permissions = VexelCoreProxy.getConfig().getTable("permissions");
        return permissions.getString(key);
    }
}

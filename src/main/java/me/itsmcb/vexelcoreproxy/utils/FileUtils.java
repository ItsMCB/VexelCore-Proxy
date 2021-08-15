package me.itsmcb.vexelcoreproxy.utils;

import com.moandjiezana.toml.Toml;
import me.itsmcb.vexelcoreproxy.VexelCoreProxy;

import java.io.File;
import java.io.InputStream;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileUtils {

    public static Toml getTomlConfig(Path path, String fileName, VexelCoreProxy VCP) {
        File folder = path.toFile();
        File file = new File(folder, fileName);
        if (!file.getParentFile().exists()) {
            VCP.getLogger().info("Generating directory \"" + file.getParentFile().getName() + "\"...");
            file.getParentFile().mkdirs();
        }
        if (!file.exists()) {
            VCP.getLogger().info("Generating file \"" + fileName + "\"...");
            try {
                InputStream input = VCP.getClass().getResourceAsStream("/" + file.getName());
                try {
                    if (input != null) {
                        Files.copy(input, file.toPath(), new CopyOption[0]);
                        input.close();
                    } else {
                        file.createNewFile();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return (new Toml()).read(file);
    }
}

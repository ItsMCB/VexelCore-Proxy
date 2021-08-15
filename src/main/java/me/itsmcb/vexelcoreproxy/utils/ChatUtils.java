package me.itsmcb.vexelcoreproxy.utils;

import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

public class ChatUtils {

    public static TextComponent parseLegacy(String input) {
        return LegacyComponentSerializer.legacyAmpersand().deserialize(input);
    }
}

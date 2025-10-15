package com.maybeizen.EasyRTP.utils;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.entity.Player;

import com.maybeizen.EasyRTP.EasyRTP;

import java.util.HashMap;
import java.util.Map;

public class MessageUtils {
    private static EasyRTP plugin;
    private static final LegacyComponentSerializer LEGACY_SERIALIZER = LegacyComponentSerializer.builder()
            .character('&')
            .hexColors()
            .build();
    private static final MiniMessage MINI_MESSAGE = MiniMessage.miniMessage();
    
    public static void initialize(EasyRTP pluginInstance) {
        plugin = pluginInstance;
    }
    
    private static Component getPrefix() {        
        String prefix = plugin.getConfigManager().getMessage("prefix");
        if (prefix.equals("Message not found: prefix")) {
            prefix = plugin.getConfigManager().getDefaultPrefix();
        }
        return parseText(prefix);
    }

    public static Component parseText(String message) {
        if (message.contains("<") && message.contains(">")) {
            return MINI_MESSAGE.deserialize(message);
        } else {
            return LEGACY_SERIALIZER.deserialize(message);
        }
    }

    public static Component formatMessage(String message) {
        return getPrefix().append(parseText(message));
    }

    public static Component formatMessage(String message, Player player) {
        return getPrefix().append(parseText(message));
    }

    public static Component formatMessage(String message, String... placeholders) {
        if (placeholders.length % 2 != 0) {
            throw new IllegalArgumentException("Placeholders must be in pairs of key-value");
        }

        Map<String, String> replacements = new HashMap<>();
        for (int i = 0; i < placeholders.length; i += 2) {
            replacements.put(placeholders[i], placeholders[i + 1]);
        }

        String formattedMessage = message;
        for (Map.Entry<String, String> entry : replacements.entrySet()) {
            formattedMessage = formattedMessage.replace("%" + entry.getKey() + "%", entry.getValue());
        }

        return getPrefix().append(parseText(formattedMessage));
    }

    public static Component formatMessage(String message, Player player, String... placeholders) {
        if (placeholders.length % 2 != 0) {
            throw new IllegalArgumentException("Placeholders must be in pairs of key-value");
        }

        Map<String, String> replacements = new HashMap<>();
        for (int i = 0; i < placeholders.length; i += 2) {
            replacements.put(placeholders[i], placeholders[i + 1]);
        }

        String formattedMessage = message;
        for (Map.Entry<String, String> entry : replacements.entrySet()) {
            formattedMessage = formattedMessage.replace("%" + entry.getKey() + "%", entry.getValue());
        }

        return getPrefix().append(parseText(formattedMessage));
    }

    public static void sendMessage(Player player, String message) {
        player.sendMessage(formatMessage(message, player));
    }

    public static void sendMessage(Player player, String message, String... placeholders) {
        player.sendMessage(formatMessage(message, player, placeholders));
    }


    public static void playTeleportEffect(Player player) {
        if (plugin != null && plugin.getConfigManager().getSoundsEnabled()) {
            VersionAdapter.playTeleportSound(player);
        }
    }
} 
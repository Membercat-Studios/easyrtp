package com.maybeizen.EasyRTP.utils;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import com.maybeizen.EasyRTP.EasyRTP;

public class ConfigManager {
    private static ConfigManager instance;
    private final EasyRTP plugin;
    private FileConfiguration config;

    private ConfigManager(EasyRTP plugin) {
        this.plugin = plugin;
        loadConfig();
    }

    public static ConfigManager getInstance(EasyRTP plugin) {
        if (instance == null) {
            instance = new ConfigManager(plugin);
        } else {
            instance.loadConfig();
        }
        return instance;
    }

    public void loadConfig() {
        plugin.saveDefaultConfig();
        plugin.reloadConfig();
        config = plugin.getConfig();
    }

    public void reloadConfig() {
        plugin.reloadConfig();
        this.config = plugin.getConfig();
    }

    public String getMessage(String path) {
        return config.getString("messages." + path, "Message not found: " + path);
    }

    public String getMessage(String path, Player player) {
        return getMessage(path);
    }

    public String getMessage(String path, String... placeholders) {
        if (placeholders.length % 2 != 0) {
            throw new IllegalArgumentException("Placeholders must be in pairs of key-value");
        }

        String message = getMessage(path);
        
        for (int i = 0; i < placeholders.length; i += 2) {
            message = message.replace("%" + placeholders[i] + "%", placeholders[i + 1]);
        }
        
        return message;
    }

    public String getMessage(String path, Player player, String... placeholders) {
        return getMessage(path, placeholders);
    }

    public int getCooldown() {
        return config.getInt("settings.cooldown", 30);
    }

    public boolean getSoundsEnabled() {
        return config.getBoolean("settings.enable-sounds", true);
    }

    public int getTeleportDelay() {
        return config.getInt("settings.teleport-delay", 3);
    }

    public int getMaxAttempts() {
        return config.getInt("settings.max-attempts", 100);
    }

    public int getMinDistance() {
        return config.getInt("settings.min-distance", 1000);
    }

    public int getMaxDistance() {
        return config.getInt("settings.max-distance", 10000);
    }

    public int getCenterX() {
        return config.getInt("settings.center-x", 0);
    }

    public int getCenterZ() {
        return config.getInt("settings.center-z", 0);
    }

    public int getMinY() {
        return config.getInt("settings.min-y", 60);
    }

    public int getMaxY() {
        return config.getInt("settings.max-y", 120);
    }

    public boolean getSafeLocationCheck() {
        return config.getBoolean("settings.safe-location-check", true);
    }

    public boolean isWorldBlacklisted(String worldName) {
        return config.getStringList("settings.world-blacklist").contains(worldName);
    }

    public boolean getPreloadChunks() {
        return config.getBoolean("settings.preload-chunks", true);
    }

    public int getPreloadRadius() {
        return config.getInt("settings.preload-radius", 2);
    }

    public int getHungerCost() {
        return config.getInt("settings.hunger-cost", 0);
    }

    public int getXpCost() {
        return config.getInt("settings.xp-cost", 0);
    }

    public boolean getRequireCosts() {
        return config.getBoolean("settings.require-costs", false);
    }

    public boolean getEnableTitles() {
        return config.getBoolean("settings.enable-titles", true);
    }

    public int getTitleFadeIn() {
        return config.getInt("settings.title-fade-in", 10);
    }

    public int getTitleStay() {
        return config.getInt("settings.title-stay", 40);
    }

    public int getTitleFadeOut() {
        return config.getInt("settings.title-fade-out", 10);
    }

    public int getBlindnessDuration() {
        return config.getInt("settings.blindness-duration", 1);
    }

    public String getDefaultPrefix() {
        return "&7[&6EasyRTP&7] &r";
    }

    public FileConfiguration getConfig() {
        return config;
    }
} 
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

    private String getDefaultMessage(String path) {
        switch (path) {
            case "prefix":
                return "&7[&6EasyRTP&7] &r";
            case "player-only":
                return "&cThis command can only be used by players.";
            case "no-permission":
                return "&cYou don't have permission to use this command.";
            case "rtp-usage":
                return "&cUsage: /rtp [player] [world]";
            case "rtpo-usage":
                return "&cUsage: /rtpo <player> [world]";
            case "admin-usage":
                return "&cUsage: /easyrtp reload";
            case "player-not-found":
                return "&cPlayer not found.";
            case "world-not-found":
                return "&cWorld not found.";
            case "cannot-teleport-self":
                return "&cYou cannot teleport yourself with /rtpo!";
            case "cooldown":
                return "&cYou must wait &e%time% &cseconds before using RTP again!";
            case "teleport-starting":
                return "&eStarting random teleport in &6%delay% &eseconds...";
            case "teleport-success":
                return "&aYou have been teleported to a random location!";
            case "teleport-success-other":
                return "&a%player% has been teleported to a random location!";
            case "teleport-failed":
                return "&cFailed to find a safe location! Please try again.";
            case "teleport-cancelled":
                return "&cRandom teleport cancelled!";
            case "teleport-world-disabled":
                return "&cRandom teleport is disabled in this world!";
            case "teleport-other-starting":
                return "&eTeleporting &6%player% &eto a random location...";
            case "teleport-other-success":
                return "&aYou have been teleported to a random location by &6%sender%&a!";
            case "teleport-in-progress":
                return "&cYou are already teleporting! Please wait for the current teleport to complete.";
            case "teleport-location":
                return "&7Location: &e%x%, %y%, %z% &7in &e%world%";
            case "teleport-time":
                return "&7Teleport took &e%time%ms";
            case "insufficient-hunger":
                return "&cYou need at least &6%hunger% &chunger points to use RTP!";
            case "insufficient-xp":
                return "&cYou need at least &6%xp% &cexperience levels to use RTP!";
            case "cost-applied":
                return "&7Cost: &6-%hunger% hunger &7and &6-%xp% XP levels";
            case "title-teleporting":
                return "&6&lTELEPORTING...";
            case "subtitle-teleporting":
                return "&7Finding safe location...";
            case "title-success":
                return "&a&lTELEPORTED!";
            case "subtitle-success":
                return "&7Welcome to your new location!";
            case "title-cancelled":
                return "&c&lTELEPORT CANCELLED!";
            case "subtitle-cancelled":
                return "&7You moved and your teleport was cancelled!";
            case "config-reloading":
                return "&eReloading EasyRTP configuration...";
            case "config-reloaded":
                return "&aConfiguration has been reloaded successfully!";
            default:
                return "&cMessage not found: " + path;
        }
    }

    public String getMessage(String path) {
        return config.getString("messages." + path, getDefaultMessage(path));
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

    public boolean getRequireSkyVisibility() {
        return config.getBoolean("settings.require-sky-visibility", true);
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

    public boolean getEnableCancelTitle() {
        return config.getBoolean("settings.enable-cancel-title", true);
    }

    public boolean getEnableCancelSound() {
        return config.getBoolean("settings.enable-cancel-sound", true);
    }

    public String getCancelSound() {
        return config.getString("settings.cancel-sound", "BLOCK_NOTE_BLOCK_BASS");
    }

    public String getDefaultPrefix() {
        return "&7[&6EasyRTP&7] &r";
    }

    public FileConfiguration getConfig() {
        return config;
    }
} 
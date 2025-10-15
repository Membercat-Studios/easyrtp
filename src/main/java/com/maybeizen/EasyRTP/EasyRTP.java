package com.maybeizen.EasyRTP;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import com.maybeizen.EasyRTP.commands.*;
import com.maybeizen.EasyRTP.managers.CooldownManager;
import com.maybeizen.EasyRTP.utils.ConfigManager;
import com.maybeizen.EasyRTP.utils.DatabaseManager;
import com.maybeizen.EasyRTP.utils.MessageUtils;
import com.maybeizen.EasyRTP.utils.VersionAdapter;

public class EasyRTP extends JavaPlugin {
    private ConfigManager configManager;
    private DatabaseManager databaseManager;
    private CooldownManager cooldownManager;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        
        this.configManager = ConfigManager.getInstance(this);
        
        MessageUtils.initialize(this);
        
        this.cooldownManager = new CooldownManager(configManager.getCooldown());
    
    
        RTPCommand rtpCommand = new RTPCommand(this);
        getCommand("rtp").setExecutor(rtpCommand);
        getCommand("rtp").setTabCompleter(rtpCommand);
        
        RTPOCommand rtpoCommand = new RTPOCommand(this);
        getCommand("rtpo").setExecutor(rtpoCommand);
        getCommand("rtpo").setTabCompleter(rtpoCommand);
        
        EasyRTPCommand adminCommand = new EasyRTPCommand(this);
        getCommand("easyrtp").setExecutor(adminCommand);
        getCommand("easyrtp").setTabCompleter(adminCommand);
        
        Bukkit.getLogger().info("EasyRTP has been enabled!");
        Bukkit.getLogger().info("Running on server version: " + VersionAdapter.getServerVersion());
    }   

    @Override
    public void onDisable() {
        if (databaseManager != null) {
            databaseManager.closeConnection();
        }
        Bukkit.getLogger().info("EasyRTP has been disabled!");
    }

    public void reloadConfiguration() {
        reloadConfig();
        
        configManager.reloadConfig();
        
        this.cooldownManager = new CooldownManager(configManager.getCooldown());

        
        MessageUtils.initialize(this);
        
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }

    public DatabaseManager getDatabaseManager() {
        return databaseManager;
    }

    public CooldownManager getCooldownManager() {
        return cooldownManager;
    }


}


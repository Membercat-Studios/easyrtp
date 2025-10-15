package com.maybeizen.EasyRTP.utils;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;

import com.maybeizen.EasyRTP.EasyRTP;


public class DatabaseManager {
    private final EasyRTP plugin;
    private Connection connection;
    private final String dbFile;
    
    // this class isnt used but is kept for potential future features that might need database storage
    
    public DatabaseManager(EasyRTP plugin) {
        this.plugin = plugin;
        this.dbFile = new File(plugin.getDataFolder(), "easyrtp.db").getAbsolutePath();

    }
    
    private void initialize() {
        try {
            if (!plugin.getDataFolder().exists()) {
                plugin.getDataFolder().mkdirs();
            }
            
            Class.forName("org.sqlite.JDBC");
            
            connection = DriverManager.getConnection("jdbc:sqlite:" + dbFile);
            
            
            plugin.getLogger().info("Database connection established successfully");
        } catch (ClassNotFoundException e) {
            plugin.getLogger().log(Level.SEVERE, "SQLite JDBC driver not found", e);
        } catch (SQLException e) {
            plugin.getLogger().log(Level.SEVERE, "Could not connect to SQLite database", e);
        }
    }
    
    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                plugin.getLogger().info("Database connection closed");
            }
        } catch (SQLException e) {
            plugin.getLogger().log(Level.WARNING, "Error closing database connection", e);
        }
    }
    
    
    public boolean isConnectionValid() {
        try {
            return connection != null && !connection.isClosed();
        } catch (SQLException e) {
            return false;
        }
    }
    
    public void reconnectIfNeeded() {
        try {
            if (connection == null || connection.isClosed()) {
                initialize();
            }
        } catch (SQLException e) {
            plugin.getLogger().log(Level.WARNING, "Error checking database connection", e);
            initialize();
        }
    }
} 
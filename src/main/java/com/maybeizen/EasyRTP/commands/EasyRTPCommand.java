package com.maybeizen.EasyRTP.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import com.maybeizen.EasyRTP.EasyRTP;
import com.maybeizen.EasyRTP.utils.MessageUtils;

import java.util.ArrayList;
import java.util.List;

public class EasyRTPCommand implements CommandExecutor, TabCompleter {
    private final EasyRTP plugin;

    public EasyRTPCommand(EasyRTP plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("easyrtp.admin")) {
            sender.sendMessage(MessageUtils.formatMessage(plugin.getConfigManager().getMessage("no-permission")));
            return true;
        }

        if (args.length != 1 || !args[0].equalsIgnoreCase("reload")) {
            sender.sendMessage(MessageUtils.formatMessage(plugin.getConfigManager().getMessage("admin-usage")));
            return true;
        }

        sender.sendMessage(MessageUtils.formatMessage(plugin.getConfigManager().getMessage("config-reloading")));
        
        try {
            plugin.reloadConfiguration();
            
            sender.sendMessage(MessageUtils.formatMessage(plugin.getConfigManager().getMessage("config-reloaded")));
            
        } catch (Exception e) {
            sender.sendMessage(MessageUtils.formatMessage("&cError reloading configuration: " + e.getMessage()));
            e.printStackTrace();
        }
        
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();
        
        if (args.length == 1 && sender.hasPermission("easyrtp.admin")) {
            if ("reload".startsWith(args[0].toLowerCase())) {
                completions.add("reload");
            }
        }
        
        return completions;
    }
} 
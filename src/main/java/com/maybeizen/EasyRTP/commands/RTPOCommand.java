package com.maybeizen.EasyRTP.commands;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import com.maybeizen.EasyRTP.EasyRTP;
import com.maybeizen.EasyRTP.managers.RTPManager;
import com.maybeizen.EasyRTP.utils.MessageUtils;

import java.util.ArrayList;
import java.util.List;

public class RTPOCommand implements CommandExecutor, TabCompleter {
    private final EasyRTP plugin;
    private final RTPManager rtpManager;

    public RTPOCommand(EasyRTP plugin) {
        this.plugin = plugin;
        this.rtpManager = new RTPManager(plugin);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("easyrtp.rtpo")) {
            sender.sendMessage(MessageUtils.formatMessage(plugin.getConfigManager().getMessage("no-permission")));
            return true;
        }

        if (args.length < 1) {
            sender.sendMessage(MessageUtils.formatMessage(plugin.getConfigManager().getMessage("rtpo-usage")));
            return true;
        }

        Player targetPlayer = Bukkit.getPlayer(args[0]);
        if (targetPlayer == null) {
            sender.sendMessage(MessageUtils.formatMessage(plugin.getConfigManager().getMessage("player-not-found")));
            return true;
        }

        if (sender instanceof Player && targetPlayer.equals(sender)) {
            sender.sendMessage(MessageUtils.formatMessage(plugin.getConfigManager().getMessage("cannot-teleport-self")));
            return true;
        }

        World targetWorld = targetPlayer.getWorld();
        if (args.length > 1) {
            World foundWorld = Bukkit.getWorld(args[1]);
            if (foundWorld != null) {
                targetWorld = foundWorld;
            } else {
                sender.sendMessage(MessageUtils.formatMessage(plugin.getConfigManager().getMessage("world-not-found")));
                return true;
            }
        }

        if (!targetPlayer.hasPermission("easyrtp.cooldown.bypass") && 
            plugin.getCooldownManager().hasCooldown(targetPlayer.getUniqueId())) {
            long timeLeft = plugin.getCooldownManager().getRemainingTime(targetPlayer.getUniqueId()) / 1000;
            sender.sendMessage(MessageUtils.formatMessage(
                plugin.getConfigManager().getMessage("cooldown", "time", String.valueOf(timeLeft))
            ));
            return true;
        }

        if (!sender.hasPermission("easyrtp.world.bypass") && 
            plugin.getConfigManager().isWorldBlacklisted(targetWorld.getName())) {
            sender.sendMessage(MessageUtils.formatMessage(plugin.getConfigManager().getMessage("teleport-world-disabled")));
            return true;
        }

        if (!rtpManager.canAffordTeleport(targetPlayer)) {
            return true; 
        }

        rtpManager.startRandomTeleportOther(sender, targetPlayer, targetWorld);

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();
        
        if (args.length == 1) {
            String partial = args[0].toLowerCase();
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (player.getName().toLowerCase().startsWith(partial)) {
                    completions.add(player.getName());
                }
            }
        } else if (args.length == 2) {
            String partial = args[1].toLowerCase();
            for (World world : Bukkit.getWorlds()) {
                if (world.getName().toLowerCase().startsWith(partial)) {
                    completions.add(world.getName());
                }
            }
        }
        
        return completions;
    }
}

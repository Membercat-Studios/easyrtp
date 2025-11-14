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

public class RTPCommand implements CommandExecutor, TabCompleter {
    private final EasyRTP plugin;
    private final RTPManager rtpManager;

    public RTPCommand(EasyRTP plugin) {
        this.plugin = plugin;
        this.rtpManager = plugin.getRTPManager();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(MessageUtils.formatMessage(plugin.getConfigManager().getMessage("player-only")));
            return true;
        }

        Player player = (Player) sender;

        if (!player.hasPermission("easyrtp.rtp")) {
            player.sendMessage(MessageUtils.formatMessage(plugin.getConfigManager().getMessage("no-permission")));
            return true;
        }

        Player targetPlayer = player;
        World targetWorld = player.getWorld();

        if (args.length > 0) {
            Player foundPlayer = Bukkit.getPlayer(args[0]);
            if (foundPlayer != null) {
                targetPlayer = foundPlayer;
                if (args.length > 1) {
                    World foundWorld = Bukkit.getWorld(args[1]);
                    if (foundWorld != null) {
                        targetWorld = foundWorld;
                    } else {
                        player.sendMessage(MessageUtils.formatMessage(plugin.getConfigManager().getMessage("world-not-found")));
                        return true;
                    }
                }
            } else {
                World foundWorld = Bukkit.getWorld(args[0]);
                if (foundWorld != null) {
                    targetWorld = foundWorld;
                } else {
                    player.sendMessage(MessageUtils.formatMessage(plugin.getConfigManager().getMessage("player-not-found")));
                    return true;
                }
            }
        }

        if (!targetPlayer.hasPermission("easyrtp.cooldown.bypass") && 
            plugin.getCooldownManager().hasCooldown(targetPlayer.getUniqueId())) {
            long timeLeft = plugin.getCooldownManager().getRemainingTime(targetPlayer.getUniqueId()) / 1000;
            player.sendMessage(MessageUtils.formatMessage(
                plugin.getConfigManager().getMessage("cooldown", "time", String.valueOf(timeLeft))
            ));
            return true;
        }

        if (!player.hasPermission("easyrtp.world.bypass") && 
            plugin.getConfigManager().isWorldBlacklisted(targetWorld.getName())) {
            player.sendMessage(MessageUtils.formatMessage(plugin.getConfigManager().getMessage("teleport-world-disabled")));
            return true;
        }

        if (!rtpManager.canAffordTeleport(targetPlayer)) {
            return true;
        }

        if (targetPlayer.equals(player)) {
            rtpManager.startRandomTeleport(player, targetWorld);
        } else {
            rtpManager.startRandomTeleportOther(player, targetPlayer, targetWorld);
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        return new ArrayList<>();
    }
}

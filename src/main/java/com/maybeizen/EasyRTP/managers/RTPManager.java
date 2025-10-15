package com.maybeizen.EasyRTP.managers;

import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.maybeizen.EasyRTP.EasyRTP;
import com.maybeizen.EasyRTP.utils.MessageUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class RTPManager {
    private final EasyRTP plugin;
    private final TeleportExecutor teleportExecutor;
    private final Map<UUID, BukkitRunnable> teleportTasks;

    public RTPManager(EasyRTP plugin) {
        this.plugin = plugin;
        this.teleportExecutor = new TeleportExecutor(plugin);
        this.teleportTasks = new HashMap<>();
    }

    public boolean canAffordTeleport(Player player) {
        return teleportExecutor.getCostManager().canAffordTeleport(player);
    }

    public void startRandomTeleport(Player player) {
        startRandomTeleport(player, player.getWorld());
    }

    public void startRandomTeleport(Player player, World world) {
        UUID playerId = player.getUniqueId();
        
        if (teleportTasks.containsKey(playerId)) {
            player.sendMessage(MessageUtils.formatMessage(plugin.getConfigManager().getMessage("teleport-in-progress")));
            return;
        }

        int delay = plugin.getConfigManager().getTeleportDelay();
        
        teleportExecutor.getTitleManager().showTeleportingTitle(player);
        
        if (delay <= 0) {
            teleportExecutor.executeTeleport(player, world);
        } else {
            player.sendMessage(MessageUtils.formatMessage(
                plugin.getConfigManager().getMessage("teleport-starting", "delay", String.valueOf(delay))
            ));

            BukkitRunnable task = new BukkitRunnable() {
                @Override
                public void run() {
                    if (player.isOnline()) {
                        teleportExecutor.executeTeleport(player, world);
                    }
                    teleportTasks.remove(playerId);
                }
            };

            task.runTaskLater(plugin, delay * 20L);
            teleportTasks.put(playerId, task);
        }
    }

    public void startRandomTeleportOther(CommandSender sender, Player targetPlayer, World world) {
        UUID playerId = targetPlayer.getUniqueId();
        
        if (teleportTasks.containsKey(playerId)) {
            sender.sendMessage(MessageUtils.formatMessage(plugin.getConfigManager().getMessage("teleport-in-progress")));
            return;
        }

        int delay = plugin.getConfigManager().getTeleportDelay();
        
        sender.sendMessage(MessageUtils.formatMessage(
            plugin.getConfigManager().getMessage("teleport-other-starting", "player", targetPlayer.getName())
        ));

        if (delay <= 0) {
            teleportExecutor.executeTeleportOther(sender, targetPlayer, world);
        } else {
            BukkitRunnable task = new BukkitRunnable() {
                @Override
                public void run() {
                    if (targetPlayer.isOnline()) {
                        teleportExecutor.executeTeleportOther(sender, targetPlayer, world);
                    }
                    teleportTasks.remove(playerId);
                }
            };

            task.runTaskLater(plugin, delay * 20L);
            teleportTasks.put(playerId, task);
        }
    }

    public void cancelTeleportTask(UUID playerId) {
        BukkitRunnable task = teleportTasks.get(playerId);
        if (task != null) {
            task.cancel();
            teleportTasks.remove(playerId);
        }
    }

    public void cancelAllTeleportTasks() {
        for (BukkitRunnable task : teleportTasks.values()) {
            task.cancel();
        }
        teleportTasks.clear();
    }        // Check if target player is already teleporting
}
package com.maybeizen.EasyRTP.managers;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.maybeizen.EasyRTP.EasyRTP;
import com.maybeizen.EasyRTP.utils.MessageUtils;
import com.maybeizen.EasyRTP.utils.VersionAdapter;

public class TeleportExecutor {
    private final EasyRTP plugin;
    private final LocationFinder locationFinder;
    private final CostManager costManager;
    private final TitleManager titleManager;

    public TeleportExecutor(EasyRTP plugin) {
        this.plugin = plugin;
        this.locationFinder = new LocationFinder(plugin);
        this.costManager = new CostManager(plugin);
        this.titleManager = new TitleManager(plugin);
    }

    public void executeTeleport(Player player, World world) {
        long startTime = System.currentTimeMillis();
        
        Location location = locationFinder.findRandomLocation(world);
        
        if (location == null) {
            player.sendMessage(MessageUtils.formatMessage(plugin.getConfigManager().getMessage("teleport-failed")));
            return;
        }

        if (plugin.getConfigManager().getPreloadChunks()) {
            locationFinder.preloadChunks(location);
        }

        costManager.applyCosts(player);

        if (!player.hasPermission("easyrtp.cooldown.bypass")) {
            plugin.getCooldownManager().setCooldown(player.getUniqueId());
        }

        player.teleport(location);
        
        applyBlindnessEffect(player);
        
        long endTime = System.currentTimeMillis();
        long teleportTime = endTime - startTime;
        
        titleManager.showSuccessTitle(player);
        
        sendTeleportMessages(player, location, teleportTime);
        
        if (plugin.getConfigManager().getSoundsEnabled()) {
            VersionAdapter.playTeleportSound(player);
        }
    }

    public void executeTeleportOther(CommandSender sender, Player targetPlayer, World world) {
        long startTime = System.currentTimeMillis();
        
        Location location = locationFinder.findRandomLocation(world);
        
        if (location == null) {
            sender.sendMessage(MessageUtils.formatMessage(plugin.getConfigManager().getMessage("teleport-failed")));
            return;
        }

        if (plugin.getConfigManager().getPreloadChunks()) {
            locationFinder.preloadChunks(location);
        }

        costManager.applyCosts(targetPlayer);

        if (!targetPlayer.hasPermission("easyrtp.cooldown.bypass")) {
            plugin.getCooldownManager().setCooldown(targetPlayer.getUniqueId());
        }

        targetPlayer.teleport(location);
        
        applyBlindnessEffect(targetPlayer);
        
        long endTime = System.currentTimeMillis();
        long teleportTime = endTime - startTime;
        
        titleManager.showSuccessTitle(targetPlayer);
        
        sendTeleportOtherMessages(sender, targetPlayer, location, teleportTime);
        
        if (plugin.getConfigManager().getSoundsEnabled()) {
            VersionAdapter.playTeleportSound(targetPlayer);
        }
    }

    private void sendTeleportMessages(Player player, Location location, long teleportTime) {
        player.sendMessage(MessageUtils.formatMessage(plugin.getConfigManager().getMessage("teleport-success")));
        player.sendMessage(MessageUtils.formatMessage(
            plugin.getConfigManager().getMessage("teleport-location",
                "x", String.valueOf(location.getBlockX()),
                "y", String.valueOf(location.getBlockY()),
                "z", String.valueOf(location.getBlockZ()),
                "world", location.getWorld().getName())
        ));
        player.sendMessage(MessageUtils.formatMessage(
            plugin.getConfigManager().getMessage("teleport-time", "time", String.valueOf(teleportTime))
        ));
    }

    private void sendTeleportOtherMessages(CommandSender sender, Player targetPlayer, Location location, long teleportTime) {
        sender.sendMessage(MessageUtils.formatMessage(
            plugin.getConfigManager().getMessage("teleport-success-other", "player", targetPlayer.getName())
        ));
        sender.sendMessage(MessageUtils.formatMessage(
            plugin.getConfigManager().getMessage("teleport-location",
                "x", String.valueOf(location.getBlockX()),
                "y", String.valueOf(location.getBlockY()),
                "z", String.valueOf(location.getBlockZ()),
                "world", location.getWorld().getName())
        ));
        sender.sendMessage(MessageUtils.formatMessage(
            plugin.getConfigManager().getMessage("teleport-time", "time", String.valueOf(teleportTime))
        ));
        
        targetPlayer.sendMessage(MessageUtils.formatMessage(
            plugin.getConfigManager().getMessage("teleport-other-success", "sender", sender.getName())
        ));
        targetPlayer.sendMessage(MessageUtils.formatMessage(
            plugin.getConfigManager().getMessage("teleport-location",
                "x", String.valueOf(location.getBlockX()),
                "y", String.valueOf(location.getBlockY()),
                "z", String.valueOf(location.getBlockZ()),
                "world", location.getWorld().getName())
        ));
        targetPlayer.sendMessage(MessageUtils.formatMessage(
            plugin.getConfigManager().getMessage("teleport-time", "time", String.valueOf(teleportTime))
        ));
    }

    public CostManager getCostManager() {
        return costManager;
    }

    public TitleManager getTitleManager() {
        return titleManager;
    }

    private void applyBlindnessEffect(Player player) {
        int blindnessDuration = plugin.getConfigManager().getBlindnessDuration();
        
        if (blindnessDuration > 0) {
            PotionEffect blindnessEffect = new PotionEffect(
                PotionEffectType.BLINDNESS,
                blindnessDuration * 20,
                0,  
                false, 
                false 
            );
            
            player.addPotionEffect(blindnessEffect);
        }
    }
}

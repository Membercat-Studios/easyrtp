package com.maybeizen.EasyRTP.listeners;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import com.maybeizen.EasyRTP.EasyRTP;
import com.maybeizen.EasyRTP.managers.TitleManager;
import com.maybeizen.EasyRTP.utils.MessageUtils;
import com.maybeizen.EasyRTP.utils.VersionAdapter;

public class PlayerMoveListener implements Listener {
    private final EasyRTP plugin;
    private final TitleManager titleManager;

    public PlayerMoveListener(EasyRTP plugin) {
        this.plugin = plugin;
        this.titleManager = new TitleManager(plugin);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        
        if (!plugin.getRTPManager().hasActiveTeleport(player.getUniqueId())) {
            return;
        }

        Location startLocation = plugin.getRTPManager().getTeleportStartLocation(player.getUniqueId());
        if (startLocation == null) {
            return;
        }

        Location to = event.getTo();
        
        if (to == null) {
            return;
        }

        double horizontalDistance = Math.sqrt(
            Math.pow(to.getX() - startLocation.getX(), 2) + 
            Math.pow(to.getZ() - startLocation.getZ(), 2)
        );

        if (horizontalDistance > 0.5) {
            plugin.getRTPManager().cancelTeleportTask(player.getUniqueId());
            
            player.sendMessage(MessageUtils.formatMessage(
                plugin.getConfigManager().getMessage("teleport-cancelled")
            ));
            
            titleManager.showCancelTitle(player);
            
            if (plugin.getConfigManager().getEnableCancelSound()) {
                VersionAdapter.playCancelSound(player, plugin.getConfigManager().getCancelSound());
            }
        }
    }
}


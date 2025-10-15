package com.maybeizen.EasyRTP.managers;

import org.bukkit.entity.Player;

import com.maybeizen.EasyRTP.EasyRTP;
import com.maybeizen.EasyRTP.utils.MessageUtils;

public class CostManager {
    private final EasyRTP plugin;

    public CostManager(EasyRTP plugin) {
        this.plugin = plugin;
    }

    public boolean canAffordTeleport(Player player) {
        int hungerCost = plugin.getConfigManager().getHungerCost();
        int xpCost = plugin.getConfigManager().getXpCost();
        boolean requireCosts = plugin.getConfigManager().getRequireCosts();

        if (!requireCosts && player.hasPermission("easyrtp.cooldown.bypass")) {
            return true;
        }

        if (hungerCost > 0 && player.getFoodLevel() < hungerCost) {
            player.sendMessage(MessageUtils.formatMessage(
                plugin.getConfigManager().getMessage("insufficient-hunger", "hunger", String.valueOf(hungerCost))
            ));
            return false;
        }

        if (xpCost > 0 && player.getLevel() < xpCost) {
            player.sendMessage(MessageUtils.formatMessage(
                plugin.getConfigManager().getMessage("insufficient-xp", "xp", String.valueOf(xpCost))
            ));
            return false;
        }

        return true;
    }

    public void applyCosts(Player player) {
        int hungerCost = plugin.getConfigManager().getHungerCost();
        int xpCost = plugin.getConfigManager().getXpCost();
        boolean requireCosts = plugin.getConfigManager().getRequireCosts();

        if (!requireCosts && player.hasPermission("easyrtp.cooldown.bypass")) {
            return;
        }

        if (hungerCost > 0) {
            player.setFoodLevel(Math.max(0, player.getFoodLevel() - hungerCost));
        }

        if (xpCost > 0) {
            player.setLevel(Math.max(0, player.getLevel() - xpCost));
        }

        if (hungerCost > 0 || xpCost > 0) {
            player.sendMessage(MessageUtils.formatMessage(
                plugin.getConfigManager().getMessage("cost-applied", 
                    "hunger", String.valueOf(hungerCost),
                    "xp", String.valueOf(xpCost))
            ));
        }
    }
}

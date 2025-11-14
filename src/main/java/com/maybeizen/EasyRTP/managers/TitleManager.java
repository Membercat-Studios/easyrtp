package com.maybeizen.EasyRTP.managers;

import org.bukkit.entity.Player;
import net.kyori.adventure.title.Title;

import com.maybeizen.EasyRTP.EasyRTP;
import com.maybeizen.EasyRTP.utils.MessageUtils;

public class TitleManager {
    private final EasyRTP plugin;

    public TitleManager(EasyRTP plugin) {
        this.plugin = plugin;
    }

    public void showTeleportingTitle(Player player) {
        if (!plugin.getConfigManager().getEnableTitles()) {
            return;
        }

        String titleText = plugin.getConfigManager().getMessage("title-teleporting");
        String subtitleText = plugin.getConfigManager().getMessage("subtitle-teleporting");
        
        Title title = Title.title(
            MessageUtils.parseText(titleText),
            MessageUtils.parseText(subtitleText),
            Title.Times.times(
                java.time.Duration.ofMillis(plugin.getConfigManager().getTitleFadeIn() * 50L),
                java.time.Duration.ofMillis(plugin.getConfigManager().getTitleStay() * 50L),
                java.time.Duration.ofMillis(plugin.getConfigManager().getTitleFadeOut() * 50L)
            )
        );
        
        player.showTitle(title);
    }

    public void showSuccessTitle(Player player) {
        if (!plugin.getConfigManager().getEnableTitles()) {
            return;
        }

        String titleText = plugin.getConfigManager().getMessage("title-success");
        String subtitleText = plugin.getConfigManager().getMessage("subtitle-success");
        
        Title title = Title.title(
            MessageUtils.parseText(titleText),
            MessageUtils.parseText(subtitleText),
            Title.Times.times(
                java.time.Duration.ofMillis(plugin.getConfigManager().getTitleFadeIn() * 50L),
                java.time.Duration.ofMillis(plugin.getConfigManager().getTitleStay() * 50L),
                java.time.Duration.ofMillis(plugin.getConfigManager().getTitleFadeOut() * 50L)
            )
        );
        
        player.showTitle(title);
    }

    public void showCancelTitle(Player player) {
        if (!plugin.getConfigManager().getEnableCancelTitle()) {
            return;
        }

        String titleText = plugin.getConfigManager().getMessage("title-cancelled");
        String subtitleText = plugin.getConfigManager().getMessage("subtitle-cancelled");
        
        Title title = Title.title(
            MessageUtils.parseText(titleText),
            MessageUtils.parseText(subtitleText),
            Title.Times.times(
                java.time.Duration.ofMillis(plugin.getConfigManager().getTitleFadeIn() * 50L),
                java.time.Duration.ofMillis(plugin.getConfigManager().getTitleStay() * 50L),
                java.time.Duration.ofMillis(plugin.getConfigManager().getTitleFadeOut() * 50L)
            )
        );
        
        player.showTitle(title);
    }
}
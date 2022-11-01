package de.cooperr.qserverplugin.listener;

import de.cooperr.cppluginutil.PaperListener;
import de.cooperr.cppluginutil.PaperPlugin;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class PlayerJoinListener extends PaperListener<PlayerJoinEvent> {
    
    public PlayerJoinListener(@NotNull PaperPlugin plugin) {
        super(plugin);
    }
    
    @EventHandler
    @Override
    public void onEvent(PlayerJoinEvent event) {
        
        var player = event.getPlayer();
        
        event.joinMessage(
            player.name().color(NamedTextColor.GREEN).style(Style.style(TextDecoration.BOLD))
                .append(Component.text(" hat den Server betreten!", NamedTextColor.GREEN))
        );
        
        var serverViewDistance = plugin.getServer().getViewDistance();
        var clientViewDistance = player.getClientViewDistance();
        
        if (clientViewDistance < serverViewDistance) {
            player.sendMessage(
                Component.text("Die View Distance ist auf ", NamedTextColor.BLUE)
                    .append(Component.text(serverViewDistance, NamedTextColor.GREEN, TextDecoration.BOLD))
                    .append(Component.text(" begrenzt! Deine: ", NamedTextColor.BLUE))
                    .append(Component.text(clientViewDistance, NamedTextColor.GREEN, TextDecoration.BOLD)));
        }
        
        var vanishedPlayers = plugin.getCustomConfig().getStringList("vanished-players");
        
        if (vanishedPlayers.contains(player.getUniqueId().toString())) {
            plugin.getServer().getOnlinePlayers().stream()
                .filter(onlinePlayer -> !onlinePlayer.isOp())
                .forEach(onlinePlayer -> onlinePlayer.hidePlayer(plugin, player));
            
            player.setSleepingIgnored(true);
        }
        
        if (player.isOp()) {
            return;
        }
        
        for (var vanishedPlayerUuid : vanishedPlayers) {
            var target = plugin.getServer().getPlayer(UUID.fromString(vanishedPlayerUuid));
            if (target != null) {
                player.hidePlayer(plugin, target);
            }
        }
    }
}

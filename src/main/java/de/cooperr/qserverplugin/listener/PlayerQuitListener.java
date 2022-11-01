package de.cooperr.qserverplugin.listener;

import de.cooperr.cppluginutil.PaperListener;
import de.cooperr.cppluginutil.PaperPlugin;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.UUID;

public class PlayerQuitListener extends PaperListener<PlayerQuitEvent> {
    
    public PlayerQuitListener(PaperPlugin plugin) {
        super(plugin);
    }
    
    @EventHandler
    @Override
    public void onEvent(PlayerQuitEvent event) {
        
        var player = event.getPlayer();
        
        event.quitMessage(
            player.name().color(NamedTextColor.RED).style(Style.style(TextDecoration.BOLD))
                .append(Component.text(" hat den Server verlassen!", NamedTextColor.RED))
        );
        
        var vanishedPlayers = plugin.getCustomConfig().getStringList("vanished-players");
        for (var vanishedPlayerUuid : vanishedPlayers) {
            var vanishedPlayer = plugin.getServer().getPlayer(UUID.fromString(vanishedPlayerUuid));
            if (vanishedPlayer != null) {
                player.showPlayer(plugin, vanishedPlayer);
            }
        }
    }
}

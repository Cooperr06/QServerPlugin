package de.cooperr.qserverplugin.listener;

import de.cooperr.cppluginutil.PaperListener;
import de.cooperr.cppluginutil.PaperPlugin;
import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.event.EventHandler;
import org.jetbrains.annotations.NotNull;

public class AsyncChatListener extends PaperListener<AsyncChatEvent> {
    
    public AsyncChatListener(@NotNull PaperPlugin plugin) {
        super(plugin);
    }
    
    @EventHandler
    @Override
    public void onEvent(AsyncChatEvent event) {
        
        var player = event.getPlayer();
        
        event.renderer((source, sourceDisplayName, message, viewer) -> {
            
            var hoverEvent = HoverEvent.hoverEvent(HoverEvent.Action.SHOW_ENTITY,
                HoverEvent.ShowEntity.of(Key.key("minecraft:player"), player.getUniqueId(), player.displayName()));
            
            return Component.text().append(sourceDisplayName.color(NamedTextColor.GOLD)
                    .clickEvent(ClickEvent.clickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/msg %s ".formatted(player.getName())))
                    .hoverEvent(hoverEvent))
                .append(Component.text(" » ", NamedTextColor.DARK_GRAY))
                .append(Component.text("").color(NamedTextColor.WHITE))
                .append(LegacyComponentSerializer.legacy('&').deserialize(
                    PlainTextComponentSerializer.plainText().serialize(message)).colorIfAbsent(NamedTextColor.WHITE))
                .build();
        });
    }
}

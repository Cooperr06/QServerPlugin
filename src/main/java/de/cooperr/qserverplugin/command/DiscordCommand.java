package de.cooperr.qserverplugin.command;

import de.cooperr.cppluginutil.PaperCommand;
import de.cooperr.cppluginutil.PaperPlugin;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class DiscordCommand extends PaperCommand {
    
    public DiscordCommand(@NotNull PaperPlugin plugin) {
        super(plugin);
    }
    
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        
        if (args.length != 0) {
            sendCommandUsage(sender);
            return true;
        }
        
        var discordServerLink = plugin.getCustomConfig().getString("discord-server");
        assert discordServerLink != null;
        
        sender.sendMessage(Component.text("Tritt dem offiziellen ", NamedTextColor.GOLD)
                .append(Component.text("Discord Server", NamedTextColor.GOLD, TextDecoration.UNDERLINED, TextDecoration.BOLD)
                    .clickEvent(ClickEvent.clickEvent(ClickEvent.Action.OPEN_URL, discordServerLink))
                    .hoverEvent(HoverEvent.hoverEvent(HoverEvent.Action.SHOW_TEXT, Component.text("Zum Beitreten klicken!"))))
                .append(Component.text(" bei!", NamedTextColor.GOLD)));
        return true;
    }
    
    @Override
    public @NotNull String getCommandName() {
        return "discord";
    }
    
    @Override
    public @NotNull String getCommandUsage() {
        return "/discord";
    }
    
    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        return null;
    }
}

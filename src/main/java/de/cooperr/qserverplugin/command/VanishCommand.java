package de.cooperr.qserverplugin.command;

import de.cooperr.cppluginutil.PaperCommand;
import de.cooperr.cppluginutil.PaperPlugin;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class VanishCommand extends PaperCommand {
    
    public VanishCommand(@NotNull PaperPlugin plugin) {
        super(plugin);
    }
    
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
    
        if (!(sender instanceof Player)) {
            sendWrongSenderMessage(sender);
            return true;
        }
        
        if (args.length != 0) {
            sendCommandUsage(sender);
            return true;
        }
        
        var player = (Player) sender;
        var vanishedPlayers = plugin.getCustomConfig().getStringList("vanished-players");
        
        if (vanishedPlayers.contains(player.getUniqueId().toString())) {
            vanishedPlayers.remove(player.getUniqueId().toString());
            plugin.getServer().getOnlinePlayers().stream()
                .filter(onlinePlayer -> !onlinePlayer.isOp())
                .forEach(onlinePlayer -> onlinePlayer.showPlayer(plugin, player));
    
            player.setSleepingIgnored(false);
        } else {
            vanishedPlayers.add(player.getUniqueId().toString());
            plugin.getServer().getOnlinePlayers().stream()
                .filter(onlinePlayer -> !onlinePlayer.isOp())
                .forEach(onlinePlayer -> onlinePlayer.hidePlayer(plugin, player));
            
            player.setSleepingIgnored(true);
        }
        
        player.sendMessage(Component.text("Du bist jetzt ", NamedTextColor.GOLD)
            .append(Component.text(vanishedPlayers.contains(player.getUniqueId().toString()) ? "" : "nicht mehr ", NamedTextColor.GOLD, TextDecoration.BOLD))
            .append(Component.text("unsichtbar!", NamedTextColor.GOLD)));
        plugin.getCustomConfig().setAndSave("vanished-players", vanishedPlayers);
        return true;
    }
    
    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        return null;
    }
    
    @Override
    public @NotNull String getCommandName() {
        return "vanish";
    }
    
    @Override
    public @NotNull String getCommandUsage() {
        return "/vanish";
    }
}

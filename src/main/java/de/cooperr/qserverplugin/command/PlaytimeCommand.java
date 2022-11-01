package de.cooperr.qserverplugin.command;

import de.cooperr.cppluginutil.PaperCommand;
import de.cooperr.cppluginutil.PaperPlugin;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.OfflinePlayer;
import org.bukkit.Statistic;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class PlaytimeCommand extends PaperCommand {
    
    public PlaytimeCommand(@NotNull PaperPlugin plugin) {
        super(plugin);
    }
    
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        
        if (!(sender instanceof Player)) {
            sendWrongSenderMessage(sender);
            return true;
        }
        
        var player = (Player) sender;
        
        switch (args.length) {
            
            case 0 -> {
                player.sendMessage(Component.text("Deine Spielzeit beträgt ", NamedTextColor.GOLD)
                    .append(Component.text(formatTime(player.getStatistic(Statistic.PLAY_ONE_MINUTE)), NamedTextColor.GOLD, TextDecoration.BOLD))
                    .append(Component.text("!", NamedTextColor.GOLD)));
                return true;
            }
            
            case 1 -> {
                
                var target = plugin.getServer().getOfflinePlayer(args[0]);
                if (target.getLastSeen() == 0) {
                    player.sendMessage(Component.text("Dieser Spieler existiert nicht oder hat noch nicht gespielt!", NamedTextColor.DARK_RED));
                    return true;
                }
                
                assert target.getName() != null;
                player.sendMessage(Component.text(target.getName(), NamedTextColor.AQUA)
                    .append(Component.text("s Spielzeit beträgt ", NamedTextColor.GOLD))
                    .append(Component.text(formatTime(target.getStatistic(Statistic.PLAY_ONE_MINUTE)), NamedTextColor.GOLD, TextDecoration.BOLD))
                    .append(Component.text("!", NamedTextColor.GOLD)));
                return true;
            }
            
            default -> {
                sendCommandUsage(player);
                return true;
            }
        }
    }
    
    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        
        var tabCompletion = new ArrayList<String>();
        
        if (args.length == 0) {
            tabCompletion.addAll(Arrays.stream(plugin.getServer().getOfflinePlayers())
                .map(OfflinePlayer::getName).collect(Collectors.toList()));
            return tabCompletion;
        } else if (args.length == 1) {
            
            for (var offlinePlayer : plugin.getServer().getOfflinePlayers()) {
                if (args[0].equals(offlinePlayer.getName())) {
                    return null;
                }
            }
            
            tabCompletion.addAll(Arrays.stream(plugin.getServer().getOfflinePlayers())
                .map(OfflinePlayer::getName).collect(Collectors.toList()));
            tabCompletion.removeIf(s -> !s.startsWith(args[0]));
            
            return tabCompletion;
        }
        return null;
    }
    
    public String formatTime(long ticks) {
        
        var seconds = ticks / 20;
        var minutes = 0L;
        var hours = 0L;
        var days = 0L;
        
        while (seconds >= 60) {
            minutes++;
            seconds -= 60;
        }
        while (minutes >= 60) {
            hours++;
            minutes -= 60;
        }
        while (hours >= 24) {
            days++;
            hours -= 24;
        }
        
        return (days == 0 ? "" : days + "d ") +
            (hours == 0 && minutes != 0 ? "" : hours + "h ") +
            (minutes == 0 && seconds != 0 ? "" : minutes + "m ") +
            seconds + "s";
    }
    
    @Override
    public @NotNull String getCommandName() {
        return "playtime";
    }
    
    @Override
    public @NotNull String getCommandUsage() {
        return "/playtime [target]";
    }
}

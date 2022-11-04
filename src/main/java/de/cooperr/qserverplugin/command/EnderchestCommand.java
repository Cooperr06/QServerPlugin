package de.cooperr.qserverplugin.command;

import de.cooperr.cppluginutil.PaperCommand;
import de.cooperr.cppluginutil.PaperPlugin;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class EnderchestCommand extends PaperCommand {
    
    public EnderchestCommand(@NotNull PaperPlugin plugin) {
        super(plugin);
    }
    
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        
        if (!(sender instanceof Player)) {
            sendWrongSenderMessage(sender);
            return true;
        }
        
        var player = (Player) sender;
        
        if (args.length != 1) {
            sendCommandUsage(sender);
            return true;
        }
        
        var target = plugin.getServer().getPlayer(args[0]);
        
        if (target == null || !target.isOnline()) {
            player.sendMessage(Component.text("Dieser Spieler existiert nicht oder ist nicht online!", NamedTextColor.DARK_RED));
            return true;
        }
        
        player.openInventory(target.getEnderChest());
        return true;
    }
    
    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        
        var tabCompletion = new ArrayList<String>();
        
        if (!(sender instanceof Player)) {
            return null;
        }
        
        var player = (Player) sender;
        
        if (args.length == 0) {
            tabCompletion.addAll(plugin.getServer().getOnlinePlayers().stream()
                .map(Player::getName).collect(Collectors.toList()));
            return tabCompletion;
        } else if (args.length == 1) {
            
            for (var onlinePlayer : plugin.getServer().getOnlinePlayers()) {
                if (args[0].equals(onlinePlayer.getName())) {
                    return null;
                }
            }
            
            tabCompletion.addAll(plugin.getServer().getOnlinePlayers().stream()
                .map(Player::getName).collect(Collectors.toList()));
            tabCompletion.removeIf(s -> !s.startsWith(args[0]));
            
            return tabCompletion;
        }
        return null;
    }
    
    @Override
    public @NotNull String getCommandName() {
        return "enderchest";
    }
    
    @Override
    public @NotNull String getCommandUsage() {
        return "/enderchest <target>";
    }
}

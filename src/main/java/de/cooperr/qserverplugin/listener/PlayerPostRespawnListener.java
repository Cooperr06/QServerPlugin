package de.cooperr.qserverplugin.listener;

import com.destroystokyo.paper.event.player.PlayerPostRespawnEvent;
import de.cooperr.cppluginutil.PaperListener;
import de.cooperr.cppluginutil.PaperPlugin;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.Statistic;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class PlayerPostRespawnListener extends PaperListener<PlayerPostRespawnEvent> {
    
    public PlayerPostRespawnListener(@NotNull PaperPlugin plugin) {
        super(plugin);
    }
    
    @EventHandler
    @Override
    public void onEvent(PlayerPostRespawnEvent event) {
        
        var player = event.getPlayer();
        
        if (player.getStatistic(Statistic.TIME_SINCE_DEATH) > 15 * 20) {
            return;
        }
        
        var deathLocation = player.getLastDeathLocation();
        assert deathLocation != null;
        var textDeathLocation = deathLocation.getBlockX() + " " + deathLocation.getBlockY() + " " + deathLocation.getBlockZ();
        
        var itemStack = new ItemStack(Material.POPPY);
        itemStack.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        itemStack.addUnsafeEnchantment(Enchantment.DURABILITY, 1);
        itemStack.editMeta(itemMeta -> itemMeta.displayName(Component.text(textDeathLocation, NamedTextColor.LIGHT_PURPLE, TextDecoration.BOLD)));
        
        player.getInventory().addItem(itemStack);
        player.sendMessage(
            Component.text("Du bist bei den Koordinaten ", NamedTextColor.GOLD)
                .append(Component.text(textDeathLocation, NamedTextColor.GOLD, TextDecoration.BOLD))
                .append(Component.text(" gestorben!", NamedTextColor.GOLD))
        );
    }
}

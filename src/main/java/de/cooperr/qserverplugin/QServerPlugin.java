package de.cooperr.qserverplugin;

import de.cooperr.cppluginutil.CustomConfig;
import de.cooperr.cppluginutil.PaperPlugin;
import de.cooperr.qserverplugin.command.DiscordCommand;
import de.cooperr.qserverplugin.command.InventoryCommand;
import de.cooperr.qserverplugin.command.PlaytimeCommand;
import de.cooperr.qserverplugin.command.VanishCommand;
import de.cooperr.qserverplugin.listener.AsyncChatListener;
import de.cooperr.qserverplugin.listener.PlayerJoinListener;
import de.cooperr.qserverplugin.listener.PlayerPostRespawnListener;
import de.cooperr.qserverplugin.listener.PlayerQuitListener;

import java.io.File;
import java.util.ArrayList;

public final class QServerPlugin extends PaperPlugin {
    
    @Override
    public void onLoad() {
        config = new CustomConfig(this, new File(getDataFolder(), "config.yml"));
        
        if (!config.isSet("discord-server")) {
            config.set("discord-server", "https://discord.com");
            config.set("vanished-players", new ArrayList<String>());
            
            saveConfig();
        }
    }
    
    @Override
    public void onEnable() {
        listenerRegistration();
        commandRegistration();
    }
    
    @Override
    protected void listenerRegistration() {
        new AsyncChatListener(this);
        new PlayerJoinListener(this);
        new PlayerPostRespawnListener(this);
        new PlayerQuitListener(this);
    }
    
    @Override
    protected void commandRegistration() {
        new DiscordCommand(this);
        new InventoryCommand(this);
        new PlaytimeCommand(this);
        new VanishCommand(this);
    }
}

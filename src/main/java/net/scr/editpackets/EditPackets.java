package net.scr.editpackets;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public final class EditPackets extends JavaPlugin {

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(new FindBlock(this), this);
        getServer().getPluginManager().registerEvents(new SetBlock(), this);



    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}

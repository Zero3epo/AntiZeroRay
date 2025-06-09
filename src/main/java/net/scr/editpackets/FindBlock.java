package net.scr.editpackets;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.BlockPosition;
import com.comphenix.protocol.wrappers.WrappedBlockData;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;


public class FindBlock implements Listener {

    private final EditPackets plugin;
    private final ProtocolManager protocolManager;
    private long lastCheckTime = 0;

    public FindBlock(EditPackets plugin) {
        this.plugin = plugin;
        this.protocolManager = ProtocolLibrary.getProtocolManager();
    }

    @EventHandler
    public void onFindBlock(PlayerMoveEvent e) {

        if (System.currentTimeMillis() - lastCheckTime < 100) {
            return;
        }
        lastCheckTime = System.currentTimeMillis();


        if (e.getFrom().getBlockX() == e.getTo().getBlockX() &&
                e.getFrom().getBlockY() == e.getTo().getBlockY() &&
                e.getFrom().getBlockZ() == e.getTo().getBlockZ()) {
            return;
        }

        int radius = 50;
        Player p = e.getPlayer();
        World world = p.getWorld();
        int Px = (int) p.getLocation().getX();
        int Py = (int) p.getLocation().getY();
        int Pz = (int) p.getLocation().getZ();

        other:
        for (int x = Px - radius; x <= Px + radius; x++) {
            for (int y = Py - 100; y <= Py + 100; y++) {
                for (int z = Pz - radius; z <= Pz + radius; z++) {
                    Block block = world.getBlockAt(x, y, z);
                    if (block.getType() == Material.CHEST) {
                        p.sendMessage("Chest found at: " + x + ", " + y + ", " + z);
                        double distance = Math.sqrt(
                                Math.pow(Px - x, 2) +
                                        Math.pow(Py - y, 2) +
                                        Math.pow(Pz - z, 2)
                        );

                        if(distance <= 10) {
                            sendFakeBlockChange(p, block, Material.CHEST);
                        }else {
                            sendFakeBlockChange(p, block, Material.AIR);
                        }



                    }
                }
            }
        }
    }


    private void sendFakeBlockChange(Player player, Block block, Material newType) {
        PacketContainer packet = protocolManager.createPacket(PacketType.Play.Server.BLOCK_CHANGE);
        packet.getBlockPositionModifier().write(0, new BlockPosition(
                block.getX(),
                block.getY(),
                block.getZ()
        ));
        packet.getBlockData().write(0, WrappedBlockData.createData(newType));

        try {
            protocolManager.sendServerPacket(player, packet);
        } catch (Exception ex) {
            player.sendMessage("Ошибка изменения блока: " + ex.getMessage());
        }
    }
}

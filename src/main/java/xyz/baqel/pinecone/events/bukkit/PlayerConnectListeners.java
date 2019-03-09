package xyz.baqel.pinecone.events.bukkit;

import com.ngxdev.tinyprotocol.api.TinyProtocolHandler;
import com.ngxdev.tinyprotocol.packet.out.WrappedOutTransaction;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;
import xyz.baqel.pinecone.Pinecone;
import xyz.baqel.pinecone.data.PlayersData;
import xyz.baqel.pinecone.data.logging.Yaml;

public class PlayerConnectListeners implements Listener {
    @EventHandler(priority = EventPriority.LOW)
    public void onPlayerJoin(PlayerJoinEvent e) {
        Pinecone.getInstance().getDataManager().createDataObject(e.getPlayer());
        Pinecone.getInstance().getDataManager().getPlayerData(e.getPlayer()).lastLogin.reset();
        new BukkitRunnable() {
            public void run() {
                TinyProtocolHandler.sendPacket(e.getPlayer(), new WrappedOutTransaction(0, (short) 69, true));
            }
        }.runTaskLater(Pinecone.getInstance(), 25L);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerQuit(PlayerQuitEvent e) {
        PlayersData data = Pinecone.getInstance().getDataManager().getPlayerData(e.getPlayer());

        ((Yaml) Pinecone.getInstance().getDataManager().getLogger()).dumpLog(data);
        Pinecone.getInstance().getDataManager().removeDataObject(Pinecone.getInstance().getDataManager().getPlayerData(e.getPlayer()));
        Pinecone.getInstance().getBannedPlayers().remove(e.getPlayer());
    }
}

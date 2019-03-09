package xyz.baqel.pinecone.events.bukkit;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRiptideEvent;
import xyz.baqel.pinecone.Pinecone;
import xyz.baqel.pinecone.data.PlayersData;

public class LatestListeners implements Listener {
    @EventHandler
    public void onEvent(PlayerRiptideEvent event) {
        PlayersData data = Pinecone.getInstance().getDataManager().getPlayerData(event.getPlayer());

        data.riptideTicks+= 5;
    }
}

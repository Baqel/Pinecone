package xyz.baqel.pinecone.data;

import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import xyz.baqel.pinecone.Pinecone;
import xyz.baqel.pinecone.data.logging.DatabaseType;
import xyz.baqel.pinecone.data.logging.Logger;
import xyz.baqel.pinecone.data.logging.Yaml;
import xyz.baqel.pinecone.utils.Config;

import java.util.ArrayList;
import java.util.List;

public class DataManager {

    /**
     * Player Object Stuff
     **/
    private final List<PlayersData> dataObjects;
    @Getter
    private Logger logger;

    public DataManager() {
        dataObjects = new ArrayList<>();
        logger = Config.databaseType.equals(DatabaseType.MONGO) ? new Yaml() : new Yaml();

        new BukkitRunnable() {
            public void run() {
                dataObjects.forEach(data -> {
                    if (data.onGround) {
                        data.serverGroundTicks++;
                        data.serverAirTicks = 0;
                    } else {
                        data.serverGroundTicks = 0;
                        data.serverAirTicks++;
                    }
                });
            }
        }.runTaskTimerAsynchronously(Pinecone.getInstance(), 1L, 1L);
    }

    public void createDataObject(Player player) {
        dataObjects.add(new PlayersData(player));
    }

    public void removeDataObject(PlayersData dataObject) {
        dataObjects.remove(dataObject);
    }

    public PlayersData getPlayerData(Player player) {
        for (PlayersData data : dataObjects) {
            if (data.player == player) return data;
        }
        return null;
    }

    public List<PlayersData> getDataObjects() {
        return dataObjects;
    }
}

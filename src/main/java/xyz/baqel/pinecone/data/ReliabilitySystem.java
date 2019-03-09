package xyz.baqel.pinecone.data;

import xyz.baqel.pinecone.Pinecone;

import java.util.concurrent.TimeUnit;

public class ReliabilitySystem {

    public ReliabilitySystem() {
        Pinecone.getInstance().executorService.scheduleAtFixedRate(() -> {
            Pinecone.getInstance().getDataManager().getDataObjects().forEach(ReliabilitySystem::handle);
        }, 0L, 50L, TimeUnit.MILLISECONDS);
    }

    public static void handle(PlayersData data) {
        if (data.hasLag() && data.lastLogin.hasPassed(150)) {
            data.reliabilityPoints += 1;
            //Bukkit.broadcastMessage(data.reliabilityPoints + " (+2)");
        }
        data.reliabilityPoints = Math.max(data.reliabilityPoints - 0.05f, 0);
        data.reliabilityPercentage = 100 - Math.min(100, data.reliabilityPoints / 20);
        //Bukkit.broadcastMessage(data.lastFlyingPacketDif + ", " + Math.abs(data.lastPing - data.ping) + ", " + data.hasLag()  + ", " + data.reliabilityPoints);
        //Bukkit.broadcastMessage(data.reliabilityPercentage + "%");
    }
}

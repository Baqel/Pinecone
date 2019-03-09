package xyz.baqel.pinecone.data.logging;

import xyz.baqel.pinecone.data.PlayersData;
import xyz.baqel.pinecone.detections.Detection;

import java.util.List;
import java.util.UUID;

public interface Logger {

    void addLog(Detection detection, double vl, String info, PlayersData data);

    List<Log> getLogs(UUID uuid);
}

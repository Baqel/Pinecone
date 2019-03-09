package xyz.baqel.pinecone.data.logging;

import com.google.common.collect.Lists;
import xyz.baqel.pinecone.Pinecone;
import xyz.baqel.pinecone.data.PlayersData;
import xyz.baqel.pinecone.detections.Detection;
import xyz.baqel.pinecone.utils.BaqelFile;
import xyz.baqel.pinecone.utils.Config;
import xyz.baqel.pinecone.utils.MathUtils;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class Yaml implements Logger {
    public Yaml() {
        startLogSaving();
    }

    @Override
    public void addLog(Detection detection, double vl, String info, PlayersData data) {
        Log log = new Log(data.player.getUniqueId(), detection.getParentCheck().getName(), detection.getId(), info, data.ping, Pinecone.getInstance().getTps(), vl, MathUtils.round(data.reliabilityPercentage, 5));

        addLogString(data, log.getCheck() + ";" + log.getDetection() + ";" + log.getData() + ";" + log.getPing() + ";" + log.getTps() + ";" + log.getVl() + ";" + log.getReliabilityPercentage());
    }

    @Override
    public List<Log> getLogs(UUID uuid) {
        List<Log> logs = Lists.newArrayList();
        BaqelFile file = new BaqelFile(Pinecone.getInstance(), "logs", uuid.toString() + ".txt");

        file.readFile();

        file.lines.forEach(line -> {
            String[] split = line.split(";");

            logs.add(new Log(uuid, split[0], split[1], split[2], Integer.parseInt(split[3]), Double.parseDouble(split[4]), Double.parseDouble(split[5]), Double.parseDouble(split[6])));
        });
        return logs;
    }

    public BaqelFile getLogFile(UUID uuid) {
        BaqelFile file = new BaqelFile(Pinecone.getInstance(), "logs", uuid.toString() + ".txt");

        file.readFile();

        return file;
    }

    public void addLogString(PlayersData data, String string) {
        if (Config.logging) data.cachedLogStrings.add(string);
    }

    public void dumpLogs() {
        Pinecone.getInstance().getDataManager().getDataObjects().stream().filter(data -> data.cachedLogStrings.size() > 0).forEach(data -> {
            BaqelFile file = new BaqelFile(Pinecone.getInstance(), "logs", data.player.getUniqueId().toString() + "txt");
            data.cachedLogStrings.forEach(file::addLine);
            file.write();
        });
    }

    public void dumpLog(PlayersData data) {
        if (data.cachedLogStrings.size() > 0) {
            BaqelFile file = new BaqelFile(Pinecone.getInstance(), "logs", data.player.getUniqueId().toString() + ".txt");
            data.cachedLogStrings.forEach(file::addLine);
            file.write();
        }
    }

    public void startLogSaving() {
        Pinecone.getInstance().executorService.scheduleAtFixedRate(this::dumpLogs, 0L, Config.saveInterval, TimeUnit.valueOf(Config.logTimeUnit));
    }
}

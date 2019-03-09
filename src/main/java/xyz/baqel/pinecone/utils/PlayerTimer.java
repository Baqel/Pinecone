package xyz.baqel.pinecone.utils;

import xyz.baqel.pinecone.Pinecone;
import xyz.baqel.pinecone.data.PlayersData;

/**
 * A timer based on
 */
public class PlayerTimer {
    public int startTime;
    private PlayersData player;

    public PlayerTimer(PlayersData player) {
        this.player = player;
        this.reset();
    }

    public static boolean hasPassed(long startTime, long toPass) {
        return (System.currentTimeMillis() - startTime) >= toPass;
    }

    public boolean wasReset() {
        return this.startTime == Pinecone.getInstance().getCurrentTick();
    }

    public void reset() {
        this.startTime = Pinecone.getInstance().getCurrentTick();
    }

    public long getPassed() {
        return Pinecone.getInstance().getCurrentTick() - this.startTime;
    }

    public void add(int amount) {
        this.startTime -= amount;
    }

    public boolean hasPassed(long toPass) {
        return this.getPassed() >= toPass;
    }

    public boolean hasNotPassed(long toPass) {
        return this.getPassed() < toPass;
    }

    public boolean hasPassed(long toPass, boolean reset) {
        boolean passed = this.getPassed() >= toPass;
        if (passed && reset) reset();
        return passed;
    }}

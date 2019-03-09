package xyz.baqel.pinecone.profiling;

public interface Profiler {
    void start(String name);

    void start();

    void stop(String name, long extense);

    void stop(String name);

    void stop();}

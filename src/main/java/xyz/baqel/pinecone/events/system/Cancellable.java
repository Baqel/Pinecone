package xyz.baqel.pinecone.events.system;

public interface Cancellable {
    boolean isCancelled();

    void setCancelled(boolean var1);
}

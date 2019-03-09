package xyz.baqel.pinecone.commands;

import xyz.baqel.pinecone.commands.pinecone.PineconeCommand;

import java.util.ArrayList;
import java.util.List;

public class BaqelCommandManager {
    public final List<BaqelCommand> commands;

    public BaqelCommandManager() {
        commands = new ArrayList<>();
        this.initialization();
    }

    private void initialization() {
        this.commands.add(new PineconeCommand());
    }

    public void removeAllCommands() {
        commands.clear();
    }
}

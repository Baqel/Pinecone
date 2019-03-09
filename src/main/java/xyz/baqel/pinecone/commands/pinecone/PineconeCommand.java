package xyz.baqel.pinecone.commands.pinecone;

import xyz.baqel.pinecone.commands.BaqelCommand;
import xyz.baqel.pinecone.commands.pinecone.args.*;

public class PineconeCommand extends BaqelCommand {

    public PineconeCommand() {
        super("pinecone", "Pinecone", "The main command for Pinecone AntiCheat.", "pinecone.help");
    }

    @Override
    public void addArguments() {
        getArguments().add(new AlertsArgument());
        getArguments().add(new ReloadArgument());
        getArguments().add(new MenuArgument());
        getArguments().add(new ViewArgument());
        getArguments().add(new AutobanArgument());
        getArguments().add(new SaveArgument());
        getArguments().add(new DebugArgument());
        getArguments().add(new BroadcastArgument());
    }
}

package network.utils;

import java.net.Socket;

public abstract class AbsConcurrentServer extends AbstractServer {

    public AbsConcurrentServer(int port) {
        super(port);
    }

    @Override
    protected void processRequest(Socket client) {
        Thread thread = new Thread(() -> handleClient(client));
        thread.start();
    }

    protected abstract void handleClient(Socket client);
}
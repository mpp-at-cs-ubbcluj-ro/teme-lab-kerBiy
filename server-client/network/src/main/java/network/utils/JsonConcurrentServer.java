package network.utils;

import network.protocol.ClientJsonWorker;
import services.IServices;

import java.net.Socket;

public class JsonConcurrentServer extends AbsConcurrentServer {
    private final IServices services;

    public JsonConcurrentServer(int port, IServices services) {
        super(port);
        this.services = services;
    }

    @Override
    protected void handleClient(Socket client) {
        ClientJsonWorker worker = new ClientJsonWorker(services, client);
        new Thread(worker).start();
    }
}
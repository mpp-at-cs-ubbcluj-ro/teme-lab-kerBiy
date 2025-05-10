package network.utils;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public abstract class AbstractServer {
    private int port;
    private ServerSocket serverSocket;

    public AbstractServer(int port) {
        this.port = port;
    }

    public void start() throws ServerException {
        System.out.println(">>>>>>>>>> SERVER STARTED <<<<<<<<<<<<");
        System.out.flush();  // Forțează afișarea
        try {
            serverSocket = new ServerSocket(port);
            while (true) {
                System.out.println("Waiting for clients on port " + port + "...");
                Socket client = serverSocket.accept();
                System.out.println("Client connected: " + client);
                System.out.flush();
                processRequest(client);
            }
        } catch (IOException e) {
            throw new ServerException("Error starting server", e);
        }
    }

    protected abstract void processRequest(Socket client);

    public void stop() throws ServerException {
        try {
            if (serverSocket != null) {
                serverSocket.close();
            }
        } catch (IOException e) {
            throw new ServerException("Error stopping server", e);
        }
    }
}
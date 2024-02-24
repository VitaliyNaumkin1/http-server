package ru.otus.java.basic.http.server;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.Socket;

public class ConnectionHandler {
    private Dispatcher dispatcher;
    private Socket socket;
    private final Logger logger = LogManager.getLogger(ConnectionHandler.class.getName());

    public ConnectionHandler(Socket socket) {
        this.dispatcher = new Dispatcher();
        this.socket = socket;
        handleConnection();
    }

    public void handleConnection() {
        try {
            byte[] buffer = new byte[8192];
            int n = socket.getInputStream().read(buffer);
            String rawRequest = new String(buffer, 0, n);
            logger.info("поступил запрос клиента: " + "port: " + socket.getPort());
            if (n > 0) {
                HttpRequest httpRequest = new HttpRequest(rawRequest);
                httpRequest.printInfo(false);
                dispatcher.execute(httpRequest, socket.getOutputStream());
            }
        } catch (IOException e) {
            logger.error(e);
        }
    }

}


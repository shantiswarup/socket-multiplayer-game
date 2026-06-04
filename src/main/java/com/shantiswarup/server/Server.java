package com.shantiswarup.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private static final Logger logger = LoggerFactory.getLogger(Server.class);
    private Server() {
        /* This utility class should not be instantiated */
    }
    public static void main(String[] args) {
        start();
    }

    private static final int PORT = 8080;

    public static void start() {
        logger.info("Starting server...");
        ClientHandler clientHandler = new ClientHandler(new GameHandler());
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            logger.info("Server started on port " + PORT);
            while (true) {
                Socket clientSocket = serverSocket.accept();
                logger.info("New client connected: " + clientSocket.getInetAddress());
                clientHandler.handleClient(clientSocket);
            }
        } catch (Exception e) {
            logger.error("Error in server: ", e);
        }
    }
}
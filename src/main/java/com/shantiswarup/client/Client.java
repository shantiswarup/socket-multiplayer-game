package com.shantiswarup.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {
    private String clientId;
    private String clientName;
    private static final Logger logger = LoggerFactory.getLogger(Client.class);

    public Client(String clientId, String clientName) {
        this.clientId = clientId;
        this.clientName = clientName;
    }

    public String getClientId() {
        return clientId;
    }

    public String getClientName() {
        return clientName;
    }

    public static void main(String[] args) {
        try(Socket clientSocket = new Socket("localhost", 8080)) {

            logger.info("Connected to server.");
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in));
            while (true) {
                String response = in.readLine();
                if (response == null) {
                    logger.info("Game Over");
                    break;
                }
                logger.info(response);

                String message = consoleReader.readLine();
                out.println(message);
                String feedback = in.readLine();
                logger.info(feedback);
            }
        } catch (IOException e) {
            logger.error("Error in client: ", e);
        }
    }
}
package com.shantiswarup.games;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicInteger;

public class GamePlayer {
    private final Logger logger = LoggerFactory.getLogger(GamePlayer.class);
    private final String playerId;
    private final Socket client;
    private final AtomicInteger score;

    public GamePlayer(String playerId, Socket client) {
        this.playerId = playerId;
        this.client = client;
        this.score = new AtomicInteger(10);
    }

    public void sendMessage(String message) {
        try {
            client.getOutputStream().write((message + "\n").getBytes());
        } catch (Exception e) {
            logger.error("Error sending message to player {}", playerId, e);
        }
    }
    public String receiveMessage() {
        try {
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(client.getInputStream()));
            logger.info("Waiting for message from player {}", playerId);
            String inp = in.readLine();
            logger.info("Received message from player {}: {}",playerId, inp);
            return inp;
        } catch (Exception e) {
            logger.error("Error receiving message from player {}", playerId, e);
        }
        return null;
    }

    public String getPlayerId() {
        return playerId;
    }

    public synchronized int getScore() {
        return score.get();
    }

    public synchronized int updateScore() {
        return score.decrementAndGet();
    }
    public void disconnect() {
        try {
            client.close();
        } catch (Exception e) {
            logger.error("Error disconnecting player {}", playerId, e);
        }
    }
    @Override
    public String toString() {
        return "GamePlayer{" +
                "playerId='" + playerId + '\'' +
                ", score=" + score +
                '}';
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GamePlayer that = (GamePlayer) o;
        return playerId.equals(that.playerId);
    }

}

package com.shantiswarup.server;

import com.shantiswarup.games.Game;
import com.shantiswarup.games.GameListener;
import com.shantiswarup.games.GamePlayer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.ArrayBlockingQueue;

public class ClientHandler implements GameListener {
    private static final Logger logger = LoggerFactory.getLogger(ClientHandler.class);
    private final ArrayBlockingQueue<Socket> clientQueue;
    private final GameHandler gameHandler;

    public ClientHandler(GameHandler gameHandler) {
        this.gameHandler = gameHandler;
        this.clientQueue = new ArrayBlockingQueue<>(2);
    }

    public void handleClient(Socket client) {
        if (clientQueue.size() < 2) {
            clientQueue.add(client);
            logger.info("Client added to queue: {}", client.getInetAddress());
            if (!gameHandler.isGameActive()) {
                startGame();
            }
        } else {
            logger.warn("Client queue is full. Cannot accept new client: {}", client.getInetAddress());
            try {
                client.close();
            } catch (IOException e) {
                logger.error("Error closing client connection: ", e);
            }
        }
    }

    private void startGame() {
        if (clientQueue.size() == 2) {
            Socket client1 = clientQueue.poll();
            logger.info("Player 1 connected: {}", client1.getInetAddress());
            GamePlayer player1 = new GamePlayer("player1", client1);
            Socket client2 = clientQueue.poll();
            logger.info("Player 2 connected: {}", client2.getInetAddress());
            GamePlayer player2 = new GamePlayer("player2", client2);
            Game game = new Game("game1", player1, player2, player1, this);
            logger.info("Game started between {} and {}", player1.getPlayerId(), player2.getPlayerId());
            gameHandler.handleGame(game);
        } else {
            logger.warn("Not enough clients to start a game. Current queue size: {}", clientQueue.size());
        }
    }

    @Override
    public void onGameFinished(Game game) {
        game.close();
        startGame();
    }

}

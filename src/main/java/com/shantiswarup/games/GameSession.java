package com.shantiswarup.games;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

public class GameSession implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(GameSession.class);
    private final Game game;

    public GameSession(Game game) {
        this.game = game;
    }

    @Override
    public void run() {
        while(game.isGameActive()) {
            GamePlayer currentPlayer = game.getTurn();
            currentPlayer.sendMessage("It's your turn!");
            String message = currentPlayer.receiveMessage();
            if (Objects.equals(message, "exit")) {
                break;
            }
            logger.debug("Received message from player {} : {} ", currentPlayer.getPlayerId(), message);
            game.makeMove(message);
        }
        String winnerMessage = "Game over. Winner: " + (game.getWinner() != null ? game.getWinner().getPlayerId() : "None");
        game.broadcastMessage(winnerMessage);
        logger.info(winnerMessage);
        game.broadcastMessage(null);
    }
}

package com.shantiswarup.server;

import com.shantiswarup.games.Game;
import com.shantiswarup.games.GameListener;
import com.shantiswarup.games.GameSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

public class GameHandler implements GameListener {
    private static final Logger logger = LoggerFactory.getLogger(GameHandler.class);
    private final ArrayBlockingQueue<Game> gameQueue;
    private final ExecutorService executorService;
    private final AtomicBoolean isGameActive = new AtomicBoolean(false);
    public GameHandler() {
        this.gameQueue = new ArrayBlockingQueue<>(1);
        this.executorService = Executors.newFixedThreadPool(10);
    }
    public synchronized void handleGame(Game newGame) {
        try {
            if (gameQueue.isEmpty()) {
                isGameActive.set(true);
                gameQueue.put(newGame);
                Game game = gameQueue.peek();
                GameSession gameSession = new GameSession(game);
                executorService.execute(gameSession);
            } else {
                logger.warn("Game queue is full. Cannot handle new game at the moment.");
            }
        } catch (InterruptedException e) {
            logger.error("Error in handling game: ", e);
        }
    }
    @Override
    public synchronized void onGameFinished(Game game) {
        gameQueue.poll();
        isGameActive.compareAndSet(true, false);
    }

    public synchronized boolean isGameActive() {
        return isGameActive.get();
    }
}

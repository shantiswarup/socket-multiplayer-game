package com.shantiswarup.games;


import java.util.Random;

public class Game {
    private final String gameId;
    private final GamePlayer player1;
    private final GamePlayer player2;
    private boolean gameActive = true;
    private GamePlayer winner = null;
    private final int num;
    private GamePlayer turn = null;
    private final GameListener gameListener;

    public Game(String gameId, GamePlayer player1, GamePlayer player2, GamePlayer turn, GameListener gameListener) {
        this.gameId = gameId;
        this.player1 = player1;
        this.player2 = player2;
        this.turn = turn;
        this.gameListener = gameListener;
        this.num = new Random().nextInt(100) + 1;
        System.out.println("Generated number for game " + gameId + ": " + num);
    }

    public int isValidInput(String input) {
        if (input == null || input.isEmpty()) {
            throw new IllegalArgumentException("Input cannot be null or empty");
        }
        return Integer.parseInt(input);
    }
    public void broadcastMessage(String message) {
        player1.sendMessage(message);
        player2.sendMessage(message);
    }

    public void close() {
        player1.disconnect();
        player2.disconnect();
    }
    public void makeMove(String guess) {
        int guessNum = isValidInput(guess);
        if (guessNum == num) {
            setWinner(turn);
            setGameActive(false);
            gameListener.onGameFinished(this);
        } else {
            turn.sendMessage("Wrong guess! Try again. in next turn");
            turn = (turn == player1) ? player2 : player1;
        }
    }

    public String getGameId() {
        return gameId;
    }

    public GamePlayer getTurn() {
        return turn;
    }

    public GamePlayer getWinner() {
        return winner;
    }

    public boolean isGameActive() {
        return gameActive;
    }

    private void setGameActive(boolean gameActive) {
        this.gameActive = gameActive;
    }

    private void setWinner(GamePlayer winner) {
        this.winner = winner;
    }
}

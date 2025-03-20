package be.kdg.quarto.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class GameSession {
    private final Game model;
    private Player winner;
    private final Player player1;
    private final Player player2;
    private final List<Move> moves;
    private final Date startTime;
    private Date endTime;



    public GameSession(Player player1, Player player2, Game model) {
        this.player1 = player1;
        this.player2 = player2;
        this.moves = new ArrayList<>();
        this.model = model;
        this.startTime = new Date();
    }

    public Player getOtherPlayer(Player currentPlayer) {
        if (currentPlayer.equals(player1)) return player2;
        if (currentPlayer.equals(player2)) return player1;
        throw new IllegalArgumentException("Given player is not part of this session.");
    }

    public boolean hasWinner() {
        return winner != null;
    }

    public void addMove(Move move) {
        moves.add(move);
    }

    public List<Move> getMoves() {
        return new ArrayList<>(moves); // Return a copy to prevent modification from outside
    }

    public Date getStartTime() {
        return new Date(startTime.getTime()); // Return a copy to maintain immutability
    }

    public Date getEndTime() {
        return (endTime != null) ? new Date(endTime.getTime()) : null; // Handle null case
    }

    public void setEndTime(Date endTime) {
        this.endTime = (endTime != null) ? new Date(endTime.getTime()) : null;
    }

    public Game getModel() {
        return model;
    }

    public boolean isGameOver() {
        return winner != null;
    }

    public Player getWinner() {
        return winner;
    }

    public void setWinner(Player winner) { // Fixed naming convention from SetWinner to setWinner
        this.winner = winner;
    }
    public Player getPlayer1() {
        return player1;
    }

    public Player getPlayer2() {
        return player2;
    }
}
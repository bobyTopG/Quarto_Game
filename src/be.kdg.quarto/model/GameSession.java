package be.kdg.quarto.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class GameSession {
    private Player winner;
    private final Player player1;
    private final Player player2;
    private final List<Move> moves;
    private Date startTime;
    private Date endTime;

    public GameSession(Player player1, Player player2) {
       this.player1 = player1;
       this.player2 = player2;
       moves = new ArrayList<>();

       startTime = new Date();
    }

    public Player getOtherPlayer(Player currentPlayer) {
        if (currentPlayer.equals(player1)) return player2;
        if (currentPlayer.equals(player2)) return player1;
        throw new IllegalArgumentException("Given player is not part of this session.");
    }

    public boolean hasWinner() {
        return winner != null;
    }
    public void addMove(Move move){
        moves.add(move);
    }
    public List<Move> getMoves() {
        return moves;
    }

    public Date getStartTime() {
        return startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }
}
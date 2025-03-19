package be.kdg.quarto.model;

import be.kdg.quarto.helpers.DbConnection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Statistics {
    private int gameSessionIdTemp;
    private int playerIdTemp;
    private int winnerId;
    private int totalMoves;
    private String totalTime;
    private float averageTime;

    public Statistics(int gameSessionId, int playerId) {
        this.gameSessionIdTemp = gameSessionId;
        this.playerIdTemp = playerId;
    }

    public void setPlayerIdTemp(int playerIdTemp) {
        this.playerIdTemp = playerIdTemp;
    }

    public int getWinnerId() {
        return winnerId;
    }

    public String loadPartialStatistics() {
        try (PreparedStatement ps = DbConnection.connection.prepareStatement(DbConnection.getPartialStats())) {
            ps.setInt(1, gameSessionIdTemp);
            ps.setInt(2, playerIdTemp);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                this.winnerId = rs.getInt(1);
                this.totalMoves = rs.getInt(2);
                this.totalTime = rs.getString(3);
                this.averageTime = rs.getFloat(4);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return (playerIdTemp == winnerId ? "Wow, congrats!" : "Better luck next time!") +
                "\nTotal moves: " + totalMoves +
                "\nTotal time: " + totalTime +
                "\nAverage time: " + averageTime;
    }

    public ArrayList<Move> loadStatistics() {
        ArrayList<Move> moves = new ArrayList<>();
        try (PreparedStatement ps = DbConnection.connection.prepareStatement(DbConnection.getStatistics())) {
            ps.setInt(1, gameSessionIdTemp);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                moves.add(new Move(rs.getInt(1), rs.getInt(2), rs.getInt(3)));
            }
//            System.out.println(moves);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return moves;
    }

    public class Move {
        int playerId;
        int moveNumber;
        int time;

        public Move(int playerId, int moveNumber, int time) {
            this.playerId = playerId;
            this.moveNumber = moveNumber;
            this.time = time;
        }

        @Override
        public String toString() {
            return "Move [playerId=" + playerId + ", moveNumber=" + moveNumber + ", time=" + time + "]";
        }

        public int getPlayerId() {
            return playerId;
        }

        public int getMoveNumber() {
            return moveNumber;
        }

        public int getTime() {
            return time;
        }
    }
}

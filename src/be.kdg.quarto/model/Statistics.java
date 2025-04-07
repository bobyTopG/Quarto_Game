package be.kdg.quarto.model;

import be.kdg.quarto.helpers.DbConnection;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Statistics {
    private int gameSessionIdTemp;
    private int playerIdTemp;
    private int winner;
    private int totalMoves;
    private String totalTime;
    private float averageTime;

    private String playerNameTemp;

    public Statistics(int gameSessionId, int playerId) {
        this.gameSessionIdTemp = gameSessionId;
        this.playerIdTemp = playerId;
        try (PreparedStatement ps = DbConnection.connection.prepareStatement(DbConnection.getWinner())) {
            ps.setInt(1, gameSessionId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                winner = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void setPlayerIdTemp(int playerIdTemp) {
        this.playerIdTemp = playerIdTemp;
    }

    public int getWinner() {
        return winner;
    }

    public String loadPartialStatistics() {
        try (PreparedStatement ps = DbConnection.connection.prepareStatement(DbConnection.getPartialStats())) {
            ps.setInt(1, playerIdTemp);
            ps.setInt(2, gameSessionIdTemp);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                this.playerNameTemp = rs.getString(2);
                this.totalMoves = rs.getInt(3);
                this.totalTime = rs.getString(4);
                this.averageTime = rs.getFloat(5);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return (playerIdTemp == winner ? "Wow, congrats. Well played!" : "Better luck next time!") +
                "\nTotal moves: " + totalMoves +
                "\nTotal time: " + totalTime +
                "\nAverage move duration: " + averageTime + " m";
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

package be.kdg.quarto.model;

import be.kdg.quarto.helpers.DbConnection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Statistics {
    private int gameSessionId;

    private int totalMoves;
    private int totalDuration;
    private float averageMoveDuration;

    private int winnerId, playerId1, playerId2, playerIdTemp;
    private String winner, player1, player2;

    public Statistics(int gameSessionId) throws SQLException {
        this.gameSessionId = gameSessionId;
        try (PreparedStatement ps = DbConnection.connection.prepareStatement(DbConnection.getPlayers())) {
            ps.setInt(1, gameSessionId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                winnerId = rs.getInt(1);
                winner = rs.getString(2);
                playerId1 = rs.getInt(3);
                player1 = rs.getString(4);
                playerId2 = rs.getInt(5);
                player2 = rs.getString(6);
            }
        } catch (SQLException | NullPointerException e) {
            throw new SQLException(e);
        }
    }

    public void setPlayerIdTemp(int playerIdTemp) {
        this.playerIdTemp = playerIdTemp;
    }

    public String getWinner() {
        return winner;
    }

    public int getPlayerId1() {
        return playerId1;
    }

    public int getPlayerId2() {
        return playerId2;
    }

    public String getPlayer1() {
        return player1;
    }

    public String getPlayer2() {
        return player2;
    }

    public String loadPartialStatistics() {
        try (PreparedStatement ps = DbConnection.connection.prepareStatement(DbConnection.getPartialStatistics())) {
            ps.setInt(1, playerIdTemp);
            ps.setInt(2, gameSessionId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                this.totalMoves = rs.getInt(2);
                this.totalDuration = rs.getInt(3);
                this.averageMoveDuration = rs.getFloat(4);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return (playerIdTemp == winnerId ? "Wow, congrats. Well played!" : "Better luck next time!") +
                "\nTotal moves: " + totalMoves +
                "\nTotal duration: " + (totalDuration / 60) + " m " + (totalDuration % 60) + " s" +
                "\nAverage move duration: " + averageMoveDuration + " m";
    }

    public ArrayList<Move> loadStatistics() {
        ArrayList<Move> moves = new ArrayList<>();
        try (PreparedStatement ps = DbConnection.connection.prepareStatement(DbConnection.getStatistics())) {
            ps.setInt(1, gameSessionId);
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

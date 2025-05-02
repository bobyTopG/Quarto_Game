package be.kdg.quarto.model;

import be.kdg.quarto.helpers.DbConnection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Statistics {
    private final int gameSessionId;

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

    public String loadPartialStatistics() throws SQLException {
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
            // Log the specific SQL error message
            System.err.println("SQL Error: " + e.getMessage());

            throw new SQLException("failed to load Statistics");
        }

        return (playerIdTemp == winnerId ? "Wow, congrats. Well played!" : "Better luck next time!") +
                "\nTotal moves: " + totalMoves +
                "\nTotal duration: " + (totalDuration / 60) + " m " + (totalDuration % 60) + " s" +
                "\nAverage move duration: " + averageMoveDuration + " m";
    }

    public ArrayList<Move> loadStatistics() throws SQLException {
        ArrayList<Move> moves = new ArrayList<>();
        try (PreparedStatement ps = DbConnection.connection.prepareStatement(DbConnection.getStatistics())) {
            ps.setInt(1, gameSessionId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                moves.add(new Move(rs.getInt(1), rs.getInt(2), rs.getInt(3)));
            }
        } catch (SQLException e) {
            throw new SQLException("Failed to load Statistics");
        }

        return moves;
    }

    public ArrayList<Move> getOutliers() throws SQLException {
        ArrayList<Move> moves = loadStatistics();
        if (moves.size() < 4) return new ArrayList<>(); // Not enough data for IQR

        ArrayList<Integer> durations = new ArrayList<>();
        for (Move move : moves) {
            durations.add(move.getTime());
        }

        durations.sort(Integer::compareTo);

        double q1 = getPercentile(durations, 25);
        double q3 = getPercentile(durations, 75);
        double iqr = q3 - q1;

        double lowerBound = q1 - 1.5 * iqr;
        double upperBound = q3 + 1.5 * iqr;

        ArrayList<Move> outliers = new ArrayList<>();
        for (Move move : moves) {
            int time = move.getTime();
            if (time < lowerBound || time > upperBound) {
                outliers.add(move);
            }
        }

        return outliers;
    }


    private double getPercentile(ArrayList<Integer> sortedList, double percentile) {
        if (sortedList.isEmpty()) return 0;
        int index = (int) Math.ceil(percentile / 100.0 * sortedList.size()) - 1;
        return sortedList.get(Math.min(Math.max(index, 0), sortedList.size() - 1));
    }

    public static class Move {
        int playerId;
        int moveNumber;
        int time;

        public Move(int playerId, int moveNumber, int time) {
            this.playerId = playerId;
            this.moveNumber = moveNumber;
            this.time = time;
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

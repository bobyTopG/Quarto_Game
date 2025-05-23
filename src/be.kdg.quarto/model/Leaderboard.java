package be.kdg.quarto.model;

import be.kdg.quarto.helpers.DbConnection;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Leaderboard {

    public ObservableList<Player> loadLeaderboard() throws SQLException {
        ObservableList<Player> playerList = FXCollections.observableArrayList();

        try (PreparedStatement ps = DbConnection.connection.prepareStatement(DbConnection.getLeaderboard())) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                playerList.add(new Player(
                        rs.getString(1),
                        rs.getInt(2),
                        rs.getInt(3),
                        rs.getInt(4),
                        rs.getFloat(5),
                        rs.getFloat(6),
                        rs.getFloat(7)
                ));
            }
        } catch (SQLException | NullPointerException e) {
            throw new SQLException("Failed to load Leaderboard");
        }

        return playerList;
    }

    public static class Player {
        private final SimpleStringProperty name;
        private final SimpleIntegerProperty totalGames;
        private final SimpleIntegerProperty wins;
        private final SimpleIntegerProperty losses;
        private final SimpleFloatProperty winPercentage;
        private final SimpleFloatProperty avgMoves;
        private final SimpleFloatProperty avgMoveDuration;

        public Player(String name, int totalGames, int wins, int losses, float winPercentage, float avgMoves, float avgMoveDuration) {
            this.name = new SimpleStringProperty(name);
            this.totalGames = new SimpleIntegerProperty(totalGames);
            this.wins = new SimpleIntegerProperty(wins);
            this.losses = new SimpleIntegerProperty(losses);
            this.winPercentage = new SimpleFloatProperty(winPercentage);
            this.avgMoves = new SimpleFloatProperty(avgMoves);
            this.avgMoveDuration = new SimpleFloatProperty(avgMoveDuration);
        }

        public String getName() {
            return name.get();
        }

        public SimpleStringProperty nameProperty() {
            return name;
        }


        public SimpleIntegerProperty totalGamesProperty() {
            return totalGames;
        }


        public SimpleIntegerProperty winsProperty() {
            return wins;
        }


        public SimpleIntegerProperty lossesProperty() {
            return losses;
        }


        public SimpleFloatProperty winPercentageProperty() {
            return winPercentage;
        }


        public SimpleFloatProperty avgMovesProperty() {
            return avgMoves;
        }


        public SimpleFloatProperty avgMoveDurationProperty() {
            return avgMoveDuration;
        }
    }
}

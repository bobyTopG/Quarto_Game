package be.kdg.quarto.view.LeaderboardScreen;

import be.kdg.quarto.helpers.CreateHelper;
import be.kdg.quarto.model.Leaderboard;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;

public class LeaderboardView extends BorderPane {

    private TableView<Leaderboard.Player> table;
    private TableColumn<Leaderboard.Player, String> name;
    private TableColumn<Leaderboard.Player, Integer> totalGames;
    private TableColumn<Leaderboard.Player, Integer> wins;
    private TableColumn<Leaderboard.Player, Integer> losses;
    private TableColumn<Leaderboard.Player, Float> winsPercent;
    private TableColumn<Leaderboard.Player, Float> avgMoves;
    private TableColumn<Leaderboard.Player, Float> avgMoveDuration;

    private Button backBtn;


    // Constructor
    public LeaderboardView() {
        initializeNodes();
        layoutNodes();

    }

    private void initializeNodes() {
        Label title = CreateHelper.createLabel("Leaderboard", "title");

        table = new TableView<>();
        name = new TableColumn<>("Name");
        name.setCellValueFactory(cellData -> cellData.getValue().nameProperty());

        totalGames = new TableColumn<>("Total Games");
        totalGames.setCellValueFactory(cellData -> cellData.getValue().totalGamesProperty().asObject());

        wins = new TableColumn<>("Wins");
        wins.setCellValueFactory(cellData -> cellData.getValue().winsProperty().asObject());

        losses = new TableColumn<>("Losses");
        losses.setCellValueFactory(cellData -> cellData.getValue().lossesProperty().asObject());

        winsPercent = new TableColumn<>("Wins Percent");
        winsPercent.setCellValueFactory(cellData -> cellData.getValue().winPercentageProperty().asObject());

        avgMoves = new TableColumn<>("Moves");
        avgMoves.setCellValueFactory(cellData -> cellData.getValue().avgMovesProperty().asObject());

        avgMoveDuration = new TableColumn<>("Move Duration");
        avgMoveDuration.setCellValueFactory(cellData -> cellData.getValue().avgMoveDurationProperty().asObject());

        table.setItems(new Leaderboard().loadLeaderboard());
        table.getColumns().addAll(name, totalGames, wins, losses, winsPercent, avgMoves, avgMoveDuration);
        for (TableColumn<Leaderboard.Player, ?> column : table.getColumns()) {
            column.setPrefWidth(120);
            column.setReorderable(false);
            column.setResizable(false);
        }

        backBtn = CreateHelper.createButton("Back", new String[]{"default-button", "red-button"});

        setTop(title);
        setAlignment(title, Pos.CENTER);
        setCenter(table);
        setBottom(backBtn);
    }

    private void layoutNodes() {
        setPadding(new Insets(10, 10, 10, 10));
        setAlignment(backBtn, Pos.CENTER);
    }

    // Package private getters
    public Button getBackBtn() {
        return backBtn;
    }

    public TableView<Leaderboard.Player> getTable() {
        return table;
    }

    public TableColumn<Leaderboard.Player, String> getName() {
        return name;
    }

    public TableColumn<Leaderboard.Player, Integer> getTotalGames() {
        return totalGames;
    }

    public TableColumn<Leaderboard.Player, Integer> getWins() {
        return wins;
    }

    public TableColumn<Leaderboard.Player, Integer> getLosses() {
        return losses;
    }

    public TableColumn<Leaderboard.Player, Float> getWinsPercent() {
        return winsPercent;
    }

    public TableColumn<Leaderboard.Player, Float> getAvgMoves() {
        return avgMoves;
    }

    public TableColumn<Leaderboard.Player, Float> getAvgMoveDuration() {
        return avgMoveDuration;
    }
}

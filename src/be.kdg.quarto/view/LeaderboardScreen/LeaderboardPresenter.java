package be.kdg.quarto.view.LeaderboardScreen;

import be.kdg.quarto.model.Leaderboard;
import be.kdg.quarto.view.StartScreen.StartPresenter;
import be.kdg.quarto.view.StartScreen.StartView;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.sql.SQLException;

public class LeaderboardPresenter {
    private final LeaderboardView view;
    private final Leaderboard leaderboard;

    private TableView<Leaderboard.Player> table;
    private TableColumn<Leaderboard.Player, String> name;
    private TableColumn<Leaderboard.Player, Integer> totalGames;
    private TableColumn<Leaderboard.Player, Integer> wins;
    private TableColumn<Leaderboard.Player, Integer> losses;
    private TableColumn<Leaderboard.Player, Float> winsPercent;
    private TableColumn<Leaderboard.Player, Float> avgMoves;
    private TableColumn<Leaderboard.Player, Float> avgMoveDuration;

    public LeaderboardPresenter(LeaderboardView view, Leaderboard leaderboard) {
        this.view = view;
        this.leaderboard = leaderboard;
        addEventHandlers();
        updateView();
    }

    private void updateView() {
        view.getScene().getRoot().setStyle("-fx-background-color: #fff4d5;");

        table = new TableView<>();

        TableColumn<Leaderboard.Player, Integer> order = new TableColumn<>("Nr");
        order.setPrefWidth(40);
        order.setCellValueFactory(cellData ->
                new ReadOnlyObjectWrapper<>(table.getItems().indexOf(cellData.getValue()) + 1));

        name = new TableColumn<>("Name");
        name.setPrefWidth(165);
        name.setCellValueFactory(cellData -> cellData.getValue().nameProperty());

        totalGames = new TableColumn<>("Total Games");
        totalGames.setPrefWidth(125);
        totalGames.setCellValueFactory(cellData -> cellData.getValue().totalGamesProperty().asObject());

        wins = new TableColumn<>("Wins");
        wins.setPrefWidth(85);
        wins.setCellValueFactory(cellData -> cellData.getValue().winsProperty().asObject());

        losses = new TableColumn<>("Losses");
        losses.setPrefWidth(85);
        losses.setCellValueFactory(cellData -> cellData.getValue().lossesProperty().asObject());

        winsPercent = new TableColumn<>("Wins Percent");
        winsPercent.setPrefWidth(125);
        winsPercent.setCellValueFactory(cellData -> cellData.getValue().winPercentageProperty().asObject());

        avgMoves = new TableColumn<>("Moves");
        avgMoves.setPrefWidth(95);
        avgMoves.setCellValueFactory(cellData -> cellData.getValue().avgMovesProperty().asObject());

        avgMoveDuration = new TableColumn<>("Move Duration");
        avgMoveDuration.setPrefWidth(135);
        avgMoveDuration.setCellValueFactory(cellData -> cellData.getValue().avgMoveDurationProperty().asObject());

        try {
            table.setItems(new Leaderboard().loadLeaderboard());
            table.getColumns().addAll(order, name, totalGames, wins, losses, winsPercent, avgMoves, avgMoveDuration);

            // disables the columns to be reordered and resized
            table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);
            table.setFixedCellSize(30);
            for (TableColumn<Leaderboard.Player, ?> column : table.getColumns()) {
                column.setReorderable(false);
                column.setResizable(false);
            }
            // disables the order column to be sorted
            order.setSortable(false);

            table.setItems(leaderboard.loadLeaderboard());
            view.setCenter(table);
        }
        catch (SQLException e){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Connection Error");
            alert.setHeaderText("You are currently offline");
            alert.setContentText("Please connect again!");
            alert.showAndWait();

                StartView startView = new StartView();
                view.getScene().setRoot(startView);
                new StartPresenter(startView);
           
        }
    }

    private void addEventHandlers() {
        view.getBackBtn().setOnAction(event -> {
            StartView startView = new StartView();
            view.getScene().setRoot(startView);
            new StartPresenter(startView);
        });
    }
}

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

    public LeaderboardPresenter(LeaderboardView view, Leaderboard leaderboard) {
        this.view = view;
        this.leaderboard = leaderboard;
        addEventHandlers();
        updateView();
    }

    private void updateView() {
        view.getScene().getRoot().setStyle("-fx-background-color: #fff4d5;");

        TableView<Leaderboard.Player> table = new TableView<>();
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);
//        table.setFixedCellSize(30);

        TableColumn<Leaderboard.Player, Integer> order = new TableColumn<>("Nr");
        order.setPrefWidth(40);
        order.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(table.getItems().indexOf(cellData.getValue()) + 1));
        order.setSortable(false);

        table.getColumns().addAll(
                order,
                createStringColumn("Name", 140, p -> p.nameProperty().get()),
                createIntegerColumn("Total Games", 140, p -> p.totalGamesProperty().get()),
                createIntegerColumn("Wins", 85, p -> p.winsProperty().get()),
                createIntegerColumn("Losses", 85, p -> p.lossesProperty().get()),
                createFloatColumn("Wins Percent", 140, p -> p.winPercentageProperty().get()),
                createFloatColumn("Moves", 85, p -> p.avgMovesProperty().get()),
                createFloatColumn("Move Duration", 140, p -> p.avgMoveDurationProperty().get())
        );

        for (TableColumn<Leaderboard.Player, ?> column : table.getColumns()) {
            column.setReorderable(false);
            column.setResizable(false);
        }

        try {
            table.setItems(leaderboard.loadLeaderboard());
            view.setCenter(table);
        } catch (SQLException e) {
            showOfflineAlert();
            switchToStartScreen();
        }
    }

    private void addEventHandlers() {
        view.getBackBtn().setOnAction(event -> switchToStartScreen());
    }

    private void switchToStartScreen() {
        StartView startView = new StartView();
        view.getScene().setRoot(startView);
        new StartPresenter(startView);
    }

    private void showOfflineAlert() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Connection Error");
        alert.setHeaderText("You are currently offline");
        alert.setContentText("Please connect again!");
        alert.showAndWait();
    }

    private TableColumn<Leaderboard.Player, String> createStringColumn(String title, double width, java.util.function.Function<Leaderboard.Player, String> mapper) {
        TableColumn<Leaderboard.Player, String> col = new TableColumn<>(title);
        col.setPrefWidth(width);
        col.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(mapper.apply(cellData.getValue())));
        return col;
    }

    private TableColumn<Leaderboard.Player, Integer> createIntegerColumn(String title, double width, java.util.function.ToIntFunction<Leaderboard.Player> mapper) {
        TableColumn<Leaderboard.Player, Integer> col = new TableColumn<>(title);
        col.setPrefWidth(width);
        col.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(mapper.applyAsInt(cellData.getValue())));
        return col;
    }

    private TableColumn<Leaderboard.Player, Float> createFloatColumn(String title, double width, java.util.function.ToDoubleFunction<Leaderboard.Player> mapper) {
        TableColumn<Leaderboard.Player, Float> col = new TableColumn<>(title);
        col.setPrefWidth(width);
        col.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>((float) mapper.applyAsDouble(cellData.getValue())));
        return col;
    }
}

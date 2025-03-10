package be.kdg.quarto.view.StartScreen;

import be.kdg.quarto.model.Board;
import be.kdg.quarto.model.Game;
import be.kdg.quarto.model.Statistics;
import be.kdg.quarto.view.BoardView.BoardPresenter;
import be.kdg.quarto.view.BoardView.BoardView;
import be.kdg.quarto.view.GameScreen.GamePresenter;
import be.kdg.quarto.view.GameScreen.GameView;
import be.kdg.quarto.view.StatisticsView.StatisticsPresenter;
import be.kdg.quarto.view.StatisticsView.StatisticsView;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Modality;
import javafx.stage.Stage;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class StartPresenter {

    private Game model;
    private StartView view;
    private BoardView boardView = new BoardView();


    public StartPresenter(Game model, StartView view) {
        this.model = model;
        this.view = view;
        new BoardPresenter(model, view.getBoard());
        addEventHandlers();
        updateView();

    }

    private void addEventHandlers() {
        view.getNewGame().setOnAction(event -> {
            GameView gameView = new GameView();
            view.getScene().setRoot(gameView);
            new GamePresenter(model, gameView);
        });


        view.getStatistics().setOnAction(event -> {
            StatisticsView statsView = new StatisticsView();
            Stage statsStage = new Stage();
            statsStage.initOwner(view.getScene().getWindow());
            statsStage.initModality(Modality.APPLICATION_MODAL);
            statsStage.setScene(new Scene(statsView));

            statsStage.setTitle("Statistics");
            statsStage.setWidth(400);
            statsStage.setHeight(400);
            statsStage.setResizable(false);

            // view.getScene().setRoot(statisticsView);
            new StatisticsPresenter(statsView, new Statistics());
            statsStage.show();
        });

        view.getQuit().setOnAction(event -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Quit");
            alert.setHeaderText(null);
            alert.setContentText("Are you sure you want to exit?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                System.exit(0);
            }
        });
    }

    private void updateView() {
        view.getScene().getRoot().setStyle("-fx-background-color: #fff4d5;");
    }
}

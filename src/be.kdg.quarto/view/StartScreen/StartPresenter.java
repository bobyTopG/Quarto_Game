package be.kdg.quarto.view.StartScreen;

import be.kdg.quarto.model.Leaderboard;
import be.kdg.quarto.model.Statistics;
import be.kdg.quarto.view.BoardView.BoardView;
import be.kdg.quarto.view.ChooseAIView.ChooseAIPresenter;
import be.kdg.quarto.view.ChooseAIView.ChooseAIView;
import be.kdg.quarto.view.LeaderboardScreen.LeaderboardPresenter;
import be.kdg.quarto.view.LeaderboardScreen.LeaderboardView;
import be.kdg.quarto.view.StatisticsScreen.StatisticsPresenter;
import be.kdg.quarto.view.StatisticsScreen.StatisticsView;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;


import java.util.Optional;

public class StartPresenter {

    private StartView view;
    private BoardView boardView = new BoardView();
    private final String pathToBoard = "/images/Example_Board.png";


    public StartPresenter(StartView view) {

        this.view = view;
        Image boardImage = new Image(pathToBoard);
        addEventHandlers();
        updateView();
        view.loadBoardImage(boardImage);


    }

    private void addEventHandlers() {
        view.getNewGame().setOnAction(event -> {


            ChooseAIView chooseAIView = new ChooseAIView();
            view.getScene().setRoot(chooseAIView);
            new ChooseAIPresenter(chooseAIView);
        });


        view.getStatistics().setOnAction(event -> {
            StatisticsView statsView = new StatisticsView();
            Stage statsStage = new Stage();
            statsStage.initOwner(view.getScene().getWindow());
            statsStage.initModality(Modality.APPLICATION_MODAL);
            Scene statsScene = new Scene(statsView);
            statsScene.getStylesheets().addAll(view.getScene().getStylesheets());
            statsStage.setScene(statsScene);

            statsStage.setTitle("Statistics");
            statsStage.setWidth(400);
            statsStage.setHeight(400);
            statsStage.setResizable(false);


            new StatisticsPresenter(statsView, new Statistics(1, 1));
            statsStage.show();
        });

        view.getLeaderboard().setOnAction(event -> {
            LeaderboardView leaderboardView = new LeaderboardView();
            view.getScene().setRoot(leaderboardView);
            new LeaderboardPresenter(leaderboardView, new Leaderboard());
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

package be.kdg.quarto.view.StartScreen;

import be.kdg.quarto.model.Board;
import be.kdg.quarto.model.Game;
import be.kdg.quarto.model.Statistics;
import be.kdg.quarto.view.BoardView.BoardPresenter;
import be.kdg.quarto.view.BoardView.BoardView;
import be.kdg.quarto.view.ChooseAIView.ChooseAIPresenter;
import be.kdg.quarto.view.ChooseAIView.ChooseAIView;
import be.kdg.quarto.view.GameScreen.GamePresenter;
import be.kdg.quarto.view.GameScreen.GameView;
import be.kdg.quarto.view.StatisticsView.StatisticsPresenter;
import be.kdg.quarto.view.StatisticsView.StatisticsView;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;


import java.util.ArrayList;
import java.util.List;

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
            //GameView gameView = new GameView();
            //view.getScene().setRoot(gameView);
            //new GamePresenter(model, gameView);

            ChooseAIView chooseAIView = new ChooseAIView();
            view.getScene().setRoot(chooseAIView);
            new ChooseAIPresenter(chooseAIView);
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
    }

    private void updateView() {
        view.getScene().getRoot().setStyle("-fx-background-color: #fff4d5;");
    }
}

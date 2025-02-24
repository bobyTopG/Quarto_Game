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


import java.util.ArrayList;
import java.util.List;

public class StartPresenter {

    private Game model;
    private StartView view;
    private BoardView boardView = new BoardView();


    public StartPresenter(Game model, StartView view) {
        this.model = model;
        this.view = view;
        new BoardPresenter(model,view.getBoard());
        addEventHandlers();
        updateView();

    }

    private void addEventHandlers() {
        view.getNewGame().setOnAction(event -> {
            GameView gameView = new GameView();
            view.getScene().setRoot(gameView);
            new GamePresenter(model,gameView);
        });


        view.getStatistics().setOnAction(event -> {
            StatisticsView statisticsView = new StatisticsView();
            view.getScene().setRoot(statisticsView);
            new StatisticsPresenter(statisticsView, new Statistics());
        });
    }

    private void updateView() {
        view.getScene().getRoot().setStyle("-fx-background-color: #fff4d5;");
    }
}

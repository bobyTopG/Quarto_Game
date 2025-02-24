package be.kdg.quarto.view.StartScreen;

import be.kdg.quarto.model.Board;
import be.kdg.quarto.model.Game;
import be.kdg.quarto.view.BoardView.BoardPresenter;
import be.kdg.quarto.view.BoardView.BoardView;
import be.kdg.quarto.view.GameScreen.GamePresenter;
import be.kdg.quarto.view.GameScreen.GameView;


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
    }

    private void updateView() {
        view.getScene().getRoot().setStyle("-fx-background-color: #fff4d5;");
    }
}

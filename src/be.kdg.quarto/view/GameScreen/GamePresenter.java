package be.kdg.quarto.view.GameScreen;

import be.kdg.quarto.model.Game;
import be.kdg.quarto.view.BoardView.BoardPresenter;

public class GamePresenter {

    private Game model;
    private GameView view;

    public GamePresenter(Game model, GameView view) {
        this.model = model;
        this.view = view;
        new BoardPresenter(model , view.getBoard());
        addEventHandlers();
        updateView();

    }

    private void addEventHandlers() {

    }

    private void updateView() {
        view.getScene().getRoot().setStyle("-fx-background-color: #fff4d5;");
    }

}

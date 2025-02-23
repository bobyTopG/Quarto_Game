package be.kdg.quarto.view.StartScreen;

import be.kdg.quarto.model.Board;
import be.kdg.quarto.model.Game;
import be.kdg.quarto.view.BoardView.BoardPresenter;
import be.kdg.quarto.view.BoardView.BoardView;


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
    }

    private void updateView() {

    }
}

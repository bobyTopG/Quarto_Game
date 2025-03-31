package be.kdg.quarto.view.GameScreen.BoardView;

import be.kdg.quarto.model.Game;

public class BoardPresenter {

    private Game model;
    private BoardView view;
    private PieceView pieceView;


    public BoardPresenter(Game model, BoardView view) {
        this.model = model;
        this.view = view;
        updateView();
        addEventHandlers();
    }

    private void addEventHandlers() {

    }


    private void updateView() {

    }
}

package be.kdg.quarto.view.BoardView;

import be.kdg.quarto.model.Board;
import be.kdg.quarto.model.Game;
import be.kdg.quarto.model.Piece;
import be.kdg.quarto.view.PieceView.PieceView;

import java.util.ArrayList;
import java.util.List;

public class BoardPresenter {

    private Game model;
    private BoardView view;

    public BoardPresenter(Game model, BoardView view) {
        this.model = model;
        this.view = view;
        addEventHandlers();
        updateView();

    }

    private void addEventHandlers() {
        for(SpaceView spaceView :view.getSpaceViews()) {


        }

    }

    private void updateView() {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                SpaceView spaceView = new SpaceView();
                view.getSpaceViews().add(spaceView);
                view.getSpaces().add(spaceView, i, j);
            }
        }


    }
}

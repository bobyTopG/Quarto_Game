package be.kdg.quarto.view.BoardView;

import be.kdg.quarto.model.Game;
import be.kdg.quarto.model.Piece;
import be.kdg.quarto.model.enums.Color;
import be.kdg.quarto.model.enums.Fill;
import be.kdg.quarto.model.enums.Height;
import be.kdg.quarto.model.enums.Shape;
import be.kdg.quarto.view.PieceView.PieceView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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

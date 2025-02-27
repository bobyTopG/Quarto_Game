package be.kdg.quarto.model;

import be.kdg.quarto.model.enums.Color;
import be.kdg.quarto.model.enums.Fill;
import be.kdg.quarto.model.enums.Height;
import be.kdg.quarto.model.enums.Shape;

import java.util.ArrayList;
import java.util.List;

public class Board {
    private static final int BOARD_SIZE = 16;
    private List<Piece> availablePieces;

    public Board() {
        this.availablePieces = new ArrayList<>(BOARD_SIZE);
        generateAllPieces();
    }

    private void generateAllPieces() {
        for (Color color : Color.values()) {
            for (Height height : Height.values()) {
                for (Fill fill : Fill.values()) {
                    for (Shape shape : Shape.values()) {
                        availablePieces.add(new Piece(color, height, fill, shape));
                    }
                }
            }
        }
    }

    public List<Piece> getAvailablePieces() {
        return new ArrayList<>(availablePieces);
    }

    public void removePiece(Piece piece) {
        availablePieces.remove(piece);
    }


}
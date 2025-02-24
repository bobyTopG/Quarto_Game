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
    private Piece selectedPiece;

    public Board() {
        this.availablePieces = generateAllPieces();
        this.selectedPiece = null;
    }

    private List<Piece> generateAllPieces() {
        List<Piece> pieces = new ArrayList<>();
        for (Color color : Color.values()) {
            for (Height height : Height.values()) {
                for (Fill fill : Fill.values()) {
                    for (Shape shape : Shape.values()) {
                        pieces.add(new Piece(color, height, fill, shape));
                    }
                }
            }
        }
        return pieces;
    }

    public List<Piece> getAvailablePieces() {
        return new ArrayList<>(availablePieces);
    }

    public void removePiece(Piece piece) {
        availablePieces.remove(piece);
    }

    public Piece getSelectedPiece() {
        return selectedPiece;
    }

    public void setSelectedPiece(Piece selectedPiece) {
        this.selectedPiece = selectedPiece;
    }

    public static int getBoardSize() {
        return BOARD_SIZE;
    }
}
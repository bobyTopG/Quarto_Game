package be.kdg.quarto.model;

import java.util.List;
import java.util.Random;

public class RandomPlayingStrategy implements PlayingStrategy {
    private final Board board;

    public RandomPlayingStrategy(Board board) {
        this.board = board;
    }

    @Override
    public Piece selectPiece() {
        return selectRandomPiece();
    }

    @Override
    public String getName() {
        return "S1";
    }


    private Piece selectRandomPiece() {
        List<Piece> availablePieces = board.getAvailablePieces();
        if (!availablePieces.isEmpty()) {
            Random rand = new Random();
            Piece selectedPiece = availablePieces.get(rand.nextInt(availablePieces.size()));
            board.removePiece(selectedPiece);
            return selectedPiece;
        }
        else return null;
    }
}

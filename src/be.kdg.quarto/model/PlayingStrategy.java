package be.kdg.quarto.model;

public interface PlayingStrategy {
    Tile selectPiece();
    Tile placePiece();
    String getName();

}

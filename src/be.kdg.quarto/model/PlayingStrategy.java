package be.kdg.quarto.model;

public interface PlayingStrategy {
    Tile selectTile();
    Piece selectPiece();
    String getName();

}

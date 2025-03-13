package be.kdg.quarto.model.strategy;

import be.kdg.quarto.model.Tile;

public interface PlayingStrategy {
    Tile selectPiece();
    Tile placePiece();
    String getName();

}

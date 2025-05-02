package be.kdg.quarto.model;

public interface PlayingStrategy {
    Tile selectTile() throws Exception;
    Piece selectPiece();
    String getName();
    void fillNecessaryData(GameSession gameSession);
    boolean isCallingQuarto();
}

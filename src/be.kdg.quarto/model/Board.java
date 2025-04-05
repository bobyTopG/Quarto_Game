package be.kdg.quarto.model;

import be.kdg.quarto.model.enums.Color;
import be.kdg.quarto.model.enums.Fill;
import be.kdg.quarto.model.enums.Size;
import be.kdg.quarto.model.enums.Shape;

import java.util.ArrayList;
import java.util.List;

public class Board {
    private static final int BOARD_SIZE = 16;
    private List<Tile> tiles;


    public Board() {
        this.tiles = new ArrayList<>(BOARD_SIZE);
    }

    public void generateAllPieces() {
        for (Color color : Color.values()) {
            for (Size size : Size.values()) {
                for (Fill fill : Fill.values()) {
                    for (Shape shape : Shape.values()) {
                        tiles.add(new Tile(new Piece(color, size, fill, shape)));
                    }
                }
            }
        }
    }

    public void createEmptyBoard() {
        for (int i = 0; i < BOARD_SIZE; i++) {
            tiles.add(new Tile());
        }
    }

    public List<Tile> getTiles() {
        return tiles;
    }
    public Tile findTile(Piece piece) {
        for (Tile tile : tiles) {
            if(tile.getPiece() == null)
                continue;
            if (tile.getPiece().equals(piece)) {
                return tile;
            }
        }
        return null;
    }

    public Tile findTile(int pos){
        return tiles.get(pos);
    }

    public boolean endMoveAIPossible() {
        // TODO: Implement logic to determine if AI can finish the game
        return false;
    }

    public boolean endMovePlayerPossible() {
        // TODO: Implement logic to check if player is about to win
        return false;
    }

    public boolean endWinningPositionAIPossible() {
        // TODO: Check if AI can force a win in a turn or two
        return false;
    }

    public boolean endWinningPositionPlayerPossible() {
        // TODO: Check if player can win in a few moves
        return false;
    }

    public void determineEndMove(Move move) {
        // TODO: Set the move that finishes the game for the AI
    }

    public void determineRandomMove(Move move) {
        // TODO: Set a random valid move on the board
    }

    public Tile getTile(int pos) {
        return findTile(pos);
    }

}
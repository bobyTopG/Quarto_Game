package be.kdg.quarto.model;

import be.kdg.quarto.model.enums.Color;
import be.kdg.quarto.model.enums.Fill;
import be.kdg.quarto.model.enums.Height;
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
            for (Height height : Height.values()) {
                for (Fill fill : Fill.values()) {
                    for (Shape shape : Shape.values()) {
                        tiles.add(new Tile(new Piece(color, height, fill, shape)));
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

}
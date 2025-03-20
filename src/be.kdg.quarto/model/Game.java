package be.kdg.quarto.model;

import be.kdg.quarto.model.enums.Color;
import be.kdg.quarto.model.enums.Height;
import be.kdg.quarto.model.enums.Shape;
import be.kdg.quarto.model.enums.Fill;

import java.util.ArrayList;

public class Game {

    private final Board tilesToSelect;
    private final Board tilesToPlace;
    private Tile currentTile = new Tile();
    private GameRules gameRules;


    public Game() {
        this.tilesToSelect = new Board();
        this.tilesToPlace = new Board();
        gameRules = new GameRules(tilesToPlace , new ArrayList<Move>());
        tilesToSelect.generateAllTiles();
        tilesToPlace.createEmptyTiles();
    }

    public Piece createPieceFromImageName(String path) {
        String filename = path.substring(path.indexOf("/pieces/") + 8, path.lastIndexOf("."));
        String[] parts = filename.split("_");

        if (parts.length != 4) throw new IllegalArgumentException("Invalid image name format: " + path);

        try {
            return new Piece(
                    Color.valueOf(parts[2].toUpperCase()),
                    Height.valueOf(parts[3].toUpperCase()),
                    Fill.valueOf(parts[0].toUpperCase()),
                    Shape.valueOf(parts[1].toUpperCase())
            );
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid image name.");
            return null;
        }
    }

    public GameRules getGameRules() {
        return gameRules;
    }

    public Board getPlacedTiles() {
        return tilesToPlace;
    }

    public Board getTilesToSelect() {
        return tilesToSelect;
    }

    public Tile getCurrentTile() {
        return currentTile;
    }

    public void setCurrentTile(Tile selectedPiece) {
        this.currentTile = selectedPiece;
    }
}
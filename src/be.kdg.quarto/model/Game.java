package be.kdg.quarto.model;

import be.kdg.quarto.model.enums.AiLevel;
import be.kdg.quarto.model.enums.Color;
import be.kdg.quarto.model.enums.Height;
import be.kdg.quarto.model.enums.Shape;
import be.kdg.quarto.model.enums.Fill;

import java.util.List;
import java.util.Random;

public class Game {

    private final Ai ai;
    private final Human human;
    private Player currentPlayer;
    private Board tilesToSelect, tilesToPlace;
    private Tile curentTile = new Tile();


    public Game() {
        this(new Human("Bob", "secretPassword"), new Ai("Open Ai", AiLevel.HARD, null, "Description"));

    }

    public Game(Human human, Ai ai) {
        this.human = human;
        this.ai = ai;
        this.tilesToPlace = new Board();
        this.tilesToSelect = new Board();
        tilesToSelect.generateAllTiles();
        tilesToPlace.createEmptyTiles();
        this.currentPlayer = ai;
        assert this.ai != null;
        this.ai.setStrategy(new RandomPlayingStrategy(tilesToSelect, tilesToSelect,true));
        aiTurn();
    }




    public Piece createPieceFromImageName(String path) {
        String filename = path.substring(path.indexOf("/images/") + 8, path.lastIndexOf("."));
        String[] parts = filename.split("_");

        if (parts.length != 4) throw new IllegalArgumentException("Invalid image name format: " + path);

        try {
            return new Piece(Color.valueOf(parts[2].toUpperCase()), Height.valueOf(parts[3].toUpperCase()), Fill.valueOf(parts[0].toUpperCase()), Shape.valueOf(parts[1].toUpperCase()));
        } catch (Exception e) {
            System.out.println("Invalid enum value in image name.");
            return null;
        }
    }


    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public void switchTurns() {
        currentPlayer = (currentPlayer == human) ? ai : human;
        if (currentPlayer == ai) {
            aiTurn();
        }
    }

    private void aiTurn() {
        if(ai.getStrategy().isToPick()) {
            getCurntTile().setPiece(ai.getStrategy().selectPiece().getPiece());
            getTilesToSelect().getTiles().get(getTilesToSelect().getTiles().indexOf(getCurntTile())).setPiece(null);
        }
        else {
            getPlacedTiles().getTiles().set(ai.getStrategy().getPlacePiece().getTiles().indexOf(getCurntTile()),getCurntTile());
        }
    }

    public Board getPlacedTiles() {
        return tilesToPlace;
    }

    public Board getTilesToSelect() {
        return tilesToSelect;
    }


    public Player getHuman() {
        return human;
    }


    public Tile getCurntTile() {
        return curentTile;
    }

    public void setCurrentTile(Tile selectedPiece) {
        this.curentTile = selectedPiece;
    }


}
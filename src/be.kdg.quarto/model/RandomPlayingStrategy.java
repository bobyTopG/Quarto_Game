package be.kdg.quarto.model;

import java.util.List;
import java.util.Random;

public class RandomPlayingStrategy implements PlayingStrategy {
    //selectPiece = pieces that are not on the board
    // placePiece = the board
    private Board selectPiece, placePiece;

    public RandomPlayingStrategy(Board selectPiece, Board placePiece) {
        this.placePiece = placePiece;
        this.selectPiece = selectPiece;
    }


    @Override
    public Piece selectPiece() {
            return selectRandomPiece().getPiece();
    }
    @Override
    public Tile selectTile() {
        return getRandomFreeTile();
    }
    @Override
    public String getName() {
        return "S1";
    }

    @Override
    public boolean isCallingQuarto() {
        int rand = new Random().nextInt(1);
        return rand == 0;
    }


    private Tile selectRandomPiece() {
        List<Tile> availableTiles = selectPiece.getTiles().stream()
                .filter(tile -> tile.getPiece() != null)
                .toList();

        if (availableTiles.isEmpty()) {
            return null;
        }
        return availableTiles.get(new Random().nextInt(availableTiles.size()));
    }



    public Tile getRandomFreeTile() {
        List<Tile> emptyTiles = placePiece.getTiles().stream()
                .filter(tile -> tile.getPiece() == null)
                .toList();

        if (emptyTiles.isEmpty()) {
            return null;
        }
        return emptyTiles.get(new Random().nextInt(emptyTiles.size()));
    }

}


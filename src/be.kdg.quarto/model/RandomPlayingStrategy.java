package be.kdg.quarto.model;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class RandomPlayingStrategy implements PlayingStrategy {
    private Board selectPiece, placePiece;

    public RandomPlayingStrategy(Board selectPiece, Board placePiece) {
        this.placePiece = placePiece;
        this.selectPiece = selectPiece;
    }

    @Override
    public Tile selectPiece() {
        return selectRandomPiece();
    }
    @Override
    public Tile placePiece() {
        return placeRandomPiece();
    }
    @Override
    public String getName() {
        return "S1";
    }




    private Tile selectRandomPiece() {
        List<Tile> availableTiles = selectPiece.getTiles().stream()
                .filter(tile -> tile.getPiece() != null)
                .collect(Collectors.toList());

        if (availableTiles.isEmpty()) {
            return null;
        }
        return availableTiles.get(new Random().nextInt(availableTiles.size()));
    }



    public Tile placeRandomPiece() {
        List<Tile> emptyTiles = placePiece.getTiles().stream()
                .filter(tile -> tile.getPiece() == null)
                .collect(Collectors.toList());

        if (emptyTiles.isEmpty()) {
            return null;
        }
        return emptyTiles.get(new Random().nextInt(emptyTiles.size()));
    }

}


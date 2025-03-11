package be.kdg.quarto.model;

import java.util.Random;

public class RandomPlayingStrategy implements PlayingStrategy {
    private  Board selectPiece ,placePiece;
    private boolean toPick;

    public boolean isToPick() {
        return toPick;
    }

    public Board getPlacePiece() {
        return placePiece;
    }

    public RandomPlayingStrategy(Board selectPiece , Board placePiece , boolean toPick) {
        this.placePiece = placePiece;
        this.selectPiece = selectPiece;
        this.toPick = toPick;
    }

    @Override
    public Tile selectPiece() {
       return selectRandomPiece();
    }

    @Override
    public String getName() {
        return "S1";
    }


    private Tile selectRandomPiece() {
        Random rand = new Random();
        if (toPick) {
           return selectPiece.getTiles().get(rand.nextInt(selectPiece.getTiles().size()));
        }
        else {
            return placePiece.getTiles().get(rand.nextInt(placePiece.getTiles().size()));
        }
        //Pick

    }
}

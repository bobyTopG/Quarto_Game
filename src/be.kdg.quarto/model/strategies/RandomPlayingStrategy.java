package be.kdg.quarto.model.strategies;

import be.kdg.quarto.model.Board;
import be.kdg.quarto.model.Tile;
import be.kdg.quarto.model.GameSession;
import be.kdg.quarto.model.Piece;
import be.kdg.quarto.model.PlayingStrategy;

import java.util.List;
import java.util.Random;

public class RandomPlayingStrategy implements PlayingStrategy {
    //selectPiece = pieces that are not on the board
    // placePiece = the board
    private Board selectPiece, board;


    public RandomPlayingStrategy() {

    }
    public void fillNecessaryData(GameSession gameSession) {
        this.selectPiece = gameSession.getGame().getPiecesToSelect();
        this.board = gameSession.getGame().getBoard();

        // Safeguard: ensure board has tiles
        if (this.board.getTiles().isEmpty()) {
            this.board.createEmptyBoard();
        }
    }


    @Override
    public Piece selectPiece() {
            return selectRandomPiece();
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
        int rand = new Random().nextInt(2);
        return rand == 0;
    }


    private Piece selectRandomPiece() {
        List<Tile> availableTiles = selectPiece.getTiles().stream()
                .filter(tile -> tile.getPiece() != null)
                .toList();

        if (availableTiles.isEmpty()) {
            return null;
        }
        return availableTiles.get(new Random().nextInt(availableTiles.size())).getPiece();
    }



    public Tile getRandomFreeTile() {
        List<Tile> emptyTiles = board.getTiles().stream()
                .filter(tile -> tile.getPiece() == null)
                .toList();

        if (emptyTiles.isEmpty()) {
            return null;
        }
        return emptyTiles.get(new Random().nextInt(emptyTiles.size()));
    }

    public void setSelectPiece(Board selectPiece) {
        this.selectPiece = selectPiece;
    }

    public void setBoard(Board board) {
        this.board = board;
    }
}


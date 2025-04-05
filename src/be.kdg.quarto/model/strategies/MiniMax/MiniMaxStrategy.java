package be.kdg.quarto.model.strategies.MiniMax;

import be.kdg.quarto.model.GameRules;
import be.kdg.quarto.model.GameSession;
import be.kdg.quarto.model.Board;
import be.kdg.quarto.model.Tile;
import be.kdg.quarto.model.Piece;
import be.kdg.quarto.model.Player;
import be.kdg.quarto.model.PlayingStrategy;

import java.util.*;

public class MiniMaxStrategy implements PlayingStrategy {
    private GameSession gameSession;
    private Player aiPlayer;

    public void fillNecessaryData(GameSession gameSession) {
        this.gameSession = gameSession;
        this.aiPlayer = gameSession.getOpponent();
    }

    @Override
    public String getName() {
        return "Improved MiniMax";
    }

    @Override
    public boolean isCallingQuarto() {
        return true;
    }

    @Override
    public Tile selectTile() {
        Board board = gameSession.getGame().getBoard();
        Piece selected = gameSession.getGame().getSelectedPiece();
        Tile best = null;

        for (Tile tile : board.getTiles()) {
            if (tile.getPiece() == null) {
                tile.setPiece(selected);
                GameRules rules = new GameRules(board, List.of()); // Dummy move list
                if (rules.checkPureWin()) {
                    tile.setPiece(null);
                    return tile; // Win now
                }
                tile.setPiece(null);
            }
        }

        for (Tile tile : board.getTiles()) {
            if (tile.getPiece() == null) {
                return tile; // fallback
            }
        }

        return null;
    }

    @Override
    public Piece selectPiece() {
        List<Tile> piecesLeft = gameSession.getGame().getPiecesToSelect().getTiles();
        Board board = gameSession.getGame().getBoard();

        for (Tile pieceTile : piecesLeft) {
            Piece piece = pieceTile.getPiece();
            if (piece == null) continue;

            boolean dangerous = false;
            for (Tile tile : board.getTiles()) {
                if (tile.getPiece() != null) continue;

                tile.setPiece(piece);
                GameRules rules = new GameRules(board, List.of());
                if (rules.checkPureWin()) {
                    dangerous = true;
                }
                tile.setPiece(null);

                if (dangerous) break;
            }

            if (!dangerous) {
                return piece;
            }
        }

        // No safe piece found, give random
        return piecesLeft.stream().filter(t -> t.getPiece() != null).findAny().map(Tile::getPiece).orElse(null);
    }
}
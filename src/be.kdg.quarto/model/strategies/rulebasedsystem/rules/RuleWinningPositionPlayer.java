package be.kdg.quarto.model.strategies.rulebasedsystem.rules;

import be.kdg.quarto.model.Game;
import be.kdg.quarto.model.Move;
import be.kdg.quarto.model.Piece;
import be.kdg.quarto.model.Tile;
import be.kdg.quarto.model.strategies.rulebasedsystem.facts.FactValues;
import be.kdg.quarto.model.strategies.rulebasedsystem.facts.FactsHandler;

import java.util.List;

public class RuleWinningPositionPlayer extends Rule {
    @Override
    public boolean conditionRule(FactsHandler facts) {
        return facts.factAvailable(FactValues.WINNINGPOSITIONPLAYER);
    }

    @Override
    public boolean actionRule(FactsHandler facts, Game game, Move move) {
        List<Piece> availablePieces = game.getPiecesToSelect().getTiles().stream()
                .filter(tile -> tile.getPiece() != null)
                .map(Tile::getPiece)
                .toList();

        for (int i = 0; i < game.getBoard().getTiles().size(); i++) {
            Tile tile = game.getBoard().getTile(i);
            if (tile.isEmpty()) {
                for (Piece piece : availablePieces) {
                    tile.setPiece(piece); // Temporarily place each available piece
                    boolean causesWin = game.getBoard().wouldCauseWin(i);
                    tile.setPiece(null); // Undo

                    if (causesWin) {
                        // If placing any available piece here leads to a win, block this tile
                        move.setPosition(i);
                        // Choose a safe piece for opponent
                        Piece safePiece = findSafePiece(game, availablePieces);
                        move.setPiece(safePiece);
                        return true;
                    }
                }
            }
        }

        return false; // No blocking move found
    }
    private Piece findSafePiece(Game game, List<Piece> availablePieces) {
        for (Piece piece : availablePieces) {
            if (!wouldPieceCauseImmediateWin(game, piece)) {
                return piece;
            }
        }
        return selectRandomPiece(availablePieces);
    }

    private boolean wouldPieceCauseImmediateWin(Game game, Piece piece) {
        for (int i = 0; i < game.getBoard().getTiles().size(); i++) {
            Tile tile = game.getBoard().getTile(i);
            if (tile.isEmpty()) {
                tile.setPiece(piece);
                boolean causesWin = game.getBoard().wouldCauseWin(i);
                tile.setPiece(null); // Undo
                if (causesWin) {
                    return true;
                }
            }
        }
        return false;
    }

    private Piece selectRandomPiece(List<Piece> availablePieces) {
        if (!availablePieces.isEmpty()) {
            int randomIndex = (int) (Math.random() * availablePieces.size());
            return availablePieces.get(randomIndex);
        }
        return null;
    }
}
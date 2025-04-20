package be.kdg.quarto.rulebasedsystem.rules;

import be.kdg.quarto.model.Game;
import be.kdg.quarto.model.Move;
import be.kdg.quarto.model.Piece;
import be.kdg.quarto.model.Tile;
import be.kdg.quarto.rulebasedsystem.facts.FactValues;
import be.kdg.quarto.rulebasedsystem.facts.FactsHandler;

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

        game.getBoard().determineBlockWinningPositionMove(move, availablePieces);
        return true;
    }
}
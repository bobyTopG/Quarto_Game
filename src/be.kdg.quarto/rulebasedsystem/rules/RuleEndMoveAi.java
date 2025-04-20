package be.kdg.quarto.rulebasedsystem.rules;

import be.kdg.quarto.model.Game;
import be.kdg.quarto.model.Move;
import be.kdg.quarto.model.Tile;
import be.kdg.quarto.rulebasedsystem.facts.FactValues;
import be.kdg.quarto.rulebasedsystem.facts.FactsHandler;

public class RuleEndMoveAi extends Rule {
    @Override
    public boolean conditionRule(FactsHandler facts) {
        return facts.factAvailable(FactValues.ENDMOVEAI);
    }


    @Override
    public boolean actionRule(FactsHandler facts, Game game, Move move) {
        for (int i = 0; i < game.getBoard().getTiles().size(); i++) {
            Tile tile = game.getBoard().getTile(i);
            if (tile.isEmpty()) {
                tile.setPiece(game.getSelectedPiece());
                boolean wouldWin = game.getBoard().wouldCauseWin(i);
                tile.setPiece(null); // Undo

                if (wouldWin) {
                    move.setPosition(i);
                    return true;
                }
            }
        }

        return false;
    }
}
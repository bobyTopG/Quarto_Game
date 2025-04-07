package be.kdg.quarto.rulebasedsystem.rules;

import be.kdg.quarto.model.Board;
import be.kdg.quarto.model.Move;
import be.kdg.quarto.rulebasedsystem.facts.FactsHandler;

public class RuleDefaultFallback extends Rule {
    @Override
    public boolean conditionRule(FactsHandler facts) {
        return false;
    }

    @Override
    public boolean actionRule(FactsHandler facts, Board board, Move move) {
        for (int i = 0; i < board.getTiles().size(); i++) {
            if (board.getTile(i).isEmpty()) {
                move.setPosition(i);
                return true;
            }
        }
        return false;
    }
}

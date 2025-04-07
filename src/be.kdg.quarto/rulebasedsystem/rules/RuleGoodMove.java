package be.kdg.quarto.rulebasedsystem.rules;

import be.kdg.quarto.model.Board;
import be.kdg.quarto.model.Move;
import be.kdg.quarto.rulebasedsystem.facts.FactsHandler;

public class RuleGoodMove extends Rule {
    @Override
    public boolean conditionRule(FactsHandler facts) {
        return false;
    }

    @Override
    public boolean actionRule(FactsHandler facts, Board board, Move move) {
        int[] preferred = {5, 6, 9, 10, 0, 3, 12, 15};

        for (int pos : preferred) {
            if (board.getTile(pos).isEmpty()) {
                move.setPosition(pos);
                return true;
            }
        }

        // If none of the preferred tiles are available, do nothing
        return false;
    }
}

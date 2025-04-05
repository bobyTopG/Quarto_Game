package be.kdg.quarto.rulebasedsystem.rules;

import be.kdg.quarto.model.Board;
import be.kdg.quarto.model.Move;
import be.kdg.quarto.rulebasedsystem.facts.FactsHandler;

public class RuleEndMoveAi extends Rule{
    @Override
    public boolean conditionRule(FactsHandler facts) {
        return facts.factAvailable(be.kdg.quarto.rulebasedsystem.facts.FactValues.ENDMOVEAI);
    }

    @Override
    public boolean actionRule(FactsHandler facts, Board board, Move move) {
        board.determineEndMove(move);
        return true;
    }
}

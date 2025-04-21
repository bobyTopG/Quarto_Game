package be.kdg.quarto.rulebasedsystem.rules;

import be.kdg.quarto.model.Game;
import be.kdg.quarto.model.Move;
import be.kdg.quarto.rulebasedsystem.facts.FactValues;
import be.kdg.quarto.rulebasedsystem.facts.FactsHandler;

public class RuleWinningPositionAi extends Rule {
    @Override
    public boolean conditionRule(FactsHandler facts) {
        return facts.factAvailable(FactValues.WINNINGPOSITIONAI);
    }

    @Override
    public boolean actionRule(FactsHandler facts, Game game, Move move) {
        move.setWarningMessage("Watch out! Risky piece!\uD83D\uDEA8");
        return true;
    }
}

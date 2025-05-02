package be.kdg.quarto.model.strategies.rulebasedsystem.rules;

import be.kdg.quarto.model.Game;
import be.kdg.quarto.model.Move;
import be.kdg.quarto.model.strategies.rulebasedsystem.facts.FactValues;
import be.kdg.quarto.model.strategies.rulebasedsystem.facts.FactsHandler;

public class EndMovePlayer extends Rule{

    @Override
    public boolean conditionRule(FactsHandler facts) {
        return facts.factAvailable(FactValues.ENDMOVEPLAYER);
    }

    @Override
    public boolean actionRule(FactsHandler facts, Game game, Move move) {
        move.setWarningMessage("You can win!");
        return true;
    }
}

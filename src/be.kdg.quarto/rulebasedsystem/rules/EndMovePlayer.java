package be.kdg.quarto.rulebasedsystem.rules;

import be.kdg.quarto.model.Game;
import be.kdg.quarto.model.Move;
import be.kdg.quarto.rulebasedsystem.facts.FactValues;
import be.kdg.quarto.rulebasedsystem.facts.FactsHandler;

public class EndMovePlayer extends Rule{

    @Override
    public boolean conditionRule(FactsHandler facts) {
        return facts.factAvailable(FactValues.ENDMOVEPLAYER);
    }

    @Override
    public boolean actionRule(FactsHandler facts, Game game, Move move) {
        move.setWarningMessage("You can win \uD83D\uDD25");
        return true;
    }
}

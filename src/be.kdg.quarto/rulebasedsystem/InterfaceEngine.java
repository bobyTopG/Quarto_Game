package be.kdg.quarto.rulebasedsystem;

import be.kdg.quarto.model.Board;
import be.kdg.quarto.model.Move;
import be.kdg.quarto.rulebasedsystem.facts.FactsHandler;
import be.kdg.quarto.rulebasedsystem.facts.FactValues;
import be.kdg.quarto.rulebasedsystem.rules.RulesHandler;

public class InterfaceEngine {
    private boolean ruleFired;
    private final FactsHandler currentFacts = new FactsHandler();
    private final RulesHandler currentRules = new RulesHandler();

    public void determineFacts(Board board) {
        currentFacts.resetFacts();
        currentFacts.setFactsEvolved(false);

        if (board.endMoveAIPossible()) {
            currentFacts.addFact(FactValues.ENDMOVEAI);
        }
        if (board.endMovePlayerPossible()) {
            currentFacts.addFact(FactValues.ENDMOVEPLAYER);
        }
        if (board.endWinningPositionAIPossible()) {
            currentFacts.addFact(FactValues.WINNINGPOSITIONAI);
        }
        if (board.endWinningPositionPlayerPossible()) {
            currentFacts.addFact(FactValues.WINNINGPOSITIONPLAYER);
        }
    }

    public void applyRules(Board board, Move move) {
        if (currentFacts.factsObserved()) {
            ruleFired = false;
            int i;
            do {
                currentFacts.setFactsEvolved(false);
                i = 0;
                while (i < currentRules.numberOfRules() && !ruleFired && !currentFacts.factsChanged()) {
                    if (currentRules.checkConditionRule(i, currentFacts)) {
                        ruleFired = currentRules.fireActionRule(i, currentFacts, board, move);
                    }
                    i++;
                }
            } while (i < currentRules.numberOfRules() && !ruleFired);
        }

        if (!ruleFired) {
            System.out.println("No rules were fired!!");
            board.determineRandomMove(move);
        }
    }
}

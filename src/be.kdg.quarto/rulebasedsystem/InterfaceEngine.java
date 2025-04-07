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

            for (int attempt = 0; attempt < currentRules.numberOfRules(); attempt++) {
                currentFacts.setFactsEvolved(false);

                for (int i = 0; i < currentRules.numberOfRules(); i++) {
                    if (!ruleFired) {
                        if (currentRules.checkConditionRule(i, currentFacts)) {
                            System.out.println("Firing rule: " + currentRules.getRuleName(i));
                            ruleFired = currentRules.fireActionRule(i, currentFacts, board, move);
                            if (ruleFired) break;
                        }
                    }
                }

                if (ruleFired || currentFacts.factsChanged()) break;
            }
        }

        if (!ruleFired) {
            board.determineRandomMove(move);
        }
    }
}

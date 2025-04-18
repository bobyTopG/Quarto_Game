package be.kdg.quarto.rulebasedsystem;

import be.kdg.quarto.model.Board;
import be.kdg.quarto.model.GameSession;
import be.kdg.quarto.model.Move;
import be.kdg.quarto.rulebasedsystem.facts.FactsHandler;
import be.kdg.quarto.rulebasedsystem.facts.FactValues;
import be.kdg.quarto.rulebasedsystem.rules.RulesHandler;

public class InterfaceEngine {
    private boolean ruleFired;
    private final FactsHandler currentFacts = new FactsHandler();
    private final RulesHandler currentRules = new RulesHandler();


    public void determineFacts(GameSession gameSession) {
        currentFacts.resetFacts();
        currentFacts.setFactsEvolved(false);

        Board board = gameSession.getGame().getBoard();
        boolean isAITurn = gameSession.getCurrentPlayer() == gameSession.getOpponent();


        if (board.isWinningPositionPossible()) {
            currentFacts.addFact(isAITurn ? FactValues.WINNINGPOSITIONAI : FactValues.WINNINGPOSITIONPLAYER);
        }
        if (board.isWinningMovePossible()) {
            currentFacts.addFact(isAITurn ? FactValues.ENDMOVEAI : FactValues.ENDMOVEPLAYER);
        }

    }

    public void applyRules(Board board, Move move) {
        ruleFired = false;

        if (currentFacts.factsObserved()) {
            for (int attempt = 0; attempt < currentRules.numberOfRules(); attempt++) {
                currentFacts.setFactsEvolved(false);

                for (int i = 0; i < currentRules.numberOfRules(); i++) {
                    if (!ruleFired && currentRules.checkConditionRule(i, currentFacts)) {
                        System.out.println("Firing rule: " + currentRules.getRuleName(i));
                        ruleFired = currentRules.fireActionRule(i, currentFacts, board, move);
                        if (ruleFired) break;
                    }
                }

                if (ruleFired || currentFacts.factsChanged()) break;
            }
        }

        if (!ruleFired) {
            System.out.println("⚠️ No rule fired. Falling back to random move.");
            board.determineRandomMove(move);
        }
    }
}
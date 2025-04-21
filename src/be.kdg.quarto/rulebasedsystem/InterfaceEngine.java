package be.kdg.quarto.rulebasedsystem;

import be.kdg.quarto.model.Board;
import be.kdg.quarto.model.Game;
import be.kdg.quarto.model.GameSession;
import be.kdg.quarto.model.Move;
import be.kdg.quarto.model.Piece;
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
        Piece selectedPiece = gameSession.getGame().getSelectedPiece();


        if (selectedPiece != null && board.isWinningMovePossible(selectedPiece)) {
            currentFacts.addFact(isAITurn ? FactValues.ENDMOVEAI : FactValues.ENDMOVEPLAYER);
        }

        else if (selectedPiece == null && board.isWinningPositionPossible()) {
            currentFacts.addFact(isAITurn ? FactValues.WINNINGPOSITIONPLAYER : FactValues.WINNINGPOSITIONAI);
        }
        else {
            currentFacts.addFact(FactValues.GOODMOVE);
        }
    }

    public void applyRules(Game game, Move move) {
        ruleFired = false;

        if (currentFacts.factsObserved()) {
            for (int attempt = 0; attempt < currentRules.numberOfRules(); attempt++) {
                currentFacts.setFactsEvolved(false);

                for (int i = 0; i < currentRules.numberOfRules(); i++) {
                    if (!ruleFired && currentRules.checkConditionRule(i, currentFacts)) {
                        ruleFired = currentRules.fireActionRule(i, currentFacts, game, move);
                        if (ruleFired)
                            break;
                    }
                }
                if (ruleFired) {
                    break;
                }
            }
        }
    }
}
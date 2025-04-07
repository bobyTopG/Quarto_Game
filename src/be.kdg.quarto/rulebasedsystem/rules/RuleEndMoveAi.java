package be.kdg.quarto.rulebasedsystem.rules;

import be.kdg.quarto.model.Board;
import be.kdg.quarto.model.Move;
import be.kdg.quarto.rulebasedsystem.facts.FactsHandler;
import be.kdg.quarto.model.Piece;

import be.kdg.quarto.model.GameRules;
import be.kdg.quarto.model.enums.*;

import java.util.List;

public class RuleEndMoveAi extends Rule{
    @Override
    public boolean conditionRule(FactsHandler facts) {
        return facts.factAvailable(be.kdg.quarto.rulebasedsystem.facts.FactValues.ENDMOVEAI);
    }

    @Override
    public boolean actionRule(FactsHandler facts, Board board, Move move) {
        for (int i = 0; i < board.getTiles().size(); i++) {
            if (board.getTile(i).isEmpty()) {
                board.getTile(i).setPiece(new Piece(Color.BLACK, Size.SMALL, Fill.FULL, Shape.SQUARE)); // Simulated AI piece
                GameRules rules = new GameRules(board, List.of());
                if (rules.checkPureWin()) {
                    board.getTile(i).removePiece();
                    move.setPosition(i);
                    return true;
                }
                board.getTile(i).removePiece();
            }
        }
        return false;
    }
}

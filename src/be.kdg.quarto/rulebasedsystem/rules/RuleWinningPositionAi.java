package be.kdg.quarto.rulebasedsystem.rules;


import be.kdg.quarto.model.*;
import be.kdg.quarto.rulebasedsystem.facts.FactsHandler;
import be.kdg.quarto.model.enums.*;

import java.util.List;

public class RuleWinningPositionAi extends Rule {
    @Override
    public boolean conditionRule(FactsHandler facts) {
        return false;
    }

    @Override
    public boolean actionRule(FactsHandler facts, Board board, Move move) {
        for (int i = 0; i < board.getTiles().size(); i++) {
            if (board.getTile(i).isEmpty()) {
                for (Tile tile : board.getTiles()) {
                    Piece testPiece = tile.getPiece();
                    if (testPiece == null) continue;

                    board.getTile(i).setPiece(testPiece);
                    GameRules rules = new GameRules(board, List.of());
                    if (rules.checkPureWin()) {
                        board.getTile(i).removePiece();
                        move.setPosition(i);
                        return true;
                    }
                    board.getTile(i).removePiece();
                }
            }
        }
        return false;
    }
}

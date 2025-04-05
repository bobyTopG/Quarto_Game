package be.kdg.quarto.model.strategies;

import be.kdg.quarto.model.*;
import be.kdg.quarto.rulebasedsystem.InterfaceEngine;

import java.util.Date;

public class RuleBasedStrategy implements PlayingStrategy {
    private final InterfaceEngine engine;
    private Move lastMove;
    private GameSession game;

    public RuleBasedStrategy() {
        this.engine = new InterfaceEngine();
    }

    @Override
    public Tile selectTile() {
        Move move = new Move();
        engine.determineFacts(game.getGame().getBoard());
        engine.applyRules(game.getGame().getBoard(), move);
        this.lastMove = move;
        return game.getGame().getBoard().getTile(move.getPosition());
    }

    @Override
    public Piece selectPiece() {
        return this.lastMove != null ? this.lastMove.getPiece() : null;
    }

    @Override
    public String getName() {
        return "";
    }

    @Override
    public void fillNecessaryData(GameSession gameSession) {
        this.game = gameSession;
    }

    @Override
    public boolean isCallingQuarto() {
        return false;
    }
}

package be.kdg.quarto.model.strategies;

import be.kdg.quarto.model.GameSession;
import be.kdg.quarto.model.Move;
import be.kdg.quarto.model.Piece;
import be.kdg.quarto.model.PlayingStrategy;
import be.kdg.quarto.model.Tile;
import be.kdg.quarto.rulebasedsystem.InterfaceEngine;

import java.util.List;

public class RuleBasedStrategy implements PlayingStrategy {
    private final InterfaceEngine engine;
    private GameSession game;

    public RuleBasedStrategy() {
        this.engine = new InterfaceEngine();
    }

    @Override
    public Tile selectTile() {
        Move move = new Move();
        try {
            engine.determineFacts(game);
            engine.applyRules(game.getGame(), move);

            move.setPlayer(game.getCurrentPlayer());
            move.setPiece(game.getGame().getSelectedPiece());
            move.setSelectedPiece(game.getGame().getSelectedPiece());
            move.setMoveNumber(game.getGame().getCurrentMove().getMoveNumber());
            move.setStartTime(new java.util.Date());
            game.getGame().getGameRules().checkWin();


        } catch (Exception e) {
            System.out.println("Rule engine error during tile selection: " + e.getMessage());
        }
        int pos = move.getPosition();
        if (pos >= 0 && pos < game.getGame().getBoard().getTiles().size()) {
            Tile tile = game.getGame().getBoard().getTile(pos);
            if (tile.isEmpty()) return tile;
        }

        return null;
    }

    @Override
    public Piece selectPiece() {
        Move move;
        try {
            move = new Move();
            engine.determineFacts(game);
            engine.applyRules(game.getGame(), move);
        } catch (Exception e) {
            move = new Move();
        }

        Piece selected = move.getPiece();
        if (selected != null) return selected;


        List<Tile> piecesLeft = game.getGame().getPiecesToSelect().getTiles().stream()
                .filter(t -> t.getPiece() != null)
                .toList();

        // Random fallback
        return piecesLeft.stream().findAny().map(Tile::getPiece).orElse(null);
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

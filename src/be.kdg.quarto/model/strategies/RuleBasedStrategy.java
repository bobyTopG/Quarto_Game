package be.kdg.quarto.model.strategies;

import be.kdg.quarto.model.GameSession;
import be.kdg.quarto.model.Move;
import be.kdg.quarto.model.Piece;
import be.kdg.quarto.model.PlayingStrategy;
import be.kdg.quarto.model.Tile;
import be.kdg.quarto.model.strategies.rulebasedsystem.InterfaceEngine;

import java.util.List;

public class RuleBasedStrategy implements PlayingStrategy {
    private final InterfaceEngine engine;
    private GameSession game;

    /**
     * Constructs a RuleBasedStrategy using an internal inference engine.
     */
    public RuleBasedStrategy() {
        this.engine = new InterfaceEngine();
    }

    /**
     * Selects a tile based on facts and rules processed by the inference engine.
     * If the inference engine fails or produces an invalid move, returns null.
     *
     * @return a Tile determined by the rule engine, or null if selection fails
     * @throws Exception if rule engine processing encounters an error
     */

    @Override
    public Tile selectTile() throws Exception {
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
            throw new Exception("\"Rule engine error during tile selection: \" + e.getMessage()");
        }
        int pos = move.getPosition();
        if (pos >= 0 && pos < game.getGame().getBoard().getTiles().size()) {
            Tile tile = game.getGame().getBoard().getTile(pos);
            if (tile.isEmpty()) return tile;
        }
        return null;
    }

    /**
     * Selects a piece to give to the opponent using rule-based logic.
     * Falls back to a random piece if the engine fails or returns null.
     *
     * @return the selected Piece
     */
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


        return piecesLeft.stream().findAny().map(Tile::getPiece).orElse(null);
    }

    /**
     * Returns the name of this strategy.
     *
     * @return an empty string (or intended strategy name if defined)
     */
    @Override
    public String getName() {
        return "";
    }

    /**
     * Loads the current GameSession data required for decision making.
     *
     * @param gameSession the current game session
     */
    @Override
    public void fillNecessaryData(GameSession gameSession) {
        this.game = gameSession;
    }

    /**
     * Determines whether the strategy decides to call Quarto.
     * Always returns true if a pure win is detected by the game rules.
     *
     * @return true if Quarto is to be called
     */
    @Override
    public boolean isCallingQuarto() {
        return game.getGame().getGameRules().checkPureWin();
    }
}

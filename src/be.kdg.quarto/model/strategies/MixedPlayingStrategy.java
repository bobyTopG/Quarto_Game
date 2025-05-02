package be.kdg.quarto.model.strategies;

import be.kdg.quarto.model.Board;
import be.kdg.quarto.model.Tile;
import be.kdg.quarto.model.GameSession;
import be.kdg.quarto.model.Piece;
import be.kdg.quarto.model.PlayingStrategy;

import java.util.List;
import java.util.Random;

public class MixedPlayingStrategy implements PlayingStrategy {
    private GameSession game;
    private RandomPlayingStrategy randomStrategy;
    private RuleBasedStrategy ruleStrategy;

    /**
     * Constructs a MixedPlayingStrategy that blends rule-based and random strategies.
     * Initializes both internal strategy instances.
     */
    public MixedPlayingStrategy() {
        randomStrategy = new RandomPlayingStrategy();
        ruleStrategy = new RuleBasedStrategy();
    }

    /**
     * Loads the current game session into both rule-based and random strategies.
     *
     * @param gameSession the current game session
     */
    public void fillNecessaryData(GameSession gameSession) {
        game = gameSession;
        randomStrategy.fillNecessaryData(game);
        ruleStrategy.fillNecessaryData(game);
    }

    /**
     * Selects a piece to give to the opponent.
     * 70% of the time uses the rule-based strategy, 30% uses the random strategy.
     *
     * @return the selected Piece
     */
    @Override
    public Piece selectPiece() {
        if (Math.random() < 0.70) {
            return ruleStrategy.selectPiece();
        } else {
            return randomStrategy.selectPiece();
        }
    }

    /**
     * Selects a tile to place the current piece.
     * 70% of the time uses the rule-based strategy, 30% uses the random strategy.
     *
     * @return the selected Tile
     * @throws Exception if tile selection fails
     */
    @Override
    public Tile selectTile() throws Exception {
        if (Math.random() < 0.70) {
            return ruleStrategy.selectTile();
        } else {
            return randomStrategy.selectTile();
        }
    }

    @Override
    public String getName() {
        return "Mixed";
    }

    /**
     * Determines whether to call Quarto.
     * Has an 85% chance to return true, combining behaviors of both strategies.
     *
     * @return true if Quarto should be called, false otherwise
     */
    @Override
    public boolean isCallingQuarto() {
        if (Math.random() < 0.85) {
            return true;
        } else {
            return false;
        }
    }
}

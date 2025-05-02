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

    public MixedPlayingStrategy() {
        randomStrategy = new RandomPlayingStrategy();
        ruleStrategy = new RuleBasedStrategy();
    }

    public void fillNecessaryData(GameSession gameSession) {
        game = gameSession;
        randomStrategy.fillNecessaryData(game);
        ruleStrategy.fillNecessaryData(game);
    }

    // for every logic-based decision, 70% of the time it will use the rule-based strategy and 30% of the time, it will use the random strategy
    @Override
    public Piece selectPiece() {
        if (Math.random() < 0.70) {
            return ruleStrategy.selectPiece();
        } else {
            return randomStrategy.selectPiece();
        }
    }

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

    // rule always returns true, random returns true 50% of the time. 0.5 * 0.3 = 0.15, the chance that the mixed AI will NOT call quarto
    @Override
    public boolean isCallingQuarto() {
        if (Math.random() < 0.85) {
            return true;
        } else {
            return false;
        }
    }
}

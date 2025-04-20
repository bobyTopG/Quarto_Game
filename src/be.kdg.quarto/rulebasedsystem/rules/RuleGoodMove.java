package be.kdg.quarto.rulebasedsystem.rules;

import be.kdg.quarto.model.Game;
import be.kdg.quarto.model.Move;
import be.kdg.quarto.model.Tile;
import be.kdg.quarto.rulebasedsystem.facts.FactsHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RuleGoodMove extends Rule {

    private static final int[] CORNER_INDEXES = {0, 3, 12, 15};

    @Override
    public boolean conditionRule(FactsHandler facts) {
        // Always possible to make a good move if the game is running
        return true;
    }

    @Override
    public boolean actionRule(FactsHandler facts, Game game, Move move) {
        List<Integer> availableCorners = new ArrayList<>();
        for (int corner : CORNER_INDEXES) {
            Tile tile = game.getBoard().getTile(corner);
            if (tile.isEmpty()) {
                availableCorners.add(corner);
            }
        }

        if (!availableCorners.isEmpty()) {
            int randomCorner = availableCorners.get(new Random().nextInt(availableCorners.size()));
            move.setPosition(randomCorner);
        } else {
            // No corners available, pick random empty tile
            List<Integer> emptyTiles = new ArrayList<>();
            for (int i = 0; i < game.getBoard().getTiles().size(); i++) {
                if (game.getBoard().getTile(i).isEmpty()) {
                    emptyTiles.add(i);
                }
            }

            if (!emptyTiles.isEmpty()) {
                int randomEmpty = emptyTiles.get(new Random().nextInt(emptyTiles.size()));
                move.setPosition(randomEmpty);
            } else {
                move.setPosition(-1); // No move possible
            }
        }

        return true;
    }
}
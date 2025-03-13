package be.kdg.quarto.model;

import java.util.List;
import java.util.Random;

public class HardMode implements PlayingStrategy {
    private final QuartoAI quartoAI = new QuartoAI();
    private final Game game; // Reference to the current game state

    public HardMode(Game game) {
        this.game = game;
    }

    @Override
    public Tile selectPiece() {

        List<Tile> availableTiles = game.getTilesToSelect().getTiles().stream()
                .filter(tile -> tile.getPiece() != null)
                .toList();

        if (availableTiles.isEmpty()) {
            return null;
        }
        return availableTiles.get(new Random().nextInt(availableTiles.size()));
    }

    @Override
    public Tile placePiece() {
        Move bestMove = quartoAI.getBestMove(game);
        if (bestMove == null) {
            return null; // No valid move found (should not happen in a normal game)
        }
        return game.getPlacedTiles().getTiles().get(bestMove.getStartY() * 4 + bestMove.getStartX());
    }
    @Override
    public String getName() {
        return "Hard Mode AI";
    }
}
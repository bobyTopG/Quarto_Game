package be.kdg.quarto.model.strategies;

import be.kdg.quarto.model.Board;
import be.kdg.quarto.model.Tile;
import be.kdg.quarto.model.GameSession;
import be.kdg.quarto.model.Piece;
import be.kdg.quarto.model.PlayingStrategy;

import java.util.List;
import java.util.Random;

public class RandomPlayingStrategy implements PlayingStrategy {
    private Board selectPiece, board;


    public RandomPlayingStrategy() {

    }
    /**
     * Initializes the strategy with the current game session data.
     * Retrieves the board and the selectable pieces board from the game.
     *
     * @param gameSession the current game session containing game data
     */
    public void fillNecessaryData(GameSession gameSession) {
        this.selectPiece = gameSession.getGame().getPiecesToSelect();
        this.board = gameSession.getGame().getBoard();

        if (this.board.getTiles().isEmpty()) {
            this.board.createEmptyBoard();
        }
    }


    /**
     * Selects a random piece from the remaining unselected pieces.
     *
     * @return a randomly chosen Piece, or null if no pieces are available
     */
    @Override
    public Piece selectPiece() {
            return selectRandomPiece();
    }
    /**
     * Selects a random tile from the game board where no piece has been placed.
     *
     * @return a randomly selected empty Tile
     */
    @Override
    public Tile selectTile() {
        return getRandomFreeTile();
    }
    /**
     * Returns the identifier for this strategy.
     *
     * @return a string representing the strategy name
     */
    @Override
    public String getName() {
        return "S1";
    }

    /**
     * Randomly decides whether to call Quarto.
     * Returns true 50% of the time.
     *
     * @return true if Quarto is called, false otherwise
     */
    @Override
    public boolean isCallingQuarto() {
        int rand = new Random().nextInt(2);
        return rand == 0;
    }


    /**
     * Internal helper that selects a random piece from non-empty tiles
     * in the selection board.
     *
     * @return a randomly selected Piece, or null if no valid tiles are available
     */
    private Piece selectRandomPiece() {
        List<Tile> availableTiles = selectPiece.getTiles().stream()
                .filter(tile -> tile.getPiece() != null)
                .toList();

        if (availableTiles.isEmpty()) {
            return null;
        }
        return availableTiles.get(new Random().nextInt(availableTiles.size())).getPiece();
    }


    /**
     * Selects a random tile from the board that does not contain a piece.
     * This method filters all tiles on the current board to find the ones
     * that are empty, and then randomly selects one from them.
     *
     * @return a randomly selected empty Tile from the board,
     *         or {@code null} if no empty tiles are available.
     */

    public Tile getRandomFreeTile() {
        List<Tile> emptyTiles = board.getTiles().stream()
                .filter(tile -> tile.getPiece() == null)
                .toList();

        if (emptyTiles.isEmpty()) {
            return null;
        }
        return emptyTiles.get(new Random().nextInt(emptyTiles.size()));
    }

    /**
     * Sets the current game board.
     *
     * @param board the Board to use for this strategy
     */
    public void setBoard(Board board) {
        this.board = board;
    }
}

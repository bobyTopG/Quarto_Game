package be.kdg.quarto.model.strategy;

import be.kdg.quarto.model.Game;
import be.kdg.quarto.model.GameSession;
import be.kdg.quarto.model.Move;
import be.kdg.quarto.model.Tile;
import be.kdg.quarto.model.Board;
import be.kdg.quarto.model.Piece;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DifficultStrategy implements PlayingStrategy {
    private static final int MAX_DEPTH = 2;
    private final Game game;
    private GameSession session;

    public DifficultStrategy(GameSession session) {
        this.session = session;
        this.game = session.getModel();
    }

    //Picking
    @Override
    public Tile selectPiece() {
        List<Tile> availableTiles = game.getTilesToSelect().getTiles().stream()
                .filter(tile -> tile.getPiece() != null)
                .toList();

        if (availableTiles.isEmpty()) {
            return null;
        }

        Tile bestTile = null;
        int lowestOpponentScore = Integer.MAX_VALUE;
        List<Tile> safeTiles = new ArrayList<>();

        // Avoid selecting a piece that allows the opponent to win
        for (Tile tile : availableTiles) {
            applyTemporarySelection(tile);
            if (opponentCanWin()) {
                undoTemporarySelection(tile);
                System.out.println("Avoiding " + tile);
                continue; // Skip this piece to prevent the opponent from winning
            }
            safeTiles.add(tile);
            undoTemporarySelection(tile);
        }

        // If all pieces are dangerous, return any to continue the game
        if (safeTiles.isEmpty()) {
            return availableTiles.get(new Random().nextInt(availableTiles.size()));
        }

        //Choose the piece that minimizes the opponent’s best move
        for (Tile tile : safeTiles) {
            applyTemporarySelection(tile);
            int opponentScore = evaluateOpponentOptions();
            undoTemporarySelection(tile);

            if (opponentScore < lowestOpponentScore) {
                lowestOpponentScore = opponentScore;
                bestTile = tile;
            }
        }

        if (bestTile != null) {
            return bestTile; // Choose the piece that gives the opponent the worst position
        }

        // If no good piece is found, select randomly from safe options
        Tile randomTile = safeTiles.get(new Random().nextInt(safeTiles.size()));
        return randomTile;
    }


    private void applyTemporarySelection(Tile tile) {
        game.getCurrentTile().setPiece(tile.getPiece());
    }


    private void undoTemporarySelection(Tile tile) {
        game.getCurrentTile().setPiece(null);
    }

    private boolean opponentCanWin() {
        for (Move move : getPossibleMoves(game)) {
            applyMove(game, move);
            boolean wins = game.getGameRules().isGameOver();
            undoMove(game, move);
            if (wins) {
                System.out.println("!Opponent has a winning move!");
                return true;
            }
        }
        return false;
    }

    private int evaluateOpponentOptions() {
        int bestScore = Integer.MIN_VALUE;
        for (Move move : getPossibleMoves(game)) {
            applyMove(game, move);
            int score = minimax(game, 1, false); // Check one depth level ahead
            undoMove(game, move);
            bestScore = Math.max(bestScore, score);
        }
        return bestScore;
    }


    //Placing
    @Override
    public Tile placePiece() {
        Move bestMove = getBestMove(game);
        if (bestMove == null) {
            return null; // No valid move found (should not happen in a normal game)
        }
        return game.getPlacedTiles().getTiles().get(bestMove.getStartY() * 4 + bestMove.getStartX());
    }

    @Override
    public String getName() {
        return "Hard Mode AI";
    }

    public Move getBestMove(Game game) {
        if (game.getGameRules().isGameOver()) {
            return null; // AI should not play after winning
        }
        int bestScore = Integer.MIN_VALUE;
        Move bestMove = null;

        for (Move move : getPossibleMoves(game)) {
            applyMove(game, move);

            if (game.getGameRules().isGameOver()) {
                undoMove(game, move);
                return move; // Stop immediately and return winning move
            }

            int score = minimax(game, MAX_DEPTH, false);
            undoMove(game, move);

            if (score > bestScore) {
                bestScore = score;
                bestMove = move;
            }
        }

        //System.out.println("Best move chosen: " + (bestMove != null ? bestMove.getStartX() + ", " + bestMove.getStartY() : "None") + " with score: " + bestScore);
        return bestMove;
    }


    private int minimax(Game game, int depth, boolean isMaximizing) {
        if (game.getGameRules().isGameOver()) {
            int score = (game.getGameRules().getWinner() == session.getAi()) ? 1000 : -1000;
            return score;
        }

        if (depth == 0) {
            return evaluateBoard(game);
        }

        if (isMaximizing) {
            int maxEval = Integer.MIN_VALUE;
            for (Move move : getPossibleMoves(game)) {
                applyMove(game, move);
                if (game.getGameRules().isGameOver()) {
                    undoMove(game, move);
                    return 1000; // Stop searching if AI finds a win
                }
                int eval = minimax(game, depth - 1, false);
                undoMove(game, move);
                maxEval = Math.max(maxEval, eval);
            }
            return maxEval;
        } else {
            int minEval = Integer.MAX_VALUE;
            for (Move move : getPossibleMoves(game)) {
                applyMove(game, move);
                if (game.getGameRules().isGameOver()) {
                    undoMove(game, move);
                    return -1000; // Stop searching if human finds a win
                }
                int eval = minimax(game, depth - 1, true);
                undoMove(game, move);
                minEval = Math.min(minEval, eval);
            }
            return minEval;
        }
    }


    private int evaluateBoard(Game game) {
        if (game.getGameRules().isGameOver()) {
            if (game.getGameRules().getWinner() == session.getAi()) {
                return 1000; // AI wins
            } else if (game.getGameRules().getWinner() != session.getAi()) {
                return -1000; // Human wins
            }
        }

        int score = 0;
        Board board = game.getPlacedTiles();

        for (int row = 0; row < 4; row++) {
            score += evaluateLine(board, row * 4, 1); // Rows
        }
        for (int col = 0; col < 4; col++) {
            score += evaluateLine(board, col, 4); // Columns
        }
        score += evaluateLine(board, 0, 5); // Main diagonal
        score += evaluateLine(board, 3, 3); // Anti-diagonal

        return score;
    }


    private boolean isWinningLine(Board board, int startIndex, int step) {
        List<Tile> tiles = board.getTiles();
        Piece[] pieces = new Piece[4];

        for (int i = 0; i < 4; i++) {
            pieces[i] = tiles.get(startIndex + (i * step)).getPiece();
            if (pieces[i] == null) return false; // Incomplete line
        }
        return allSame(pieces, "color") || allSame(pieces, "high") || allSame(pieces, "shape") || allSame(pieces, "fill");
    }


    private int evaluateLine(Board board, int startIndex, int step) {
        List<Tile> tiles = board.getTiles();
        Piece[] pieces = new Piece[4];

        for (int i = 0; i < 4; i++) {
            pieces[i] = tiles.get(startIndex + (i * step)).getPiece();
        }

        if (pieces[0] == null || pieces[1] == null || pieces[2] == null || pieces[3] == null) {
            return 0;
        }

        int score = 0;
        if (allSame(pieces, "color")) score += 10;
        if (allSame(pieces, "high")) score += 10;
        if (allSame(pieces, "shape")) score += 10;
        if (allSame(pieces, "fill")) score += 10;

        return score;
    }


    private boolean allSame(Piece[] pieces, String attribute) {
        String v1 = getAttribute(pieces[0], attribute);
        String v2 = getAttribute(pieces[1], attribute);
        String v3 = getAttribute(pieces[2], attribute);
        String v4 = getAttribute(pieces[3], attribute);

        return v1.equals(v2) && v2.equals(v3) && v3.equals(v4);
    }


    private String getAttribute(Piece piece, String attribute) {
        return switch (attribute) {
            case "color" -> piece.getColor().toString();
            case "high" -> piece.getHeight().toString();
            case "shape" -> piece.getShape().toString();
            case "fill" -> piece.getFill().toString();
            default -> "";
        };
    }


    private List<Move> getPossibleMoves(Game game) {
        List<Move> moves = new ArrayList<>();
        Board board = game.getPlacedTiles();
        List<Tile> tiles = board.getTiles();

        for (int i = 0; i < tiles.size(); i++) {
            if (tiles.get(i).getPiece() == null) {
                moves.add(new Move(i % 4, i / 4, -1, -1)); // Only placing move
            }
        }
        return moves;
    }


    private void applyMove(Game game, Move move) {
        Tile tile = game.getPlacedTiles().getTiles().get(move.getStartY() * 4 + move.getStartX());
        tile.setPiece(game.getCurrentTile().getPiece());
    }


    private void undoMove(Game game, Move move) {
        Tile tile = game.getPlacedTiles().getTiles().get(move.getStartY() * 4 + move.getStartX());
        tile.setPiece(null);
    }
}

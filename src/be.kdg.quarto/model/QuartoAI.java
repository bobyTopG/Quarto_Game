package be.kdg.quarto.model;

import java.util.ArrayList;
import java.util.List;

public class QuartoAI {
    private static final int MAX_DEPTH = 3; // Limits recursion depth

    public Move getBestMove(Game game) {
        int bestScore = Integer.MIN_VALUE;
        Move bestMove = null;

        for (Move move : getPossibleMoves(game)) {
            applyMove(game, move);
            int score = minimax(game, MAX_DEPTH, false);
            undoMove(game, move);

            if (score > bestScore) {
                bestScore = score;
                bestMove = move;
            }
        }
        return bestMove;
    }

    private int minimax(Game game, int depth, boolean isMaximizing) {
        if (depth == 0 || game.getGameRules().isGameOver()) {
            return evaluateBoard(game);
        }

        if (isMaximizing) {
            int maxEval = Integer.MIN_VALUE;
            for (Move move : getPossibleMoves(game)) {
                applyMove(game, move);
                int eval = minimax(game, depth - 1, false);
                undoMove(game, move);
                maxEval = Math.max(maxEval, eval);
            }
            return maxEval;
        } else {
            int minEval = Integer.MAX_VALUE;
            for (Move move : getPossibleMoves(game)) {
                applyMove(game, move);
                int eval = minimax(game, depth - 1, true);
                undoMove(game, move);
                minEval = Math.min(minEval, eval);
            }
            return minEval;
        }
    }

    private int evaluateBoard(Game game) {
        if (game.getGameRules().isGameOver()) {
            return game.getGameRules().getWinner() == game.getHuman() ? -1000 : 1000;
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

package be.kdg.quarto.model.strategies.MiniMax;

import be.kdg.quarto.model.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MiniMaxStrategy implements PlayingStrategy {
    private static final int MAX_DEPTH = 3;
    private GameSession gameSession;
    private Player strategyAI;

    public MiniMaxStrategy() {
    }

    public void fillNecessaryData(GameSession gameSession) {
        this.gameSession = gameSession;
        this.strategyAI = gameSession.getOpponent();
    }

    @Override
    public Piece selectPiece() {
        List<Tile> availableTiles = gameSession.getGame().getPiecesToSelect().getTiles().stream()
                .filter(tile -> tile.getPiece() != null)
                .toList();

        if (availableTiles.isEmpty()) {
            return null;
        }

        Tile bestTile = null;
        int lowestOpponentScore = Integer.MAX_VALUE;
        List<Tile> safeTiles = new ArrayList<>();

        for (Tile tile : availableTiles) {
            Piece piece = tile.getPiece();
            gameSession.getGame().setSelectedPiece(piece);

            boolean opponentCanImmediatelyWin = false;
            for (StrategyMove move : getPossibleMoves(gameSession)) {
                applyMove(gameSession, move);

                if (checkWin()) {
                    opponentCanImmediatelyWin = true;
                   // System.out.println("!Opponent has a winning move!");
                }

                undoMove(gameSession, move);
                if (opponentCanImmediatelyWin) break;
            }

            gameSession.getGame().setSelectedPiece(null);

            if (opponentCanImmediatelyWin) {
             //   System.out.println("Avoiding " + piece);
                continue;
            }

            safeTiles.add(tile);
        }

        // If all pieces are dangerous, return any to continue the game
        if (safeTiles.isEmpty()) {
            return availableTiles.get(new Random().nextInt(availableTiles.size())).getPiece();
        }

        // Choose the piece that minimizes the opponent's best move
        for (Tile tile : safeTiles) {
            applyTemporarySelection(tile);
            int opponentScore = evaluateOpponentOptions();
            undoTemporarySelection();

            if (opponentScore < lowestOpponentScore) {
                lowestOpponentScore = opponentScore;
                bestTile = tile;
            }
        }

        if (bestTile != null) {
            return bestTile.getPiece(); // Choose the piece that gives the opponent the worst position
        }

        // If no good piece is found, select randomly from safe options
        Tile randomTile = safeTiles.get(new Random().nextInt(safeTiles.size()));
        return randomTile.getPiece();
    }

    private void applyTemporarySelection(Tile tile) {
        // Store the current selected piece temporarily
        gameSession.getGame().setSelectedPiece(tile.getPiece());
    }

    private void undoTemporarySelection() {
        // Restore the previous state
        gameSession.getGame().setSelectedPiece(null);
    }

    private int evaluateOpponentOptions() {
        int bestScore = Integer.MIN_VALUE;
        for (StrategyMove move : getPossibleMoves(gameSession)) {
            applyMove(gameSession, move);
            int score = minimax(gameSession.getGame().getBoard(), 1, false, Integer.MIN_VALUE, Integer.MAX_VALUE); // Check one depth level ahead
            undoMove(gameSession, move);
            bestScore = Math.max(bestScore, score);
        }
        return bestScore;
    }

    @Override
    public Tile selectTile() {
        StrategyMove bestMove = getBestMove(gameSession);
        if (bestMove == null) {
            // No best move found, pick a random legal move
            List<StrategyMove> possibleMoves = getPossibleMoves(gameSession);
            if (possibleMoves.isEmpty()) return null;
        }
        return gameSession.getGame().getBoard().getTiles().get(bestMove.getStartY() * 4 + bestMove.getStartX());
    }

    @Override
    public String getName() {
        return "Hard Mode AI";
    }

    public boolean isCallingQuarto() {
        return true;
    }

    public StrategyMove getBestMove(GameSession gameSession) {
        if (checkWin()) {
            return null; // AI should not play after winning
        }
        int tilesLeft = getPossibleMoves(gameSession).size();
        int depth = (tilesLeft > 10) ? 3 : (tilesLeft > 5) ? 4 : 5;
        System.out.println("Evaluating " + tilesLeft + " possible moves.");
        int bestScore = Integer.MIN_VALUE;
        StrategyMove bestMove = null;

        for (StrategyMove move : getPossibleMoves(gameSession)) {
            applyMove(gameSession, move);

            if (checkWin()) {
                undoMove(gameSession, move);
                return move; // Stop immediately and return winning move
            }

            int score = minimax(gameSession.getGame().getBoard(), depth, false, Integer.MIN_VALUE, Integer.MAX_VALUE);
            undoMove(gameSession, move);

            if (score > bestScore) {
                bestScore = score;
                bestMove = move;
            }

        }

        if (bestScore == 0) {
            List<StrategyMove> possibleMoves = getPossibleMoves(gameSession);
            if (!possibleMoves.isEmpty()) {
                return possibleMoves.get(new Random().nextInt(possibleMoves.size()));
            }
        }

        return bestMove;
    }

    private int minimax(Board board, int depth, boolean isMaximizing, int alpha, int beta) {
        GameRules rules = new GameRules(board, List.of());
        if (rules.checkPureWin()) return isMaximizing ? -1000 + depth : 1000 - depth;
        if (depth >= MAX_DEPTH) return evaluateBoard(board);

        int bestScore = isMaximizing ? Integer.MIN_VALUE : Integer.MAX_VALUE;
        Piece selected = gameSession.getGame().getSelectedPiece();

        for (Tile tile : board.getTiles()) {
            if (tile.getPiece() != null) continue;

            tile.setPiece(selected);
            gameSession.getGame().setSelectedPiece(null);

            int score = minimax(board, depth + 1, !isMaximizing, alpha, beta);

            tile.setPiece(null);
            gameSession.getGame().setSelectedPiece(selected);

            if (isMaximizing) {
                bestScore = Math.max(score, bestScore);
                alpha = Math.max(alpha, bestScore);
            } else {
                bestScore = Math.min(score, bestScore);
                beta = Math.min(beta, bestScore);
            }

            if (beta <= alpha) break; // Alpha-beta pruning
        }
        return bestScore;
    }

    private int evaluateBoard(Board board) {
        int score = 0;
        List<Tile> tiles = board.getTiles();

        int[][] lines = {
            {0, 1, 2, 3}, {4, 5, 6, 7}, {8, 9, 10, 11}, {12, 13, 14, 15}, // Rows
            {0, 4, 8, 12}, {1, 5, 9, 13}, {2, 6, 10, 14}, {3, 7, 11, 15}, // Columns
            {0, 5, 10, 15}, {3, 6, 9, 12} // Diagonals
        };

        for (int[] line : lines) {
            Piece[] pieces = new Piece[4];
            for (int i = 0; i < 4; i++) {
                pieces[i] = tiles.get(line[i]).getPiece();
            }

            if (pieces[0] == null || pieces[1] == null || pieces[2] == null || pieces[3] == null)
                continue;

            if (allSame(pieces, "color")) score += 10;
            if (allSame(pieces, "high")) score += 10;
            if (allSame(pieces, "shape")) score += 10;
            if (allSame(pieces, "fill")) score += 10;
        }

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
            case "high" -> piece.getSize().toString();
            case "shape" -> piece.getShape().toString();
            case "fill" -> piece.getFill().toString();
            default -> "";
        };
    }

    private List<StrategyMove> getPossibleMoves(GameSession gameSession) {
        List<StrategyMove> moves = new ArrayList<>();
        Board board = gameSession.getGame().getBoard();
        List<Tile> tiles = board.getTiles();

        for (int i = 0; i < tiles.size(); i++) {
            if (tiles.get(i).getPiece() == null) {
                moves.add(new StrategyMove(i % 4, i / 4, -1, -1));
            }
        }
        return moves;
    }

    private void applyMove(GameSession gameSession, StrategyMove move) {
        Tile tile = gameSession.getGame().getBoard().getTiles().get(move.getStartY() * 4 + move.getStartX());
        tile.setPiece(gameSession.getGame().getSelectedPiece());
    }

    private void undoMove(GameSession gameSession, StrategyMove move) {
        Tile tile = gameSession.getGame().getBoard().getTiles().get(move.getStartY() * 4 + move.getStartX());
        tile.setPiece(null);
    }

    public boolean checkWin() {
        return gameSession.getGame().getGameRules().checkPureWin();
    }
}
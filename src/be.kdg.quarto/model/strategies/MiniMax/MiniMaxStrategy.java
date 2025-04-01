package be.kdg.quarto.model.strategies.MiniMax;


import be.kdg.quarto.model.GameSession;
import be.kdg.quarto.model.Tile;
import be.kdg.quarto.model.Board;
import be.kdg.quarto.model.Piece;
import be.kdg.quarto.model.Player;
import be.kdg.quarto.model.PlayingStrategy;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MiniMaxStrategy implements PlayingStrategy {
    private static final int MAX_DEPTH = 4;
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

        // Avoid selecting a piece that allows the opponent to win
        for (Tile tile : availableTiles) {
            applyTemporarySelection(tile);
            if (opponentCanWin()) {
                undoTemporarySelection();
                System.out.println("Avoiding " + tile.getPiece());
                continue; // Skip this piece to prevent the opponent from winning
            }
            safeTiles.add(tile);
            undoTemporarySelection();
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

    private boolean opponentCanWin() {
        for (StrategyMove move : getPossibleMoves(gameSession)) {
            applyMove(gameSession, move);
            undoMove(gameSession, move);
            if (checkWin()) {
                System.out.println("!Opponent has a winning move!");
                return true;
            }
        }
        return false;
    }

    private int evaluateOpponentOptions() {
        int bestScore = Integer.MIN_VALUE;
        for (StrategyMove move : getPossibleMoves(gameSession)) {
            applyMove(gameSession, move);
            int score = minimax(gameSession, 1, false); // Check one depth level ahead
            undoMove(gameSession, move);
            bestScore = Math.max(bestScore, score);
        }
        return bestScore;
    }

    @Override
    public Tile selectTile() {
        StrategyMove bestMove = getBestMove(gameSession);
        if (bestMove == null) {
            return null; // No valid StrategyMove found (should not happen in a normal game)
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
        int bestScore = Integer.MIN_VALUE;
        StrategyMove bestMove = null;

        for (StrategyMove move : getPossibleMoves(gameSession)) {
            applyMove(gameSession, move);

            if (checkWin()) {
                undoMove(gameSession, move);
                return move; // Stop immediately and return winning move
            }

            int score = minimax(gameSession, MAX_DEPTH, false);
            undoMove(gameSession, move);

            if (score > bestScore) {
                bestScore = score;
                bestMove = move;
            }
        }

        System.out.println("Best move chosen: " + (bestMove != null ? bestMove.getStartX() + ", " + bestMove.getStartY() : "None") + " with score: " + bestScore);
        return bestMove;
    }

    private int minimax(GameSession gameSession, int depth, boolean isMaximizing) {
        if (checkWin()) {
            int score = (gameSession.getWinner() == strategyAI) ? 1000 : -1000;
            return score;
        }

        if (depth == 0) {
            return evaluateBoard(gameSession);
        }

        if (isMaximizing) {
            int maxEval = Integer.MIN_VALUE;
            for (StrategyMove move : getPossibleMoves(gameSession)) {
                applyMove(gameSession, move);
                if (checkWin()) {
                    undoMove(gameSession, move);
                    return 1000; // Stop searching if AI finds a win
                }
                int eval = minimax(gameSession, depth - 1, false);
                undoMove(gameSession, move);
                maxEval = Math.max(maxEval, eval);
            }
            return maxEval;
        } else {
            int minEval = Integer.MAX_VALUE;
            for (StrategyMove move : getPossibleMoves(gameSession)) {
                applyMove(gameSession, move);
                if (checkWin()) {
                    undoMove(gameSession, move);
                    return -1000; // Stop searching if human finds a win
                }
                int eval = minimax(gameSession, depth - 1, true);
                undoMove(gameSession, move);
                minEval = Math.min(minEval, eval);
            }
            return minEval;
        }
    }

    private int evaluateBoard(GameSession gameSession) {
        if (checkWin()) {
            if (gameSession.getWinner() == strategyAI) {
                return 1000; // AI wins
            } else if (gameSession.getWinner() != strategyAI) {
                return -1000; // Human wins
            }
        }

        int score = 0;
        Board board = gameSession.getGame().getBoard();

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
                moves.add(new StrategyMove(i % 4, i / 4, -1, -1)); // Only placing move
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
        return gameSession.getGame().getGameRules().checkWin();
    }
}
package be.kdg.quarto.model.strategies.MiniMax;

import be.kdg.quarto.model.GameSession;
import be.kdg.quarto.model.Board;
import be.kdg.quarto.model.Tile;
import be.kdg.quarto.model.Piece;
import be.kdg.quarto.model.Player;
import be.kdg.quarto.model.PlayingStrategy;

import java.util.*;

public class MiniMaxStrategy implements PlayingStrategy {
    private static final int MAX_DEPTH = 6;
    private GameSession gameSession;
    private Player aiPlayer;

    public MiniMaxStrategy() {
    }

    public void fillNecessaryData(GameSession gameSession) {
        this.gameSession = gameSession;
        this.aiPlayer = gameSession.getOpponent();
    }

    @Override
    public String getName() {
        return "Hard Mode AI";
    }

    @Override
    public boolean isCallingQuarto() {
        return true;
    }

    @Override
    public Piece selectPiece() {
        List<Tile> availableTiles = gameSession.getGame().getPiecesToSelect().getTiles().stream()
                .filter(tile -> tile.getPiece() != null)
                .toList();

        List<Tile> safeTiles = new ArrayList<>();

        for (Tile tile : availableTiles) {
            gameSession.getGame().setSelectedPiece(tile.getPiece());
            if (!opponentHasWinningMove()) {
                safeTiles.add(tile);
            }
            gameSession.getGame().setSelectedPiece(null);
        }

        if (safeTiles.isEmpty()) {
            return availableTiles.get(new Random().nextInt(availableTiles.size())).getPiece();
        }

        Tile bestTile = null;
        int worstOpponentScore = Integer.MAX_VALUE;

        for (Tile tile : safeTiles) {
            gameSession.getGame().setSelectedPiece(tile.getPiece());
            int score = evaluateOpponentBestMove();
            gameSession.getGame().setSelectedPiece(null);

            if (score < worstOpponentScore) {
                worstOpponentScore = score;
                bestTile = tile;
            }
        }

        return bestTile != null ? bestTile.getPiece() :
                safeTiles.get(new Random().nextInt(safeTiles.size())).getPiece();
    }

    @Override
    public Tile selectTile() {
        StrategyMove bestMove = getBestMove();
        return (bestMove != null)
                ? gameSession.getGame().getBoard().getTiles().get(bestMove.getStartY() * 4 + bestMove.getStartX())
                : null;
    }

    private StrategyMove getBestMove() {
        StrategyMove bestMove = null;
        int bestScore = Integer.MIN_VALUE;

        for (StrategyMove move : getPossibleMoves()) {
            applyMove(move);
            if (isWinningMove()) {
                undoMove(move);
                return move;
            }

            int score = minimax(MAX_DEPTH - 1, false);
            undoMove(move);

            if (score > bestScore) {
                bestScore = score;
                bestMove = move;
            }
        }

        return bestMove;
    }

    private boolean opponentHasWinningMove() {
        for (StrategyMove move : getPossibleMoves()) {
            applyMove(move);
            boolean win = isWinningMove();
            undoMove(move);
            if (win) return true;
        }
        return false;
    }

    private int evaluateOpponentBestMove() {
        int worstScore = Integer.MIN_VALUE;
        for (StrategyMove move : getPossibleMoves()) {
            applyMove(move);
            int score = minimax(1, false);
            undoMove(move);
            worstScore = Math.max(worstScore, score);
        }
        return worstScore;
    }

    private int minimax(int depth, boolean maximizing) {
        if (depth == 0 || isWinningMove()) {
            return evaluateBoard();
        }

        int bestScore = maximizing ? Integer.MIN_VALUE : Integer.MAX_VALUE;

        for (StrategyMove move : getPossibleMoves()) {
            applyMove(move);
            int score = minimax(depth - 1, !maximizing);
            undoMove(move);
            bestScore = maximizing ? Math.max(bestScore, score) : Math.min(bestScore, score);
        }

        return bestScore;
    }

    private boolean isWinningMove() {
        return gameSession.getGame().getGameRules().checkWin();
    }

    private int evaluateBoard() {
        if (isWinningMove()) {
            Player winner = gameSession.getWinner();
            return (winner == aiPlayer) ? 1000 : -1000;
        }

        int score = 0;
        Board board = gameSession.getGame().getBoard();

        for (int i = 0; i < 4; i++) {
            score += evaluateLine(board, i * 4, 1); // rows
            score += evaluateLine(board, i, 4);     // columns
        }

        score += evaluateLine(board, 0, 5); // main diagonal
        score += evaluateLine(board, 3, 3); // anti-diagonal

        return score;
    }

    private int evaluateLine(Board board, int start, int step) {
        List<Tile> tiles = board.getTiles();
        Piece[] line = new Piece[4];

        for (int i = 0; i < 4; i++) {
            line[i] = tiles.get(start + i * step).getPiece();
        }

        if (Arrays.stream(line).anyMatch(Objects::isNull)) return 0;

        int score = 0;
        if (allSame(line, "color")) score += 10;
        if (allSame(line, "high")) score += 10;
        if (allSame(line, "shape")) score += 10;
        if (allSame(line, "fill")) score += 10;

        return score;
    }

    private boolean allSame(Piece[] pieces, String attr) {
        String val = getAttr(pieces[0], attr);
        return Arrays.stream(pieces).allMatch(p -> getAttr(p, attr).equals(val));
    }

    private String getAttr(Piece piece, String attr) {
        return switch (attr) {
            case "color" -> piece.getColor().toString();
            case "high" -> piece.getSize().toString();
            case "shape" -> piece.getShape().toString();
            case "fill" -> piece.getFill().toString();
            default -> "";
        };
    }

    private List<StrategyMove> getPossibleMoves() {
        List<Tile> tiles = gameSession.getGame().getBoard().getTiles();
        List<StrategyMove> moves = new ArrayList<>();

        for (int i = 0; i < tiles.size(); i++) {
            if (tiles.get(i).getPiece() == null) {
                moves.add(new StrategyMove(i % 4, i / 4, -1, -1));
            }
        }
        return moves;
    }

    private void applyMove(StrategyMove move) {
        Tile tile = gameSession.getGame().getBoard().getTiles().get(move.getStartY() * 4 + move.getStartX());
        tile.setPiece(gameSession.getGame().getSelectedPiece());
    }

    private void undoMove(StrategyMove move) {
        Tile tile = gameSession.getGame().getBoard().getTiles().get(move.getStartY() * 4 + move.getStartX());
        tile.setPiece(null);
    }
}
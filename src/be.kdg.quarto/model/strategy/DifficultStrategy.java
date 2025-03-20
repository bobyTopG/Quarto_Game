package be.kdg.quarto.model.strategy;



import be.kdg.quarto.model.Game;
import be.kdg.quarto.model.Board;
import be.kdg.quarto.model.GameSession;
import be.kdg.quarto.model.Piece;
import be.kdg.quarto.model.Tile;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DifficultStrategy implements PlayingStrategy {
    private static final int MAX_DEPTH = 2;
    private final Game game;
    private final GameSession session;

    public DifficultStrategy(GameSession session) {
        this.session = session;
        this.game = session.getModel();
    }

    @Override
    public Tile selectPiece() {
        List<Tile> availableTiles = game.getTilesToSelect().getTiles().stream()
                .filter(tile -> tile.getPiece() != null)
                .toList();

        if (availableTiles.isEmpty()) return null;

        Tile bestTile = null;
        int lowestOpponentScore = Integer.MAX_VALUE;
        List<Tile> safeTiles = new ArrayList<>();

        for (Tile tile : availableTiles) {
            Piece tempPiece = tile.getPiece();
            tile.setPiece(null);
            if (opponentCanWin()) {
                tile.setPiece(tempPiece);
                continue;
            }
            safeTiles.add(tile);
            tile.setPiece(tempPiece);
        }

        if (safeTiles.isEmpty()) return availableTiles.get(new Random().nextInt(availableTiles.size()));

        for (Tile tile : safeTiles) {
            Piece tempPiece = tile.getPiece();
            tile.setPiece(null);
            int opponentScore = evaluateOpponentOptions();
            tile.setPiece(tempPiece);

            if (opponentScore < lowestOpponentScore) {
                lowestOpponentScore = opponentScore;
                bestTile = tile;
            }
        }

        return (bestTile != null) ? bestTile : safeTiles.get(new Random().nextInt(safeTiles.size()));
    }

    private boolean opponentCanWin() {
        for (Move move : getPossibleMoves(game)) {
            applyMove(game, move);
            boolean wins = game.getGameRules().isGameOver();
            undoMove(game, move);
            if (wins) return true;
        }
        return false;
    }

    private int evaluateOpponentOptions() {
        int bestScore = Integer.MIN_VALUE;
        for (Move move : getPossibleMoves(game)) {
            applyMove(game, move);
            int score = minimax(game, 1, false);
            undoMove(game, move);
            bestScore = Math.max(bestScore, score);
        }
        return bestScore;
    }

    @Override
    public Tile placePiece() {
        Move bestMove = getBestMove(game);
        if (bestMove == null) return null;
        return game.getPlacedTiles().getTiles().get(bestMove.getStartY() * 4 + bestMove.getStartX());
    }

    @Override
    public String getName() {
        return "Hard Mode AI";
    }

    public Move getBestMove(Game game) {
        if (game.getGameRules().isGameOver()) return null;

        int bestScore = Integer.MIN_VALUE;
        Move bestMove = null;

        for (Move move : getPossibleMoves(game)) {
            applyMove(game, move);
            if (game.getGameRules().isGameOver()) {
                undoMove(game, move);
                return move;
            }

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
        if (game.getGameRules().isGameOver()) {
            return (session.getWinner() == session.getPlayer2()) ? 1000 : -1000;
        }

        if (depth == 0) return evaluateBoard(game);

        int bestEval = isMaximizing ? Integer.MIN_VALUE : Integer.MAX_VALUE;

        for (Move move : getPossibleMoves(game)) {
            applyMove(game, move);
            if (game.getGameRules().isGameOver()) {
                undoMove(game, move);
                return isMaximizing ? 1000 : -1000;
            }
            int eval = minimax(game, depth - 1, !isMaximizing);
            undoMove(game, move);
            bestEval = isMaximizing ? Math.max(bestEval, eval) : Math.min(bestEval, eval);
        }

        return bestEval;
    }

    private int evaluateBoard(Game game) {
        if (game.getGameRules().isGameOver()) {
            return (session.getWinner() == session.getPlayer2()) ? 1000 : -1000;
        }

        int score = 0;
        Board board = game.getPlacedTiles();

        for (int row = 0; row < 4; row++) {
            score += evaluateLine(board, row * 4, 1);
        }
        for (int col = 0; col < 4; col++) {
            score += evaluateLine(board, col, 4);
        }
        score += evaluateLine(board, 0, 5);
        score += evaluateLine(board, 3, 3);

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
                moves.add(new Move(i % 4, i / 4, -1, -1));
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
package be.kdg.quarto.model;

import java.util.List;
import java.util.Set;

public class GameRules {
    private Piece lastPiecePlaced;
    private Piece beforeLastPiecePlaced;
    private Board board;
    private List<Move> moves;

    public GameRules(Board board, List<Move> moves) {
        this.board = board;
        this.moves = moves;
    }

    public boolean checkWin() {
        if (moves.size() < 4) return false;

        this.lastPiecePlaced = moves.get(moves.size() - 1).getPiece();
        this.beforeLastPiecePlaced = moves.get(moves.size() - 2).getPiece();
        this.board = board;

        // Check all winning conditions
        return checkLines(4, 1) || // Horizontal
                checkLines(1, 4) || // Vertical
                checkLines(5, 1) || // Main diagonal
                checkLines(3, 1);   // Anti-diagonal
    }

    public boolean isGameOver() {
        return checkWin() || moves.size() == 16; // All tiles filled
    }

    private boolean checkLines(int step, int iterations) {
        for (int i = 0; i < 4 * iterations; i += step) {
            Piece p1 = board.getTiles().get(i).getPiece();
            Piece p2 = board.getTiles().get(i + step).getPiece();
            Piece p3 = board.getTiles().get(i + 2 * step).getPiece();
            Piece p4 = board.getTiles().get(i + 3 * step).getPiece();

            if (p1 != null && p2 != null && p3 != null && p4 != null &&
                    pieceCombinationIsWinner(p1, p2, p3, p4)) {
                return true;
            }
        }
        return false;
    }

    private boolean pieceCombinationIsWinner(Piece p1, Piece p2, Piece p3, Piece p4) {
        return piecesAreRelated(p1, p2, p3, p4) &&
                pieceHasBeenPlayedInThisOrLastMove(p1, p2, p3, p4);
    }

    private boolean piecesAreRelated(Piece p1, Piece p2, Piece p3, Piece p4) {
        return (p1.getColor() == p2.getColor() && p2.getColor() == p3.getColor() && p3.getColor() == p4.getColor()) ||
                (p1.getFill() == p2.getFill() && p2.getFill() == p3.getFill() && p3.getFill() == p4.getFill()) ||
                (p1.getHeight() == p2.getHeight() && p2.getHeight() == p3.getHeight() && p3.getHeight() == p4.getHeight()) ||
                (p1.getShape() == p2.getShape() && p2.getShape() == p3.getShape() && p3.getShape() == p4.getShape());
    }

    private boolean pieceHasBeenPlayedInThisOrLastMove(Piece... pieces) {
        Set<Piece> lastMoves = Set.of(lastPiecePlaced, beforeLastPiecePlaced);
        for (Piece piece : pieces) {
            if (lastMoves.contains(piece)) return true;
        }
        return false;
    }
}
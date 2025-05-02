package be.kdg.quarto.model;

import java.util.ArrayList;
import java.util.List;

public class GameRules {
    private Piece lastPiecePlaced;
    private Piece beforeLastPiecePlaced;
    private final Board board;
    private final List<Move> moves;

    public GameRules(Board board, List<Move> moves) {
        this.board = board;
        this.moves = moves;
    }

    public boolean checkWin() {
        if (moves.size() < 4) return false;
        lastPiecePlaced = moves.getLast().getPiece();
        beforeLastPiecePlaced = moves.get(moves.size() - 2).getPiece();
        return checkHorizontalWin() || checkVerticalWin() || checkMainDiagonalWin() || checkAntiDiagonalWin();
    }

    public boolean checkPureWin() {
        return checkLineWin(0,1,2,3) || checkLineWin(4,5,6,7) || checkLineWin(8,9,10,11) || checkLineWin(12,13,14,15) ||
                checkLineWin(0,4,8,12) || checkLineWin(1,5,9,13) || checkLineWin(2,6,10,14) || checkLineWin(3,7,11,15) ||
                checkLineWin(0,5,10,15) || checkLineWin(3,6,9,12);
    }

    private boolean checkHorizontalWin() {
        for (int i = 0; i < 16; i += 4) {
            List<Piece> pieces = new ArrayList<>();
            boolean rowHasNull = false;
            for (int j = 0; j < 4; j++) {
                Piece p = board.getTiles().get(i + j).getPiece();
                pieces.add(p);
                if (p == null) {
                    rowHasNull = true;
                    break;
                }
            }
            if (!rowHasNull && pieceCombinationIsWinner(pieces.get(0), pieces.get(1), pieces.get(2), pieces.get(3)))
                return true;
        }
        return false;
    }

    private boolean checkVerticalWin() {
        for (int i = 0; i < 4; i++) {
            List<Piece> pieces = new ArrayList<>();
            boolean colHasNull = false;
            for (int j = 0; j < 4; j++) {
                Piece p = board.getTiles().get(i + j * 4).getPiece();
                pieces.add(p);
                if (p == null) {
                    colHasNull = true;
                    break;
                }
            }
            if (!colHasNull && pieceCombinationIsWinner(pieces.get(0), pieces.get(1), pieces.get(2), pieces.get(3)))
                return true;
        }
        return false;
    }

    private boolean checkMainDiagonalWin() {
        return pieceCombinationIsWinner(
                board.getTiles().get(0).getPiece(),
                board.getTiles().get(5).getPiece(),
                board.getTiles().get(10).getPiece(),
                board.getTiles().get(15).getPiece());
    }

    private boolean checkAntiDiagonalWin() {
        return pieceCombinationIsWinner(
                board.getTiles().get(3).getPiece(),
                board.getTiles().get(6).getPiece(),
                board.getTiles().get(9).getPiece(),
                board.getTiles().get(12).getPiece());
    }

    private boolean checkLineWin(int a, int b, int c, int d) {
        Piece p1 = board.getTiles().get(a).getPiece();
        Piece p2 = board.getTiles().get(b).getPiece();
        Piece p3 = board.getTiles().get(c).getPiece();
        Piece p4 = board.getTiles().get(d).getPiece();

        return p1 != null && p2 != null && p3 != null && p4 != null && piecesAreRelated(p1, p2, p3, p4);
    }

    private boolean pieceCombinationIsWinner(Piece p1, Piece p2, Piece p3, Piece p4) {
        if (p1 == null || p2 == null || p3 == null || p4 == null) return false;
        return piecesAreRelated(p1, p2, p3, p4) &&
                pieceHasBeenPlayedInThisOrLastMove(p1, p2, p3, p4, lastPiecePlaced, beforeLastPiecePlaced);
    }

    private boolean piecesAreRelated(Piece p1, Piece p2, Piece p3, Piece p4) {
        return (p1.getColor() == p2.getColor() && p2.getColor() == p3.getColor() && p3.getColor() == p4.getColor())
                || (p1.getFill() == p2.getFill() && p2.getFill() == p3.getFill() && p3.getFill() == p4.getFill())
                || (p1.getSize() == p2.getSize() && p2.getSize() == p3.getSize() && p3.getSize() == p4.getSize())
                || (p1.getShape() == p2.getShape() && p2.getShape() == p3.getShape() && p3.getShape() == p4.getShape());
    }

    private boolean pieceHasBeenPlayedInThisOrLastMove(Piece p1, Piece p2, Piece p3, Piece p4, Piece last, Piece beforeLast) {
        return (p1 == last || p1 == beforeLast)
                || (p2 == last || p2 == beforeLast)
                || (p3 == last || p3 == beforeLast)
                || (p4 == last || p4 == beforeLast);
    }
}
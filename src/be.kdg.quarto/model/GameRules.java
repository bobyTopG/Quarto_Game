package be.kdg.quarto.model;

import java.util.ArrayList;
import java.util.List;

public class GameRules {
    Piece lastPiecePlaced;
    Piece beforeLastPiecePlaced;
    Board board;
    public boolean checkWin(Board board, List<Move> moves) {
        // no one can possibly win with under 4 moves made
        if(moves.size() < 4) {
            return false;
        }
        lastPiecePlaced = moves.get(moves.size() - 1).getPiece();
        beforeLastPiecePlaced = moves.get(moves.size() - 2).getPiece();
        this.board = board;
        //we have to check 10 possible win rows (4 horizontal, 4 vertical and 2 diagonals)
        return checkHorizontalWin() || checkVerticalWin() || checkMainDiagonalWin() || checkAntiDiagonalWin();
    }
    private boolean checkHorizontalWin() {
        for (int i = 0; i < 16; i += 4) {
            boolean rowHasNull = false; // Flag to track null pieces
            List<Piece> pieces = new ArrayList<>();

            for (int j = 0; j < 4; j++) { // Iterate through the row
                pieces.add(j,board.getTiles().get(i + j).getPiece());
                if (pieces.get(j) == null) {
                    rowHasNull = true;
                    break; // Exit the inner loop
                }
            }
            if (rowHasNull) continue; // Skip the rest of the outer loop (next row)
            if (pieceCombinationIsWinner(pieces.get(0), pieces.get(1), pieces.get(2), pieces.get(3))) return true; // Found a winning row
        }
        return false; // No winning row found
    }
    private boolean checkVerticalWin() {
        for (int i = 0; i < 4; i++) { // Iterate through columns
            boolean columnHasNull = false;
            List<Piece> pieces = new ArrayList<>();
            for (int j = 0; j < 4; j++) { // Iterate through rows
                pieces.add(j,board.getTiles().get(i + j * 4).getPiece());
                if (pieces.get(j) == null) {
                    columnHasNull = true;
                    break;
                }
            }
            if (columnHasNull) continue;
            if (pieceCombinationIsWinner(pieces.get(0), pieces.get(1), pieces.get(2), pieces.get(3))) return true;

        }
        return false;
    }
    private boolean checkMainDiagonalWin() {
        Piece piece1 = board.getTiles().get(0).getPiece();
        Piece piece2 = board.getTiles().get(5).getPiece();
        Piece piece3 = board.getTiles().get(10).getPiece();
        Piece piece4 = board.getTiles().get(15).getPiece();

        if (piece1 != null && piece2 != null && piece3 != null && piece4 != null) {
            return pieceCombinationIsWinner(piece1, piece2, piece3, piece4);
        }
        return false;
    }
    private boolean checkAntiDiagonalWin() {
        Piece piece1 = board.getTiles().get(3).getPiece();
        Piece piece2 = board.getTiles().get(6).getPiece();
        Piece piece3 = board.getTiles().get(9).getPiece();
        Piece piece4 = board.getTiles().get(12).getPiece();

        if (piece1 != null && piece2 != null && piece3 != null && piece4 != null) {
            return pieceCombinationIsWinner(piece1, piece2, piece3, piece4);
        }

        return false;
    }

    private boolean pieceCombinationIsWinner(Piece piece1, Piece piece2, Piece piece3, Piece piece4) {
        return piecesAreRelated(piece1, piece2, piece3, piece4) &&
                pieceHasBeenPlayedInThisOrLastMove(piece1, piece2, piece3, piece4, lastPiecePlaced, beforeLastPiecePlaced);
    }
    private boolean piecesAreRelated(Piece piece1, Piece piece2, Piece piece3, Piece piece4) {
        return     (piece1.getColor() == piece2.getColor() && piece2.getColor() == piece3.getColor() && piece3.getColor() == piece4.getColor())
                || (piece1.getFill() == piece2.getFill() && piece2.getFill() == piece3.getFill() && piece3.getFill() == piece4.getFill())
                || (piece1.getSize() == piece2.getSize() && piece2.getSize() == piece3.getSize() && piece3.getSize() == piece4.getSize())
                || (piece1.getShape() == piece2.getShape() && piece2.getShape() == piece3.getShape() && piece3.getShape() == piece4.getShape());
    }
    private boolean pieceHasBeenPlayedInThisOrLastMove(Piece piece1, Piece piece2, Piece piece3, Piece piece4, Piece lastPiecePlaced, Piece beforeLastPiecePlaced) {
       return (lastPiecePlaced == piece1 || beforeLastPiecePlaced == piece1) ||
               (lastPiecePlaced == piece2 || beforeLastPiecePlaced == piece2) ||
               (lastPiecePlaced == piece3 || beforeLastPiecePlaced == piece3) ||
               (lastPiecePlaced == piece4 || beforeLastPiecePlaced == piece4);
    }

}

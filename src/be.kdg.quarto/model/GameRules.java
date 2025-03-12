package be.kdg.quarto.model;

import java.util.List;

public class GameRules {
    private boolean gameOver, isTie;
    private Player winner;
    private Board placingBoard;
    private Board selectedBoard;

    public GameRules(Board selectedBoard, Board placingBoard) {
        this.selectedBoard = selectedBoard;
        this.placingBoard = placingBoard;
    }


    public boolean isGameOver() {
        List<Tile> emptyTiles = placingBoard.getTiles().stream()
                .filter(tile -> tile.getPiece() == null)
                .toList();


        if (emptyTiles.isEmpty()) {
            gameOver = true;
            isTie = true;
            return gameOver;
        } else if (checkRowsAndCols() || checkColumns() || checkDiagonals()) {
            gameOver = true;
        }
        return gameOver;
    }

    private boolean checkRowsAndCols() {
        for (int row = 0; row < 4; row++) {
            int startIndex = row * 4;  // First element of the row
            if (checkLine(startIndex, 1)) return true; // Step = 1 for rows
        }
        return false;
    }

    private boolean checkColumns() {
        for (int col = 0; col < 4; col++) {
            if (checkLine(col, 4)) return true; // Step = 4 for columns
        }
        return false;
    }

    private boolean checkDiagonals() {
        if (checkLine(0, 5)) return true;
        if (checkLine(3, 3)) return true;
        return false;
    }

    private boolean checkLine(int startIndex, int step) {
        Piece p1 = placingBoard.getTiles().get(startIndex).getPiece();
        Piece p2 = placingBoard.getTiles().get(startIndex + step).getPiece();
        Piece p3 = placingBoard.getTiles().get(startIndex + 2 * step).getPiece();
        Piece p4 = placingBoard.getTiles().get(startIndex + 3 * step).getPiece();

        if (p1 == null || p2 == null || p3 == null || p4 == null) return false;

        return allSame(p1, p2, p3, p4, "color") ||
                allSame(p1, p2, p3, p4, "high") ||
                allSame(p1, p2, p3, p4, "shape") ||
                allSame(p1, p2, p3, p4, "fill");
    }

    private boolean allSame(Piece p1, Piece p2, Piece p3, Piece p4, String attribute) {
        String v1 = getAttribute(p1, attribute);
        String v2 = getAttribute(p2, attribute);
        String v3 = getAttribute(p3, attribute);
        String v4 = getAttribute(p4, attribute);

        return v1.equals(v2) && v2.equals(v3) && v3.equals(v4);
    }

    private String getAttribute(Piece piece, String attribute) {
        switch (attribute) {
            case "color":
                return piece.getColor().toString();
            case "high":
                return piece.getHeight().toString();
            case "shape":
                return piece.getShape().toString();
            case "fill":
                return piece.getFill().toString();
            default:
                return "";
        }
    }

    public void setWinner(Player winner) {
        this.winner = winner;
    }

    public Player getWinner() {
        return winner;
    }
}

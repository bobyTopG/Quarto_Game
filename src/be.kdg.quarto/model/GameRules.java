package be.kdg.quarto.model;

import java.util.List;

public class GameRules {
    private boolean gameOver;
    private boolean isTie;
    private Player winner;
    private final Game game;
    private Board placingBoard;

    public GameRules(Game game) {
        this.game = game;
        placingBoard = game.getPlacedTiles();
    }

    public boolean isGameOver() {
        placingBoard = game.getPlacedTiles(); // Ensure board is updated
        List<Tile> tiles = placingBoard.getTiles();

        boolean hasWinner = checkRows() || checkColumns() || checkDiagonals();
        boolean boardFull = tiles.stream().noneMatch(tile -> tile.getPiece() == null);


        if (hasWinner) {
            gameOver = true;
            isTie = false;
            return true;
        }

        if (boardFull) {
            gameOver = true;
            isTie = true;
            return true;
        }

        gameOver = false;
        return false;
    }


    private boolean checkRows() {
        List<Tile> tiles = placingBoard.getTiles();
        for (int row = 0; row < 4; row++) {
            int startIndex = row * 4;
            if (checkLine(tiles, startIndex, 1)) {
                return true;
            }
        }
        return false;
    }

    private boolean checkColumns() {
        List<Tile> tiles = placingBoard.getTiles();
        for (int col = 0; col < 4; col++) {
            if (checkLine(tiles, col, 4)) {
                return true;
            }
        }
        return false;
    }

    private boolean checkDiagonals() {
        List<Tile> tiles = placingBoard.getTiles();
        if (checkLine(tiles, 0, 5)) {
            return true;
        }
        if (checkLine(tiles, 3, 3)) {
            return true;
        }
        return false;
    }

    private boolean checkLine(List<Tile> tiles, int startIndex, int step) {
        Piece p1 = tiles.get(startIndex).getPiece();
        Piece p2 = tiles.get(startIndex + step).getPiece();
        Piece p3 = tiles.get(startIndex + 2 * step).getPiece();
        Piece p4 = tiles.get(startIndex + 3 * step).getPiece();

        if (p1 == null || p2 == null || p3 == null || p4 == null) {
            return false; // Prevents early false win detection
        }

        boolean sameColor = allSame(p1, p2, p3, p4, "color");
        boolean sameHeight = allSame(p1, p2, p3, p4, "height");
        boolean sameShape = allSame(p1, p2, p3, p4, "shape");
        boolean sameFill = allSame(p1, p2, p3, p4, "fill");

        if (sameColor || sameHeight || sameShape || sameFill) {
            return true;
        }

        return false;
    }


    private boolean allSame(Piece p1, Piece p2, Piece p3, Piece p4, String attribute) {
        String v1 = getAttribute(p1, attribute);
        String v2 = getAttribute(p2, attribute);
        String v3 = getAttribute(p3, attribute);
        String v4 = getAttribute(p4, attribute);

        if (v1 == null || v2 == null || v3 == null || v4 == null) return false;

        return v1.equals(v2) && v2.equals(v3) && v3.equals(v4);
    }

    private String getAttribute(Piece piece, String attribute) {
        if (piece == null) return null; // Prevent NullPointerException
        return switch (attribute) {
            case "color" -> piece.getColor() != null ? piece.getColor().toString() : null;
            case "height" -> piece.getHeight() != null ? piece.getHeight().toString() : null;
            case "shape" -> piece.getShape() != null ? piece.getShape().toString() : null;
            case "fill" -> piece.getFill() != null ? piece.getFill().toString() : null;
            default -> null;
        };
    }

    public void setWinner(Player winner) {
        this.winner = winner;
    }

    public Player getWinner() {
        return winner;
    }
}
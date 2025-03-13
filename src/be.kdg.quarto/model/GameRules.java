package be.kdg.quarto.model;

import java.util.List;

public class GameRules {
    private boolean gameOver;
    private boolean isTie;
    private Player winner;
    private Game game;
    private Board placingBoard;

    public boolean isTie() {
        return isTie;
    }

    public GameRules(Game game) {
        this.game = game;
        placingBoard = game.getPlacedTiles();
    }

    public boolean isGameOver() {
        List<Tile> tiles = placingBoard.getTiles();
        boolean hasEmptyTiles = tiles.stream().anyMatch(tile -> tile.getPiece() == null);

        if (!hasEmptyTiles) {
            gameOver = true;
            isTie = true;
            return true;
        } else if (checkRowsAndCols() || checkColumns() || checkDiagonals()) {
            gameOver = true;
            return true;
        }
       gameOver = false;
        return false;

    }

    private boolean checkRowsAndCols() {
        List<Tile> tiles = placingBoard.getTiles();
        for (int row = 0; row < 4; row++) {
            int startIndex = row * 4;  // First element of the row
            if (checkLine(tiles, startIndex, 1)) return true; // Step = 1 for rows
        }
        return false;
    }

    private boolean checkColumns() {
        List<Tile> tiles = placingBoard.getTiles();
        for (int col = 0; col < 4; col++) {
            if (checkLine(tiles, col, 4)) return true; // Step = 4 for columns
        }
        return false;
    }

    private boolean checkDiagonals() {
        List<Tile> tiles = placingBoard.getTiles();
        if (checkLine(tiles, 0, 5)) return true;
        return checkLine(tiles, 3, 3);
    }

    private boolean checkLine(List<Tile> tiles, int startIndex, int step) {
        Piece p1 = tiles.get(startIndex).getPiece();
        Piece p2 = tiles.get(startIndex + step).getPiece();
        Piece p3 = tiles.get(startIndex + 2 * step).getPiece();
        Piece p4 = tiles.get(startIndex + 3 * step).getPiece();

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
        return switch (attribute) {
            case "color" -> piece.getColor().toString();
            case "high" -> piece.getHeight().toString();
            case "shape" -> piece.getShape().toString();
            case "fill" -> piece.getFill().toString();
            default -> "";
        };
    }

    public void setWinner(Player winner) {
        this.winner = winner;
    }

    public Player getWinner() {
        return winner;
    }
}
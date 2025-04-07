package be.kdg.quarto.model;

import be.kdg.quarto.model.enums.Color;
import be.kdg.quarto.model.enums.Fill;
import be.kdg.quarto.model.enums.Size;
import be.kdg.quarto.model.enums.Shape;

import java.util.ArrayList;
import java.util.List;

public class Board {
    private static final int BOARD_SIZE = 16;
    private List<Tile> tiles;

    public Board() {
        this.tiles = new ArrayList<>(BOARD_SIZE);
    }

    public void generateAllPieces() {
        for (Color color : Color.values()) {
            for (Size size : Size.values()) {
                for (Fill fill : Fill.values()) {
                    for (Shape shape : Shape.values()) {
                        tiles.add(new Tile(new Piece(color, size, fill, shape)));
                    }
                }
            }
        }
    }

    public void createEmptyBoard() {
        for (int i = 0; i < BOARD_SIZE; i++) {
            tiles.add(new Tile());
        }
    }

    public boolean isEmpty() {
        for (Tile tile : tiles) {
            if (tile.getPiece() != null)
                return false;
        }
        return true;
    }

    public List<Tile> getTiles() {
        return tiles;
    }

    public Tile findTile(Piece piece) {
        for (Tile tile : tiles) {
            if (tile.getPiece() == null)
                continue;
            if (tile.getPiece().equals(piece)) {
                return tile;
            }
        }
        return null;
    }

    public Tile findTile(int pos) {
        return tiles.get(pos);
    }

    private boolean isWinningMovePossible() {
        for (int i = 0; i < tiles.size(); i++) {
            Tile tile = tiles.get(i);
            if (tile.isEmpty()) {
                Piece original = tile.getPiece(); // should be null but for safety
                for (Color color : Color.values()) {
                    for (Size size : Size.values()) {
                        for (Fill fill : Fill.values()) {
                            for (Shape shape : Shape.values()) {
                                Piece testPiece = new Piece(color, size, fill, shape);
                                tile.setPiece(testPiece);
                                boolean causesWin = wouldCauseWin(i);
                                tile.setPiece(original); // restore original state
                                if (causesWin) {
                                    return true;
                                }
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    public boolean endMoveAIPossible() {
        return isWinningMovePossible();
    }

    public boolean endMovePlayerPossible() {
        return isWinningMovePossible();
    }

    public boolean endWinningPositionAIPossible() {
        return isWinningMovePossible();
    }

    public boolean endWinningPositionPlayerPossible() {
        return isWinningMovePossible();
    }

    public void determineEndMove(Move move) {
        for (int i = 0; i < tiles.size(); i++) {
            Tile tile = tiles.get(i);
            if (tile.isEmpty() && wouldCauseWin(i)) {
                move.setPosition(i);
                return;
            }
        }
        determineRandomMove(move); // fallback
    }

    public void determineRandomMove(Move move) {
        for (int i = 0; i < tiles.size(); i++) {
            if (tiles.get(i).isEmpty()) {
                move.setPosition(i);
                return;
            }
        }
    }

    private boolean wouldCauseWin(int index) {
        int row = index / 4;
        int col = index % 4;

        // Helper to check a list of tiles for a shared attribute
        java.util.function.Predicate<java.util.List<Tile>> sharesAttribute = tiles -> {
            if (tiles.stream().anyMatch(t -> t.isEmpty())) return false;

            boolean sameColor = tiles.stream().map(t -> t.getPiece().getColor()).distinct().count() == 1;
            boolean sameSize = tiles.stream().map(t -> t.getPiece().getSize()).distinct().count() == 1;
            boolean sameFill = tiles.stream().map(t -> t.getPiece().getFill()).distinct().count() == 1;
            boolean sameShape = tiles.stream().map(t -> t.getPiece().getShape()).distinct().count() == 1;

            return sameColor || sameSize || sameFill || sameShape;
        };

        // Check row
        List<Tile> rowTiles = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            rowTiles.add(tiles.get(row * 4 + i));
        }
        if (sharesAttribute.test(rowTiles)) return true;

        // Check column
        List<Tile> colTiles = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            colTiles.add(tiles.get(i * 4 + col));
        }
        if (sharesAttribute.test(colTiles)) return true;

        // Check main diagonal
        if (row == col) {
            List<Tile> mainDiag = new ArrayList<>();
            for (int i = 0; i < 4; i++) {
                mainDiag.add(tiles.get(i * 5));
            }
            if (sharesAttribute.test(mainDiag)) return true;
        }

        // Check anti-diagonal
        if (row + col == 3) {
            List<Tile> antiDiag = new ArrayList<>();
            for (int i = 0; i < 4; i++) {
                antiDiag.add(tiles.get((i + 1) * 3));
            }
            if (sharesAttribute.test(antiDiag)) return true;
        }

        return false;
    }

    public Tile getTile(int pos) {
        return findTile(pos);
    }
}
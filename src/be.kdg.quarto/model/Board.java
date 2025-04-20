package be.kdg.quarto.model;

import be.kdg.quarto.model.enums.Color;
import be.kdg.quarto.model.enums.Fill;
import be.kdg.quarto.model.enums.Size;
import be.kdg.quarto.model.enums.Shape;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class Board {
    private static final int BOARD_SIZE = 16;
    private final List<Tile> tiles = new ArrayList<>(BOARD_SIZE);

    public Board() {
        createEmptyBoard();
    }

    public void createEmptyBoard() {
        tiles.clear();
        for (int i = 0; i < BOARD_SIZE; i++) {
            tiles.add(new Tile());
        }
    }

    public void generateAllPieces() {
        tiles.clear();
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

    public boolean isEmpty() {
        return tiles.stream().allMatch(Tile::isEmpty);
    }

    public List<Tile> getTiles() {
        return tiles;
    }

    public Tile getTile(int pos) {
        return tiles.get(pos);
    }


    // Game Logic //

    public boolean isWinningMovePossible(Piece selectedPiece) {
        for (int i = 0; i < tiles.size(); i++) {
            Tile tile = tiles.get(i);
            if (tile.isEmpty()) {
                tile.setPiece(selectedPiece);
                boolean wouldWin = wouldCauseWin(i);
                tile.setPiece(null); // Undo
                if (wouldWin) return true;
            }
        }
        return false;
    }

    public boolean isWinningPositionPossible() {
        List<List<Tile>> lines = getAllLines();
        for (List<Tile> line : lines) {
            long empty = line.stream().filter(Tile::isEmpty).count();
            if (empty == 1) {
                List<Piece> placed = line.stream()
                        .filter(tile -> !tile.isEmpty())
                        .map(Tile::getPiece)
                        .toList();
                boolean sameColor = placed.stream().map(Piece::getColor).distinct().count() == 1;
                boolean sameSize = placed.stream().map(Piece::getSize).distinct().count() == 1;
                boolean sameFill = placed.stream().map(Piece::getFill).distinct().count() == 1;
                boolean sameShape = placed.stream().map(Piece::getShape).distinct().count() == 1;

                if (sameColor || sameSize || sameFill || sameShape) {
                    return true;
                }
            }
        }
        return false;
    }


    public boolean wouldCauseWin(int index) {
        int row = index / 4;
        int col = index % 4;

        Predicate<List<Tile>> sharesAttribute = line -> {
            if (line.stream().anyMatch(Tile::isEmpty)) return false;

            boolean sameColor = line.stream().map(t -> t.getPiece().getColor()).distinct().count() == 1;
            boolean sameSize = line.stream().map(t -> t.getPiece().getSize()).distinct().count() == 1;
            boolean sameFill = line.stream().map(t -> t.getPiece().getFill()).distinct().count() == 1;
            boolean sameShape = line.stream().map(t -> t.getPiece().getShape()).distinct().count() == 1;

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

        // Check diagonals
        if (row == col) {
            List<Tile> mainDiag = new ArrayList<>();
            for (int i = 0; i < 4; i++) {
                mainDiag.add(tiles.get(i * 5));
            }
            if (sharesAttribute.test(mainDiag)) return true;
        }

        if (row + col == 3) {
            List<Tile> antiDiag = new ArrayList<>();
            for (int i = 0; i < 4; i++) {
                antiDiag.add(tiles.get((i + 1) * 3));
            }
            if (sharesAttribute.test(antiDiag)) return true;
        }

        return false;
    }

    private List<List<Tile>> getAllLines() {
        List<List<Tile>> lines = new ArrayList<>();

        // Rows
        for (int i = 0; i < 4; i++) {
            List<Tile> row = new ArrayList<>();
            for (int j = 0; j < 4; j++) {
                row.add(tiles.get(i * 4 + j));
            }
            lines.add(row);
        }

        // Columns
        for (int j = 0; j < 4; j++) {
            List<Tile> col = new ArrayList<>();
            for (int i = 0; i < 4; i++) {
                col.add(tiles.get(i * 4 + j));
            }
            lines.add(col);
        }

        // Diagonals
        List<Tile> mainDiag = new ArrayList<>();
        List<Tile> antiDiag = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            mainDiag.add(tiles.get(i * 5));
            antiDiag.add(tiles.get((i + 1) * 3));
        }
        lines.add(mainDiag);
        lines.add(antiDiag);

        return lines;
    }

    public Tile findTile(int index) {
        return tiles.get(index);
    }


}
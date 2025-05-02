package be.kdg.quarto.model;

import java.util.Objects;

public class Tile {
    private Piece piece;


    public Tile(Piece piece) {
        setPiece(piece);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tile tile = (Tile) o;
        return Objects.equals(piece, tile.piece);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(piece);
    }


    public Tile() {}
    public Piece getPiece() {
        return piece;
    }
    public void setPiece(Piece piece) {
        this.piece = piece;
    }

    public boolean isEmpty() {
        return this.piece == null;
    }
}

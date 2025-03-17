package be.kdg.quarto.model;

import be.kdg.quarto.model.enums.Color;
import be.kdg.quarto.model.enums.Fill;
import be.kdg.quarto.model.enums.Size;
import be.kdg.quarto.model.enums.Shape;

import java.util.Objects;

public class Piece {
    private  Color color;
    private Size size;
    private  Fill fill;
    private  Shape shape;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Piece piece = (Piece) o;
        return color == piece.color && size == piece.size && fill == piece.fill && shape == piece.shape;
    }

    @Override
    public int hashCode() {
        return Objects.hash(color, size, fill, shape);
    }


    public Piece(Color color, Size size, Fill fill, Shape shape) {
        this.color = color;
        this.size = size;
        this.fill = fill;
        this.shape = shape;
    }

    public Color getColor() {
        return color;
    }

    public Size getSize() {
        return size;
    }

    public Fill getFill() {
        return fill;
    }

    public Shape getShape() {
        return shape;
    }

    @Override
    public String toString() {
        return color + " " + size + " " + fill + " " + shape;
    }
}
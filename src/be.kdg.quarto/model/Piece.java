package be.kdg.quarto.model;

import be.kdg.quarto.model.enums.Color;
import be.kdg.quarto.model.enums.Fill;
import be.kdg.quarto.model.enums.Height;
import be.kdg.quarto.model.enums.Shape;

import java.util.Objects;

public class Piece {
    private  Color color;
    private  Height height;
    private  Fill fill;
    private  Shape shape;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Piece piece = (Piece) o;
        return color == piece.color && height == piece.height && fill == piece.fill && shape == piece.shape;
    }

    @Override
    public int hashCode() {
        return Objects.hash(color, height, fill, shape);
    }



    public Piece(Color color, Height height, Fill fill, Shape shape) {
        this.color = color;
        this.height = height;
        this.fill = fill;
        this.shape = shape;
    }

    public Color getColor() {
        return color;
    }

    public Height getHeight() {
        return height;
    }

    public Fill getFill() {
        return fill;
    }

    public Shape getShape() {
        return shape;
    }

    @Override
    public String toString() {
        return "Piece{" +
                "color=" + color +
                ", height=" + height +
                ", fill=" + fill +
                ", shape=" + shape +
                '}';
    }

}
package src.be.kdg.quarto.model;

import src.be.kdg.quarto.model.enums.Color;
import src.be.kdg.quarto.model.enums.Fill;
import src.be.kdg.quarto.model.enums.Height;
import src.be.kdg.quarto.model.enums.Shape;

public class Piece {
    private final Color color;
    private final Height height;
    private final Fill fill;
    private final Shape shape;

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
        return color + " " + height + " " + fill + " " + shape;
    }
}
package be.kdg.quarto.model;

import be.kdg.quarto.helpers.DbConnection;
import be.kdg.quarto.model.enums.Color;
import be.kdg.quarto.model.enums.Fill;
import be.kdg.quarto.model.enums.Size;
import be.kdg.quarto.model.enums.Shape;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

public class Piece {
    private Color color;
    private Size size;
    private Fill fill;
    private Shape shape;

    private int pieceId;

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

        // loads the id of a piece from the database
        try (PreparedStatement ps = DbConnection.connection.prepareStatement(DbConnection.getPieceId())) {
            ps.setString(1, fill.toString().toUpperCase());
            ps.setString(2, shape.toString().toUpperCase());
            ps.setString(3, color.toString().toUpperCase());
            ps.setString(4, size.toString().toUpperCase());

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                this.pieceId = rs.getInt(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int getPieceId() {
        return pieceId;
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
        return pieceId + " " + color + " " + size + " " + fill + " " + shape;
    }
}
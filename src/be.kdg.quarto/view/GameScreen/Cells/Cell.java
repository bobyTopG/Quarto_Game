package be.kdg.quarto.view.GameScreen.Cells;

import be.kdg.quarto.helpers.ImageHelper;
import be.kdg.quarto.model.Piece;
import be.kdg.quarto.model.enums.Size;
import javafx.geometry.Pos;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

/**
 * Cell is a class made to connect logic with UI and make updating the visuals easier
 */
public class Cell {
    protected static final double SMALL_PIECE_SIZE = 23.0;
    protected static final double REGULAR_PIECE_SIZE = 30.0;
    protected StackPane cell;
    protected Piece piece;
    public boolean isHovered;
    public boolean isSelected;
    protected ImageView pieceImageView;
    protected int row;
    protected int column;

    /**
     *  Creates the Cell with its visual
     * @param row position y
     * @param column position x
     *
     */
    public Cell(int row, int column) {
        this.row = row;
        this.column = column;
        this.cell = new StackPane();
        cell.setAlignment(Pos.CENTER);

    }

    /**
     * Finds the piece image and sets it to the Cell
     *
     */
    public void setPiece(Piece piece) {
        cell.getChildren().removeIf(node -> node instanceof ImageView);
        this.piece = piece;

        if (piece != null) {
            ImageView imageView = new ImageView(ImageHelper.getPieceImagePath(piece));
            boolean isSmall = piece.getSize() == Size.SMALL;
            imageView.setFitHeight(isSmall ? SMALL_PIECE_SIZE : REGULAR_PIECE_SIZE);
            imageView.setFitWidth(isSmall ? SMALL_PIECE_SIZE : REGULAR_PIECE_SIZE);
            cell.getChildren().add(imageView);
            this.pieceImageView = imageView;
        }
    }
    public StackPane getCellVisual() { return cell; }
    public Piece getPiece() { return piece; }

    public int getRow() { return row; }
    public int getColumn() { return column; }


}

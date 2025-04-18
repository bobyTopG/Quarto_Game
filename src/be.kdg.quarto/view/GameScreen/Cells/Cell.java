package be.kdg.quarto.view.GameScreen.Cells;

import be.kdg.quarto.helpers.ImageHelper;
import be.kdg.quarto.model.Piece;
import be.kdg.quarto.model.enums.Size;
import javafx.geometry.Pos;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

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

    public Cell(int row, int column) {
        this.row = row;
        this.column = column;
        this.cell = new StackPane();
        cell.setAlignment(Pos.CENTER);

    }

    public void setPiece(Piece piece) {
        cell.getChildren().removeIf(node -> node instanceof ImageView);
        this.piece = piece;

        if (piece != null) {
            ImageView imageView = new ImageView(ImageHelper.getPieceImage(piece));
            boolean isSmall = piece.getSize() == Size.SMALL;
            imageView.setFitHeight(isSmall ? SMALL_PIECE_SIZE : REGULAR_PIECE_SIZE);
            imageView.setFitWidth(isSmall ? SMALL_PIECE_SIZE : REGULAR_PIECE_SIZE);
            cell.getChildren().add(imageView);
            this.pieceImageView = imageView;
        }
    }
    public StackPane getCellVisual() { return cell; }
    public Piece getPiece() { return piece; }
    public ImageView getPieceVisual(){
        return pieceImageView;
    }

    public int getRow() { return row; }
    public int getColumn() { return column; }


}

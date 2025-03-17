package be.kdg.quarto.view.GameScreen;

import be.kdg.quarto.model.Piece;
import be.kdg.quarto.model.enums.Size;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class PieceView extends StackPane {
    private Rectangle pieceRect;
    private ImageView pieceImage;
    private boolean isSmall;

    // Standard sizes for pieces
    private static final double REGULAR_RECT_SIZE = 45.0;
    private static final double REGULAR_IMAGE_SIZE = 35.0;
    private static final double SMALL_RECT_SIZE = 35.0;
    private static final double SMALL_IMAGE_SIZE = 25.0;

    public Rectangle getPieceRect() {
        return pieceRect;
    }

    public ImageView getPieceImage() {
        return pieceImage;
    }

    public void setPieceImage(ImageView pieceImage) {
        this.pieceImage = pieceImage;
    }

    public void clearPiece() {
        this.getPieceImage().setImage(null);
    }

    public PieceView() {
        this(false);
    }

    public PieceView(boolean isSmall) {
        this.isSmall = isSmall;
        initialiseNodes();
        layoutNodes();
    }

    public void setSmallSize(boolean isSmall) {
        this.isSmall = isSmall;
        updateSizes();
    }

    private void initialiseNodes() {
        pieceImage = new ImageView();

        double rectSize = isSmall ? SMALL_RECT_SIZE : REGULAR_RECT_SIZE;
        pieceRect = new Rectangle(rectSize, rectSize);
    }

    private void layoutNodes() {
        pieceRect.setFill(Color.TRANSPARENT);
        pieceRect.setArcHeight(20);
        pieceRect.setArcWidth(20);
        pieceRect.setStroke(Color.BLACK);

        StackPane stackPane = new StackPane();
        stackPane.getChildren().addAll(pieceRect);

        HBox hbox = new HBox();
        hbox.setAlignment(Pos.CENTER);
        hbox.setPadding(new Insets(5, 5, 5, 5));
        hbox.getChildren().addAll(stackPane);

        // Set image size based on whether it's a small piece
        double imageSize = isSmall ? SMALL_IMAGE_SIZE : REGULAR_IMAGE_SIZE;
        pieceImage.setFitHeight(imageSize);
        pieceImage.setFitWidth(imageSize);

        getChildren().addAll(hbox, pieceImage);
    }

    private void updateSizes() {
        double rectSize = isSmall ? SMALL_RECT_SIZE : REGULAR_RECT_SIZE;
        double imageSize = isSmall ? SMALL_IMAGE_SIZE : REGULAR_IMAGE_SIZE;

        // Update rectangle size
        pieceRect.setWidth(rectSize);
        pieceRect.setHeight(rectSize);

        // Update image size
        pieceImage.setFitHeight(imageSize);
        pieceImage.setFitWidth(imageSize);
    }

    public void setPiece(Piece piece) {
        if (piece != null) {
            // Set size based on the HEIGHT enum
            boolean isSmallPiece = (piece.getSize() == Size.SMALL);
            setSmallSize(isSmallPiece);

            // You'll need to add code here to set the appropriate image based on piece properties
            // This depends on how you're handling images for different piece types
        }
    }
}
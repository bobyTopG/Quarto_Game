package src.be.kdg.quarto.model;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public enum PieceType {
    WHITE_HIGH_FILL_CIRCLE("/quarto2.gif"),
    WHITE_HIGH_FILL_SQUARE("/quarto0.gif"),
    WHITE_HIGH_EMPTY_CIRCLE("/quarto10.gif"),
    WHITE_HIGH_EMPTY_SQUARE("/quarto8.gif"),
    WHITE_LOW_FILL_CIRCLE("/quarto3.gif"),
    WHITE_LOW_FILL_SQUARE("/quarto1.gif"),
    WHITE_LOW_EMPTY_CIRCLE("/quarto11.gif"),
    WHITE_LOW_EMPTY_SQUARE("/quarto9.gif"),

    BLACK_HIGH_FILL_CIRCLE("/quarto6.gif"),
    BLACK_HIGH_FILL_SQUARE("/quarto4.gif"),
    BLACK_HIGH_EMPTY_CIRCLE("/quarto14.gif"),
    BLACK_HIGH_EMPTY_SQUARE("/quarto12.gif"),
    BLACK_LOW_FILL_CIRCLE("/quarto7.gif"),
    BLACK_LOW_FILL_SQUARE("/quarto5.gif"),
    BLACK_LOW_EMPTY_CIRCLE("/quarto15.gif"),
    BLACK_LOW_EMPTY_SQUARE("/quarto13.gif");

    private final String imagePath;

    PieceType(String imagePath) {
        this.imagePath = imagePath;
    }
    public Image getImage() {
        return new Image(getClass().getResourceAsStream(imagePath));
    }
    public ImageView getImageView(double width, double height) {
        ImageView imageView = new ImageView(getImage());
        imageView.setFitWidth(width);
        imageView.setFitHeight(height);
        return imageView;
    }
}
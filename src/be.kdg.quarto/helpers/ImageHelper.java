package be.kdg.quarto.helpers;

import be.kdg.quarto.model.Piece;

public class ImageHelper {
    public static String getPieceImage(Piece piece) {
        return String.format("/images/pieces/%s_%s_%s_%s.PNG", piece.getFill(), piece.getShape(), piece.getColor(), piece.getSize());
    }
}

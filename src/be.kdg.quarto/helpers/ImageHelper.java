package be.kdg.quarto.helpers;

import be.kdg.quarto.model.Piece;

public class ImageHelper {
    public static String getPieceImagePath(Piece piece) {
        return String.format("/images/pieces/%s_%s_%s_%s.PNG", piece.getFill(), piece.getShape(), piece.getColor(), piece.getSize());
    }
    public static String getButtonPath(boolean online, String state){
        String text = online ? "Online" : "Offline";
        return switch (state) {
            case "hover" -> String.format("/images/buttons/" + text + "/" + text + "_hover.png");
            case "press" -> String.format("/images/buttons/" + text + "/" + text + "_Press.png");
            default -> String.format("/images/buttons/" + text + "/" + text + ".png");
        };
    }

    public static String getLoadingButtonPath(){
        return "/images/buttons/Loading.png";
    }

}

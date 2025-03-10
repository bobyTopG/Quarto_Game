package be.kdg.quarto.view.GameScreen;

import be.kdg.quarto.model.Game;
import be.kdg.quarto.model.Piece;
import be.kdg.quarto.model.enums.Color;
import be.kdg.quarto.model.enums.Fill;
import be.kdg.quarto.model.enums.Height;
import be.kdg.quarto.model.enums.Shape;
import be.kdg.quarto.view.BoardView.BoardPresenter;
import be.kdg.quarto.view.BoardView.BoardSpaceView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.List;
import java.util.Random;

public class GamePresenter {
    private final Game model;
    private final GameView view;
    private ImageView imageView;

    public GamePresenter(Game model, GameView view) {
        this.model = model;
        this.view = view;
        view.getScene().getRoot().setStyle("-fx-background-color: #fff4d5;");

        new BoardPresenter(model, view.getBoard());

        createPieceSelection();
        updateView();
        addEventHandlers();
    }

    private void addEventHandlers() {

        //Place
        view.getBoard().getSpaceViews().forEach(boardSpaceView -> {
//            boardSpaceView.getChildren()
//                    .add(createImageView(getPieceImage(model.getSelectedPiece())));
        });

        //Pick
        view.getSelectView().getPieceViews().forEach(pieceView -> {
            // model.setSelectedPiece();
        });

    }


    private void updateView() {

        view.getSelectView().getPieceViews().forEach(pieceView -> {

        });

        view.getBoard().getSpaceViews().forEach(boardSpaceView -> {

        });


        boolean isHumanTurn = model.getCurrentPlayer().equals(model.getHuman());
        view.getTurn().setText(isHumanTurn ? "Your Turn!" : "AI Turn!");

    }

    private void createPieceSelection() {
        view.getSelectView().getPieceViews().clear();

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                PieceView pieceView = new PieceView();
                view.getSelectView().getPieceViews().add(pieceView);
                view.getSelectView().add(pieceView, i, j);
            }
        }

        for (int i = 0; i < model.getPieces().size(); i++) {
            Piece piece = model.getPieces().get(i);

            view.getSelectView().getPieceViews().get(i)
                    .getPieceImage().setImage(new Image(getPieceImage(piece)));
        }
    }

    private Piece createPieceFromImageName(String path) {
        String filename = path.substring(path.lastIndexOf("/") + 1, path.lastIndexOf("."));

        String[] parts = filename.split("_");

        if (parts.length != 4) {
            throw new IllegalArgumentException("Invalid image name format: " + path);
        }

        try {
            Color color = Color.valueOf(parts[0].toUpperCase());
            Height height = Height.valueOf(parts[1].toUpperCase());
            Fill fill = Fill.valueOf(parts[2].toUpperCase());
            Shape shape = Shape.valueOf(parts[3].toUpperCase());

            // Create and return the Piece
            return new Piece(color, height, fill, shape);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid enum value in image name: " + path, e);
        }
    }

    private String getPieceImage(Piece piece) {
        return String.format("/images/%s_%s_%s_%s.PNG", piece.getFill(), piece.getShape(), piece.getColor(), piece.getHeight());
    }
}

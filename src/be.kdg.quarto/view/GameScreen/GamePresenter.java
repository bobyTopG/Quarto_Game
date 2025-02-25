package be.kdg.quarto.view.GameScreen;

import be.kdg.quarto.model.Game;
import be.kdg.quarto.model.Piece;
import be.kdg.quarto.view.BoardView.BoardPresenter;
import be.kdg.quarto.view.BoardView.SpaceView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class GamePresenter {

    private Game model;
    private GameView view;
    private ImageView imageView;

    public GamePresenter(Game model, GameView view) {
        this.model = model;
        this.view = view;
        new BoardPresenter(model , view.getBoard());
        addEventHandlers();
        updateView();

    }

    private void addEventHandlers() {
        for (SpaceView spaceView: view.getBoard().getSpaceViews()){
            spaceView.getCircle().setOnMouseClicked(event -> {
                spaceView.getChildren().add(imageView);
                updateView();
            });
        }
    }

    private void updateView() {
        piecePreview();
        view.getScene().getRoot().setStyle("-fx-background-color: #fff4d5;");
    }

    private void piecePreview() {
        if (!model.getPieces().isEmpty()) {
            model.selectRandomPiece();
            Piece piece = model.getRandomPiece();

            String path = "/" + piece.getFill() + "_" + piece.getShape() + "_" + piece.getColor() + "_" + piece.getHeight() + ".PNG";

            imageView = new ImageView(new Image(path));
            imageView.setFitHeight(30);
            imageView.setFitWidth(30);

            view.getPiece().getChildren().add(imageView);

        }
    }


}

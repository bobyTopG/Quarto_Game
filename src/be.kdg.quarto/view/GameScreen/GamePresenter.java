package be.kdg.quarto.view.GameScreen;

import be.kdg.quarto.model.Game;
import be.kdg.quarto.model.Piece;
import be.kdg.quarto.view.BoardView.BoardPresenter;
import be.kdg.quarto.view.BoardView.SpaceView;
import be.kdg.quarto.view.PieceView.PieceView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.List;

public class GamePresenter {

    private Game model;
    private GameView view;
    private ImageView imageView;

    public GamePresenter(Game model, GameView view) {
        this.model = model;
        this.view = view;
        view.getScene().getRoot().setStyle("-fx-background-color: #fff4d5;");
        new BoardPresenter(model, view.getBoard());
        addEventHandlers();
        updateView();
    }

    private void addEventHandlers() {
        for (SpaceView spaceView : view.getBoard().getSpaceViews()) {
            spaceView.getCircle().setOnMouseClicked(event -> {
                if (!spaceView.isOwned()) {
                    spaceView.setOwned();
                    spaceView.getChildren().add(imageView);
                    updateView();
                }
            });
        }
    }

    private void updateView() {
        piecePreview();
        updatePieceSelection();
    }

    private void updatePieceSelection() {
        view.getSelectView().getPieceViewList().clear();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 5; j++) {
                PieceView pieceView = new PieceView();
                view.getSelectView().getPieceViewList().add(pieceView);
                view.getSelectView().add(pieceView, i, j);
            }
        }

        for (int i = 0; i < view.getSelectView().getPieceViewList().size(); i++) {  //set images
            PieceView pieceView = view.getSelectView().getPieceViewList().get(i);
            Piece piece = model.getPieces().get(i);
            ImageView iv = new ImageView(getPieceImage(piece));
            iv.setFitHeight(40);
            iv.setFitWidth(40);
            pieceView.getChildren().add(iv);
        }
    }


    private void piecePreview() {
        if (!model.getPieces().isEmpty()) {
            model.selectRandomPiece();
            Piece piece = model.getRandomPiece();
            imageView = new ImageView(new Image(getPieceImage(piece)));
            imageView.setFitHeight(30);
            imageView.setFitWidth(30);

            view.getPiece().getChildren().add(imageView);

        }
    }

    private String getPieceImage(Piece piece) {
        return "/" + piece.getFill() + "_" + piece.getShape() + "_" + piece.getColor() + "_" + piece.getHeight() + ".PNG";
    }


}

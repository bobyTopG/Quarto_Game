package be.kdg.quarto.view.GameScreen;

import be.kdg.quarto.model.Game;
import be.kdg.quarto.model.Piece;
import be.kdg.quarto.view.BoardView.BoardPresenter;
import be.kdg.quarto.view.BoardView.SpaceView;
import be.kdg.quarto.view.PieceView.PieceView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class GamePresenter {

    private Game model;
    private GameView view;
    private ImageView imageView;

    public GamePresenter(Game model, GameView view) {
        this.model = model;
        this.view = view;
        view.getScene().getRoot().setStyle("-fx-background-color: #fff4d5;");
        new BoardPresenter(model, view.getBoard());

        updatePieceSelection();
        updateView();
        addEventHandlers();
    }

    private void addEventHandlers() {

        if (model.getCurrentPlayer().equals(model.getAi())) {
            for (SpaceView spaceView : view.getBoard().getSpaceViews()) {
                spaceView.getCircle().setDisable(true);

            }
            for (PieceView pieceView : view.getSelectView().getPieceViews()) {
                pieceView.setDisable(false);
                try {
                    pieceView.getChildren().getLast().setOnMouseClicked(event -> aiTurn(pieceView));
                } catch (Exception e) {
                }

            }

        } else if (model.getCurrentPlayer().equals(model.getHuman())) {
            for (PieceView pieceView : view.getSelectView().getPieceViews()) {
                pieceView.setDisable(true);
            }
            for (SpaceView spaceView : view.getBoard().getSpaceViews()) {
                spaceView.getCircle().setDisable(false);

                spaceView.getCircle().setOnMouseClicked(event -> humanTurn(spaceView));

            }
        }
        updateView();
    }


    private void humanTurn(SpaceView spaceView) {
        if (!spaceView.isOwned()) {
            view.getTurn().setText("Your Turn!");
            spaceView.setOwned();
            ImageView imageView = new ImageView(getPieceImage(model.getSelectedPiece()));
            imageView.setFitHeight(30);
            imageView.setFitWidth(30);
            spaceView.getChildren().add(imageView);
          view.getPiece().setPiece(null);
            model.switchTurns();
            addEventHandlers();

        }
    }

    private void aiTurn(PieceView pieceView) {
        view.getTurn().setText("AI Turn!");
        pieceView.getChildren().clear();
        piecePreview(pieceView);
        model.setSelectedPiece(pieceView.getPiece());
        model.switchTurns();
        addEventHandlers();

    }


    private void updateView() {

    }

    private void updatePieceSelection() {
        view.getSelectView().getPieceViews().clear();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 5; j++) {
                PieceView pieceView = new PieceView();
                view.getSelectView().getPieceViews().add(pieceView);
                view.getSelectView().add(pieceView, i, j);
            }
        }

        for (int i = 0; i < view.getSelectView().getPieceViews().size(); i++) {  //set images
            if (!(model.getPieces().size() <= i)) {
                Piece piece = model.getPieces().get(i);
                PieceView pieceView = view.getSelectView().getPieceViews().get(i);
                ImageView pieceImageView = new ImageView(getPieceImage(piece));
                pieceImageView.setFitHeight(40);
                pieceImageView.setFitWidth(40);
                pieceView.getChildren().add(pieceImageView);
                pieceView.setPiece(piece);

            }
        }

    }


    private void piecePreview(PieceView pieceView) {
        if (!model.getPieces().isEmpty()) {
            imageView = new ImageView(new Image(getPieceImage(pieceView.getPiece())));
            imageView.setFitHeight(30);
            imageView.setFitWidth(30);
            view.getPiece().getChildren().add(imageView);

        }
    }

    private String getPieceImage(Piece piece) {
        return "/" + piece.getFill() + "_" + piece.getShape() + "_" + piece.getColor() + "_" + piece.getHeight() + ".PNG";
    }


}

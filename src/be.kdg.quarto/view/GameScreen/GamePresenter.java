package be.kdg.quarto.view.GameScreen;

import be.kdg.quarto.model.Game;
import be.kdg.quarto.model.Piece;
import be.kdg.quarto.model.Tile;
import be.kdg.quarto.view.BoardView.BoardPresenter;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;

public class GamePresenter {
    private final Game model;
    private final GameView view;


    public GamePresenter(Game model, GameView view) {
        this.model = model;
        this.view = view;
        new BoardPresenter(model, view.getBoard());
        view.getScene().getRoot().setStyle("-fx-background-color: #fff4d5;");

        createSelectBoard();
        updateView();
        addEventHandlers();
    }

    private void addEventHandlers() {
        //Place
        view.getBoard().getSpaceViews().forEach(boardSpaceView -> {
            boardSpaceView.getCircle().setOnMouseClicked(event -> {
                int place = view.getBoard().getSpaceViews().indexOf(boardSpaceView);
                Tile tile = new Tile();
                tile.setPiece(model.getSelectedPiece());
                model.getPlacedTiles().getTiles().set(place, tile);
                view.getPiece().getPieceImage().setImage(null);
                model.setSelectedPiece(null);
                updateView();

            });
        });

        //Pick
        view.getSelectView().getPieceViews().forEach(pieceView -> {
            pieceView.getPieceImage().setOnMouseClicked(event -> {
                if (model.getSelectedPiece() == null) {
                    model.setSelectedPiece(
                            model.createPieceFromImageName
                                    (pieceView.getPieceImage().getImage().getUrl()));
                    model.getPiecesToSelect().findTile(model.getSelectedPiece()).setPiece(null);
                    model.switchTurns();
                    updateView();
                }
            });
        });
    }

    private void updateView() {

        //Updates the current piece
        if (model.getSelectedPiece() != null) {
            view.getPiece().getPieceRect().setFill(Color.WHITE);
            view.getPiece().getPieceImage().setImage
                    (new Image(getPieceImage(model.getSelectedPiece())));
        }


        //Update select board
        for (int i = 0; i < model.getPiecesToSelect().getTiles().size(); i++) {
            if (model.getPiecesToSelect().getTiles().get(i).getPiece() != null) {
                Piece piece = model.getPiecesToSelect().getTiles().get(i).getPiece();
                view.getSelectView().getPieceViews().get(i)
                        .getPieceImage().setImage(new Image(getPieceImage(piece)));
            }
            else {
                view.getSelectView().getPieceViews().get(i).getPieceImage().setImage(null);
            }
        }


        //Updates playing board
        for (int i = 0; i < view.getBoard().getSpaceViews().size(); i++) {
            if (model.getPlacedTiles().getTiles().get(i).getPiece() != null) {
                ImageView imageView =
                        new ImageView(getPieceImage(model.getPlacedTiles().getTiles().get(i).getPiece()));
                imageView.setFitHeight(30);
                imageView.setFitWidth(30);

                view.getBoard().getSpaceViews().get(i)
                        .getChildren().add(imageView);
            }
        }

        boolean isHumanTurn = model.getCurrentPlayer().equals(model.getHuman());
        view.getTurn().setText(isHumanTurn ? "Your Turn!" : "AI Turn!");
    }

    private void createSelectBoard() {
        view.getSelectView().getPieceViews().clear();

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                PieceView pieceView = new PieceView();
                view.getSelectView().getPieceViews().add(pieceView);
                view.getSelectView().add(pieceView, i, j);
            }
        }
    }


    private String getPieceImage(Piece piece) {
        return String.format("/images/pieces/%s_%s_%s_%s.PNG", piece.getFill(), piece.getShape(), piece.getColor(), piece.getHeight());
    }
}

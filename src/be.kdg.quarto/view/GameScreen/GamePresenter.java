package be.kdg.quarto.view.GameScreen;

import be.kdg.quarto.model.Game;
import be.kdg.quarto.model.Piece;
import be.kdg.quarto.view.BoardView.BoardPresenter;
import be.kdg.quarto.view.GameScreen.SettingsScreen.SettingsPresenter;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;

import javax.swing.text.View;

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
                model.placePiece(model.getBoard().findTile(place) ,model.getHuman());


                view.getSelectedPiece().getPieceImage().setImage(null);
                updateView();

            });
        });

        //Pick
        view.getSelectView().getPieceViews().forEach(pieceView -> {
            pieceView.getPieceImage().setOnMouseClicked(event -> {
                //place piece before picking
                if (model.getSelectedPiece() == null) {

                    Piece selectedPiece = model.createPieceFromImageName(pieceView.getPieceImage().getImage().getUrl());
                    if(selectedPiece != null) {
                        model.pickPiece(selectedPiece, model.getHuman());
                    }else{
                        System.out.println("Selected Piece is null!");
                    }

                    updateView();
                }
            });
        });
        view.getQuarto().setOnMouseClicked(event -> {
            model.callQuarto();
        });

        view.getSettings().setOnAction(event -> {
            view.getOverlayContainer().setVisible(true);
            new SettingsPresenter(this, view.getSettingsView() , this.model);
        });

    }

    public void updateView() {

        //Updates the current piece
        if (model.getSelectedPiece() != null) {
            view.getSelectedPiece().getPieceRect().setFill(Color.WHITE);
            view.getSelectedPiece().getPieceImage().setImage
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

    public GameView getView() {
        return view;
    }

}

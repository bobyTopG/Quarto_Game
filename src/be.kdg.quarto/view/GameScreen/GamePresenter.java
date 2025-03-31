package be.kdg.quarto.view.GameScreen;

import be.kdg.quarto.model.Game;
import be.kdg.quarto.model.Piece;
import be.kdg.quarto.model.Statistics;
import be.kdg.quarto.model.enums.Size;
import be.kdg.quarto.view.GameScreen.BoardView.BoardPresenter;
import be.kdg.quarto.view.GameScreen.BoardView.PieceView;
import be.kdg.quarto.view.GameScreen.SettingsScreen.SettingsPresenter;
import be.kdg.quarto.view.StatisticsScreen.StatisticsPresenter;
import be.kdg.quarto.view.StatisticsScreen.StatisticsView;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class GamePresenter {
    private final Game model;
    private final GameView view;

    // Constants for piece sizes
    private static final double SMALL_PIECE_SIZE = 23.0;
    private static final double REGULAR_PIECE_SIZE = 30.0;
    private static final double SELECT_SMALL_PIECE_SIZE = 25.0;
    private static final double SELECT_REGULAR_PIECE_SIZE = 35.0;

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

            if(model.getGameSession().isGameOver()){
                //todo: show statistics
            }

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

            // Create image with proper sizing for selected piece
            Image pieceImage = new Image(getPieceImage(model.getSelectedPiece()));
            view.getSelectedPiece().getPieceImage().setImage(pieceImage);

            // Apply size based on piece properties
            boolean isSmall = model.getSelectedPiece().getSize() == Size.SMALL;
            view.getSelectedPiece().getPieceImage().setFitHeight(isSmall ? SELECT_SMALL_PIECE_SIZE : SELECT_REGULAR_PIECE_SIZE);
            view.getSelectedPiece().getPieceImage().setFitWidth(isSmall ? SELECT_SMALL_PIECE_SIZE : SELECT_REGULAR_PIECE_SIZE);
        }


        //Update select board
        for (int i = 0; i < model.getPiecesToSelect().getTiles().size(); i++) {
            if (model.getPiecesToSelect().getTiles().get(i).getPiece() != null) {
                Piece piece = model.getPiecesToSelect().getTiles().get(i).getPiece();
                Image pieceImage = new Image(getPieceImage(piece));
                view.getSelectView().getPieceViews().get(i).getPieceImage().setImage(pieceImage);

                // Apply size based on piece properties
                boolean isSmall = piece.getSize() == Size.SMALL;
                view.getSelectView().getPieceViews().get(i).getPieceImage().setFitHeight(isSmall ? SELECT_SMALL_PIECE_SIZE : SELECT_REGULAR_PIECE_SIZE);
                view.getSelectView().getPieceViews().get(i).getPieceImage().setFitWidth(isSmall ? SELECT_SMALL_PIECE_SIZE : SELECT_REGULAR_PIECE_SIZE);
            }
            else {
                view.getSelectView().getPieceViews().get(i).getPieceImage().setImage(null);
            }
        }


        //Updates playing board
        for (int i = 0; i < view.getBoard().getSpaceViews().size(); i++) {
            // Clear previous pieces
            if (view.getBoard().getSpaceViews().get(i).getChildren().size() > 1) {
                view.getBoard().getSpaceViews().get(i).getChildren().remove(1);
            }

            if (model.getPlacedTiles().getTiles().get(i).getPiece() != null) {
                Piece piece = model.getPlacedTiles().getTiles().get(i).getPiece();
                ImageView imageView = new ImageView(getPieceImage(piece));

                // Apply size based on piece properties
                boolean isSmall = piece.getSize() == Size.SMALL;
                imageView.setFitHeight(isSmall ? SMALL_PIECE_SIZE : REGULAR_PIECE_SIZE);
                imageView.setFitWidth(isSmall ? SMALL_PIECE_SIZE : REGULAR_PIECE_SIZE);

                view.getBoard().getSpaceViews().get(i).getChildren().add(imageView);
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
        return String.format("/images/pieces/%s_%s_%s_%s.PNG", piece.getFill(), piece.getShape(), piece.getColor(), piece.getSize());
    }

    public GameView getView() {
        return view;
    }

}
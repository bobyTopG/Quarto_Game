package be.kdg.quarto.view.GameScreen;

import be.kdg.quarto.model.Game;
import be.kdg.quarto.model.Piece;
import be.kdg.quarto.model.enums.Size;
import be.kdg.quarto.view.GameScreen.SettingsScreen.SettingsPresenter;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

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
        view.getScene().getRoot().setStyle("-fx-background-color: #fff4d5;");

        addEventHandlers();
        updateView();
    }

    private void addEventHandlers() {
        // Place piece on board
        for (int row = 0; row < 4; row++) {
            for (int col = 0; col < 4; col++) {
                int index = row * 4 + col;
                int finalRow = row;
                int finalCol = col;
                view.getBoardSpaces()[row][col].setOnMouseClicked(event -> {
                    model.placePiece(model.getBoard().findTile(index), model.getHuman());
                    view.getSelectedPieceImage().setImage(null);
                    updateView();
                });
            }
        }

        // Pick piece from select board
        for (int row = 0; row < 4; row++) {
            for (int col = 0; col < 4; col++) {
                ImageView pieceImageView = view.getSelectPieceImages()[row][col];
                pieceImageView.setOnMouseClicked(event -> {
                    if (model.getSelectedPiece() == null) {
                        Piece selectedPiece = model.createPieceFromImageName(pieceImageView.getImage().getUrl());
                        if (selectedPiece != null) {
                            model.pickPiece(selectedPiece, model.getHuman());
                            updateView();
                        } else {
                            System.out.println("Selected Piece is null!");
                        }
                    }
                });
            }
        }

        view.getQuarto().setOnMouseClicked(event -> {
            model.callQuarto();
            if (model.getGameSession().isGameOver()) {
                // todo: show statistics
            }
        });

        view.getSettings().setOnAction(event -> {
            view.getOverlayContainer().setVisible(true);
            new SettingsPresenter(this, view.getSettingsView(), this.model);
        });
    }

    public void updateView() {
        // Update selected piece
        if (model.getSelectedPiece() != null) {
            Image pieceImage = new Image(getPieceImage(model.getSelectedPiece()));
            view.getSelectedPieceImage().setImage(pieceImage);
            boolean isSmall = model.getSelectedPiece().getSize() == Size.SMALL;
            view.getSelectedPieceImage().setFitHeight(isSmall ? SELECT_SMALL_PIECE_SIZE : SELECT_REGULAR_PIECE_SIZE);
            view.getSelectedPieceImage().setFitWidth(isSmall ? SELECT_SMALL_PIECE_SIZE : SELECT_REGULAR_PIECE_SIZE);
        }

        // Update selection grid
        for (int i = 0; i < model.getPiecesToSelect().getTiles().size(); i++) {
            int row = i / 4;
            int col = i % 4;
            ImageView imageView = view.getSelectPieceImages()[row][col];
            if (model.getPiecesToSelect().getTiles().get(i).getPiece() != null) {
                Piece piece = model.getPiecesToSelect().getTiles().get(i).getPiece();
                Image img = new Image(getPieceImage(piece));
                imageView.setImage(img);
                boolean isSmall = piece.getSize() == Size.SMALL;
                imageView.setFitHeight(isSmall ? SELECT_SMALL_PIECE_SIZE : SELECT_REGULAR_PIECE_SIZE);
                imageView.setFitWidth(isSmall ? SELECT_SMALL_PIECE_SIZE : SELECT_REGULAR_PIECE_SIZE);
            } else {
                imageView.setImage(null);
            }
        }

        // Update board grid
        for (int i = 0; i < model.getPlacedTiles().getTiles().size(); i++) {
            int row = i / 4;
            int col = i % 4;
            Piece piece = model.getPlacedTiles().getTiles().get(i).getPiece();
            StackPane space = view.getBoardSpaces()[row][col];

            // Remove any piece image
            if (space.getChildren().size() > 1) {
                space.getChildren().remove(1);
            }

            if (piece != null) {
                ImageView imageView = new ImageView(getPieceImage(piece));
                boolean isSmall = piece.getSize() == Size.SMALL;
                imageView.setFitHeight(isSmall ? SMALL_PIECE_SIZE : REGULAR_PIECE_SIZE);
                imageView.setFitWidth(isSmall ? SMALL_PIECE_SIZE : REGULAR_PIECE_SIZE);
                space.getChildren().add(imageView);
            }
        }

        // Update turn label
        boolean isHumanTurn = model.getCurrentPlayer().equals(model.getHuman());
        view.getTurn().setText(isHumanTurn ? "Your Turn!" : "AI Turn!");
    }

    private String getPieceImage(Piece piece) {
        return String.format("/images/pieces/%s_%s_%s_%s.PNG", piece.getFill(), piece.getShape(), piece.getColor(), piece.getSize());
    }

    public GameView getView() {
        return view;
    }
}
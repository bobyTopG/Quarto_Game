package be.kdg.quarto.view.GameScreen;

import be.kdg.quarto.model.Game;
import be.kdg.quarto.model.Piece;
import be.kdg.quarto.view.BoardView.BoardPresenter;
import be.kdg.quarto.view.BoardView.SpaceView;
import be.kdg.quarto.view.PieceView.PieceView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class GamePresenter {
    private final Game model;
    private final GameView view;
    private ImageView imageView;

    public GamePresenter(Game model, GameView view) {
        this.model = model;
        this.view = view;

        // Set background color
        view.getScene().getRoot().setStyle("-fx-background-color: #fff4d5;");

        // Initialize Board
        new BoardPresenter(model, view.getBoard());

        updatePieceSelection();
        updateView();
        addEventHandlers();
    }

    private void addEventHandlers() {
        boolean isAITurn = model.getCurrentPlayer().equals(model.getAi());

        // Toggle interactivity based on turn
        for (SpaceView spaceView : view.getBoard().getSpaceViews()) {
            spaceView.getCircle().setDisable(isAITurn);
            if (!isAITurn) {
                spaceView.getCircle().setOnMouseClicked(event -> humanTurn(spaceView));
            }
        }

        for (PieceView pieceView : view.getSelectView().getPieceViews()) {
            pieceView.setDisable(!isAITurn);
            if (isAITurn && !pieceView.getChildren().isEmpty()) {
                pieceView.getChildren().get(pieceView.getChildren().size() - 1)
                        .setOnMouseClicked(event -> aiTurn(pieceView));
            }
        }

        updateView();
    }

    private void humanTurn(SpaceView spaceView) {
        if (spaceView.isOwned()) return; // Prevent overriding existing pieces

        view.getTurn().setText("Your Turn!");
        spaceView.setOwned();

        // Add selected piece image to the board
        ImageView pieceImage = createImageView(getPieceImage(model.getSelectedPiece()), 30, 30);
        spaceView.getChildren().add(pieceImage);

        // Clear preview section
        view.getPiece().getChildren().clear();
        view.getPiece().setPiece(null);

        // Switch turns
        model.switchTurns();
        addEventHandlers();
    }

    private void aiTurn(PieceView pieceView) {
        view.getTurn().setText("AI Turn!");

        pieceView.getChildren().clear(); // Remove piece from selection
        piecePreview(pieceView); // Show preview
        model.setSelectedPiece(pieceView.getPiece());

        model.switchTurns();
        addEventHandlers();
    }

    private void updateView() {
        boolean isHumanTurn = model.getCurrentPlayer().equals(model.getHuman());

        // Update piece selection availability
        for (PieceView pieceView : view.getSelectView().getPieceViews()) {
            pieceView.setDisable(isHumanTurn);
        }

        // Update board spaces availability
        for (SpaceView spaceView : view.getBoard().getSpaceViews()) {
            spaceView.getCircle().setDisable(!isHumanTurn);
        }

        // Update turn indicator
        view.getTurn().setText(isHumanTurn ? "Your Turn!" : "AI Turn!");
    }

    private void updatePieceSelection() {
        view.getSelectView().getPieceViews().clear();

        // Create a 3x5 grid for pieces
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 5; j++) {
                PieceView pieceView = new PieceView();
                view.getSelectView().getPieceViews().add(pieceView);
                view.getSelectView().add(pieceView, i, j);
            }
        }

        // Assign images to pieces
        for (int i = 0; i < view.getSelectView().getPieceViews().size(); i++) {
            if (i >= model.getPieces().size()) continue;

            Piece piece = model.getPieces().get(i);
            PieceView pieceView = view.getSelectView().getPieceViews().get(i);

            ImageView pieceImageView = createImageView(getPieceImage(piece), 40, 40);
            pieceView.getChildren().add(pieceImageView);
            pieceView.setPiece(piece);
        }
    }

    private void piecePreview(PieceView pieceView) {
        if (model.getPieces().isEmpty()) return;

        view.getPiece().getChildren().clear(); // Clear old images
        imageView = createImageView(getPieceImage(pieceView.getPiece()), 30, 30);
        view.getPiece().getChildren().add(imageView);
    }

    private String getPieceImage(Piece piece) {
        return "/" + piece.getFill() + "_" + piece.getShape() + "_" + piece.getColor() + "_" + piece.getHeight() + ".PNG";
    }

    private ImageView createImageView(String path, double width, double height) {
        ImageView imgView = new ImageView(new Image(path));
        imgView.setFitWidth(width);
        imgView.setFitHeight(height);
        return imgView;
    }
}
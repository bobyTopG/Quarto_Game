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
    private boolean isPieceSelected = false;

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

        view.getSelectView().getPieceViews().forEach(pieceView -> {
            pieceView.setDisable(false);
            pieceView.getChildren().get(pieceView.getChildren().size() - 1)
                    .setOnMouseClicked(event -> pickPiece(pieceView));
        });

        view.getBoard().getSpaceViews().forEach(spaceView -> {
            spaceView.getCircle().setDisable(false);
            spaceView.getCircle().setOnMouseClicked(event ->
                    placePiece(spaceView));

        });
    }

    private void placePiece(SpaceView spaceView) {
        if (spaceView.isOwned()) return; // Prevent overriding existing pieces
        isPieceSelected = false;
        spaceView.setOwned();
        spaceView.getChildren().add(createImageView(getPieceImage(model.getSelectedPiece()), 30, 30));

        // Clear preview section
        view.getPiece().getChildren().clear();
        view.getPiece().getChildren().add(new PieceView());

        updateView();
        addEventHandlers();
    }

    private void pickPiece(PieceView pieceView) {
        isPieceSelected = true;
        pieceView.getChildren().clear(); // Remove piece from selection
        model.setSelectedPiece(pieceView.getPiece());
        piecePreview(pieceView);
        model.switchTurns();
        updateView();
        addEventHandlers();
    }

    private void updateView() {

        view.getSelectView().getPieceViews().forEach(pieceView -> {
            pieceView.setDisable(isPieceSelected);
        });

        view.getBoard().getSpaceViews().forEach(spaceView -> {
            spaceView.getCircle().setDisable(!isPieceSelected);
        });


        boolean isHumanTurn = model.getCurrentPlayer().equals(model.getHuman());
        view.getTurn().setText(isHumanTurn ? "Your Turn!" : "AI Turn!");
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

        for (int i = 0; i < view.getSelectView().getPieceViews().size() && i < model.getPieces().size(); i++) {
            Piece piece = model.getPieces().get(i);
            PieceView pieceView = view.getSelectView().getPieceViews().get(i);

            pieceView.getChildren().add(createImageView(getPieceImage(piece), 40, 40));
            pieceView.setPiece(piece);
        }
    }

    private void piecePreview(PieceView pieceView) {
        if (model.getPieces().isEmpty()) return;

        view.getPiece().getChildren().clear();
        imageView = createImageView(getPieceImage(pieceView.getPiece()), 30, 30);
        PieceView pieceView1 = new PieceView();
        pieceView1.getChildren().add(imageView);
        view.getPiece().getChildren().add(pieceView1);
    }

    private String getPieceImage(Piece piece) {
        return String.format("/images/%s_%s_%s_%s.PNG", piece.getFill(), piece.getShape(), piece.getColor(), piece.getHeight());
    }

    private ImageView createImageView(String path, double width, double height) {
        ImageView imgView = new ImageView(new Image(path));
        imgView.setFitWidth(width);
        imgView.setFitHeight(height);
        return imgView;
    }
}

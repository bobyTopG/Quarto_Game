package be.kdg.quarto.view.GameScreen;

import be.kdg.quarto.model.Board;
import be.kdg.quarto.model.Game;
import be.kdg.quarto.model.Piece;
import be.kdg.quarto.model.Tile;
import be.kdg.quarto.view.BoardView.BoardPresenter;
import be.kdg.quarto.view.GameScreen.SettingsScreen.SettingsPresenter;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import java.util.Optional;

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

    public GameView getView() {
        return view;
    }

    private void addEventHandlers() {
        //Place
        view.getBoard().getSpaceViews().forEach(boardSpaceView ->
                boardSpaceView.getCircle().setOnMouseClicked(event -> {

                    int place = view.getBoard().getSpaceViews().indexOf(boardSpaceView);
                    Tile tile = new Tile();

                    tile.setPiece(model.getCurrentTile().getPiece());
                    model.getPlacedTiles().getTiles().set(place, tile);

                    view.getPiece().getPieceImage().setImage(null);

                    model.getCurrentTile().setPiece(null);
                    updateView();
                    model.getGameRules().isGameOver();
                }));

        //Pick
        view.getSelectView().getPieceViews().forEach(pieceView ->
                pieceView.getPieceImage().setOnMouseClicked(event -> {
                    if (model.getCurrentTile().getPiece() == null) {

                        model.setCurrentTile(new Tile(
                                model.createPieceFromImageName
                                        (pieceView.getPieceImage().getImage().getUrl())));

                        model.getTilesToSelect().getTiles().
                                get(model.getTilesToSelect().getTiles()
                                        .indexOf(model.getCurrentTile())).setPiece(null);

                        if (model.getGameRules().isGameOver()) {
                            model.getGameRules().setWinner(model.getCurrentPlayer());
                            model.getGameSession().setWinner(model.getCurrentPlayer());
                        }

                        model.switchTurns();
                        updateView();
                    }
                }));


        view.getSettings().setOnAction(event -> {
            view.getOverlayContainer().setVisible(true); // Show settings overlay
            new SettingsPresenter(this, view.getSettingsView() , model);
        });
    }

    private void updateView() {

        //Game Over
        if (model.getGameRules().isGameOver()) {

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Game Over");
            alert.setContentText("Do you want to play again?");

            if (!model.getGameRules().isTie()) {

                alert.setHeaderText("Winner is : " + model.getGameRules().getWinner().getName());
                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ButtonType.OK) {
                    model.restartGame();
                }
                alert.showAndWait();
            } else {
                alert.setHeaderText("Tie");
                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ButtonType.OK) {
                    model.restartGame();
                }
                alert.showAndWait();
            }
        }


        //Updates the current piece
        if (model.getCurrentTile().getPiece() != null) {
            view.getPiece().getPieceRect().setFill(Color.WHITE);
            view.getPiece().getPieceImage().setImage
                    (new Image(getPieceImage(model.getCurrentTile().getPiece())));
        }


        //Update select board
        for (int i = 0; i < model.getTilesToSelect().getTiles().size(); i++) {
            if (model.getTilesToSelect().getTiles().get(i).getPiece() != null) {
                Piece piece = model.getTilesToSelect().getTiles().get(i).getPiece();
                view.getSelectView().getPieceViews().get(i)
                        .getPieceImage().setImage(new Image(getPieceImage(piece)));
            } else {
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

                view.getBoard().getSpaceViews().get(i).getChildren().add(imageView);
            } else {
                view.getBoard().getSpaceViews().get(i).reset();
            }
        }


        //Update turn label
        boolean isHumanTurn = model.getCurrentPlayer().equals(model.getHuman());
        view.getTurn().setText(isHumanTurn ? "Your Turn!" : "AI Turn!");
    }

    private void createSelectBoard() {
        view.getSelectView().getPieceViews().clear();

        for (int i = 0; i < Board.BOARD_SIZE; i++) {
            for (int j = 0; j < Board.BOARD_SIZE; j++) {
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

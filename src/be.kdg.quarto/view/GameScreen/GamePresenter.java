package be.kdg.quarto.view.GameScreen;


import be.kdg.quarto.model.Board;
import be.kdg.quarto.model.GameSession;
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
    private final GameSession session;
    private final GameView view;

    public GamePresenter(GameSession session, GameView view) {
        this.session = session;
        this.view = view;
        new BoardPresenter(session.getModel(), view.getBoard());
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

                    tile.setPiece(session.getModel().getCurrentTile().getPiece());
                    session.getModel().getPlacedTiles().getTiles().set(place, tile);

                    view.getPiece().getPieceImage().setImage(null);

                    session.getModel().getCurrentTile().setPiece(null);
                    updateView();
                    session.getModel().getGameRules().isGameOver();
                    session.getModel().getGameRules().setWinner(session.getCurrentPlayer());
                }));

        //Pick
        view.getSelectView().getPieceViews().forEach(pieceView ->
                pieceView.getPieceImage().setOnMouseClicked(event -> {
                    if (session.getModel().getCurrentTile().getPiece() == null) {

                        session.getModel().setCurrentTile(new Tile(
                                session.getModel().createPieceFromImageName
                                        (pieceView.getPieceImage().getImage().getUrl())));

                        session.getModel().getTilesToSelect().getTiles().
                                get(session.getModel().getTilesToSelect().getTiles()
                                        .indexOf(session.getModel().getCurrentTile())).setPiece(null);

                        if (session.getModel().getGameRules().isGameOver()) {
                            session.getModel().getGameRules().setWinner(session.getCurrentPlayer());
                        }

                        session.switchTurns();
                        updateView();
                    }
                }));


        view.getSettings().setOnAction(event -> {
            view.getOverlayContainer().setVisible(true);
            new SettingsPresenter(this, view.getSettingsView() , session);
        });
    }

    private void updateView() {
        //Game Over
        if (session.getModel().getGameRules().isGameOver()) {

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Game Over");
            alert.setContentText("Do you want to play again?");

            if (!session.getModel().getGameRules().isTie()) {

                alert.setHeaderText("Winner is : " + session.getModel().getGameRules().getWinner().getName());
                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ButtonType.OK) {
                    session.restartGame();
                }
                alert.showAndWait();
            } else {
                alert.setHeaderText("Tie");
                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ButtonType.OK) {
                    session.restartGame();
                }
                alert.showAndWait();
            }
        }


        //Updates the current piece
        if (session.getModel().getCurrentTile().getPiece() != null) {
            view.getPiece().getPieceRect().setFill(Color.WHITE);
            view.getPiece().getPieceImage().setImage
                    (new Image(getPieceImage(session.getModel().getCurrentTile().getPiece())));
        }


        //Update select board
        for (int i = 0; i < session.getModel().getTilesToSelect().getTiles().size(); i++) {
            if (session.getModel().getTilesToSelect().getTiles().get(i).getPiece() != null) {
                Piece piece = session.getModel().getTilesToSelect().getTiles().get(i).getPiece();
                view.getSelectView().getPieceViews().get(i)
                        .getPieceImage().setImage(new Image(getPieceImage(piece)));
            } else {
                view.getSelectView().getPieceViews().get(i).getPieceImage().setImage(null);
            }
        }


        //Updates playing board
        for (int i = 0; i < view.getBoard().getSpaceViews().size(); i++) {
            if (session.getModel().getPlacedTiles().getTiles().get(i).getPiece() != null) {
                ImageView imageView =
                        new ImageView(getPieceImage(session.getModel().getPlacedTiles().getTiles().get(i).getPiece()));
                imageView.setFitHeight(30);
                imageView.setFitWidth(30);

                view.getBoard().getSpaceViews().get(i).getChildren().add(imageView);
            } else {
                view.getBoard().getSpaceViews().get(i).reset();
            }
        }


        //Update turn label
        boolean isHumanTurn = session.getCurrentPlayer().equals(session.getHuman());
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

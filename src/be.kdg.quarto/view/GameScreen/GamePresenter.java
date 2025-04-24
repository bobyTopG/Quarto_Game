package be.kdg.quarto.view.GameScreen;

import be.kdg.quarto.helpers.ImageHelper;
import be.kdg.quarto.model.Ai;
import be.kdg.quarto.model.GameSession;
import be.kdg.quarto.model.Move;
import be.kdg.quarto.model.Piece;
import be.kdg.quarto.model.Statistics;
import be.kdg.quarto.model.Tile;
import be.kdg.quarto.model.enums.Size;
import be.kdg.quarto.model.strategies.rulebasedsystem.InterfaceEngine;
import be.kdg.quarto.view.GameScreen.Cells.BoardCell;
import be.kdg.quarto.view.GameScreen.Cells.SelectCell;
import be.kdg.quarto.view.StartScreen.StartPresenter;
import be.kdg.quarto.view.StartScreen.StartView;
import be.kdg.quarto.view.StatisticsScreen.StatisticsPresenter;
import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.scene.image.Image;
import javafx.util.Duration;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GamePresenter {
    private static final double SELECT_SMALL_PIECE_SIZE = 30.0;
    private static final double SELECT_REGULAR_PIECE_SIZE = 45.0;

    private Move move = new Move();
    private final GameSession model;
    private final GameView view;
    private final InterfaceEngine engine = new InterfaceEngine();
    private SelectCell selectedPiece;
    private BoardCell selectedTile;
    private List<BoardCell> board;
    private List<SelectCell> piecesToSelect;

    public GamePresenter(GameSession model, GameView view) {
        this.model = model;
        this.view = view;

        view.getScene().getRoot().setStyle("-fx-background-color: #fff4d5;");
        createBoard();
        createSelectPieces();
        setUpTimer();
        addEventHandlers();
        updateView();
    }

    private void createBoard() {
        board = new ArrayList<>();
        for (int r = 0; r < 4; r++) {
            for (int c = 0; c < 4; c++) {
                BoardCell cell = new BoardCell(r, c, 21);
                view.getBoardGrid().add(cell.getCellVisual(), r, c);
                board.add(cell);
            }
        }
    }

    private void createSelectPieces() {
        piecesToSelect = new ArrayList<>();
        for (int r = 0; r < 4; r++) {
            for (int c = 0; c < 4; c++) {
                int index = r * 4 + c;
                SelectCell cell = new SelectCell(r, c, (r + c) % 2 == 0 ? "#ffffff" : "#DBD6B2");
                cell.setPiece(model.getGame().getPiecesToSelect().getTiles().get(index).getPiece());
                view.getSelectGrid().add(cell.getCellVisual(), c, r);
                piecesToSelect.add(cell);
            }
        }
    }

    private void setUpTimer() {
        Timeline uiUpdateTimer = new Timeline(new KeyFrame(Duration.seconds(1), event -> updateTimerDisplay()));
        uiUpdateTimer.setCycleCount(Timeline.INDEFINITE);
        uiUpdateTimer.play();
    }

    private void updateTimerDisplay() {
        view.setTimer(model.getGameTimer().getGameDurationInSeconds());
    }

    private void addEventHandlers() {

        view.getHelpButton().selectedProperty().addListener((obs, oldVal, newVal) -> {
            updateMassage();
        });

        for (int index = 0; index < board.size(); index++) {
            BoardCell boardCell = board.get(index);
            int finalIndex = index;
            boardCell.getCellVisual().setOnMouseEntered(event -> boardCell.hover());
            boardCell.getCellVisual().setOnMouseExited(event -> boardCell.unhover());
            boardCell.getCellVisual().setOnMouseClicked(event -> onBoardCellClicked(finalIndex, boardCell));
        }

        for (SelectCell selectCell : piecesToSelect) {
            selectCell.getCellVisual().setOnMouseClicked(event -> onSelectCellClicked(selectCell));
            selectCell.getCellVisual().setOnMouseEntered(event -> selectCell.hover());
            selectCell.getCellVisual().setOnMouseExited(event -> selectCell.unhover());
        }

        view.getChoosePiece().setOnMouseClicked(event -> view.switchToChoosePiece());


        view.getPlacePiece().setOnAction(event -> {
            placePiece();

            engine.determineFacts(model);
            engine.applyRules(model.getGame(), move);
            updateView();
        });

        view.getBackButton().setOnAction(event -> view.switchToMainSection());

        view.getChoosePieceConfirmButton().setOnAction(event -> {
            confirmPieceSelection();
            engine.determineFacts(model);
            engine.applyRules(model.getGame(), move);
            updateView();
        });


        view.getQuarto().setOnMouseClicked(event -> handleQuarto());
        view.getSettings().setOnAction(event -> {
            view.showSettingsScreen();
            model.getGameTimer().pauseGame();
            new SettingsPresenter(this, view.getSettingsView(), model);
        });
    }

    private void onBoardCellClicked(int index, BoardCell boardCell) {
        if (model.getGame().getBoard().findTile(index).getPiece() == null) {
            if (selectedTile != null) {
                if (selectedTile.equals(boardCell)) {
                    placePiece();
                } else {
                    selectedTile.deselect();
                    boardCell.select();
                    selectedTile = boardCell;
                }
            } else {
                boardCell.select();
                selectedTile = boardCell;
            }
        }
    }

    private void onSelectCellClicked(SelectCell selectCell) {
        if (selectedPiece != null) selectedPiece.deselect();
        selectedPiece = selectCell;
        selectCell.select();
    }

    private void placePiece() {
        if (selectedTile == null) return;
        Tile tile = model.getGame().getBoard().findTile(selectedTile.getRow() * 4 + selectedTile.getColumn());

        if (tile.getPiece() == null) {
            model.placePiece(tile);
            model.getGame().setSelectedPiece(null);
        } else {
            System.out.println("You can't place a piece on a tile that already has a piece!");
        }

        selectedTile.deselect();
        selectedTile = null;
        updateView();
    }

    private void confirmPieceSelection() {
        if (selectedPiece == null) {
            System.out.println("Selected Piece is null!");
            return;
        }

        try {
            model.pickPiece(selectedPiece.getPiece());
        } catch (NullPointerException e) {
            view.getQuartoText().setText("No piece selected!");
        }
        selectedPiece.deselect();
        selectedPiece.setPiece(null);
        selectedPiece = null;
        view.switchToMainSection();
        updateView();

        if (model.getOpponent() instanceof Ai) {
            handleAiTurn();
        }
    }

    private void handleAiTurn() {
        Random rand = new Random();
        float placeDuration = rand.nextFloat(1.5f) + 1;
        float pickDuration = rand.nextFloat(1.5f) + 1;

        animateLoadingBar(placeDuration + pickDuration);

        PauseTransition placePieceDelay = new PauseTransition(Duration.seconds(placeDuration));
        updateView();

        placePieceDelay.setOnFinished(event -> {
            model.placePieceAi();
            engine.determineFacts(model);
            engine.applyRules(model.getGame(), move);
            if (model.isCallingQuarto()) {
                handleQuarto();
            }
            updateView();

            model.handlePendingWin();

            PauseTransition pickPieceDelay = new PauseTransition(Duration.seconds(pickDuration));
            pickPieceDelay.setOnFinished(e -> {
                model.pickPieceAi();
                engine.determineFacts(model);
                engine.applyRules(model.getGame(), move);
                updateView();
            });
            pickPieceDelay.play();
        });

        placePieceDelay.play();
    }

    private void handleQuarto() {
        model.callQuarto();
        if (model.getGame().getGameRules().checkWin()) {
            view.showStatisticsScreen();
            view.getStatisticsView().getCloseBtn().setOnMouseClicked(event -> {
                StartView startView = new StartView();
                view.getScene().setRoot(startView);
                new StartPresenter(startView);
            });
            try {
                new StatisticsPresenter(view.getStatisticsView(), new Statistics(model.getGameSessionId()));
            } catch (SQLException e) {
                System.out.println("Error loading statistics: " + e.getMessage());
            }
        }
    }

    public void updateView() {
        updateMassage();
        updateSelectedPiece();
        updateBoard();
        updateSelectGrid();
        updateTurnInfo();
    }

    private void updateMassage() {
        if (model.getGame().getGameRules().checkWin() && view.getHelpButton().isSelected() && model.getCurrentPlayer() == model.getPlayer()) {
            view.getQuartoText().setText("Press the button \uD83D\uDD3D to win\uD83C\uDFC6");
        } else {
            if (view.getHelpButton().isSelected() && move.getWarningMessage() != null && model.getCurrentPlayer() == model.getPlayer()) {
                view.getQuartoText().setText(move.getWarningMessage());
            } else {
                view.getQuartoText().setText("");
            }
        }
    }

    private void updateSelectedPiece() {
        Piece selected = model.getGame().getSelectedPiece();
        if (selected != null) {
            Image img = new Image(ImageHelper.getPieceImage(selected));
            view.getSelectedPieceImage().setImage(img);
            double size = selected.getSize() == Size.SMALL ? SELECT_SMALL_PIECE_SIZE : SELECT_REGULAR_PIECE_SIZE;
            view.getSelectedPieceImage().setFitHeight(size);
            view.getSelectedPieceImage().setFitWidth(size);
        } else {
            view.getSelectedPieceImage().setImage(null);
        }
    }

    private void updateBoard() {
        for (int i = 0; i < model.getGame().getBoard().getTiles().size(); i++) {
            Piece piece = model.getGame().getBoard().getTiles().get(i).getPiece();
            BoardCell cell = board.get(i);
            cell.setPiece(piece);
        }
    }

    private void updateSelectGrid() {
        for (int i = 0; i < model.getGame().getPiecesToSelect().getTiles().size(); i++) {
            Piece piece = model.getGame().getPiecesToSelect().getTiles().get(i).getPiece();
            piecesToSelect.get(i).setPiece(piece);
        }
    }

    private void updateTurnInfo() {
        boolean isHumanTurn = model.getCurrentPlayer().equals(model.getPlayer());
        boolean pieceSelected = model.getGame().getSelectedPiece() != null;
        boolean isVsAi = model.getOpponent() instanceof Ai;
        view.getLoadingBar().setOpacity(0);
        view.getTurn().setStyle(isHumanTurn ? "-fx-background-color: #29ABE2" : "-fx-background-color: transparent");

        if (isHumanTurn) {
            if (pieceSelected) {
                view.getChoosePiece().setDisable(true);
                view.getPlacePiece().setDisable(false);
                view.getTurn().setText("Your turn to place piece");
            } else {
                view.getChoosePiece().setDisable(false);
                view.getPlacePiece().setDisable(true);
                view.getTurn().setText("Your turn to choose piece");
            }
        } else {
            if (isVsAi) {
                view.getLoadingBar().setOpacity(1);
                view.getChoosePiece().setDisable(true);
                view.getPlacePiece().setDisable(true);
                view.getTurn().setText(view.getOpponentName() + " is thinking...");
            } else {
                view.getTurn().setStyle("-fx-background-color: rgb(218,66,66)");

                if (pieceSelected) {
                    view.getChoosePiece().setDisable(true);
                    view.getPlacePiece().setDisable(false);
                    view.getTurn().setText("Opponent's turn to place piece");
                } else {
                    view.getChoosePiece().setDisable(false);
                    view.getPlacePiece().setDisable(true);
                    view.getTurn().setText("Opponent's turn to choose piece");
                }
            }
        }
    }

    private void animateLoadingBar(double durationInSeconds) {
        // Reset the loading bar to 0
        view.getLoadingBar().setProgress(0);

        // Create a timeline that will update the progress bar over the duration
        Timeline timeline = new Timeline();

        // We'll update the progress bar 60 times per second for smooth animation
        int totalFrames = (int) (durationInSeconds * 60);
        double incrementPerFrame = 1.0 / totalFrames;

        // Create the keyframes for the animation
        for (int i = 0; i <= totalFrames; i++) {
            final int frameNumber = i;
            KeyFrame keyFrame = new KeyFrame(
                    Duration.seconds(durationInSeconds * i / totalFrames),
                    event -> view.getLoadingBar().setProgress((double) frameNumber / totalFrames)
            );
            timeline.getKeyFrames().add(keyFrame);
        }

        // Start the animation
        timeline.play();
    }

    public GameView getView() {
        return view;
    }
}
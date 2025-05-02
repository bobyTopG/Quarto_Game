package be.kdg.quarto.view.GameScreen;

import be.kdg.quarto.helpers.ErrorHelper;
import be.kdg.quarto.helpers.ImageHelper;
import be.kdg.quarto.model.Ai;
import be.kdg.quarto.model.GameSession;
import be.kdg.quarto.model.Move;
import be.kdg.quarto.model.Piece;
import be.kdg.quarto.model.Statistics;
import be.kdg.quarto.model.Tile;
import be.kdg.quarto.model.enums.Size;
import be.kdg.quarto.model.strategies.rulebasedsystem.InferenceEngine;
import be.kdg.quarto.view.GameScreen.Cells.BoardCell;
import be.kdg.quarto.view.GameScreen.Cells.SelectCell;
import be.kdg.quarto.view.SettingsScreen.SettingsPresenter;
import be.kdg.quarto.view.StartScreen.StartPresenter;
import be.kdg.quarto.view.StartScreen.StartView;
import be.kdg.quarto.view.StatisticsScreen.StatisticsPresenter;
import be.kdg.quarto.view.WinScreen.WinPresenter;
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

    private final Move move = new Move();
    private final GameSession model;
    private final GameView view;
    private final InferenceEngine inferenceEngine = new InferenceEngine();
    private SelectCell selectedPiece;
    private BoardCell selectedTile;
    private List<BoardCell> board;
    private List<SelectCell> piecesToSelect;

    private SettingsPresenter settingsPresenter;
    private WinPresenter winPresenter;



    public GamePresenter(GameSession model, GameView view) {
        this.model = model;
        this.view = view;

        view.getScene().getRoot().setStyle("-fx-background-color: #fff4d5;");
        createBoard();
        createSelectPieces();
        setUpTimer();
        addEventHandlers();
        updateView();

        handleAiStartTurn();

    }

    /**
     * creates an empty board with Board Cells
     */
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

    /**
     * creates an empty Board with selectCells
     */
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

    /**
     * sets up and starts the timer
     */
    private void setUpTimer() {
        updateTimerDisplay();
        Timeline uiUpdateTimer = new Timeline(new KeyFrame(Duration.seconds(1), event -> updateTimerDisplay()));
        uiUpdateTimer.setCycleCount(Timeline.INDEFINITE);
        uiUpdateTimer.play();
    }

    /**
     * updates the timers visuals
     */
    private void updateTimerDisplay() {
        view.setTimer(model.getGameTimer().getGameDurationInSeconds());
    }

    private void addEventHandlers() {

        view.getHelpButton().selectedProperty().addListener((obs, oldVal, newVal) -> updateMassage());


        //board , adds events for each cell
        for (int index = 0; index < board.size(); index++) {
            BoardCell boardCell = board.get(index);
            int finalIndex = index;
            boardCell.getCellVisual().setOnMouseEntered(event -> boardCell.hover());
            boardCell.getCellVisual().setOnMouseExited(event -> boardCell.unhover());
            boardCell.getCellVisual().setOnMouseClicked(event -> onBoardCellClicked(finalIndex, boardCell));
        }
        //select, adds events for each cell
        for (SelectCell selectCell : piecesToSelect) {
            selectCell.getCellVisual().setOnMouseClicked(event -> {
                try {
                    onSelectCellClicked(selectCell);
                } catch (Exception e) {
                    ErrorHelper.showError(e,"Failed to select Cell");
                }
            });
            selectCell.getCellVisual().setOnMouseEntered(event -> selectCell.hover());
            selectCell.getCellVisual().setOnMouseExited(event -> selectCell.unhover());
        }

        view.getChoosePiece().setOnMouseClicked(event -> view.switchToChoosePiece());


        view.getPlacePiece().setOnAction(event -> {
            placePiece();

            inferenceEngine.determineFacts(model);
            inferenceEngine.applyRules(model.getGame(), move);

            //forces call Quarto on last Move
            if(model.getGame().getPiecesToSelect().isEmpty()){
                try {
                    handleQuarto();
                } catch (SQLException e) {
                    ErrorHelper.showDBError(e);
                }
            }
            updateView();
        });

        view.getBackButton().setOnAction(event -> view.switchToMainSection());

        view.getChoosePieceConfirmButton().setOnAction(event -> {
            try {
                confirmPieceSelection();
            } catch (Exception e) {
                ErrorHelper.showError(e,"Failed to select Piece");
            }
            inferenceEngine.determineFacts(model);
            inferenceEngine.applyRules(model.getGame(), move);
            updateView();
        });


        view.getQuarto().setOnMouseClicked(event -> {
            try {
                handleQuarto();
            } catch (SQLException e) {
                ErrorHelper.showDBError(e);
            }
        });
        view.getSettings().setOnAction(event -> {
            model.getGameTimer().pauseGame();
            pauseAnimations();
            view.showSettingsScreen();

            // Create the settings presenter if it doesn't exist yet
            if (settingsPresenter == null) {
                settingsPresenter = new SettingsPresenter(view.getSettingsView(), model, this);
            }
        });


    }
    /**
     * handles the response for BoardCell Clicked
     * @param boardCell specifies which cell was clicked
     * @param index the position on the board of the boardCell
     */
    private void onBoardCellClicked(int index, BoardCell boardCell) {
        if (model.getGame().getBoard().findTile(index).getPiece() == null && (model.getPlayer() == model.getCurrentPlayer() || model.getOpponent().getName().equals("Friend"))) {
            if (selectedTile != null) {
                if (selectedTile.equals(boardCell) && !view.getPlacePiece().isDisabled()) {
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

    /**
     * handles the response for SelectCell Clicked
     * @param selectCell specifies which cell was clicked
     */
    private void onSelectCellClicked(SelectCell selectCell) throws Exception {
        if(selectCell.getPiece() != null){
            //double click
            if(selectCell == selectedPiece){
                confirmPieceSelection();
                inferenceEngine.determineFacts(model);
                inferenceEngine.applyRules(model.getGame(), move);
                updateView();
            }else{
                if (selectedPiece != null) selectedPiece.deselect();
                selectedPiece = selectCell;
                selectCell.select();

            }
        }
    }

    /**
     * handles placePiece, updates the visuals and model, also resets the SelectedPiece
     */
    private void placePiece() {
        if (selectedTile == null) return;
        Tile tile = model.getGame().getBoard().findTile(selectedTile.getRow() * 4 + selectedTile.getColumn());

        if (tile.getPiece() == null && model.getGame().getSelectedPiece() != null) {
            model.placePiece(tile);
            model.getGame().setSelectedPiece(null);

            try {
                checkForGameEnd();
            } catch (SQLException e) {
                ErrorHelper.showDBError(e);
            }
        } else {
            ErrorHelper.showError(new Exception("You can't place a piece on a tile that already has a piece!"),"Failed to place piece");
        }

        selectedTile.deselect();
        selectedTile = null;
        updateView();
    }

    /**
     * checks if the game is over, which happens after all the pieces are placed.
     * If the game is over and there is a winning position, assign the win to the current Player
     */
    private void checkForGameEnd() throws SQLException {
        if(model.getGame().getPiecesToSelect().isEmpty()){
            boolean isWon = model.getGame().getGameRules().checkWin();
            model.endGameSession(!isWon);
            showEndScreen(!isWon);
        }
    }

    /**
     * picks the piece and updates the visuals and model with the chosen piece.
     * The piece is picked only if a piece from the Selected Cells is chosen,
     */
    private void confirmPieceSelection() throws Exception {
        if (selectedPiece == null) {
            throw new Exception("Selected Piece is null!");
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

        if (model.getOpponent() instanceof Ai) {
            handleAiFullTurn();
        }
    }
    PauseTransition placePieceDelay;
    PauseTransition pickPieceDelay;

    /**
     * function to showEndScreen if callQuarto successful
     */
    private void handleQuarto() throws SQLException {
        model.callQuarto();
        if (model.getGame().getGameRules().checkWin()) {
            showEndScreen(false);
        }

    }

    /**
     * shows the end screen, two scenarios: winScreen if offline, statistics if online
     * @param isTie specifies if it was a tie
     */
    private void showEndScreen(boolean isTie) throws SQLException {
        view.getTurnStack().getChildren().clear();
        if (model.isOnline) {
            loadStatisticsView();
        } else {
            // Create the win presenter if it doesn't exist yet
            if (winPresenter == null) {
                winPresenter = new WinPresenter(view.getWinView(), model, this);
            }

            // Set the winner in the presenter, not directly on the view
            winPresenter.setWinner(!isTie ? model.getCurrentPlayer().getName() : null,
                    model.getCurrentPlayer() == model.getOpponent());

            view.showWinScreen();
        }
    }
    public void updateView() {
        updateMassage();
        updateSelectedPiece();
        updateBoard();
        updateSelectGrid();
        updateTurnInfo();
    }

    /**
     * If Help enabled, updates the help message calculated with the rule system
     */
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

    /**
     * updates the selected piece in the model and visual
     */
    private void updateSelectedPiece() {
        Piece selected = model.getGame().getSelectedPiece();
        if (selected != null) {
            Image img = new Image(ImageHelper.getPieceImagePath(selected));
            view.getSelectedPieceImage().setImage(img);
            double size = selected.getSize() == Size.SMALL ? SELECT_SMALL_PIECE_SIZE : SELECT_REGULAR_PIECE_SIZE;
            view.getSelectedPieceImage().setFitHeight(size);
            view.getSelectedPieceImage().setFitWidth(size);
        } else {
            view.getSelectedPieceImage().setImage(null);
        }
    }

    /**
     * loads the statistics screen at the game end
     */
    private void loadStatisticsView() throws SQLException {
        view.showStatisticsScreen();
        view.getStatisticsView().getCloseBtn().setOnMouseClicked(event -> {
            StartView startView = new StartView();
            view.getScene().setRoot(startView);
            new StartPresenter(startView);
        });
            new StatisticsPresenter(view.getStatisticsView(), new Statistics(model.getGameSessionId()));

    }

    /**
     * updates the board visuals based on the model
     */
    private void updateBoard() {
        for (int i = 0; i < model.getGame().getBoard().getTiles().size(); i++) {
            Piece piece = model.getGame().getBoard().getTiles().get(i).getPiece();
            BoardCell cell = board.get(i);
            cell.setPiece(piece);
        }
    }

    /**
     * updates the pieces to select Grid based on the model
     */
    private void updateSelectGrid() {
        for (int i = 0; i < model.getGame().getPiecesToSelect().getTiles().size(); i++) {
            Piece piece = model.getGame().getPiecesToSelect().getTiles().get(i).getPiece();
            piecesToSelect.get(i).setPiece(piece);
        }
    }

    /**
     * updates the turn StackPane based on the current player
     */
    private void updateTurnInfo() {
        boolean isHumanTurn = model.getCurrentPlayer().equals(model.getPlayer());
        boolean pieceSelected = model.getGame().getSelectedPiece() != null;
        boolean isVsAi = model.getOpponent() instanceof Ai;
        view.getLoadingBar().setOpacity(0);
        if (isHumanTurn) view.getTurn().setStyle("-fx-background-color: #29ABE2");
        else view.getTurn().setStyle("-fx-background-color: transparent");

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
                view.getTurn().setText(model.getOpponent().getName() + " is thinking...");
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



    //Loading Bar Logic
    Timeline loadingBarTimer;
    private double remainingLoadingDuration = 0;
    private double loadingProgressBeforePause = 0;

    /**
     * Handles the AI turn, sets a delay for both actions and animates a loading bar
     */
    private void handleAiFullTurn() {

        Random rand = new Random();
        float placeDuration = rand.nextFloat(1f) + 1;
        float pickDuration = rand.nextFloat(1f) + 1;
        animateLoadingBar(placeDuration + pickDuration);

        placePieceDelay = new PauseTransition(Duration.seconds(placeDuration));
        pickPieceDelay = null;
        updateView();

        placePieceDelay.setOnFinished(event -> {
            try {
                model.placePieceAi();
            } catch (Exception e) {
                ErrorHelper.showError(e,"AI place piece error");
            }
            inferenceEngine.determineFacts(model);
            inferenceEngine.applyRules(model.getGame(), move);
            updateView();
            try {
                checkForGameEnd();
                model.handlePendingWin();

                if (model.isGameOver()) {
                    showEndScreen(false);

                }
            }catch (SQLException e){
                ErrorHelper.showDBError(e);
            }
            pickPieceWithDelay(pickDuration);

        });

        placePieceDelay.play();
    }

    /**
     * handles the AI turn on the first move
     */
    private void handleAiStartTurn(){
        Random rand = new Random();
        float pickDuration = rand.nextFloat(1f) + 1;
        animateLoadingBar(pickDuration);

        pickPieceWithDelay(pickDuration);
    }

    /**
     * places the piece after a delay
     * @param pickDuration the delay in seconds
     */
    private void pickPieceWithDelay(float pickDuration) {
        pickPieceDelay = new PauseTransition(Duration.seconds(pickDuration));
        pickPieceDelay.setOnFinished(e -> {
            try {
                if(!model.isGameOver())
                    model.pickPieceAi();
            } catch (SQLException ex) {
                ErrorHelper.showDBError(ex);
            }

            inferenceEngine.determineFacts(model);
            inferenceEngine.applyRules(model.getGame(), move);
            updateView();
        });
        pickPieceDelay.play();
    }

    /**
     * starts the animation of the loading bar
     * @param durationInSeconds the duration of the animation
     */
    private void animateLoadingBar(double durationInSeconds) {
        remainingLoadingDuration = durationInSeconds;
        loadingProgressBeforePause = 0;
        view.getLoadingBar().setProgress(0);

        createAndStartLoadingAnimation(durationInSeconds);
    }

    /**
     * creates a timeline and the frames for the animation and plays it
     * @param duration duration of the timeline
     */
    private void createAndStartLoadingAnimation(double duration) {

        loadingBarTimer = new Timeline();
        int totalFrames = (int) (duration * 60);

        for (int i = 0; i <= totalFrames; i++) {
            final int frameNumber = i;

            //sets a keyframe to when the loading bar should change its progress
            KeyFrame keyFrame = new KeyFrame(
                    Duration.seconds(duration * i / totalFrames),
                    event -> view.getLoadingBar().setProgress(
                            loadingProgressBeforePause + ((double) frameNumber / totalFrames) * (1 - loadingProgressBeforePause))
            );
            loadingBarTimer.getKeyFrames().add(keyFrame);
        }

        loadingBarTimer.play();
    }

    public GameView getView() {
        return view;
    }

    /**
     * resumes the timer and restarts the loading bar if it is active
     */
    public void resumeTimer() {
        if (remainingLoadingDuration > 0) {
            createAndStartLoadingAnimation(remainingLoadingDuration);
        }

        if (placePieceDelay != null) {
            placePieceDelay.play();
        }
        if(pickPieceDelay != null){
            pickPieceDelay.play();
        }
    }

    /**
     * pauses the loading bar and AI move, if AI move was in progress
     */
    public void pauseAnimations() {
        if (loadingBarTimer != null) {
            // Save the current progress before pausing
            loadingProgressBeforePause = view.getLoadingBar().getProgress();

            // Calculate remaining duration based on current progress
            remainingLoadingDuration = remainingLoadingDuration * (1 - loadingProgressBeforePause);

            // Now pause the timeline
            loadingBarTimer.pause();
        }

        if (placePieceDelay != null) {
            placePieceDelay.pause();
        }
        if(pickPieceDelay != null){
            pickPieceDelay.pause();
        }
    }
    public void closeSettings() {
        view.getOverlayContainer().setVisible(false);
    }
}
package be.kdg.quarto.view.GameScreen;

import be.kdg.quarto.helpers.ImageHelper;
import be.kdg.quarto.model.GameSession;
import be.kdg.quarto.model.Piece;
import be.kdg.quarto.model.Statistics;
import be.kdg.quarto.model.Tile;
import be.kdg.quarto.model.enums.Size;
import be.kdg.quarto.view.GameScreen.Cells.BoardCell;
import be.kdg.quarto.view.GameScreen.Cells.SelectCell;
import be.kdg.quarto.view.StatisticsScreen.StatisticsPresenter;
import javafx.animation.PauseTransition;
import javafx.scene.image.Image;
import javafx.util.Duration;

public class GamePresenter {
    private static final double SELECT_SMALL_PIECE_SIZE = 30.0;
    private static final double SELECT_REGULAR_PIECE_SIZE = 45.0;

    private final GameSession model;
    private final GameView view;
    SelectCell selectedPiece;
    BoardCell selectedTile;


    //to make a delay between AI moves (in seconds)
    int AiThinkingDuration = 2;


    BoardCell[][] board;
    SelectCell[][] piecesToSelect;





    public GamePresenter(GameSession model, GameView view) {
        this.model = model;
        this.view = view;
        view.getScene().getRoot().setStyle("-fx-background-color: #fff4d5;");
        createBoard();
        createSelectPieces();
        addEventHandlers();
        updateView();
    }

    private void addEventHandlers() {
        // on click for Board
        for (int row = 0; row < 4; row++) {
            for (int col = 0; col < 4; col++) {
                int index = row * 4 + col;
                BoardCell boardCell = board[row][col];
                boardCell.getCellVisual().setOnMouseEntered(mouseEvent -> boardCell.hover());
                boardCell.getCellVisual().setOnMouseExited(mouseEvent -> boardCell.unhover());
                boardCell.getCellVisual().setOnMouseClicked(event -> {

                    if (model.getGame().getBoard().findTile(index).getPiece() == null) {
                        if(selectedTile != null){
                            selectedTile.deselect();
                        }
                        boardCell.select();
                        selectedTile = boardCell;



                    }
                });
            }
        }

        //ChoosePiece View
        view.getBackButton().setOnAction(event -> view.switchToMainSection());

        view.getChoosePieceConfirmButton().setOnAction(event -> {
            if (selectedPiece != null) {
                model.pickPiece(selectedPiece.getPiece(), model.getOpponent());
                model.getGame().getPiecesToSelect().getTiles().stream()
                        .filter(tile -> selectedPiece.getPiece().equals(tile.getPiece()))
                        .findFirst()
                        .ifPresent(tile -> tile.setPiece(null));
                selectedPiece.deselect();
                selectedPiece.setPiece(null);
                selectedPiece = null;
                view.switchToMainSection();

                handleAiTurn();

            } else {
                System.out.println("Selected Piece is null!");
            }
        });
        //onclick for selectGrid
        for (int row = 0; row < 4; row++) {
            for (int col = 0; col < 4; col++) {
                SelectCell selectCell = piecesToSelect[row][col];
                if(selectCell.getPiece() != null){
                    selectCell.getCellVisual().setOnMouseClicked(mouseEvent -> {
                            if(selectedPiece != null){
                                selectedPiece.deselect();
                            }
                            selectedPiece = selectCell;
                            selectCell.select();

                    });

                    selectCell.getCellVisual().setOnMouseEntered(mouseEvent -> selectCell.hover());
                    selectCell.getCellVisual().setOnMouseExited(mouseEvent -> selectCell.unhover());
                }

            }
        }


        view.getChoosePiece().setOnMouseClicked(event -> view.switchToChoosePiece());
        view.getPlacePiece().setOnAction(event -> {
            if(selectedTile != null) {
                Tile tile = model.getGame().getBoard().findTile(selectedTile.getRow()*4 + selectedTile.getColumn());
                if(tile.getPiece() != null){
                    System.out.println("You can't place a piece on a tile that already has a piece!");

                }else{
                    model.getGame().placePiece(tile, model.getPlayer());
                    model.getGame().setSelectedPiece(null);
                }
                selectedTile.deselect();
                selectedTile = null;

                updateView();
            }

        });
        view.getQuarto().setOnMouseClicked(event -> {
            model.callQuarto();
            if (model.getGame().getGameRules().checkWin()) {
                view.showStatisticsScreen();
                //todo: calculate the player ID && GameSessionID
                new StatisticsPresenter(view.getStatisticsView(), new Statistics(1));
            }
        });

        view.getSettings().setOnAction(event -> {
            view.showSettingsScreen();
            new SettingsPresenter(this, view.getSettingsView(), this.model);
        });
    }

    private void handleAiTurn() {
        // First delay before placing a piece
        PauseTransition placePieceDelay = new PauseTransition(Duration.seconds(AiThinkingDuration));
        updateView();

        placePieceDelay.setOnFinished(event -> {
            model.placePieceAi();
            updateView();


            model.handlePendingWin();

            // Second delay before picking a piece
            PauseTransition pickPieceDelay = new PauseTransition(Duration.seconds(AiThinkingDuration));
            pickPieceDelay.setOnFinished(e -> {
                model.pickPieceAi();
                updateView();
            });
            pickPieceDelay.play();
        });

        placePieceDelay.play();
    }
    private void createBoard(){
        board = new BoardCell[4][4];
        for(int r = 0; r < 4; r++) {
            for(int c = 0; c < 4; c++) {
                BoardCell cell = new BoardCell(r,c,21);
                view.getBoardGrid().add(cell.getCellVisual(),r,c);
                board[r][c] = cell;
            }
        }
    }


    private void createSelectPieces(){
        piecesToSelect = new SelectCell[4][4];

        for(int r = 0; r < 4; r++) {
            for(int c = 0; c < 4; c++) {
                int index = r * 4 + c;
                SelectCell cell;
                if((r + c) % 2 == 0){
                    cell = new SelectCell(r,c,"#ffffff");
                }else{
                    cell = new SelectCell(r,c,"#DBD6B2");
                }

                cell.setPiece(model.getGame().getPiecesToSelect().getTiles().get(index).getPiece());
                view.getSelectGrid().add(cell.getCellVisual(), c, r);
                piecesToSelect[r][c] = cell;
            }
        }
    }
    public void updateView() {
        // Update selected piece
        if (model.getGame().getSelectedPiece() != null) {
            Image pieceImage = new Image(ImageHelper.getPieceImage(model.getGame().getSelectedPiece()));
            view.getSelectedPieceImage().setImage(pieceImage);
            boolean isSmall = model.getGame().getSelectedPiece().getSize() == Size.SMALL;
            view.getSelectedPieceImage().setFitHeight(isSmall ? SELECT_SMALL_PIECE_SIZE : SELECT_REGULAR_PIECE_SIZE);
            view.getSelectedPieceImage().setFitWidth(isSmall ? SELECT_SMALL_PIECE_SIZE : SELECT_REGULAR_PIECE_SIZE);
        }
        else  view.getSelectedPieceImage().setImage(null);


        // Update board grid
        for (int i = 0; i < model.getGame().getBoard().getTiles().size(); i++) {
            int row = i / 4;
            int col = i % 4;
            Piece piece = model.getGame().getBoard().getTiles().get(i).getPiece();
            BoardCell cell = board[row][col];

            cell.setPiece(null);

            if (piece != null) {
                cell.setPiece(piece);
            }
        }

//         Update turn label
        boolean isHumanTurn = model.getCurrentPlayer().equals(model.getPlayer());
        if(isHumanTurn){
            view.getTurn().setStyle("-fx-background-color: #29ABE2");

            if(model.getGame().getSelectedPiece() != null){
                view.getChoosePiece().setDisable(true);
                view.getPlacePiece().setDisable(false);
                view.getTurn().setText("Your turn to place your piece");
            }else{
                view.getChoosePiece().setDisable(false);
                view.getPlacePiece().setDisable(true);
                view.getTurn().setText("Your turn to choose a piece");

            }
        }else{
            view.getTurn().setStyle("-fx-background-color: #FF1D25");
            if(model.getGame().getSelectedPiece() != null){
                view.getChoosePiece().setDisable(true);
                view.getPlacePiece().setDisable(true);
                view.getTurn().setText("Opponent's turn to place a piece");
            }else{
                view.getChoosePiece().setDisable(true);
                view.getPlacePiece().setDisable(true);
                view.getTurn().setText("Opponent's turn to choose a piece");
            }
        }
    }



    public GameView getView() {
        return view;
    }
}
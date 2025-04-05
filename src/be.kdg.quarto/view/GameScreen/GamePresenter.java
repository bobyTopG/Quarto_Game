package be.kdg.quarto.view.GameScreen;

import be.kdg.quarto.helpers.ImageHelper;
import be.kdg.quarto.model.Board;
import be.kdg.quarto.model.GameSession;
import be.kdg.quarto.model.Piece;
import be.kdg.quarto.model.enums.Size;
import be.kdg.quarto.view.GameScreen.Cells.BoardCell;
import be.kdg.quarto.view.GameScreen.Cells.SelectCell;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

public class GamePresenter {
    private static final double SELECT_SMALL_PIECE_SIZE = 30.0;
    private static final double SELECT_REGULAR_PIECE_SIZE = 45.0;

    private final GameSession model;
    private final GameView view;
    SelectCell selectedPiece;
    BoardCell selectedTile;

    BoardCell[][] board;
    SelectCell[][] piecesToSelect;
    // Constants for piece sizes


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
                boardCell.getCellVisual().setOnMouseEntered(mouseEvent -> {
                    boardCell.hover();

                });
                boardCell.getCellVisual().setOnMouseExited(mouseEvent -> {
                    boardCell.unhover();
                });
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
        view.getBackButton().setOnAction(event -> {
            view.switchToMainSection();
        });

        view.getChoosePieceConfirmButton().setOnAction(event -> {
            if (selectedPiece != null) {
                model.pickPiece(selectedPiece.getPiece(), model.getOpponent());
                model.getGame().getPiecesToSelect().getTiles().stream()
                        .filter(tile -> selectedPiece.equals(tile.getPiece()))
                        .findFirst()
                        .ifPresent(tile -> tile.setPiece(null));
                selectedPiece.deselect();
                selectedPiece.setPiece(null);
                selectedPiece = null;
                view.switchToMainSection();
                updateView();
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

                    selectCell.getCellVisual().setOnMouseEntered(mouseEvent -> {
                        selectCell.hover();
                    });
                    selectCell.getCellVisual().setOnMouseExited(mouseEvent -> {
                        selectCell.unhover();
                    });
                }

            }
        }


        view.getChoosePiece().setOnMouseClicked(event -> {
            view.switchToChoosePiece();

        });
        view.getPlacePiece().setOnAction(event -> {
            if(selectedTile != null) {

                model.getGame().placePiece(model.getGame().getBoard().findTile(selectedTile.getRow()*4 + selectedTile.getColumn()), model.getPlayer());
                model.getGame().setSelectedPiece(null);
                selectedTile.deselect();
                selectedTile = null;
                updateView();
            }

        });
        view.getQuarto().setOnMouseClicked(event -> {
            model.callQuarto();
            if (model.getGame().getGameRules().checkWin()) {
                // todo: show statistics
            }
        });

        view.getSettings().setOnAction(event -> {
            view.getOverlayContainer().setVisible(true);
            view.getOverlayContainer().toFront();
            new SettingsPresenter(this, view.getSettingsView(), this.model);
        });
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
            if(model.getGame().getSelectedPiece() != null){
                view.getChoosePiece().setDisable(true);
                view.getPlacePiece().setDisable(false);
            }else{
                view.getChoosePiece().setDisable(false);
                view.getPlacePiece().setDisable(true);
            }
        }else{
            view.getTurn().setText("AI turn to select a piece");
        }
    }



    public GameView getView() {
        return view;
    }
}
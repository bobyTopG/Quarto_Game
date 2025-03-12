package be.kdg.quarto.model;

import be.kdg.quarto.model.enums.AiLevel;
import be.kdg.quarto.model.enums.Color;
import be.kdg.quarto.model.enums.Height;
import be.kdg.quarto.model.enums.Shape;
import be.kdg.quarto.model.enums.Fill;
import javafx.scene.control.Alert;

import java.util.Date;

public class Game {
    private final Ai ai;
    private final Human human;
    private final GameSession gameSession;
    private Player currentPlayer;
    private Move currentMove;
    private int numberOfMoves = 0;

    //we make the pieces into a board for convenience
    private final Board piecesToSelect;
    private final Board board;
    private Piece selectedPiece;

    public Game() {
        this(new Human("Bob", "secretPassword"), new Ai("Open Ai", AiLevel.HARD, null, "Description"));
    }

    public Game(Human human, Ai ai) {
        this.human = human;
        this.ai = ai;
        this.piecesToSelect = new Board();
        this.board = new Board();
        piecesToSelect.generateAllPieces();
        board.createEmptyBoard();

        this.gameSession = new GameSession(human, ai);
        this.currentPlayer = ai;
        ai.setStrategy(new RandomPlayingStrategy(piecesToSelect, board));

        if (isAiTurn()) {
            handleAiTurn();
        }
    }

    public Piece createPieceFromImageName(String path) {
        String filename = path.substring(path.indexOf("/pieces/") + 8, path.lastIndexOf("."));
        String[] parts = filename.split("_");

        if (parts.length != 4) throw new IllegalArgumentException("Invalid image name format: " + path);

        try {
            return new Piece(
                    Color.valueOf(parts[2].toUpperCase()),
                    Height.valueOf(parts[3].toUpperCase()),
                    Fill.valueOf(parts[0].toUpperCase()),
                    Shape.valueOf(parts[1].toUpperCase())
            );
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid enum value in image name.");
            return null;
        }
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public void switchTurns() {
        if (gameSession.hasWinner()) return;
        currentPlayer = gameSession.getOtherPlayer(currentPlayer);
        if (isAiTurn()) {
            handleAiTurn();
        }
    }

    private void handleAiTurn() {
        if (currentPlayer == ai) {
            if (selectedPiece != null) {
                //Placing
                placePiece(ai.getStrategy().selectTile(), ai);
                //after placing piece -> pick piece
                handleAiTurn();
            } else {
                //Picking
                try {
                    pickPiece(ai.getStrategy().selectPiece(), ai);
                } catch (NullPointerException e) {
                    //all pieces have been placed
                    System.out.println(gameSession.getMoves());
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Game Over");
                    alert.setHeaderText("There are no pieces to select!");
                    alert.showAndWait();
                }
            }
        }
    }
    public void  handeHumanTurn(){
        if(currentPlayer == human){
            if (selectedPiece != null) {

            }
            }
    }
    private boolean isAiTurn() {
        return currentPlayer == ai;
    }
    public void pickPiece(Piece piece, Player player) {
        setSelectedPiece(piece);
        getPiecesToSelect().findTile(getSelectedPiece()).setPiece(null);
        endMove(player);
        switchTurns();
    }

    public void placePiece(Tile selectedTile, Player player) {
        selectedTile.setPiece(selectedPiece);
        startMove(player, selectedTile);
        setSelectedPiece(null);
    }

    public void startMove(Player player, Tile selectedTile) {
        //TO DO: calculate startTime and endTime
         currentMove = new Move(ai, board.getTiles().indexOf(selectedTile), selectedPiece, numberOfMoves, new Date() ,new Date());
        numberOfMoves++;
    }
    public void endMove(Player player){
        if(currentMove !=null){
            currentMove.setSelectedPiece(getSelectedPiece());
        }else{
            //first move made will be only choosing the piece without placing any
            currentMove = new Move(player, getSelectedPiece(), new Date(), new Date());
        }
        gameSession.addMove(currentMove);

    }
    public Board getPlacedTiles() {
        return board;
    }

    public Board getPiecesToSelect() {
        return piecesToSelect;
    }

    public Player getHuman() {
        return human;
    }

    public Piece getSelectedPiece() {
        return selectedPiece;
    }

    public void setSelectedPiece(Piece selectedPiece) {
        this.selectedPiece = selectedPiece;
    }
}
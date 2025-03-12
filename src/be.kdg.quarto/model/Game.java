package be.kdg.quarto.model;

import be.kdg.quarto.model.enums.AiLevel;
import be.kdg.quarto.model.enums.Color;
import be.kdg.quarto.model.enums.Height;
import be.kdg.quarto.model.enums.Shape;
import be.kdg.quarto.model.enums.Fill;
import javafx.scene.control.Alert;

import java.util.Date;
import java.util.List;

public class Game {
    private final Ai ai;
    private final Human human;
    private final GameSession gameSession;
    private Player currentPlayer;
    private Move currentMove;
    private int numberOfMoves = 1;

    //we make the pieces into a board for convenience
    private final Board piecesToSelect;
    private final Board board;
    private Piece selectedPiece;

    private GameRules gameRules = new GameRules();

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
    public Player callQuarto(){
         if(gameRules.checkWin(board, gameSession.getMoves())){
             System.out.println(currentPlayer.getName() + " Has won the game!");
             return currentPlayer;
         }
        System.out.println("No one won yet :(");
         return null;
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
                    for(Move move : gameSession.getMoves()) {
                        System.out.println(move);
                    }
                    //all pieces have been placed
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Game Over");
                    alert.setHeaderText("There are no pieces to select!");
                    alert.showAndWait();
                }
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
        currentMove = new Move(player, board.getTiles().indexOf(selectedTile), selectedPiece, numberOfMoves, getStartTimeForMove());
        gameSession.addMove(currentMove);
        numberOfMoves++;
    }
    public void endMove(Player player){
        if(currentMove !=null){
            currentMove.setSelectedPiece(getSelectedPiece());
            currentMove.setEndTime(new Date());
        }else{
            //first move made will be only choosing the piece without placing any
            currentMove = new Move(player, getSelectedPiece(), getStartTimeForMove(), new Date());
            gameSession.addMove(currentMove);
        }
    }
    public Date getStartTimeForMove() {
        List<Move> moves = gameSession.getMoves();
        if (moves == null || moves.isEmpty()) { // Check for null and empty
            return gameSession.getStartTime();
        } else {
            //noinspection SequencedCollectionMethodCanBeUsed
            return moves.get(moves.size() - 1).getEndTime();
        }
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
    public Board getBoard() {
        return board;
    }

    public void setSelectedPiece(Piece selectedPiece) {
        this.selectedPiece = selectedPiece;
    }

    public GameRules getGameRules(){
        return gameRules;
    }
}
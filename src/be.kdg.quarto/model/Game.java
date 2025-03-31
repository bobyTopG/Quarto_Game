package be.kdg.quarto.model;

import be.kdg.quarto.helpers.CreateHelper;
import be.kdg.quarto.model.enums.Color;
import be.kdg.quarto.model.enums.Size;
import be.kdg.quarto.model.enums.Shape;
import be.kdg.quarto.model.enums.Fill;

import java.util.Date;
import java.util.List;
import java.util.Random;

public class Game {
    private final Ai ai;
    private final Human human;
    private GameSession gameSession;
    private Player currentPlayer;
    private Move currentMove;
    private int numberOfMoves = 1;

    //we make the pieces into a board for convenience
    private final Board piecesToSelect;
    private final Board board;
    private Piece selectedPiece;

    private final GameRules gameRules = new GameRules();

    public Game(Player human, Player ai) {

        if (!(human instanceof Human) || !(ai instanceof Ai)) {
            throw new IllegalArgumentException("Game must be initialized with Human and Ai player types");
        }
        this.human = (Human) human;
        this.ai = (Ai) ai;

        this.piecesToSelect = new Board();
        this.board = new Board();
        piecesToSelect.generateAllPieces();
        board.createEmptyBoard();
        this.gameSession = new GameSession(human, ai, this);
        this.currentPlayer = getRandomPlayer();


        this.ai.getStrategy().fillNecessaryData(this);

        if (isAiTurn()) {
            handleAiTurn();
        }

    }
    public Player getRandomPlayer(){
        int rand =  new Random().nextInt(2);

        return rand == 1 ? human : ai;
    }
    public Player callQuarto(){
         if(gameRules.checkWin(board, gameSession.getMoves())){
             System.out.println(currentPlayer.getName() + " Has won the game!");
             CreateHelper.createAlert( "Game Over", currentPlayer.getName() + " Has won the game!","Game Win");
             return currentPlayer;
         }
         return null;
    }
    public Piece createPieceFromImageName(String path) {
        String filename = path.substring(path.indexOf("/pieces/") + 8, path.lastIndexOf("."));
        String[] parts = filename.split("_");

        if (parts.length != 4) throw new IllegalArgumentException("Invalid image name format: " + path);

        try {
            return new Piece(
                    Color.valueOf(parts[2].toUpperCase()),
                    Size.valueOf(parts[3].toUpperCase()),
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
        if (gameSession.hasWinner()){

            return;
        }

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
                if(ai.getStrategy().isCallingQuarto()){
                    gameSession.SetWinner(callQuarto());
                }
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
                    CreateHelper.createAlert("Game Over", "Game Over", "There are no pieces to select!" );
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
    public Player getAI(){
        return ai;
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


    public GameSession getGameSession(){
        return gameSession;
    }
}
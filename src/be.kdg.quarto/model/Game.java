package be.kdg.quarto.model;

import be.kdg.quarto.model.enums.Color;
import be.kdg.quarto.model.enums.Fill;
import be.kdg.quarto.model.enums.Shape;
import be.kdg.quarto.model.enums.Size;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Game {

    private final Board piecesToSelect;
    private final Board board;
    private final GameRules gameRules;

    private final List<Move> moves;

    private Piece selectedPiece;
    private Move currentMove;




    public Game() {
        this.moves = new ArrayList<>();

        board = new Board();
        piecesToSelect = new Board();
        board.createEmptyBoard();
        piecesToSelect.generateAllPieces();

        gameRules = new GameRules(board, moves);



    }
    public void startNewMove(Player player){
        currentMove = new Move();
        currentMove.setPlayer(player);
        currentMove.setMoveNumber(moves.size() + 1);
    }
    public void endMove(){
        addCurrentMove();
        currentMove = null;
    }
    public void placePieceIntoMove(Tile selectedTile) {
        currentMove.setPiece(selectedPiece);
        currentMove.setPosition(board.getTiles().indexOf(selectedTile));
        currentMove.setStartTime(getStartTimeForMove());
        //if last move
        if (piecesToSelect.isEmpty()) {
            currentMove.setEndTime(new Date());
            endMove();
        }

    }
    public void pickPieceIntoMove() {
        currentMove.setSelectedPiece(getSelectedPiece());
        currentMove.setEndTime(new Date());

        if(board.isEmpty()){
            currentMove.setStartTime(getStartTimeForMove());
        }

    }


    public Date getStartTimeForMove() {
        List<Move> moves = getMoves();
        if (moves == null || moves.isEmpty()) { // Check for null and empty
            return new Date();
        } else {
            //noinspection SequencedCollectionMethodCanBeUsed
            return moves.get(moves.size() - 1).getEndTime();
        }
    }


    public void setSelectedPiece(Piece selectedPiece) {
        this.selectedPiece = selectedPiece;
    }

    public Piece getSelectedPiece() {
        return selectedPiece;
    }

    public Board getBoard() {
        return board;
    }

    public Board getPiecesToSelect() {
        return piecesToSelect;
    }

    public void addCurrentMove() {
        moves.add(currentMove);
    }

    public GameRules getGameRules() {
        return gameRules;
    }

    public List<Move> getMoves() {
        return moves;
    }

    public Move getCurrentMove() {
        return currentMove;
    }

    public void setCurrentMove(Move currentMove) {
        this.currentMove = currentMove;
    }

}
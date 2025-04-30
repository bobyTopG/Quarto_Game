package be.kdg.quarto.model;

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

    public void startNewMove(Player player) {
        currentMove = new Move();
        addCurrentMove();
        currentMove.setPlayer(player);
        currentMove.setMoveNumber(moves.size());
        currentMove.setStartTime(getStartTimeForMove());
    }

    public void endMove() {
        currentMove = null;
    }

    public void placePieceIntoMove(Tile selectedTile) {
        currentMove.setPiece(selectedPiece);
        currentMove.setPosition(board.getTiles().indexOf(selectedTile));
        //if last move
        if (piecesToSelect.isEmpty()) {
            currentMove.setEndTime(new Date());
            endMove();
        }

    }

    public void pickPieceIntoMove(Date gameSessionStartTime) {
        currentMove.setSelectedPiece(getSelectedPiece());
        currentMove.setEndTime(new Date());
        // first Move
        if (board.isEmpty()) {
            currentMove.setStartTime(gameSessionStartTime);
        }

    }

    public Date getStartTimeForMove() {
        List<Move> moves = getMoves();
        if (moves == null || moves.isEmpty() || moves.size() - 1 == 0) { // Check for null and empty
            return new Date();
        } else {
            return moves.get(moves.size() - 2).getEndTime();
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
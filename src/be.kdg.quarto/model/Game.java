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

    public Game(List<Move> moves) {
        this.moves = moves;
        board = new Board();
        piecesToSelect = new Board();
        board.createEmptyBoard();
        piecesToSelect.generateAllPieces();

        for (Move move : moves) {
            if (move.getPiece() != null && move.getPosition() != -1) {
                board.getTiles().get(move.getPosition()).setPiece(move.getPiece());
            }
            if (move.getSelectedPiece() != null) {
                piecesToSelect.getTiles().removeIf(tile -> tile.getPiece().equals(move.getSelectedPiece()));
            }
        }

        gameRules = new GameRules(board, moves);

    }

    public void startNewMove(Player player) {
        currentMove = new Move();
        addCurrentMove();
        currentMove.setPlayer(player);
        currentMove.setMoveNumber(moves.size());
    }

    public void endMove() {
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

    public void pickPieceIntoMove(Date gameSessionStartTime) {
        currentMove.setSelectedPiece(getSelectedPiece());
        currentMove.setEndTime(new Date());
        //first Move
        if (board.isEmpty()) {
            currentMove.setStartTime(gameSessionStartTime);
        }

    }

    public Date getStartTimeForMove() {
            return moves.get(moves.size() - 2).getEndTime();
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
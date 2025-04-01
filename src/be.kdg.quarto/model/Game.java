package be.kdg.quarto.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Game {
    private final List<Move> moves;
    private Move currentMove;
    private int numberOfMoves = 1;
    private GameRules gameRules;

    private Board piecesToSelect;
    private Board board;
    private Piece selectedPiece;


    public Game() {
        this.moves = new ArrayList<>();
        board = new Board();
        piecesToSelect = new Board();
        board.createEmptyBoard();
        piecesToSelect.generateAllPieces();
        gameRules = new GameRules(board , moves);
    }


    public void placePiece(Tile selectedTile, Player player) {
        selectedTile.setPiece(selectedPiece);
        startMove(player, selectedTile);
    }

    public void startMove(Player player, Tile selectedTile) {
        currentMove = new Move(player, board.getTiles().indexOf(selectedTile), selectedPiece, numberOfMoves, getStartTimeForMove());
        addMove(currentMove);
        numberOfMoves++;
    }

    public void endMove(Player player) {
        if (currentMove != null) {
            currentMove.setSelectedPiece(getSelectedPiece());
            currentMove.setEndTime(new Date());
        } else {
            //first move made will be only choosing the piece without placing any
            currentMove = new Move(player, getSelectedPiece(), getStartTimeForMove(), new Date());
            addMove(currentMove);
        }
    }

    public Date getStartTimeForMove() {
        List<Move> moves = getMoves();
        if (moves == null || moves.isEmpty()) { // Check for null and empty
            return null;
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

    public void addMove(Move move) {
        moves.add(move);
    }

    public GameRules getGameRules() {
        return gameRules;
    }

    public List<Move> getMoves() {
        return moves;
    }


}
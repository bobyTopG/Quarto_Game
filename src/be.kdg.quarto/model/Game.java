package be.kdg.quarto.model;

import be.kdg.quarto.model.enums.Color;
import be.kdg.quarto.model.enums.Fill;
import be.kdg.quarto.model.enums.Shape;
import be.kdg.quarto.model.enums.Size;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Game {

    private final List<Move> moves;
    private Move currentMove;

    private Board piecesToSelect;
    private Board board;
    private Piece selectedPiece;


    private GameRules gameRules;


    public Game() {
        this.moves = new ArrayList<>();

        board = new Board();
        piecesToSelect = new Board();
        board.createEmptyBoard();
        piecesToSelect.generateAllPieces();

        gameRules = new GameRules(board, moves);
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


    public void startMove(Player player, Tile selectedTile) {

        currentMove = new Move(player, board.getTiles().indexOf(selectedTile), selectedPiece, currentMove.getMoveNumber(), getStartTimeForMove());
        addCurrentMove();
        currentMove.setMoveNumber(currentMove.getMoveNumber() + 1);
        //if last move
        if (piecesToSelect.isEmpty()) {
            currentMove.setEndTime(new Date());
        }

    }

    public void endMove(Player player) {
        if (!board.isEmpty()) {
            currentMove.setSelectedPiece(getSelectedPiece());
            currentMove.setEndTime(new Date());
        } else {
            //first move made will be only choosing the piece without placing any
            currentMove = new Move(player, getSelectedPiece(), getStartTimeForMove(), new Date());
            addCurrentMove();
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
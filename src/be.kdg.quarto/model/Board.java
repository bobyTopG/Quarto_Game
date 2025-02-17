package src.be.kdg.quarto.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Board {
    private  List<PieceType> pieceTypes = new ArrayList<>(Arrays.asList(PieceType.values()));
    private List<Tile> fillBoardtiles;
    private List<Tile> blankBoadtiles;
    private List<Piece> pieces;
    private int BOARD_SIZE = 16;
    private  PieceType SelectedPieceType;

    public Board() {
        pieces = new ArrayList<>();
        fillBoardtiles = new ArrayList<>();
        blankBoadtiles = new ArrayList<>();
        createFillBoard();
        createBoard();
    }

    public PieceType getSelectedPieceType() {
        return SelectedPieceType;
    }

    public void setSelectedPieceType(PieceType selectedPieceType) {
        SelectedPieceType = selectedPieceType;
    }

    public void createFillBoard() {

        pieceTypes.remove(SelectedPieceType);

        for (int i = 0; i < pieceTypes.size(); i++) {
            fillBoardtiles.add(new Tile(i));
            pieces.add(i, new Piece(i, pieceTypes.get(i)));
        }

        for (Tile tile : fillBoardtiles) {
            tile.setPiece(pieces.get(tile.getIndex()));
        }

    }

    public void createBoard() {
        for (int i = 0; i < BOARD_SIZE; i++) {
            blankBoadtiles.add(new Tile(i));
        }
    }
    public void remove(PieceType type) {
        for (int i = 0; i < fillBoardtiles.size(); i++) {
            if (fillBoardtiles.get(i).getPiece().getType() == type) {
               fillBoardtiles.remove(i);
                break;
            }
        }

    }

    public List<Piece> getPieces() {
        return pieces;
    }

    public List<Tile> getTilesBlank() {
        return blankBoadtiles;
    }
    public List<Tile> getTilesFill() {
        return fillBoardtiles;
    }


}

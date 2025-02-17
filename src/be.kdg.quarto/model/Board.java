package src.be.kdg.quarto.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Board {
    private  List<PieceType> pieceTypes = new ArrayList<>(Arrays.asList(PieceType.values()));
    private int BOARD_SIZE = 16;
    private  PieceType SelectedPieceType;

    public Board(List<PieceType> pieceTypes, int BOARD_SIZE, PieceType selectedPieceType) {
        this.pieceTypes = pieceTypes;
        this.BOARD_SIZE = BOARD_SIZE;
        SelectedPieceType = selectedPieceType;
    }

    public List<PieceType> getPieceTypes() {
        return pieceTypes;
    }

    public void setPieceTypes(List<PieceType> pieceTypes) {
        this.pieceTypes = pieceTypes;
    }

    public int getBOARD_SIZE() {
        return BOARD_SIZE;
    }

    public void setBOARD_SIZE(int BOARD_SIZE) {
        this.BOARD_SIZE = BOARD_SIZE;
    }

    public PieceType getSelectedPieceType() {
        return SelectedPieceType;
    }

    public void setSelectedPieceType(PieceType selectedPieceType) {
        SelectedPieceType = selectedPieceType;
    }
}

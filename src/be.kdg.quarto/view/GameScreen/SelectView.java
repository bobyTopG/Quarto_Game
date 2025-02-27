package be.kdg.quarto.view.GameScreen;

import be.kdg.quarto.view.PieceView.PieceView;
import javafx.scene.layout.GridPane;

import java.util.ArrayList;
import java.util.List;

public class SelectView extends GridPane {
    private List<PieceView> pieceViewList;

    public SelectView() {
        initialiseNodes();
        layoutNodes();
    }

    private void initialiseNodes() {
        pieceViewList = new ArrayList<>();
    }

    public List<PieceView> getPieceViews() {
        return pieceViewList;
    }

    public void swap(PieceView pieceView , int index) {
        pieceViewList.set(index, pieceView);
    }

    private void layoutNodes() {
        setGridLinesVisible(true);
    }


}

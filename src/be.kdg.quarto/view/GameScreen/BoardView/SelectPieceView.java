package be.kdg.quarto.view.GameScreen.BoardView;

import javafx.scene.layout.GridPane;

import java.util.ArrayList;
import java.util.List;

public class SelectPieceView extends GridPane {
    private List<PieceView> pieceViewList;

    public SelectPieceView() {
        initialiseNodes();
        layoutNodes();
    }

    private void initialiseNodes() {
        pieceViewList = new ArrayList<>();
    }

    public List<PieceView> getPieceViews() {
        return pieceViewList;
    }


    private void layoutNodes() {
        setGridLinesVisible(false);
    }


}

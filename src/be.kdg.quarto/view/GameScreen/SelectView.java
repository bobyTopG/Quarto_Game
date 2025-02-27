package be.kdg.quarto.view.GameScreen;

import be.kdg.quarto.view.BoardView.SpaceView;
import be.kdg.quarto.view.PieceView.PieceView;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class SelectView extends GridPane {
    private List<PieceView> pieceViewList;

    public SelectView() {
        initialiseNodes();
        layoutNodes();
    }

    private void initialiseNodes() {
        pieceViewList = new ArrayList<>();
    }

    public List<PieceView> getPieceViewList() {
        return pieceViewList;
    }

    private void layoutNodes() {
        setGridLinesVisible(true);
    }


}

package be.kdg.quarto.view.GameScreen.Cells;

import javafx.geometry.Pos;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

 public class BoardCell extends Cell {

     private final Circle circle;
     public BoardCell(int row, int col, int radius) {
         super(row,col);
         StackPane cell = new StackPane();
        cell.setAlignment(Pos.CENTER);

        Circle bg = new Circle(radius);
        bg.setFill(Color.TRANSPARENT);
        bg.setStroke(Color.WHITE);
        bg.setStrokeWidth(3);
        cell.getChildren().add(bg);


        this.circle = bg;
        this.cell = cell;


    }
    public Circle getCircle() { return circle; }
    public void select(){
         isSelected = true;
         this.circle.setFill(new Color(1.0,1.0,1.0,0.7));
    }
    public void hover(){
         isHovered = true;
         if(!isSelected) this.circle.setFill(new Color(1.0,1.0,1.0,0.3));
    }
    public void unhover(){
         isHovered = false;
         if(!isSelected) this.circle.setFill(Color.TRANSPARENT);

    }

    public void deselect(){
         isSelected = false;
         isHovered = false;
         this.circle.setFill(Color.TRANSPARENT);
    }

}


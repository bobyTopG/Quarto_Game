package be.kdg.quarto.view.GameScreen.Cells;


import javafx.scene.paint.Color;
/**
 *  Cell made specifically for the pieces to select
 */

public class SelectCell extends Cell {
    String color;

    public SelectCell(int row, int col, String color) {
        super(row,col);
        this.cell.setStyle("-fx-background-color: " + color + ";");
        this.color = color;
        int SIZE = 55;
        this.cell.setPrefSize(SIZE, SIZE);
        this.cell.setMinSize(SIZE, SIZE);
        this.cell.setMaxSize(SIZE, SIZE);
    }

    public void select() {
        isSelected = true;
        String darkerColor = getDarkerVersion(this.color);
        this.cell.setStyle("-fx-background-color: " + darkerColor + ";");
    }
    public void hover(){
        isHovered = true;
        if(!isSelected){
            String darkerColor = getDarkerVersion(this.color);
            this.cell.setStyle("-fx-background-color: " + darkerColor + ";");
        }
    }
    public void unhover(){
        isHovered = false;
        if(!isSelected){
            this.cell.setStyle("-fx-background-color: " + this.color + ";");
        }
    }

    public void deselect() {
        isSelected = false;
        isHovered = false;
        this.cell.setStyle("-fx-background-color: " + this.color + ";");
    }

    /**
     * darkens the specified color and returns it
     */
    private String getDarkerVersion(String hexColor) {
        try {
            Color color = Color.web(hexColor);
            Color darker = color.darker();
            return String.format("#%02X%02X%02X",
                    (int)(darker.getRed() * 255),
                    (int)(darker.getGreen() * 255),
                    (int)(darker.getBlue() * 255));
        } catch (Exception e) {
            // Fallback if color parsing fails
            return hexColor;
        }
    }
}

package be.kdg.quarto.model;

public class Move {
    private int startX;
    private int startY;
    private int endX;
    private int endY;

    public Move(int startX, int startY, int endX, int endY) {
        this.startX = startX;
        this.startY = startY;
        this.endX = endX;
        this.endY = endY;
    }

    // Getters
    public int getStartX() {
        return startX;
    }

    public int getStartY() {
        return startY;
    }

    public int getEndX() {
        return endX;
    }

    public int getEndY() {
        return endY;
    }

    @Override
    public String toString() {
        return "Move from (" + startX + ", " + startY + ") to (" + endX + ", " + endY + ")";
    }
}
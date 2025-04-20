package be.kdg.quarto.model.strategies.MiniMax;

public class StrategyMove {
    private int startX;
    private int startY;
    private int endX;
    private int endY;

    public StrategyMove(int startX, int startY, int endX, int endY) {
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

    @Override
    public String toString() {
        return "Move from (" + startX + ", " + startY + ") to (" + endX + ", " + endY + ")";
    }
}
package src.be.kdg.quarto.model;

public class PlayingStrategy {
    private String strategyName;

    public PlayingStrategy(String strategyName) {
        this.strategyName = strategyName;
    }

    public String getStrategyName() {
        return strategyName;
    }

    public void setStrategyName(String strategyName) {
        this.strategyName = strategyName;
    }
}

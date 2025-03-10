package be.kdg.quarto.model;

public interface PlayingStrategy {


    Piece selectPiece();
    String getName();
//    private String strategyName;
//
//    public PlayingStrategy(String strategyName) {
//        this.strategyName = strategyName;
//    }
//
//    public String getStrategyName() {
//        return strategyName;
//    }
//
//    public void setStrategyName(String strategyName) {
//        this.strategyName = strategyName;
//    }
}

package be.kdg.quarto.model.strategies.rulebasedsystem.facts;

public enum FactValues {
    ENDMOVEAI,
    ENDMOVEPLAYER,
    WINNINGPOSITIONAI,
    WINNINGPOSITIONPLAYER,
    GOODMOVE;

    public static FactValues fromString(String name) {
        try {
            return FactValues.valueOf(name);
        } catch (IllegalArgumentException | NullPointerException e) {
            System.err.println("Invalid FactValue name: " + name);
            return null;
        }
    }
}

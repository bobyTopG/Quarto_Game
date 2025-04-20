package be.kdg.quarto.rulebasedsystem.facts;

public enum FactValues {
    ENDMOVEAI,
    ENDMOVEPLAYER,
    WINNINGPOSITIONAI,
    WINNINGPOSITIONPLAYER;

    public static FactValues fromString(String name) {
        try {
            return FactValues.valueOf(name);
        } catch (IllegalArgumentException | NullPointerException e) {
            System.err.println("Invalid FactValue name: " + name);
            return null;
        }
    }
}

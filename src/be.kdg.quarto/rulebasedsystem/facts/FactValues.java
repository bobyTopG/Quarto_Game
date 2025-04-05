package be.kdg.quarto.rulebasedsystem.facts;

public enum FactValues {
    ENDMOVEAI,
    ENDMOVEPLAYER,
    WINNINGPOSITIONAI,
    WINNINGPOSITIONPLAYER,
    GOODMOVE,
    OTHERFACT;

    public static FactValues fromString(String name) {
        try {
            return FactValues.valueOf(name);
        } catch (IllegalArgumentException | NullPointerException e) {
            return null;
        }
    }

    public static FactValues[] all() {
        return values();
    }
}

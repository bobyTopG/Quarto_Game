package be.kdg.quarto.model;

import java.util.Date;
import java.util.Objects;

public class PausePeriod {
    private final Date pauseStart;
    private final Date pauseEnd;

    public PausePeriod(Date pauseStart, Date pauseEnd) {
        this.pauseStart = pauseStart;
        this.pauseEnd = pauseEnd;
    }

    public Date getPauseStart() {
        return pauseStart;
    }
    public Date getPauseEnd() {
        return pauseEnd;
    }


    public long getDurationInMillis() {
        return Objects.requireNonNullElseGet(pauseEnd, Date::new).getTime() - pauseStart.getTime();
    }
}
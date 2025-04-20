package be.kdg.quarto.rulebasedsystem.facts;

import java.util.EnumSet;
import java.util.Set;

public class FactsHandler {
    private final Set<FactValues> facts = EnumSet.noneOf(FactValues.class);
    private boolean factsChanged;
    private boolean factsEvolved;

    public void addFact(FactValues fact) {
        if (!facts.contains(fact)) {
            facts.add(fact);
            factsChanged = true;
        }
    }

    public boolean factAvailable(FactValues fact) {
        return facts.contains(fact);
    }

    public void resetFacts() {
        facts.clear();
        factsChanged = false;
        factsEvolved = false;
    }

    public boolean factsObserved() {
        return !facts.isEmpty();
    }

    public boolean factsChanged() {
        return factsChanged;
    }

    public void setFactsEvolved(boolean value) {
        this.factsEvolved = value;
    }

    public boolean factsEvolved() {
        return factsEvolved;
    }

    public Set<FactValues> getFacts() {
        return Set.copyOf(facts);
    }
}
package be.kdg.quarto.model.strategies.rulebasedsystem.rules;


import be.kdg.quarto.model.Game;
import be.kdg.quarto.model.strategies.rulebasedsystem.facts.FactsHandler;
import be.kdg.quarto.model.Move;

public abstract class Rule {
    public abstract boolean conditionRule(FactsHandler facts);
    public abstract boolean actionRule(FactsHandler facts, Game game, Move move);
}

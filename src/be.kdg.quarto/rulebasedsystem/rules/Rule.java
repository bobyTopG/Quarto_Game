package be.kdg.quarto.rulebasedsystem.rules;


import be.kdg.quarto.model.Game;
import be.kdg.quarto.rulebasedsystem.facts.FactsHandler;
import be.kdg.quarto.model.Board;
import be.kdg.quarto.model.Move;

public abstract class Rule {
    public abstract boolean conditionRule(FactsHandler facts);
    public abstract boolean actionRule(FactsHandler facts, Game game, Move move);
}

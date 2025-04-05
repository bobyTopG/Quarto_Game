package be.kdg.quarto.rulebasedsystem.rules;

import be.kdg.quarto.model.Board;
import be.kdg.quarto.model.Move;
import be.kdg.quarto.rulebasedsystem.facts.FactsHandler;

import java.util.ArrayList;
import java.util.List;

public class RulesHandler {
    private final List<Rule> rules;

    public RulesHandler() {
        rules = new ArrayList<>();
        rules.add(new RuleEndMoveAi());
        // Add more rules in priority order as needed
    }

    public boolean checkConditionRule(int index, FactsHandler facts) {
        return rules.get(index).conditionRule(facts);
    }

    public boolean fireActionRule(int index, FactsHandler facts, Board board, Move move) {
        return rules.get(index).actionRule(facts, board, move);
    }

    public int numberOfRules() {
        return rules.size();
    }
}

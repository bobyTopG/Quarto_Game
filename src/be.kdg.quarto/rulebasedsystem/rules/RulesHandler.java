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
        rules.add(new RuleBlockEndMovePlayer());
        rules.add(new RuleWinningPositionAi());
        rules.add(new RuleBlockWinningPositionPlayer());
        rules.add(new RuleGoodMove());
        rules.add(new RuleDefaultFallback());
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

    public String getRuleName(int i) {
        return rules.get(i).getClass().getSimpleName();
    }


}

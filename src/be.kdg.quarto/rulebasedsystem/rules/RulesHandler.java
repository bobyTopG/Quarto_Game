package be.kdg.quarto.rulebasedsystem.rules;

import be.kdg.quarto.model.Board;
import be.kdg.quarto.model.Game;
import be.kdg.quarto.model.Move;
import be.kdg.quarto.rulebasedsystem.facts.FactsHandler;

import java.util.ArrayList;
import java.util.List;

public class RulesHandler {
    private final List<Rule> rules;

    public RulesHandler() {
        this.rules = new ArrayList<>();
        rules.add(new RuleWinningPositionPlayer());
        rules.add(new RuleEndMoveAi());
        rules.add(new RuleGoodMove());

    }

    public boolean fireActionRule(int index, FactsHandler facts, Game game, Move move) {
        return rules.get(index).actionRule(facts, game, move);
    }

    public boolean checkConditionRule(int index, FactsHandler facts) {
        return rules.get(index).conditionRule(facts);
    }

    public int numberOfRules() {
        return rules.size();
    }

}
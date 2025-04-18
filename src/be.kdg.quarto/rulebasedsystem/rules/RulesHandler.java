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
    }

    public boolean checkConditionRule(int index, FactsHandler facts) {
        return rules.get(index).conditionRule(facts);
    }

    public boolean fireActionRule(int index, FactsHandler facts, Board board, Move move) {
        String ruleName = getRuleName(index);

        switch (ruleName) {
            case "RuleEndMoveAi" -> {
                board.determineEndMove(move); // TEMP: use random until determineEndMove exists
                return true;
            }

            case "RuleWinningPositionPlayer" -> {
                board.determineBlockWinningPositionMove(move); // you already have this
                return true;
            }

            default -> {
                return false; // no rule matched or nothing fired
            }
        }
    }

    public int numberOfRules() {
        return rules.size();
    }

    public String getRuleName(int i) {
        return rules.get(i).getClass().getSimpleName();
    }


}

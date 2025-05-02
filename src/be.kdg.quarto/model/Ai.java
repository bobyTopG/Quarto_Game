package be.kdg.quarto.model;

import be.kdg.quarto.model.enums.AiLevel;

public class Ai extends Player {
    private AiLevel difficultyLevel;
    private final PlayingStrategy strategy;
    private final String description;


    public Ai(int id, String name, AiLevel difficultyLevel, PlayingStrategy strategy, String description) {
        super(id, name);
        setDifficultyLevel(difficultyLevel);
        this.strategy = strategy;
        this.description = description;
    }

    public PlayingStrategy getStrategy() {
        return strategy;
    }

    public AiLevel getDifficultyLevel() {
        return difficultyLevel;
    }

    public void setDifficultyLevel(AiLevel difficultyLevel) {
        this.difficultyLevel = difficultyLevel;
    }

    public String getDescription() {
        return this.description;
    }

}

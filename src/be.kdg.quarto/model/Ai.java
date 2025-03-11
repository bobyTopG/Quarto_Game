package be.kdg.quarto.model;

import be.kdg.quarto.model.enums.AiLevel;

public class Ai extends Player{
    private AiLevel difficultyLevel;
    private PlayingStrategy strategy;
    private String description;


    public Ai(String name , AiLevel difficultyLevel , PlayingStrategy strategy, String description) {
        super(name);
        setDifficultyLevel(difficultyLevel);
        this.strategy = strategy;
        this.description = description;
    }

    public void setStrategy(PlayingStrategy strategy) {
        this.strategy = strategy;
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

    public void setDescription(String description) {
        this.description = description;
    }
}

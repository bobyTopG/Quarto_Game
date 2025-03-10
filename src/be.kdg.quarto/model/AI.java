package be.kdg.quarto.model;

import be.kdg.quarto.model.enums.AILevel;

public class AI extends Player{
    private AILevel difficultyLevel;
    private PlayingStrategy strategy;
    private String description;


    public AI(String name , AILevel difficultyLevel , PlayingStrategy strategy, String description) {
        super(name);
        setDifficultyLevel(difficultyLevel);
        this.strategy = strategy;
        this.description = description;
    }


    public AILevel getDifficultyLevel() {
        return difficultyLevel;
    }

    public void setDifficultyLevel(AILevel difficultyLevel) {
        this.difficultyLevel = difficultyLevel;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

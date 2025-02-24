package be.kdg.quarto.model;

import be.kdg.quarto.model.enums.AiLevel;

public class Ai extends Player{
    private AiLevel difficultyLevel;
    private PlayingStrategy strategy;


    public Ai(String name , AiLevel difficultyLevel , PlayingStrategy strategy) {
        super(name);
        setDifficultyLevel(difficultyLevel);
        this.strategy = strategy;
    }


    public AiLevel getDifficultyLevel() {
        return difficultyLevel;
    }

    public void setDifficultyLevel(AiLevel difficultyLevel) {
        this.difficultyLevel = difficultyLevel;
    }


}

package src.be.kdg.quarto.model;

public class Ai extends Player{
    private AiLevel difficultyLevel;


    public Ai(String name , AiLevel difficultyLevel) {
        super(name);
        setDifficultyLevel(difficultyLevel);
    }


    public AiLevel getDifficultyLevel() {
        return difficultyLevel;
    }

    public void setDifficultyLevel(AiLevel difficultyLevel) {
        this.difficultyLevel = difficultyLevel;
    }

}

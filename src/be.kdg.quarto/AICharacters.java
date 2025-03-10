package be.kdg.quarto;

import be.kdg.quarto.model.Ai;
import be.kdg.quarto.model.PlayingStrategy;
import be.kdg.quarto.model.enums.AiLevel;

import java.util.ArrayList;
import java.util.List;

public class AICharacters {
    private static AICharacters instance;
    private final List<Ai> characters;

    private AICharacters() {
        // Initializing the List
        this.characters = new ArrayList<>();
        Ai bobAi = new Ai("Bob", AiLevel.EASY, new PlayingStrategy("No Strategy"),"New to the game, understands \n the basic rules but has \n no other knowledge");
        this.characters.add(bobAi);
        // Add more AI characters here...
    }

    // Public static method to provide access to the single instance
    public static AICharacters getInstance() {
        if (instance == null) {
            // Create the instance only if it does not exist
            instance = new AICharacters();
        }
        return instance;
    }

    public List<Ai> getCharacters() {
        return this.characters;
    }

    public Ai getCharacter(int index) {
        // TO DO, check if index is out of bounds
        return this.characters.get(index);
    }
}
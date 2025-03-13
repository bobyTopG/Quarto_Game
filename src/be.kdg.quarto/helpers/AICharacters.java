package be.kdg.quarto.helpers;

import be.kdg.quarto.model.Ai;
import be.kdg.quarto.model.enums.AiLevel;

import java.util.ArrayList;
import java.util.List;

public class AICharacters {
    private final static List<Ai> characters;

    static {
        // Initializing the List
        characters = new ArrayList<>();
        Ai bobAi = new Ai("Bob", AiLevel.EASY, null,"New to the game, understands \n the basic rules but has \n no other knowledge");
        characters.add(bobAi);
        // Add more AI characters here...
    }

    public static List<Ai> getCharacters() {
        return characters;
    }

    public static Ai getCharacter(int index) {
        // TO DO, check if index is out of bounds
        return characters.get(index);
    }
}
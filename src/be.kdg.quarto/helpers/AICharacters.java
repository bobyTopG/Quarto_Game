package be.kdg.quarto.helpers;

import be.kdg.quarto.model.Ai;
import be.kdg.quarto.model.enums.AiLevel;

import java.util.ArrayList;
import java.util.List;

public class AICharacters {
    private final static List<Ai> characters;

    static {
        characters = new ArrayList<>();

        Ai bobAi = new Ai("Bob", AiLevel.EASY, null,"New to the game, understands \n the basic rules but has \n no other knowledge");
        characters.add(bobAi);

        Ai tomAi = new Ai("Bob", AiLevel.HARD, null,"A strategic player\n who has a good understanding \n of the game and makes \n calculated moves.");
        characters.add(tomAi);

    }

    public static List<Ai> getCharacters() {
        return characters;
    }

    public static Ai getCharacter(int index) {
        // TO DO, check if index is out of bounds
        return characters.get(index);
    }
}
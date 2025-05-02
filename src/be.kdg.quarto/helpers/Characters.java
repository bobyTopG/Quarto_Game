package be.kdg.quarto.helpers;

import be.kdg.quarto.model.Ai;
import be.kdg.quarto.model.Human;
import be.kdg.quarto.model.Player;
import be.kdg.quarto.model.enums.AiLevel;
import be.kdg.quarto.model.strategies.MixedPlayingStrategy;
import be.kdg.quarto.model.strategies.RandomPlayingStrategy;
import be.kdg.quarto.model.strategies.RuleBasedStrategy;

import java.util.ArrayList;
import java.util.List;

public class Characters {
    private static final List<Player> characters = new ArrayList<>();

    static {
        // Initializing the List
        Ai bobAi = new Ai(1, "Bob", AiLevel.EASY, new RandomPlayingStrategy(), "New to the game, understands \n the basic rules but has \n no other knowledge");
        Ai catAi = new Ai(2, "Cat", AiLevel.MEDIUM, new MixedPlayingStrategy(), "A cat, \n smarter than it seems");
        Ai robertAi = new Ai(3, "Robert", AiLevel.HARD, new RuleBasedStrategy(), "A robot specifically designed \n to beat you at the game.");
        Player friend = new Human(4,"Friend" , null);

        characters.add(bobAi);
        characters.add(catAi);
        characters.add(robertAi);
        characters.add(friend);
        // Add more AI characters here...
    }

    public static List<Player> getCharacters() {
        return characters;
    }

    public static Player getCharacter(int index) {
        // Check if index is out of bounds
        if (index < 0 || index >= characters.size()) {
            return null;
        }
        return characters.get(index);
    }
}
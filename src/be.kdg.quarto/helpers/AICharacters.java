package be.kdg.quarto.helpers;

import be.kdg.quarto.model.Ai;
import be.kdg.quarto.model.PlayingStrategy;
import be.kdg.quarto.model.enums.AiLevel;
//import be.kdg.quarto.model.strategies.MiniMax.MiniMaxStrategy;
import be.kdg.quarto.model.strategies.MiniMax.MiniMaxStrategy;
import be.kdg.quarto.model.strategies.RandomPlayingStrategy;
import be.kdg.quarto.model.strategies.RuleBasedStrategy;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AICharacters {
    private final static List<Ai> characters;

    static {
        // Initializing the List
        characters = new ArrayList<>();
        List<Integer> ids = new ArrayList<>();
        List<String> names = new ArrayList<>();

        try (PreparedStatement ps = DbConnection.connection.prepareStatement("SELECT player_id, name FROM players WHERE is_ai = true;")) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                ids.add(rs.getInt("player_id"));
                names.add(rs.getString("name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        Ai bobAi = new Ai(ids.getFirst(), names.getFirst(), AiLevel.EASY, new RandomPlayingStrategy(), "New to the game, understands \n the basic rules but has \n no other knowledge");
        Ai robertAi = new Ai(ids.get(1), names.get(1), AiLevel.HARD, new RuleBasedStrategy(), "A robot specifically designed \n to beat you at the game.");
        characters.add(bobAi);
        characters.add(robertAi);
        // Add more AI characters here...
    }


    public static List<Ai> getCharacters() {
        return characters;
    }

    public static Ai getCharacter(int index) {
        // TODO: check if index is out of bounds
        return characters.get(index);
    }
}
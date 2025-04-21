package be.kdg.quarto.helpers;

import be.kdg.quarto.model.Ai;
import be.kdg.quarto.model.Human;
import be.kdg.quarto.model.Player;
import be.kdg.quarto.model.enums.AiLevel;
import be.kdg.quarto.model.strategies.RandomPlayingStrategy;
import be.kdg.quarto.model.strategies.RuleBasedStrategy;

import java.util.ArrayList;
import java.util.List;

public class AICharacters {
    private List<Player> characters;

public AICharacters() {
    // Initializing the List
    characters = new ArrayList<Player>();
    List<Integer> ids = new ArrayList<>();
    List<String> names = new ArrayList<>();

//        try (PreparedStatement ps = DbConnection.connection.prepareStatement("SELECT player_id, name FROM players WHERE is_ai = true;")) {
//            ResultSet rs = ps.executeQuery();
//            while (rs.next()) {
//                ids.add(rs.getInt("player_id"));
//                names.add(rs.getString("name"));
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }

    Ai bobAi = new Ai(1, "Bob", AiLevel.EASY, new RandomPlayingStrategy(), "New to the game, understands \n the basic rules but has \n no other knowledge");
    Ai robertAi = new Ai(1, "Robert", AiLevel.HARD, new RuleBasedStrategy(), "A robot specifically designed \n to beat you at the game.");
    //Ai catAi = new Ai(1, "Cat", AiLevel.MEDIUM, new RuleBasedStrategy(), "A cat, \n smarter than it seems");
    Player friend = new Human("Friend" , null);

    characters.add(bobAi);
    //characters.add(catAi);
    characters.add(robertAi);
    characters.add(friend);
    // Add more AI characters here...
}

    public List<Player> getCharacters () {
        return characters;
    }

    public  Player getCharacter ( int index){
        // TODO: check if index is out of bounds
        return characters.get(index);
    }

}
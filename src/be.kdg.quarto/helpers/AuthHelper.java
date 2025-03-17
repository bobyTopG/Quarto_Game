package be.kdg.quarto.helpers;

import be.kdg.quarto.model.Player;

public class AuthHelper {

    private Player loggedInPlayer;

    public Player getLoggedInPlayer() {
        return loggedInPlayer;
    }

    public void setLoggedInPlayer(Player loggedInPlayer) {
        this.loggedInPlayer = loggedInPlayer;
    }
}

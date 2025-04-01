package be.kdg.quarto.model;

import be.kdg.quarto.helpers.Auth.AuthException;
import be.kdg.quarto.helpers.Auth.AuthHelper;

/**
 * Represents a human player in the Quarto game.
 */
public class Human extends Player {
    private String password;
    public Human(String name, String password) {
        super(name);
        this.password = password;
    }

    public Human(int id, String name, String password) {
        super(id, name);
        this.password = password;
    }
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    public boolean loginPlayer(String password, AuthHelper authHelper) throws AuthException {
        try {
            Human loggedInHuman = (Human) AuthHelper.login(getName(), password);
            return loggedInHuman != null;
        } catch (AuthException e) {
            return false;
        }
    }
    @Override
    public String toString() {
        return "Human{" +
                "id=" + getId() +
                ", name='" + getName() + '\'' +
                '}';
    }
}
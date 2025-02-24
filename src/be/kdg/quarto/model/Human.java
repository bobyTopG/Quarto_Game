package be.kdg.quarto.model;

public class Human extends Player {
    private String password;


    public Human(String name , String password) {
        super(name);
        setPassword(password);
    }

    private void setPassword(String password) {
        this.password = password;
    }

    public boolean checkIfPasswordMatch(String password) {
        return this.password.equals(password);
    }

    public void loginPlayer(String password) {
        // todo: make this
    }
}

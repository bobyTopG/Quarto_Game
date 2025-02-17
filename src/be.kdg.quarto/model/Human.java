package src.be.kdg.quarto.model;

public class Human extends Player {
    private String password;


    public Human(String name , String password) {
        super(name);
        setPassword(password);
    }

    private void setPassword(String password) {
        this.password = password;
    }
}

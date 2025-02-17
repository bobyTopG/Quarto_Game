package src.be.kdg.quarto.model;

public abstract class Player {
    public Player(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }


    private String name;
}

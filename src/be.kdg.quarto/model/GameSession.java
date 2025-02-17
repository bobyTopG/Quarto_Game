package src.be.kdg.quarto.model;

public class GameSession {
    private Player winner;
    private Player player1, player2;


    public GameSession(Player winner, Player player1, Player player2) {
        this.winner = winner;
        this.player1 = player1;
        this.player2 = player2;
    }

    public Player getWinner() {
        return winner;
    }

    public void setWinner(Player winner) {
        this.winner = winner;
    }
}

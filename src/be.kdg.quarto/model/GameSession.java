package be.kdg.quarto.model;

public class GameSession {
    private Player winner;
    private final Player player1;
    private final Player player2;

    public GameSession(Player player1, Player player2) {
       this.player1 = player1;
       this.player2 = player2;
    }

    public Player getOtherPlayer(Player currentPlayer) {
        if (currentPlayer.equals(player1)) return player2;
        if (currentPlayer.equals(player2)) return player1;
        throw new IllegalArgumentException("Given player is not part of this session.");
    }

    public boolean hasWinner() {
        return winner != null;
    }
}
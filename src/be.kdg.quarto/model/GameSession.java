package be.kdg.quarto.model;

import be.kdg.quarto.helpers.CreateHelper;
import be.kdg.quarto.helpers.DbConnection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.Random;

public class GameSession {
    private Player player;
    private Player opponent;
    private Game game;
    private Player currentPlayer;
    private Player winner;
    private Date startTime;
    private Date endTime;
    private boolean isCallingQuarto = false;

    private int gameSessionId;


    public GameSession(Player player, Player opponent) {
        this.game = new Game();
        this.opponent = opponent;
        this.player = player;
        this.currentPlayer = getRandomPlayer();
        startTime = new Date();

        if (this.opponent instanceof Ai aiOpponent) {
            aiOpponent.getStrategy().fillNecessaryData(this);
        }

        if (isAiTurn()) {
            handleAiTurn();
        }

        // insert a new game session into db
        try (PreparedStatement ps = DbConnection.connection.prepareStatement(DbConnection.setGameSession(),
                Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, this.player.getId());
            ps.setInt(2, this.opponent.getId());
            ps.executeUpdate();

            // save the new game session id
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                this.gameSessionId = rs.getInt(1);
                System.out.println("game session: " + gameSessionId);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public Player getRandomPlayer() {
        int rand = new Random().nextInt(2);

        return rand == 1 ? opponent : player;
    }

    public Player callQuarto() {

        if (game.getGameRules().checkWin()) {
//            CreateHelper.createAlert("Game Over", currentPlayer.getName() + " Has won the game!", "Game Win");
            endTime = new Date();

            saveGameSession(currentPlayer.getId());
            return currentPlayer;
        }
        return null;
    }

    public void switchTurns() {
        currentPlayer = currentPlayer == player ? opponent : player;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public void placePieceAi() {
        Ai ai = getAiPlayer();
        if (ai == null || currentPlayer != ai) return; // Not AI’s turn
        if (game.getSelectedPiece() != null) {
            // Placing
            game.placePiece(ai.getStrategy().selectTile(), ai);

            if (ai.getStrategy().isCallingQuarto()) {
                // check for win later if calling quarto
                isCallingQuarto = true;
            }

            game.setSelectedPiece(null);
        }
    }

    public void pickPieceAi() {
        Ai ai = getAiPlayer();
        if (ai == null || currentPlayer != ai) return; // Not AI’s turn

        if (game.getSelectedPiece() == null) {
            // Picking
            try {
                pickPiece(ai.getStrategy().selectPiece(), ai);
            } catch (NullPointerException e) {
                for (Move move : game.getMoves()) {
                    System.out.println(move);
                }
                CreateHelper.createAlert("Game Over", "Game Over", "It is a tie!");
            }
        }
    }

    public void handleAiTurn() {
        Ai ai = getAiPlayer();
        if (ai == null || currentPlayer != ai) return; // Not AI’s turn

        if (game.getSelectedPiece() != null) {
            // Placing
            game.placePiece(ai.getStrategy().selectTile(), ai);

            if (ai.getStrategy().isCallingQuarto()) {
                setWinner(callQuarto());
            }

            game.setSelectedPiece(null);
            handleAiTurn();
        } else {
            // Picking
            try {
                pickPiece(ai.getStrategy().selectPiece(), ai);
            } catch (NullPointerException e) {
                for (Move move : game.getMoves()) {
                    System.out.println(move);
                }
                CreateHelper.createAlert("Game Over", "Game Over", "There are no pieces to select!");
            }
        }
    }

    private void setWinner(Player player) {
        this.winner = player;
    }

    private boolean isAiTurn() {
        return currentPlayer instanceof Ai;
    }

    public void pickPiece(Piece piece, Player player) {
        game.setSelectedPiece(piece);
        game.getPiecesToSelect().getTiles().stream().filter(tile -> piece.equals(tile.getPiece())).findFirst().ifPresent(tile -> tile.setPiece(null));

        game.endMove(player);
        switchTurns();
    }

    // save game session to db
    public void saveGameSession(int playerId) {
        try (PreparedStatement ps = DbConnection.connection.prepareStatement(DbConnection.updateGameSession())) {
            System.out.println("game session: " + gameSessionId);
            ps.setInt(1, playerId);
            ps.setBoolean(2, true);
            ps.setInt(3, gameSessionId);
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // to prevent error in case if AI won during the counting of timer
    public void handlePendingWin() {
        if (isCallingQuarto) {
            javafx.application.Platform.runLater(() -> {
                callQuarto();
                isCallingQuarto = false;
            });
        }
    }

    public Player getPlayer() {
        return player;
    }

    public void setOpponent(Player opponent) {
        this.opponent = opponent;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    private Ai getAiPlayer() {
        if (player instanceof Ai aiPlayer) return aiPlayer;
        if (opponent instanceof Ai aiOpponent) return aiOpponent;
        return null;
    }

    public Player getOpponent() {
        return opponent;
    }

    private Ai getAiPlayer(Player player) {
        return player instanceof Ai ai ? ai : null;
    }

    public Game getGame() {
        return game;
    }

    public Player getWinner() {
        return winner;
    }
}
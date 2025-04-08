package be.kdg.quarto.model;

import be.kdg.quarto.helpers.CreateHelper;
import be.kdg.quarto.helpers.DbConnection;

import java.sql.*;
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

        // insert a new game session into db
        try (PreparedStatement ps = DbConnection.connection.prepareStatement(DbConnection.setGameSession(), Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, this.player.getId());
            ps.setInt(2, this.opponent.getId());
            ps.executeUpdate();

            // save the new game session id
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                this.gameSessionId = rs.getInt(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (this.opponent instanceof Ai aiOpponent) {
            aiOpponent.getStrategy().fillNecessaryData(this);
        }

        if (isAiTurn()) {
            handleAiTurn();
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

            endGameSession(currentPlayer.getId());
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
            placePiece(ai.getStrategy().selectTile(), ai);

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
            placePiece(ai.getStrategy().selectTile(), ai);

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
        int moveIdTemp = -1;

        // insert move into db
        try (PreparedStatement ps = DbConnection.connection.prepareStatement(DbConnection.setMove(),
                Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, gameSessionId);
            ps.setInt(2, currentPlayer.getId());
            ps.setTimestamp(3,
                    new java.sql.Timestamp(game.getCurrentMove().getStartTime().getTime()));
            ps.setTimestamp(4,
                    new java.sql.Timestamp(game.getCurrentMove().getEndTime().getTime()));
            ps.setInt(5, game.getCurrentMove().getMoveNumber());
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                moveIdTemp = rs.getInt(1);
            }
            System.out.println("move id: " + moveIdTemp + " current player: " + currentPlayer.getId());

        } catch (SQLException e) {
            e.printStackTrace();
        }

        // insert piece into db
        try (PreparedStatement ps = DbConnection.connection.prepareStatement(DbConnection.setPiece())) {
            ps.setInt(1, piece.getPieceId());
            ps.setInt(2, moveIdTemp);
            ps.setInt(3, game.getCurrentMove().getPosition());
            ps.executeUpdate();

            System.out.println("move id: " + moveIdTemp + " position: " + game.getCurrentMove().getPosition());

        } catch (SQLException e) {
            e.printStackTrace();
        }

        switchTurns();
    }

    public void placePiece(Tile selectedTile, Player player) {
        selectedTile.setPiece(game.getSelectedPiece());
        game.startMove(player, selectedTile);
    }

    // saves a finished game session to db
    public void endGameSession(int winnerId) {
        try (PreparedStatement ps = DbConnection.connection.prepareStatement(DbConnection.updateGameSession())) {
            ps.setInt(1, winnerId);
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

    public int getGameSessionId() {
        return gameSessionId;
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
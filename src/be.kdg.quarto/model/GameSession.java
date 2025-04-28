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
    private Date startTime;
    private Date endTime;
    private boolean isCallingQuarto = false;
    private final GameTimer gameTimer;
    private int gameSessionId;

    public boolean isOnline;
    public GameSession(Player player, Player opponent , Player currentPlayer, boolean online) {
        this.game = new Game();
        this.opponent = opponent;
        this.player = player;
        this.isOnline = online;

        startTime = new Date();
        this.currentPlayer = currentPlayer == null ? player : opponent;
        gameTimer = new GameTimer(this.game);
        game.startNewMove(this.currentPlayer);


        if(isOnline) {
            saveGameSessionToDb();
        }
        if (this.opponent instanceof Ai aiOpponent) {
            aiOpponent.getStrategy().fillNecessaryData(this);
        }
        if(currentPlayer == this.opponent) {
            pickPieceAi();
        }


    }

    public GameSession(Player player, Player opponent, Game game, Date startTime, int gameSessionId) {
        this.game = game;
        this.opponent = opponent;
        this.player = player;
        this.startTime = startTime;
        this.gameSessionId = gameSessionId;
        if (game.getMoves().getLast().getPlayer().equals(player)) {
            this.currentPlayer = opponent;
        } else {
            this.currentPlayer = player;
        }

        this.gameTimer = new GameTimer(game, startTime);
    }


    public void callQuarto() {
        if (game.getGameRules().checkWin()) {
            endTime = new Date();
            if (game.getMoves() == null) {
                game.getCurrentMove().setEndTime(endTime);
                game.getCurrentMove().setPosition(-1);
                if(isOnline)
                    saveMoveToDb(game.getCurrentMove());
            }
            if(isOnline)
                updateGameSession();
        }
    }

    public void switchTurns() {
        currentPlayer = currentPlayer == player ? opponent : player;
        if(!game.getBoard().isFull())
        {
            game.startNewMove(currentPlayer);

        }
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public void placePieceAi() {
        Ai ai = getAiPlayer();
        if (ai == null || currentPlayer != ai) return; // Not AI’s turn
        if (game.getSelectedPiece() != null) {
            placePiece(ai.getStrategy().selectTile());
            //force calling quarto at the last move
            if (ai.getStrategy().isCallingQuarto() || game.getPiecesToSelect().isEmpty()) {
                //isCallingQuarto = true;
            }
            if(game.getPiecesToSelect().isEmpty()){
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
                pickPiece(ai.getStrategy().selectPiece());
            } catch (NullPointerException ignored) {

            }
        }
    }

    public void pickPiece(Piece piece) {
        game.setSelectedPiece(piece);
        game.getPiecesToSelect().getTiles().stream().filter(tile -> piece.equals(tile.getPiece())).findFirst().ifPresent(tile -> tile.setPiece(null));
        game.pickPieceIntoMove(startTime);

        if(isOnline)
            saveMoveToDb(game.getCurrentMove());

        game.endMove();
        switchTurns();
    }

    public void placePiece(Tile selectedTile) {
        selectedTile.setPiece(game.getSelectedPiece());
        game.placePieceIntoMove(selectedTile);

    }

    public void saveMoveToDb(Move move) {
        int moveIdTemp = -1;
        try (PreparedStatement ps = DbConnection.connection.prepareStatement(DbConnection.setMove(),
                Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, gameSessionId);
            ps.setInt(2, currentPlayer.getId());
            ps.setTimestamp(3,
                    new java.sql.Timestamp(move.getStartTime().getTime()));
            ps.setTimestamp(4,
                    new java.sql.Timestamp(move.getEndTime().getTime()));
            ps.setInt(5, move.getMoveNumber());
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                moveIdTemp = rs.getInt(1);
            }


        } catch (SQLException e) {
//            e.printStackTrace();
        }

        savePausePeriodsFromMoveToDb(move, moveIdTemp);
        if (move.getPosition() != -1) {
            savePieceToDb(moveIdTemp, move.getSelectedPiece(), move.getPosition());
        }

    }

    public void savePausePeriodsFromMoveToDb(Move move, int moveId) {
        for (PausePeriod pausePeriod : move.getPausePeriods()) {
            try (PreparedStatement ps = DbConnection.connection.prepareStatement(DbConnection.setPausePeriod())) {
                ps.setInt(1, moveId);
                ps.setTimestamp(2, new java.sql.Timestamp(pausePeriod.getPauseStart().getTime()));
                ps.setTimestamp(3, new java.sql.Timestamp(pausePeriod.getPauseEnd().getTime()));

                ps.executeUpdate();

            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
        }
    }

    private void savePieceToDb(int moveId, Piece piece, int position) {
        try (PreparedStatement ps = DbConnection.connection.prepareStatement(DbConnection.setPiece())) {
            ps.setInt(1, piece.getPieceId());
            ps.setInt(2, moveId);
            ps.setInt(3, position);
            ps.executeUpdate();


        } catch (SQLException e) {
//            e.printStackTrace();
        }
    }

    public void saveGameSessionToDb() {
        try (PreparedStatement ps = DbConnection.connection.prepareStatement(DbConnection.setGameSession(), Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, this.player.getId());
            ps.setInt(2, this.opponent.getId());
            java.sql.Timestamp sqlStartTime = new java.sql.Timestamp(startTime.getTime());
            ps.setTimestamp(3, sqlStartTime);
            ps.executeUpdate();

            // save the new game session id
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                this.gameSessionId = rs.getInt(1);
            }

        } catch (SQLException | NullPointerException e) {
//            e.printStackTrace();
        }
    }

    // saves a finished game session to db
    public void updateGameSession() {
        try (PreparedStatement ps = DbConnection.connection.prepareStatement(DbConnection.updateGameSession())) {
            ps.setInt(1, isCallingQuarto ? currentPlayer.getId() : null);
            ps.setBoolean(2, true);
            java.sql.Timestamp sqlEndTime = new java.sql.Timestamp(endTime.getTime());
            ps.setTimestamp(3, sqlEndTime);
            ps.setInt(4, gameSessionId);
            ps.executeUpdate();

        } catch (SQLException e) {
//            e.printStackTrace();
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

    public boolean isCallingQuarto() {
        return isCallingQuarto;
    }

    public int getGameSessionId() {
        return gameSessionId;
    }

    public Player getPlayer() {
        return player;
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

    public Game getGame() {
        return game;
    }

    public GameTimer getGameTimer() {
        return gameTimer;
    }
}
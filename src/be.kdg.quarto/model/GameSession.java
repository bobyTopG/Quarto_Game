package be.kdg.quarto.model;

import be.kdg.quarto.helpers.Characters;
import be.kdg.quarto.helpers.Auth.AuthHelper;
import be.kdg.quarto.helpers.DbConnection;
import be.kdg.quarto.model.enums.Color;
import be.kdg.quarto.model.enums.Fill;
import be.kdg.quarto.model.enums.Shape;
import be.kdg.quarto.model.enums.Size;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * Represents a full session of a Quarto game between two players (human or AI).
 * A GameSession handles the game flow, including player turns, move tracking,
 * AI integration, database persistence, and game timer management.
 *
 * <p>It supports both offline and online games. In online mode, it saves and loads
 * session data, moves, and piece selections from a database. It can resume a session
 * using an ID and restore all previous moves and states.</p>
 *
 * <p>Responsibilities include:</p>
 * <ul>
 *     <li>Initializing new games and restoring saved ones</li>
 *     <li>Switching turns and placing/picking pieces</li>
 *     <li>Calling Quarto and handling endgame logic</li>
 *     <li>Supporting AI player actions via strategy patterns</li>
 *     <li>Saving and updating data in the database</li>
 * </ul>
 *
 * <p>It interacts with Game, Player, Tile, Piece, Move, GameTimer, and the database layer
 * via prepared statements defined in DbConnection.</p>
 */

public class GameSession {
    private Player player;
    private Player opponent;
    private final Game game;
    private Player currentPlayer;
    private Date startTime;
    private Date endTime;

    //variable used for AI, it's purpose to delay quarto call until the animation ends
    private boolean isCallingQuarto;
    private final GameTimer gameTimer;
    private int gameSessionId;



    private boolean isCompleted;
    public boolean isOnline;

    /**
     * Constructor for starting a new game
     * @param player the current logged in player/guest player if not logged in
     * @param opponent the selected opponent
     * @param currentPlayer specifies who starts first
     * @param online specifies if the saving is activated or not
     * @throws SQLException
     */
    public GameSession(Player player, Player opponent, Player currentPlayer, boolean online) throws SQLException {
        this.game = new Game();
        this.opponent = opponent;
        this.player = player;
        this.isOnline = online;

        startTime = new Date();
        this.currentPlayer = currentPlayer == null ? player : opponent;
        gameTimer = new GameTimer(this.game);
        game.startNewMove(this.currentPlayer);


        if (isOnline) {
            saveGameSessionToDb();
            loadPieceTypesFromDb();
        }
        if (this.opponent instanceof Ai aiOpponent) {
            aiOpponent.getStrategy().fillNecessaryData(this);
        }

    }

    /**
     * constructor for loading the game session
     * @param gameSessionId specifies which game session to load
     */
    public GameSession(int gameSessionId) throws Exception {
        this.gameSessionId = gameSessionId;
        this.player = AuthHelper.getLoggedInPlayer();
        this.game = new Game();
        loadPieceTypesFromDb();


        loadSessionFromDb();
        loadMovesFromDb();
        placePiecesOnBoard();


        currentPlayer = game.getMoves().getLast().getPlayer();
        this.gameTimer = new GameTimer(game,startTime);
        game.setCurrentMove(game.getMoves().getLast());

        if(game.getCurrentMove().getPiece() == null && game.getMoves().size() != 1) {
        Piece piece = game.getMoves().get(game.getMoves().size() - 2).getSelectedPiece();
        game.setSelectedPiece(piece);
        game.getPiecesToSelect().getTiles().stream().filter(tile -> piece.equals(tile.getPiece())).findFirst().ifPresent(tile -> tile.setPiece(null));
    }


        this.isOnline = true;
        if (this.opponent instanceof Ai aiOpponent) {
            aiOpponent.getStrategy().fillNecessaryData(this);
        }

    }

    /**
     * Cascade deletes the specified move from the DB
     *
     */
    private void deleteMoveFromDb(int moveId) throws SQLException {
        try(PreparedStatement ps = DbConnection.connection.prepareStatement(DbConnection.deleteMove())){
            ps.setInt(1,moveId);
            ps.executeUpdate();
        }catch (Exception e){
            throw new SQLException("Failed to delete current Move");
        }
    }

    /**
     * Deletes the current GameSession from the DB (only used while Restarting or Deleting the Game, or it will break the saving system)
     */
    public void deleteCurrentGameSessionFromDb() throws SQLException {
        try (PreparedStatement ps = DbConnection.connection.prepareStatement(DbConnection.deleteGameSession())) {
            ps.setInt(1,this.gameSessionId);
            ps.executeUpdate();
        } catch (Exception e) {
            throw new SQLException("Failed to delete current gameSession");
        }
    }


    /**
     * Loads The GameSession from The DB
     */
    private void loadSessionFromDb() throws SQLException {
        try (PreparedStatement ps = DbConnection.connection.prepareStatement(DbConnection.getGameSession())) {
            ps.setInt(1, this.gameSessionId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                this.opponent = Characters.getCharacter(rs.getInt("opponent_id") - 1);
                this.startTime = rs.getTimestamp("start_time");
            }
        } catch (SQLException e) {
            throw new SQLException("Failed to load GameSession");
        }
    }
    private void loadPieceTypesFromDb() throws SQLException {
        for(Tile tile : game.getPiecesToSelect().getTiles()){
            tile.getPiece().loadIdFromDb();
        }
    }

    /**
     * Loads all the moves from the DB
     */
    private void loadMovesFromDb() throws SQLException {
        try (PreparedStatement ps = DbConnection.connection.prepareStatement(DbConnection.loadMoves())) {
            ps.setInt(1, this.gameSessionId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Player player;
                if (rs.getInt("player_id") == AuthHelper.getLoggedInPlayer().getId()) {
                    player = this.player;
                } else {
                    player = this.opponent;
                }
                Piece piece = null;
                rs.getInt("placed_piece_id");
                if(!rs.wasNull()) {
                    piece = new Piece(
                            Color.valueOf(rs.getString("placed_piece_color").toUpperCase()),
                            Size.valueOf(rs.getString("placed_piece_size").toUpperCase()),
                            Fill.valueOf(rs.getString("placed_piece_fill").toUpperCase()),
                            Shape.valueOf(rs.getString("placed_piece_shape").toUpperCase()),
                            rs.getInt("placed_piece_id")

                    );
                }

                Piece selectedPiece = null;
                rs.getInt("selected_piece_id");
                if(!rs.wasNull()) {
                    selectedPiece =  new Piece(
                            Color.valueOf(rs.getString("selected_piece_color").toUpperCase()),
                            Size.valueOf(rs.getString("selected_piece_size").toUpperCase()),
                            Fill.valueOf(rs.getString("selected_piece_fill").toUpperCase()),
                            Shape.valueOf(rs.getString("selected_piece_shape").toUpperCase()),
                            rs.getInt("selected_piece_id")

                    );
                }

                Move move = new Move(player, piece, selectedPiece, rs.getInt("pos"), rs.getInt("move_nr"), rs.getTimestamp("start_time"), rs.getTimestamp("end_time"));
                int moveId = rs.getInt("move_id");
                move.getPausePeriods().addAll(loadPausePeriodsForMove(moveId));
                rs.getTimestamp("end_time");
                if(rs.wasNull()) {
                    deleteMoveFromDb(moveId);
                }

                game.getMoves().add(move);
            }
        }
    }

    /**
     * Loads all the Pause periods for the specific turn
     */
    private List<PausePeriod> loadPausePeriodsForMove(int moveId) throws SQLException {
        List<PausePeriod> pausePeriods = new ArrayList<>();
            try (PreparedStatement ps = DbConnection.connection.prepareStatement(DbConnection.loadPausePeriods())) {
                ps.setInt(1, moveId);
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    PausePeriod pausePeriod;
                    if(rs.getTimestamp("end_time") != null)
                         pausePeriod = new PausePeriod(rs.getTimestamp("start_time"), rs.getTimestamp("end_time"));
                    else {
                        Date currentTime = new Date();
                        pausePeriod = new PausePeriod(rs.getTimestamp("start_time"), currentTime);
                    }
                    pausePeriods.add(pausePeriod);
                }
            } catch (SQLException e) {
                throw new SQLException("Failed to load pause periods for move");
            }
            return  pausePeriods;
        }

    /**
     * places the pieces on the board after loading them
     */
    private void placePiecesOnBoard() {
        if (!game.getMoves().isEmpty()) {
            for (Move move : game.getMoves()) {
                if (move.getPiece() != null && move.getPosition() != -1) {
                    game.getBoard().getTiles().get(move.getPosition()).setPiece(move.getPiece());
                    game.getPiecesToSelect().removePiece(move.getPiece());
                }
            }
        }
    }

    /**
     * check for win, if yes ends Game
     */
    public void callQuarto() throws SQLException {
        if (game.getGameRules().checkWin()) {
            endTime = new Date();
            //if move is not complete at the end of the game, complete it
            if (game.getMoves().getLast().getEndTime() == null) {
                game.getCurrentMove().setEndTime(endTime);
                if (isOnline) { saveMoveToDb(game.getCurrentMove()); }
            }
            endGameSession(false);
            gameTimer.pauseGame();
        }
    }


    /**
     * Ends the gameSession
     * @param isTie  variable to check if it is a tie
     */
    public void endGameSession(boolean isTie) throws SQLException {
        if(isCompleted) return;
        isCompleted = true;
        if (isOnline) {
            updateGameSession(isTie);
        }
    }

    public void switchTurns() {
        currentPlayer = currentPlayer == player ? opponent : player;
        if (!game.getBoard().isFull()) {
            game.startNewMove(currentPlayer);

        }
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    /**
     * places the piece for the AI, the location is gotten from the strategy.
     * Additional: calls quarto if the strategy function isCallingQuarto() returns true
     */
    public void placePieceAi() throws Exception {
        Ai ai = getAiPlayer();
        if (ai == null || currentPlayer != ai) return; // Not AI’s turn
        if (game.getSelectedPiece() != null) {
            placePiece(ai.getStrategy().selectTile());
            //force calling quarto at the last move
            if (ai.getStrategy().isCallingQuarto()) {
                isCallingQuarto = true;
            }


            game.setSelectedPiece(null);
        }
    }

    /**
     *     picks the piece for the AI, the piece is gotten from the strategy.
     */
    public void pickPieceAi() throws SQLException {
        Ai ai = getAiPlayer();
        if (ai == null || currentPlayer != ai) return; // Not AI’s turn

        if (game.getSelectedPiece() == null) {
            // Picking
            pickPiece(ai.getStrategy().selectPiece());

        }
    }

    /**
     * Picks the piece and Saves move to db
     * @param piece - the picked piece
     */
    public void pickPiece(Piece piece) throws SQLException {
        if (piece != null) {
            game.setSelectedPiece(piece);
            game.getPiecesToSelect().getTiles().stream().filter(tile -> piece.equals(tile.getPiece())).findFirst().ifPresent(tile -> tile.setPiece(null));
            game.pickPieceIntoMove(startTime);
        }
        if (isOnline) { saveMoveToDb(game.getCurrentMove()); }

        game.endMove();
        // to prevent a new move from starting if game ended
        if(!isCompleted)
            switchTurns();
    }
    /**
     * places the piece
     * @param selectedTile - the picked tile to place the piece
     */
    public void placePiece(Tile selectedTile) {
        selectedTile.setPiece(game.getSelectedPiece());
        game.placePieceIntoMove(selectedTile);

    }

    /**
     * Saves the specified move to DB
     *
     */
    public void saveMoveToDb(Move move) throws SQLException {
        int moveIdTemp = -1;
        try (PreparedStatement ps = DbConnection.connection.prepareStatement(DbConnection.setMove(),
                Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, gameSessionId);
            ps.setInt(2, currentPlayer.getId());
            ps.setTimestamp(3,
                    new java.sql.Timestamp(move.getStartTime().getTime()));
            ps.setTimestamp(4,
                    move.getEndTime() != null ? new Timestamp(move.getEndTime().getTime()) : null);
            ps.setInt(5, move.getMoveNumber());
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                moveIdTemp = rs.getInt(1);
            }

        }

        savePausePeriodsFromMoveToDb(move, moveIdTemp);

        savePieceToDb(moveIdTemp, move.getSelectedPiece(), move.getPiece(), move.getPosition());

    }

    /**
     * Saves the pause periods for the specified move
     * @param move variable given to loop through all the pause periods
     * @param moveId saves the pause periods with this moveid
     */
    public void savePausePeriodsFromMoveToDb(Move move, int moveId) throws SQLException {
        for (PausePeriod pausePeriod : move.getPausePeriods()) {
            try (PreparedStatement ps = DbConnection.connection.prepareStatement(DbConnection.setPausePeriod())) {
                ps.setInt(1, moveId);
                ps.setTimestamp(2, new Timestamp(pausePeriod.getPauseStart().getTime()));
                ps.setTimestamp(3,pausePeriod.getPauseEnd() != null ? new Timestamp(pausePeriod.getPauseEnd().getTime()) : null);

                ps.executeUpdate();

            } catch (SQLException e) {
                throw new SQLException("failed to save pause periods for move");
            }
        }
    }


    /**
     * Saves the Information for what piece was placed and then what piece was selected
     * @param moveId the move row the piece row is going to be connected to
     * @param selectedPiece the piece that was selected for the opponent
     * @param placedPiece the piece that was placed on the "position" index
     * @param position the position of the selected tile
     */
    private void savePieceToDb(int moveId, Piece selectedPiece, Piece placedPiece, int position) throws SQLException {
        try (PreparedStatement ps = DbConnection.connection.prepareStatement(DbConnection.setPiece())) {
            ps.setInt(3, moveId);

            if(selectedPiece != null) {
                ps.setInt(1, selectedPiece.getPieceId());
            }else{
                ps.setNull(1, Types.INTEGER);
            }
            if(placedPiece != null) {
                ps.setInt(2, placedPiece.getPieceId());
                ps.setInt(4, position);

            }else{
                ps.setNull(2, Types.INTEGER);
                ps.setNull(4, Types.INTEGER);

            }
            ps.executeUpdate();


        } catch (SQLException e) {
            throw new SQLException("Failed to Save piece");
        }
    }

    /**
     * Saves the current gameSession to DB
     */
    public void saveGameSessionToDb() throws SQLException {
        try (PreparedStatement ps = DbConnection.connection.prepareStatement(DbConnection.setGameSession(), Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, this.player.getId());
            ps.setInt(2, this.opponent.getId());
            java.sql.Timestamp sqlStartTime = new Timestamp(startTime.getTime());
            ps.setTimestamp(3, sqlStartTime);
            ps.executeUpdate();

            // save the new game session id
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                this.gameSessionId = rs.getInt(1);
            }

        }
    }

    /**
     * saves a Finished game session to db
     * @param isTie specifies whether it is a tie or not
     */
    public void updateGameSession(boolean isTie) throws SQLException {
        try (PreparedStatement ps = DbConnection.connection.prepareStatement(DbConnection.updateGameSession())) {
            if (!isTie) {
                ps.setInt(1, currentPlayer.getId());
            } else {
                ps.setNull(1, Types.INTEGER);
            }
            ps.setBoolean(2, true);
            Timestamp sqlEndTime = new Timestamp(endTime.getTime());
            ps.setTimestamp(3, sqlEndTime);
            ps.setInt(4, gameSessionId);
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new SQLException("Failed to save the finished game");
        }
    }

    public void handlePendingWin() throws SQLException {
        if (this.isCallingQuarto) callQuarto();
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

    public boolean isGameOver(){
        return isCompleted;
    }
}
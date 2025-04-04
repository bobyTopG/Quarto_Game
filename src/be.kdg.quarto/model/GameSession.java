package be.kdg.quarto.model;

import be.kdg.quarto.helpers.AICharacters;
import be.kdg.quarto.helpers.CreateHelper;
import be.kdg.quarto.model.enums.Color;
import be.kdg.quarto.model.enums.Size;
import be.kdg.quarto.model.enums.Shape;
import be.kdg.quarto.model.enums.Fill;

import java.util.Date;
import java.util.List;
import java.util.Random;

public class GameSession {
    private Player player;
    private Player opponent;
    private Game game;
    private Player currentPlayer;
    private Player winner;
    private Move currentMove;
    private int numberOfMoves = 1;
    private Date startTime;
    private Date endTime;

    public GameSession(Player player, Player opponent) {
        this.game = new Game();
        this.opponent = opponent;
        this.player = player;
        this.currentPlayer = getRandomPlayer();

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
            System.out.println(currentPlayer.getName() + " Has won the game!");
            CreateHelper.createAlert("Game Over", currentPlayer.getName() + " Has won the game!", "Game Win");
            return currentPlayer;
        }
        return null;
    }

    public void switchTurns() {
        currentPlayer = currentPlayer == player ? opponent : player;
        if (isAiTurn()) {
            handleAiTurn();
        }
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    private void handleAiTurn() {
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
        game.getPiecesToSelect().getTiles().stream()
                .filter(tile -> piece.equals(tile.getPiece()))
                .findFirst()
                .ifPresent(tile -> tile.setPiece(null));

        endMove(player);
        switchTurns();
    }

    public void startMove(Player player, Tile selectedTile) {
        currentMove = new Move(player, game.getBoard().getTiles().indexOf(selectedTile), game.getSelectedPiece(), numberOfMoves, getStartTimeForMove());
        game.addMove(currentMove);
        numberOfMoves++;
    }

    public void endMove(Player player) {
        if (currentMove != null) {
            currentMove.setSelectedPiece(game.getSelectedPiece());
            currentMove.setEndTime(new Date());
        } else {
            //first move made will be only choosing the piece without placing any
            currentMove = new Move(player, game.getSelectedPiece(), getStartTimeForMove(), new Date());
            game.addMove(currentMove);
        }
    }

    public Date getStartTimeForMove() {
        List<Move> moves = game.getMoves();
        if (moves == null || moves.isEmpty()) { // Check for null and empty
            return null;
        } else {
            //noinspection SequencedCollectionMethodCanBeUsed
            return moves.get(moves.size() - 1).getEndTime();
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
package be.kdg.quarto.model;

import be.kdg.quarto.model.enums.AiLevel;
import be.kdg.quarto.model.strategy.DifficultStrategy;
import be.kdg.quarto.model.strategy.PlayingStrategy;

public class GameSession {
    private Game model;
    private final Ai ai;
    private PlayingStrategy strategy;
    private final Human human;
    private Player currentPlayer;


    public GameSession () {
        this(new Human("Human", "secretPassword"),
                new Ai("Computer", AiLevel.HARD, null, "Description") , new Game());
    }

    public GameSession(Human human, Ai ai , Game model) {
        this.human = human;
        this.ai = ai;
        this.model = model;
        this.currentPlayer = human;

        //ai.setStrategy(new DifficultStrategy(this));
        if (isAiTurn()) {
            handleAiTurn();
        }
    }

    public void restartGame() {
        System.out.println("Restarting game session");
        model.getTilesToSelect().getTiles().clear();
        model.getPlacedTiles().getTiles().clear();
        model.getCurrentTile().setPiece(null);

        model.getTilesToSelect().generateAllTiles();
        model.getPlacedTiles().createEmptyTiles();
        getAi().resetStrategy();

        if (isAiTurn()) {
            handleAiTurn();
        }
    }

    private void handleAiTurn() {
        if (currentPlayer == ai) {
            if (model.getCurrentTile().getPiece() != null) {
                //Placing
                ai.getStrategy().placePiece().setPiece(model.getCurrentTile().getPiece());//set piece to the chosen tile
                model.getCurrentTile().setPiece(null);
                model.getGameRules().isGameOver();
                handleAiTurn();
            } else {
                //Picking

                model.getCurrentTile().setPiece(ai.getStrategy().selectPiece().getPiece());
                model.getTilesToSelect().getTiles()
                        .get(model.getTilesToSelect().getTiles()
                                .indexOf(model.getCurrentTile())).setPiece(null);
                if (model.getGameRules().isGameOver()) {
                    model.getGameRules().setWinner(currentPlayer);
                }

                switchTurns();
            }
        }
    }

    public void switchTurns() {
        currentPlayer = currentPlayer == ai ? human : ai;
        if (isAiTurn()) {
            handleAiTurn();
        }
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }
    public Ai getAi() {
        return ai;
    }
    private boolean isAiTurn() {
        return currentPlayer == ai;
    }

    public Player getHuman() {
        return human;
    }

    public Game getModel() {
        return model;
    }
}
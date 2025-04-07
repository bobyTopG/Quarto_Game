package be.kdg.quarto.model.strategies;

import be.kdg.quarto.model.*;
import be.kdg.quarto.rulebasedsystem.InterfaceEngine;

import java.util.List;
import java.util.stream.Collectors;

public class RuleBasedStrategy implements PlayingStrategy {
    private final InterfaceEngine engine;
    private Move lastMove;
    private GameSession game;

    public RuleBasedStrategy() {
        this.engine = new InterfaceEngine();
    }

    @Override
    public Tile selectTile() {
        Move move = new Move();
        try {

            engine.determineFacts(game.getGame().getBoard());
            engine.applyRules(game.getGame().getBoard(), move);

            move.setPlayer(game.getCurrentPlayer());
            move.setPiece(game.getGame().getSelectedPiece());
            move.setSelectedPiece(game.getGame().getSelectedPiece());
            move.setMoveNumber(game.getGame().getCurrentMove().getMoveNumber());
            move.setStartTime(new java.util.Date());
            game.getGame().getGameRules().checkPureWin();
            System.out.println("!!!!!!!!!!" + game.getGame().getGameRules().checkPureWin());

        } catch (Exception e) {
            System.out.println("Rule engine error during tile selection: " + e.getMessage());
        }
        System.out.println("Selected move: " + move);

        int pos = move.getPosition();
        if (pos >= 0 && pos < game.getGame().getBoard().getTiles().size()) {
            Tile tile = game.getGame().getBoard().getTile(pos);
            if (tile.isEmpty()) return tile;
        }

        // MiniMax-like fallback logic
        Board board = game.getGame().getBoard();
        Piece selected = game.getGame().getSelectedPiece();

        for (Tile tile : board.getTiles()) {
            if (tile.getPiece() == null) {
                tile.setPiece(selected);
                GameRules rules = new GameRules(board, List.of());
                if (rules.checkPureWin()) {
                    tile.setPiece(null);
                    return tile;
                }
                tile.setPiece(null);
            }
        }

        for (Tile tile : board.getTiles()) {
            if (tile.getPiece() == null) {
                return tile;
            }
        }

        return null;
    }

    @Override
    public Piece selectPiece() {
        Move move;
        try {
            move = new Move();
            engine.determineFacts(game.getGame().getBoard());
            engine.applyRules(game.getGame().getBoard(), move);
        } catch (Exception e) {
            System.out.println("Rule engine error during piece selection: " + e.getMessage());
            move = new Move();
        }

        Piece selected = move.getPiece();
        if (selected != null) return selected;

        // MiniMax-like fallback
        List<Tile> piecesLeft = game.getGame().getPiecesToSelect().getTiles().stream()
                .filter(t -> t.getPiece() != null)
                .collect(Collectors.toList());

        Board board = game.getGame().getBoard();

        for (Tile pieceTile : piecesLeft) {
            Piece piece = pieceTile.getPiece();
            boolean dangerous = false;

            for (Tile tile : board.getTiles()) {
                if (tile.getPiece() != null) continue;

                tile.setPiece(piece);
                GameRules rules = new GameRules(board, List.of());
                if (rules.checkPureWin()) {
                    dangerous = true;
                    tile.setPiece(null);
                    break;
                }
                tile.setPiece(null);
            }

            if (!dangerous) {
                return piece;
            }
        }

        // Random fallback
        return piecesLeft.stream().findAny().map(Tile::getPiece).orElse(null);
    }

    @Override
    public String getName() {
        return "";
    }

    @Override
    public void fillNecessaryData(GameSession gameSession) {
        this.game = gameSession;
    }

    @Override
    public boolean isCallingQuarto() {
        return false;
    }
}

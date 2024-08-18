package battleship.controller;

import battleship.model.ship.Ship;
import battleship.model.table.Point;
import battleship.model.table.BattleshipTable;
import battleship.model.table.CellState;
import battleship.view.UIHandler;

public class GameController {

    private final GameEngine gameEngine;
    private final UIHandler uiHandler;

    public GameController(GameEngine gameEngine, UIHandler uiHandler) {
        this.gameEngine = gameEngine;
        this.uiHandler = uiHandler;
    }

    public void start() {
        uiHandler.promptPlayerToPlaceShips(1);
        gameEngine.setupFleetForPlayer(1);
        uiHandler.switchPlayer();

        uiHandler.promptPlayerToPlaceShips(2);
        gameEngine.setupFleetForPlayer(2);
        uiHandler.switchPlayer();

        while (!gameEngine.isGameOver()) {
            uiHandler.displayGameState(gameEngine.getCurrentPlayer());
            uiHandler.promptPlayerToShoot(gameEngine.getCurrentPlayer());

            if (gameEngine.isFleetSunk(gameEngine.getOpponent())) {
                uiHandler.displayWinner(gameEngine.getCurrentPlayer());
                break;
            }

            gameEngine.switchTurns();
            uiHandler.switchPlayer();
        }
    }
}

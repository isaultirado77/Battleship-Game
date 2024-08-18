package battleship.controller;

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
            uiHandler.displayUserTables(gameEngine.getCurrentPlayerTable(), gameEngine.getCurrentPlayerFogTable());
            uiHandler.displayGameState(gameEngine.getCurrentPlayer());
            gameEngine.takeAShot();
            if (gameEngine.isFleetSunk(gameEngine.getOpponent())) {
                uiHandler.displayWinner(gameEngine.getCurrentPlayer());
                break;
            }

            gameEngine.switchTurns();
            uiHandler.switchPlayer();
        }
    }
}

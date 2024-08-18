package battleship;

import java.util.Scanner;

import battleship.controller.GameController;
import battleship.controller.GameEngine;
import battleship.view.UIHandler;

public class Main {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        GameEngine gameEngine = new GameEngine();
        UIHandler uiHandler = new UIHandler(scanner);
        GameController gameController = new GameController(gameEngine, uiHandler);

        gameController.start();
    }
}


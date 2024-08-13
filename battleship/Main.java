package battleship;

import battleship.controller.GameController;

public class Main {

    public static void main(String[] args) {
        GameController gameController = new GameController();
        gameController.makeFleet();

        /**
         * Stage 3/6: Adding shooting functionality
         */
        System.out.println("\n The game starts! \n");
        gameController.getTable().displayTable();
        System.out.println("\n Take a shot! \n");
        gameController.takeAShot();
    }
}


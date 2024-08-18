package battleship.view;

import battleship.model.ship.Battleship;
import battleship.model.ship.Ship;
import battleship.model.table.BattleshipTable;

import java.util.Scanner;

public class UIHandler {

    private final Scanner scanner;

    public UIHandler(Scanner scanner) {
        this.scanner = scanner;
    }

    public void promptPlayerToPlaceShips(int playerNumber) {
        System.out.println("Player " + playerNumber + ", place your ships on the game field\n");
    }

    public void switchPlayer() {
        System.out.println("\nPress Enter and pass the move to another player\n...");
        scanner.nextLine();
    }


    public void promptPlayerToShoot(int playerNumber) {
        System.out.println("\nPlayer " + playerNumber + ", enter coordinates to shoot:\n");
    }

    public String getInputCoordinates(Ship ship) {
        return scanner.nextLine();
    }

    public void displayError(String message) {
        System.out.println("\n" + message + " Try again:\n");
    }

    public void displayUserTables(BattleshipTable table, BattleshipTable fogTable){
        fogTable.displayTable();
        System.out.println("---------------------");
        table.displayTable();
    }

    public void displayHit() {
        System.out.println("\nYou hit a ship!");
    }

    public void displayMiss() {
        System.out.println("\nYou missed!");
    }

    public void displayShipSunk() {
        System.out.println("\nYou sank a ship!");
    }

    public void displayWinner(int playerNumber) {
        System.out.println("Player " + playerNumber + " won. Congratulations!");
    }

    public void displayGameState(int playerNumber) {
        System.out.println("\nPlayer " + playerNumber + "'s turn!\n");
    }
}

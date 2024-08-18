package battleship.view;

import battleship.model.ship.Ship;

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

    public void displayGameState(int playerNumber) {
        System.out.println("\nPlayer " + playerNumber + "'s turn!");
    }

    public void promptPlayerToShoot(int playerNumber) {
        System.out.println("\nPlayer " + playerNumber + ", enter coordinates to shoot:");
    }

    public String getInputCoordinates(Ship ship) {
        System.out.println("Enter the coordinates of the " + ship.shipInfo() + ":");
        return scanner.nextLine();
    }

    public void displayError(String message) {
        System.out.println(message + " Try again:");
    }

    public void displayHit() {
        System.out.println("You hit a ship!");
    }

    public void displayMiss() {
        System.out.println("You missed!");
    }

    public void displayShipSunk() {
        System.out.println("You sank a ship!");
    }

    public void displayWinner(int playerNumber) {
        System.out.println("Player " + playerNumber + " won. Congratulations!");
    }
}

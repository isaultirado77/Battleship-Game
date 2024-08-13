package battleship.controller;

import java.util.Scanner;

import battleship.model.table.BattleshipTable;
import battleship.model.table.CellState;
import battleship.model.table.Point;
import battleship.model.ship.Ship;

public class GameController {


    private BattleshipTable table;
    private FleetManager fleet;
    private CoordinateParser coordinateParser;

    public GameController() {
        table = new BattleshipTable();
        fleet = new FleetManager();
        coordinateParser = new CoordinateParser();
    }

    public void makeFleet() {
        Scanner scanner = new Scanner(System.in);

        table.displayTable();

        for (Ship ship : fleet.getFleet()) {
            System.out.println("\n Enter the coordinates of the " + ship.shipInfo() + ": \n");
            ShipBuilder shipBuilder = new ShipBuilder();
            boolean shipPlaced = false;

            while (!shipPlaced) {
                try {
                    String line = scanner.nextLine();
                    System.out.println();
                    Ship auxShip = shipBuilder.buildShip(line, ship);

                    if (isShipTooClose(auxShip)) {
                        throw new IllegalArgumentException("Error! You placed it too close to another one.");
                    }

                    fleet.placeShip(auxShip, auxShip.getPositions());
                    updateTableBuiltShip(auxShip);
                    shipPlaced = true;
                } catch (Exception e) {
                    System.out.println(e.getMessage() + " Try again: \n ");
                }
            }
            table.displayTable();
        }
        scanner.close();
    }

    public void updateTableBuiltShip(Ship ship) {
        for (Point point : ship.getPositions()) {
            table.updateTable(point.getX(), point.getY(), CellState.SHIP);
        }
    }

    public boolean isShipTooClose(Ship newShip) {
        for (Point point : newShip.getPositions()) {
            if (isAdjacentCellOccupied(point)) {
                return true;
            }
        }
        return false;
    }

    private boolean isAdjacentCellOccupied(Point point) {
        int[][] directions = {
                {-1, -1}, {-1, 0}, {-1, 1},
                {0, -1}, {0, 0}, {0, 1},
                {1, -1}, {1, 0}, {1, 1}
        };

        for (int[] dir : directions) {
            int newRow = point.getX() + dir[0];
            int newCol = point.getY() + dir[1];

            if (newRow >= 0 && newRow < table.ROWS && newCol >= 0 && newCol < table.COLUMNS) {
                if (table.getSquare(newRow, newCol) == CellState.SHIP) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Adding shooting functionality
     */

    private void takeAShot() {
        Scanner scanner = new Scanner(System.in);

        String line = scanner.next();
        Point shootCoord = coordinateParser.parse(line);

        scanner.close();
    }

    private void getShootState(Point p){
        if (isHit(p)){
            System.out.println("You hit a ship!");
            updateTableHit(p);
        }else {
            System.out.println("You missed!");
            updateTableMiss(p);
        }
    }

    private boolean isHit(Point p) {
        return this.table.getSquare(p.getX(), p.getY()).equals(CellState.HIT);
    }

    private void updateTableHit(Point p) {
        this.table.updateTable(p.getX(), p.getY(), CellState.HIT);
    }

    private void updateTableMiss(Point p) {
        this.table.updateTable(p.getX(), p.getY(), CellState.MISS);
    }

}

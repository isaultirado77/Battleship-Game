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
    private Scanner scanner;

    public GameController() {
        table = new BattleshipTable();
        fleet = new FleetManager();
        coordinateParser = new CoordinateParser();
        scanner = new Scanner(System.in);
    }

    public void makeFleet() {
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

    public void takeAShot() {

        boolean isValidShoot = false;

        while (!isValidShoot) {
            try{
                String line = scanner.nextLine();
                Point shootCoord = coordinateParser.parse(line);
                getShootState(shootCoord);
                this.table.displayTable();
                isValidShoot = true;
            }catch (Exception e){
                System.out.println("\n" + e.getMessage());
            }
        }
    }

    private void getShootState(Point p) {
        if (isHit(p)) {
            System.out.println("\n You hit a ship! \n");
            updateTableHit(p);
        } else {
            System.out.println("\n You missed! \n");
            updateTableMiss(p);
        }
    }

    private boolean isHit(Point p) {
        return this.table.getSquare(p.getX(), p.getY()).equals(CellState.SHIP);
    }

    private void updateTableHit(Point p) {
        this.table.updateTable(p.getX(), p.getY(), CellState.HIT);
    }

    private void updateTableMiss(Point p) {
        this.table.updateTable(p.getX(), p.getY(), CellState.MISS);
    }

    public BattleshipTable getTable() {
        return this.table;
    }

    public FleetManager getFleet() {
        return fleet;
    }
}

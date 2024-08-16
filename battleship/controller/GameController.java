package battleship.controller;

import java.util.Scanner;

import battleship.model.table.BattleshipTable;
import battleship.model.table.CellState;
import battleship.model.table.Point;
import battleship.model.ship.Ship;

public class GameController {

    private BattleshipTable table;
    private BattleshipTable gameTable;
    private FleetManager fleet;
    private CoordinateParser coordinateParser;
    private Scanner scanner;

    public GameController() {
        table = new BattleshipTable();
        gameTable = new BattleshipTable();
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

    public void start() {
        System.out.println("\nThe game starts!\n");
        gameTable.displayTable();
        System.out.println("\nTake a shot!\n");

        while (true) {
            takeAShot();

            if (isFleetSunk()) {
                System.out.println("\nYou sank the last ship. You won. Congratulations!");
                break;
            }
        }
    }

    public void takeAShot() {
        boolean isValidShoot = false;

        while (!isValidShoot) {
            try {
                String line = scanner.nextLine();
                Point shootCoord = coordinateParser.parse(line);
                getShootState(shootCoord);
                isValidShoot = true;

                if (isShipSunk(shootCoord)) {
                    System.out.println("\nYou sank a ship! Specify a new target:\n");
                    handleShipSinking(shootCoord);
                } else if (isHit(shootCoord)) {
                    System.out.println("\nYou hit a ship! Try again:\n");
                } else {
                    System.out.println("\nYou missed. Try again:\n");
                }

                gameTable.displayTable();
                System.out.println();
            } catch (Exception e) {
                System.out.println("\n" + e.getMessage() + "\n");
            }
        }
    }

    private void getShootState(Point p) {
        if (isHit(p)) {
            updateTableHit(p);
        } else {
            updateTableMiss(p);
        }
    }

    private boolean isHit(Point p) {
        return table.getSquare(p.getX(), p.getY()).equals(CellState.SHIP);
    }

    private void updateTableHit(Point p) {
        gameTable.updateTable(p.getX(), p.getY(), CellState.HIT);
    }

    private void updateTableMiss(Point p) {
        gameTable.updateTable(p.getX(), p.getY(), CellState.MISS);
    }

    private boolean isShipSunk(Point p) {
        for (Ship ship : fleet.getFleet()) {
            if (ship.getPositions().contains(p)) {
                boolean isSunk = isCurrentShipSunk(ship, gameTable);
                if (isSunk) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isCurrentShipSunk(Ship ship, BattleshipTable table) {
        for (Point pos : ship.getPositions()) {
            if (table.getSquare(pos.getX(), pos.getY()) != CellState.HIT) {
                return false;
            }
        }
        return true;
    }

    private void handleShipSinking(Point p) {
        for (Ship ship : fleet.getFleet()) {
            if (ship.getPositions().contains(p)) {
                fleet.getFleet().remove(ship);
                return;
            }
        }
    }

    private boolean isFleetSunk() {
        return fleet.getFleet().isEmpty();
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
}

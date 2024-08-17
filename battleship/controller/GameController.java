package battleship.controller;

import java.util.Scanner;

import battleship.model.table.BattleshipTable;
import battleship.model.table.CellState;
import battleship.model.table.Point;
import battleship.model.ship.Ship;

public class GameController {

    private BattleshipTable player1Table;
    private BattleshipTable player2Table;
    private FleetManager player1Fleet;
    private FleetManager player2Fleet;
    private BattleshipTable player1GameTable;
    private BattleshipTable player2GameTable;
    private CoordinateParser coordinateParser;
    private Scanner scanner;
    private boolean isPlayer1Turn;

    public GameController() {
        player1Table = new BattleshipTable();
        player2Table = new BattleshipTable();
        player1Fleet = new FleetManager();
        player2Fleet = new FleetManager();
        player1GameTable = new BattleshipTable();
        player2GameTable = new BattleshipTable();
        coordinateParser = new CoordinateParser();
        scanner = new Scanner(System.in);
        isPlayer1Turn = true;
    }

    public void start() {
        System.out.println("Player 1, place your ships on the game field\n");
        makeFleet(player1Table, player1Fleet);
        promptNextPlayer();
        System.out.println("Player 2, place your ships on the game field\n");
        makeFleet(player2Table, player2Fleet);
        promptNextPlayer();

        while (true) {
            printTable();
            System.out.println("\n" + (isPlayer1Turn ? "Player 1" : "Player 2") + ", it's your turn!\n");
            takeAShot();

            if (isFleetSunk(isPlayer1Turn ? player2Fleet : player1Fleet)) {
                System.out.println("\nYou sank the last ship. " + (isPlayer1Turn ? "Player 1" : "Player 2") + " won. Congratulations!");
                break;
            }

            promptNextPlayer();
            isPlayer1Turn = !isPlayer1Turn;
        }
    }

    private void promptNextPlayer() {
        System.out.println("\nPress Enter and pass the move to another player\n...");
        scanner.nextLine();
    }

    private void printTable() {
        if (isPlayer1Turn) {
            player1GameTable.displayTable();
            System.out.println("---------------------");
            player1Table.displayTable();
        } else {
            player2GameTable.displayTable();
            System.out.println("---------------------");
            player2Table.displayTable();
        }
    }

    public void makeFleet(BattleshipTable table, FleetManager fleet) {
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

                    if (isShipTooClose(auxShip, table)) {
                        throw new IllegalArgumentException("Error! You placed it too close to another one.");
                    }

                    fleet.placeShip(auxShip, auxShip.getPositions());
                    updateTableBuiltShip(auxShip, table);
                    shipPlaced = true;
                } catch (Exception e) {
                    System.out.println(e.getMessage() + " Try again: \n ");
                }
            }
            table.displayTable();
        }
    }

    public void takeAShot() {
        boolean isValidShoot = false;

        BattleshipTable currentPlayerTable = isPlayer1Turn ? player1GameTable : player2GameTable;
        BattleshipTable opponentTable = isPlayer1Turn ? player2Table : player1Table;
        FleetManager opponentFleet = isPlayer1Turn ? player2Fleet : player1Fleet;

        while (!isValidShoot) {
            try {
                String line = scanner.nextLine();
                Point shootCoord = coordinateParser.parse(line);
                getShootState(shootCoord, opponentTable, currentPlayerTable);
                isValidShoot = true;

                if (isShipSunk(shootCoord, opponentFleet, currentPlayerTable)) {
                    System.out.println("\nYou sank a ship! Specify a new target!");
                    handleShipSinking(shootCoord, opponentFleet);
                } else if (isHit(shootCoord, opponentTable) || opponentTable.getSquare(shootCoord.getX(), shootCoord.getY()).equals(CellState.HIT)) {
                    System.out.println("\nYou hit a ship!");
                } else {
                    System.out.println("\nYou missed!");
                }

            } catch (Exception e) {
                System.out.println("\n" + e.getMessage() + "\n");
            }
        }
    }

    private void getShootState(Point p, BattleshipTable opponentTable, BattleshipTable currentPlayerTable) {
        if (isHit(p, opponentTable)) {
            updateTableHit(p, currentPlayerTable);
            updateTableHit(p, opponentTable);
        } else {
            updateTableMiss(p, currentPlayerTable);
            updateTableMiss(p, opponentTable);
        }
    }

    private boolean isHit(Point p, BattleshipTable opponentTable) {
        return opponentTable.getSquare(p.getX(), p.getY()).equals(CellState.SHIP);
    }

    private void updateTableHit(Point p, BattleshipTable currentPlayerTable) {
        currentPlayerTable.updateTable(p.getX(), p.getY(), CellState.HIT);
    }

    private void updateTableMiss(Point p, BattleshipTable currentPlayerTable) {
        currentPlayerTable.updateTable(p.getX(), p.getY(), CellState.MISS);
    }

    private boolean isShipSunk(Point p, FleetManager opponentFleet, BattleshipTable currentPlayerTable) {
        for (Ship ship : opponentFleet.getFleet()) {
            if (ship.getPositions().contains(p)) {
                boolean isSunk = isCurrentShipSunk(ship, currentPlayerTable);
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

    private void handleShipSinking(Point p, FleetManager opponentFleet) {
        for (Ship ship : opponentFleet.getFleet()) {
            if (ship.getPositions().contains(p)) {
                opponentFleet.getFleet().remove(ship);
                return;
            }
        }
    }

    private boolean isFleetSunk(FleetManager fleet) {
        return fleet.getFleet().isEmpty();
    }

    public void updateTableBuiltShip(Ship ship, BattleshipTable table) {
        for (Point point : ship.getPositions()) {
            table.updateTable(point.getX(), point.getY(), CellState.SHIP);
        }
    }

    public boolean isShipTooClose(Ship newShip, BattleshipTable table) {
        for (Point point : newShip.getPositions()) {
            if (isAdjacentCellOccupied(point, table)) {
                return true;
            }
        }
        return false;
    }

    private boolean isAdjacentCellOccupied(Point point, BattleshipTable table) {
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
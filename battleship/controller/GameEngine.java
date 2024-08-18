package battleship.controller;

import battleship.model.ship.Ship;
import battleship.model.table.Point;
import battleship.model.table.BattleshipTable;
import battleship.model.table.CellState;
import battleship.view.UIHandler;

import java.util.List;
import java.util.Scanner;

public class GameEngine {

    private BattleshipTable player1Table;
    private BattleshipTable player2Table;
    private BattleshipTable player1FogTable;
    private BattleshipTable player2FogTable;
    private FleetManager player1Fleet;
    private FleetManager player2Fleet;
    private UIHandler uiHandler;
    private CoordinateParser coordinateParser;
    private boolean isPlayer1Turn;
    private final Scanner scanner;

    public GameEngine() {
        player1Table = new BattleshipTable();
        player2Table = new BattleshipTable();
        player1FogTable = new BattleshipTable();
        player2FogTable = new BattleshipTable();
        player1Fleet = new FleetManager();
        player2Fleet = new FleetManager();
        scanner = new Scanner(System.in);
        uiHandler = new UIHandler(scanner);
        coordinateParser = new CoordinateParser();
        isPlayer1Turn = true;
    }

    public void setupFleetForPlayer(int playerNumber) {
        BattleshipTable table = getPlayerTable(playerNumber);
        FleetManager fleet = getPlayerFleet(playerNumber);

        table.displayTable();
        for (Ship ship : fleet.getFleet()) {
            boolean shipPlaced = false;
            System.out.println("\nEnter the coordinates of the " + ship.shipInfo() + ":\n");
            while (!shipPlaced) {
                try {
                    String line = uiHandler.getInputCoordinates(ship);
                    ShipBuilder shipBuilder = new ShipBuilder();
                    Ship auxShip = shipBuilder.buildShip(line, ship);

                    if (isShipTooClose(auxShip, table)) {
                        throw new IllegalArgumentException("\nError! You placed it too close to another one.");
                    }
                    fleet.placeShip(auxShip, auxShip.getPositions());
                    updateTableBuiltShip(auxShip, table);
                    shipPlaced = true;
                } catch (Exception e) {
                    uiHandler.displayError(e.getMessage());
                }
            }
            System.out.println();
            table.displayTable();
        }
    }

    public void takeAShot() {
        boolean isValidShoot = false;
        BattleshipTable currentPlayerTable = getCurrentPlayerTable();
        BattleshipTable currentPlayerFogTable = getCurrentPlayerFogTable();
        BattleshipTable opponentTable = getOpponentTable();
        FleetManager opponentFleet = getOpponentFleet();

        while (!isValidShoot) {
            try {
                String line = scanner.nextLine();
                Point shootCoord = coordinateParser.parse(line);

                boolean hit = isHit(shootCoord, opponentTable);

                getShootState(shootCoord, opponentTable, currentPlayerTable, currentPlayerFogTable);

                if (isGameOver()){
                    return;
                }

                if (hit) {
                    if (isShipSunk(shootCoord, opponentFleet, opponentTable)) {
                        uiHandler.displayShipSunk();
                        handleShipSinking(shootCoord, opponentFleet);
                    } else {
                        uiHandler.displayHit();
                    }
                } else {
                    uiHandler.displayMiss();
                }



                isValidShoot = true;
            } catch (Exception e) {
                uiHandler.displayError(e.getMessage());
            }
        }
    }


    public boolean isGameOver() {
        return player1Fleet.getFleet().isEmpty() || player2Fleet.getFleet().isEmpty();
    }

    public void switchTurns() {
        isPlayer1Turn = !isPlayer1Turn;
    }

    public int getCurrentPlayer() {
        return isPlayer1Turn ? 1 : 2;
    }

    public int getOpponent() {
        return isPlayer1Turn ? 2 : 1;
    }

    private BattleshipTable getPlayerTable(int playerNumber) {
        return playerNumber == 1 ? player1Table : player2Table;
    }

    private FleetManager getPlayerFleet(int playerNumber) {
        return playerNumber == 1 ? player1Fleet : player2Fleet;
    }

    public BattleshipTable getCurrentPlayerTable() {
        return isPlayer1Turn ? player1Table : player2Table;
    }

    public BattleshipTable getCurrentPlayerFogTable() {
        return isPlayer1Turn ? player1FogTable : player2FogTable;
    }

    private BattleshipTable getOpponentTable() {
        return isPlayer1Turn ? player2Table : player1Table;
    }

    private FleetManager getCurrentPlayerFleet() {
        return isPlayer1Turn ? player1Fleet : player2Fleet;
    }

    private FleetManager getOpponentFleet() {
        return isPlayer1Turn ? player2Fleet : player1Fleet;
    }

    private void updateTableBuiltShip(Ship ship, BattleshipTable table) {
        List<Point> positions = ship.getPositions();
        for (Point position : positions) {
            table.updateTable(position.getX(), position.getY(), CellState.SHIP);
        }
    }

    private void getShootState(Point shootCoord, BattleshipTable opponentTable, BattleshipTable currentPlayerTable, BattleshipTable currentPlayerFogTable) {
        CellState currentState = opponentTable.getSquare(shootCoord.getX(), shootCoord.getY());

        if (currentState == CellState.HIT) {
            return;
        }

        if (isHit(shootCoord, opponentTable)) {
            currentPlayerFogTable.updateTable(shootCoord.getX(), shootCoord.getY(), CellState.HIT);
            opponentTable.updateTable(shootCoord.getX(), shootCoord.getY(), CellState.HIT);
        } else {
            currentPlayerFogTable.updateTable(shootCoord.getX(), shootCoord.getY(), CellState.MISS);
            opponentTable.updateTable(shootCoord.getX(), shootCoord.getY(), CellState.MISS);
        }
    }



    private boolean isShipTooClose(Ship ship, BattleshipTable table) {
        for (Point position : ship.getPositions()) {
            if (table.isAdjacentCellOccupied(position)) {
                return true;
            }
        }
        return false;
    }

    private boolean isShipSunk(Point shootCoord, FleetManager opponentFleet, BattleshipTable opponentTable) {
        for (Ship ship : opponentFleet.getFleet()) {
            if (ship.getPositions().contains(shootCoord)) {
                boolean isSunk = isCurrentShipSunk(ship, opponentTable);
                if (isSunk) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isCurrentShipSunk(Ship ship, BattleshipTable table) {
        int countHit = 0;
        for (Point pos : ship.getPositions()) {
            if (table.getSquare(pos.getX(), pos.getY()) == CellState.HIT) {
                countHit++;
            }
        }
        return countHit == ship.getLength();
    }

    private void handleShipSinking(Point p, FleetManager opponentFleet) {
        for (Ship ship : opponentFleet.getFleet()) {
            if (ship.getPositions().contains(p)) {
                opponentFleet.getFleet().remove(ship);
                return;
            }
        }
    }

    private boolean isHit(Point shootCoord, BattleshipTable opponentTable) {
        return opponentTable.getSquare(shootCoord.getX(), shootCoord.getY()) == CellState.SHIP;
    }

    public boolean isFleetSunk(int opponent) {
        return getPlayerFleet(opponent).getFleet().isEmpty();
    }
}

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
    private FleetManager player1Fleet;
    private FleetManager player2Fleet;
    private UIHandler uiHandler;
    private boolean isPlayer1Turn;
    private final Scanner scanner;

    public GameEngine() {
        player1Table = new BattleshipTable();
        player2Table = new BattleshipTable();
        player1Fleet = new FleetManager();
        player2Fleet = new FleetManager();
        scanner = new Scanner(System.in);
        uiHandler = new UIHandler(scanner);
        isPlayer1Turn = true;
    }

    public void setupFleetForPlayer(int playerNumber) {
        BattleshipTable table = getPlayerTable(playerNumber);
        FleetManager fleet = getPlayerFleet(playerNumber);

        table.displayTable();
        for (Ship ship : fleet.getFleet()) {
            boolean shipPlaced = false;
            while (!shipPlaced) {
                try {
                    String line = uiHandler.getInputCoordinates(ship);
                    ShipBuilder shipBuilder = new ShipBuilder();
                    Ship auxShip = shipBuilder.buildShip(line, ship);

                    if (isShipTooClose(auxShip, table)) {
                        throw new IllegalArgumentException("Error! You placed it too close to another one.");
                    }

                    fleet.placeShip(auxShip, auxShip.getPositions());
                    updateTableBuiltShip(auxShip, table);
                    shipPlaced = true;
                } catch (Exception e) {
                    uiHandler.displayError(e.getMessage());
                }
            }
            table.displayTable();
        }
    }

    public void takeAShot(Point shootCoord) {
        BattleshipTable currentPlayerTable = getCurrentPlayerTable();
        BattleshipTable opponentTable = getOpponentTable();

        getShootState(shootCoord, opponentTable, currentPlayerTable);

        if (isShipSunk(shootCoord, getOpponentFleet(), currentPlayerTable)) {
            uiHandler.displayShipSunk();
            handleShipSinking(shootCoord, getOpponentFleet());
        } else if (isHit(shootCoord, opponentTable)) {
            uiHandler.displayHit();
        } else {
            uiHandler.displayMiss();
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

    private BattleshipTable getCurrentPlayerTable() {
        return isPlayer1Turn ? player1Table : player2Table;
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

    private void getShootState(Point shootCoord, BattleshipTable opponentTable, BattleshipTable currentPlayerTable) {
        if (opponentTable.getSquare(shootCoord.getY(), shootCoord.getY()) == CellState.SHIP) {
            opponentTable.updateTable(shootCoord.getX(), shootCoord.getY(), CellState.HIT);
            currentPlayerTable.updateTable(shootCoord.getX(), shootCoord.getY(), CellState.HIT);
        } else {
            opponentTable.updateTable(shootCoord.getX(), shootCoord.getY(), CellState.MISS);
            currentPlayerTable.updateTable(shootCoord.getX(), shootCoord.getY(), CellState.MISS);
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

    private boolean isShipSunk(Point shootCoord, FleetManager opponentFleet, BattleshipTable currentPlayerTable) {
        Ship hitShip = opponentFleet.getShipAtPosition(shootCoord);
        return hitShip != null && hitShip.isSunk();
    }

    private void handleShipSinking(Point shootCoord, FleetManager opponentFleet) {
        Ship sunkShip = opponentFleet.getShipAtPosition(shootCoord);
        opponentFleet.removeShip(sunkShip);
    }

    private boolean isHit(Point shootCoord, BattleshipTable opponentTable) {
        return opponentTable.getSquare(shootCoord.getX(), shootCoord.getY()) == CellState.HIT;
    }
}

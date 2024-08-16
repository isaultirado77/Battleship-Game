package battleship.controller;


import java.util.List;
import java.util.ArrayList;
import battleship.model.ship.*;
import battleship.model.table.Point;

public class FleetManager {
    private List<Ship> fleet;

    public FleetManager() {
        fleet = new ArrayList<>();
        fleet.add(new AircraftCarrier());
        fleet.add(new Battleship());
        fleet.add(new Submarine());
        fleet.add(new Cruiser());
        fleet.add(new Destroyer());
    }

    public List<Ship> getFleet() {
        return fleet;
    }

    public void placeShip(Ship ship, List<Point> positions) {
        ship.setPositions(positions);
    }
    
}


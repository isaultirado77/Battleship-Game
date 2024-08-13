package battleship.controller;

import battleship.model.ship.Ship;
import battleship.model.table.Point;
import java.util.ArrayList;

public class ShipBuilder {

    private final CoordinateParser coordinateParser;

    public ShipBuilder() {
        this.coordinateParser = new CoordinateParser();
    }

    public Ship buildShip(String line, Ship ship) {
        String[] lineArray = prepareCoords(line);

        Point start = coordinateParser.parse(lineArray[0]);
        Point end = coordinateParser.parse(lineArray[1]);

        if (isValidToBuild(start, end, ship)) {
            ArrayList<Point> positions = buildPositions(start, end);
            ship.setPositions(positions);
            return ship;
        } else {
            throw new IllegalArgumentException("Error! Cannot build the ship with the provided coordinates.");
        }
    }

    private String[] prepareCoords(String line) {
        if (!line.contains(" ")) {
            throw new IllegalArgumentException("Error! Invalid line of coordinates.");
        }
        return line.split(" ");
    }

    private boolean isValidToBuild(Point start, Point end, Ship ship) {
        boolean validPlace = (start.getX() == end.getX()) || (start.getY() == end.getY());
        boolean isValidLength = (Point.distanceOfTwoPoints(start, end) + 1) == ship.getLength();

        if (!validPlace) {
            throw new IllegalArgumentException("Error! Ships must be placed either horizontally or vertically.");
        }

        if (!isValidLength) {
            throw new IllegalArgumentException("Error! Invalid ship length -> " + ship.shipInfo() + ".");
        }

        return validPlace && isValidLength;
    }

    private ArrayList<Point> buildPositions(Point start, Point end) {
        ArrayList<Point> positions = new ArrayList<>();

        if (isVertical(start, end)) {
            buildVerticalShip(start, end, positions);
        } else {
            buildHorizontalShip(start, end, positions);
        }

        return positions;
    }

    private boolean isVertical(Point start, Point end) {
        return start.getX() == end.getX();
    }

    private void buildVerticalShip(Point start, Point end, ArrayList<Point> positions) {
        if (start.getY() < end.getY()) {
            for (int i = start.getY(); i <= end.getY(); i++) {
                positions.add(new Point(start.getX(), i));
            }
        } else {
            for (int i = start.getY(); i >= end.getY(); i--) {
                positions.add(new Point(start.getX(), i));
            }
        }
    }

    private void buildHorizontalShip(Point start, Point end, ArrayList<Point> positions) {
        if (start.getX() < end.getX()) {
            for (int i = start.getX(); i <= end.getX(); i++) {
                positions.add(new Point(i, start.getY()));
            }
        } else {
            for (int i = start.getX(); i >= end.getX(); i--) {
                positions.add(new Point(i, start.getY()));
            }
        }
    }
}

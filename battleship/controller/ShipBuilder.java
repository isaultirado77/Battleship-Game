package battleship.controller;

import battleship.model.ship.Ship;
import battleship.model.table.Point;
import java.util.ArrayList;

public class ShipBuilder {

    private final String rowIndex = "ABCDEFGHIJ";

    public Ship buildShip(String line, Ship ship) {
        String[] lineArray = prepareCoords(line);

        Point start = getPointCoord(lineArray[0]);
        Point end = getPointCoord(lineArray[1]);

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

    public Point getPointCoord(String coord) {
        int coordX = getIntRow(String.valueOf(coord.charAt(0)));
        int coordY = getIntCol(coord.substring(1));

        return new Point(coordX, coordY);
    }

    private int getIntRow(String row) {
        if (!isValidRow(row)) {
            throw new IllegalArgumentException("Error! Invalid row.");
        }
        return rowIndex.indexOf(row);
    }

    private boolean isValidRow(String row) {
        return rowIndex.contains(row);
    }

    private int getIntCol(String col) {
        try {
            if (!isValidCol(col)) {
                throw new IllegalArgumentException("Error! Invalid column.");
            }
            return Integer.parseInt(col) - 1;
        } catch (NumberFormatException e) {
            throw new NumberFormatException("Error! Invalid column format.");
        }
    }

    private boolean isValidCol(String col) {
        int colInt = Integer.parseInt(col);
        return colInt >= 1 && colInt <= 10;
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

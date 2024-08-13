package battleship.controller;

import battleship.model.table.Point;

public class CoordinateParser {

    private final String rowIndex = "ABCDEFGHIJ";

    public Point parse(String coord) {
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
}


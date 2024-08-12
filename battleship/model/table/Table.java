package battleship.model.table;

public interface Table {

    void initializeTable();

    void updateTable(int row, int column, CellState state);

    void displayTable();

    CellState getSquare(int row, int column);

    void resetTable();

}

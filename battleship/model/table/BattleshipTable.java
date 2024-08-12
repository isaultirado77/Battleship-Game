package battleship.model.table;

public class BattleshipTable implements Table {

    public final int ROWS = 10;
    public final int COLUMNS = 10;
    private CellState[][] table;

    public BattleshipTable() {
        table = new CellState[ROWS][COLUMNS];
        initializeTable();
    }

    @Override
    public void initializeTable() {
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLUMNS; j++) {
                this.table[i][j] = CellState.FOG;
            }
        }
    }

    @Override
    public void displayTable() {
        System.out.print("  ");
        for (int i = 1; i <= 10; i++) {
            System.out.print(i + " ");
        }

        System.out.println();

        for (int i = 0; i < ROWS; i++) {
            System.out.print((char) ('A' + i) + " ");
            for (int j = 0; j < COLUMNS; j++) {
                if (this.table[i][j] != null) {
                    System.out.print(this.table[i][j].getSymbol() + " ");
                } else {
                    System.out.print("  ");
                }
            }
            System.out.println();
        }
    }

    @Override
    public void updateTable(int row, int column, CellState state) {
        if (row >= 0 && row < ROWS && column >= 0 && column < COLUMNS) {
            this.table[row][column] = state;
        } else {
            throw new IllegalArgumentException("Error! Row or Column out of boundaries.");
        }
    }

    @Override
    public CellState getSquare(int row, int column) {
        if (row >= 0 && row < ROWS && column >= 0 && column < COLUMNS) {
            return this.table[row][column];
        } else {
            throw new IllegalArgumentException("Error! Row or Column out of boundries.");
        }
    }

    @Override
    public void resetTable() {
        initializeTable();
    }
}

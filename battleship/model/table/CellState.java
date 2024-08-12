package battleship.model.table;

public enum CellState {

    FOG("~"),
    SHIP("O"),
    HIT("X"),
    MISS("M");

    private final String symbol;

    CellState (String symbol){
        this.symbol = symbol;
    }

    public String getSymbol(){
        return this.symbol;
    }

    public static String fromString(String str){
        for (CellState state : CellState.values()){
            if(state.symbol.equalsIgnoreCase(str)){
                return state.getSymbol();
            }
        }
        throw new IllegalArgumentException("Error! No matching CellState for symbol. ");
    }
}

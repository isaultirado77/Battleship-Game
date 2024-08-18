package battleship.model.ship;

import battleship.model.table.Point;

import java.util.ArrayList;
import java.util.List;

public abstract class Ship {

    private int length;
    private String name;
    private List<Point> positions;

    public Ship(int length, String name){
        this.length = length;
        this.name = name;
        this.positions = new ArrayList<>();
    }

    public int getLength() {
        return this.length;
    }

    public String getName(){
        return this.name;
    }

    public List<Point> getPositions(){
        return this.positions;
    }

    public void setPositions(List<Point> positions){
        if(positions.size() == this.length){
            this.positions = positions;
        }else{
            throw new IllegalArgumentException("Error! Invalid number of positions for the ship length.");
        }
    }

    public String shipInfo(){
        return this.name + "(" + this.length + " cells)";
    }

    public boolean isSunk(){
        return this.positions.isEmpty();
    }
}
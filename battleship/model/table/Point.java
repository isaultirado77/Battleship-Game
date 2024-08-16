package battleship.model.table;

public class Point {

    private int x;
    private int y;

    public Point(int x, int y){
        this.x = x;
        this.y = y;
    }

    public int getX(){
        return x;
    }

    public int getY(){
        return y;
    }

    public static int distanceOfTwoPoints(Point u, Point v) {
        return Math.abs(u.getX() - v.getX()) + Math.abs(u.getY() - v.getY());
    }
}

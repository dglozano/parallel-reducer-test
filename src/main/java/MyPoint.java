public class MyPoint implements Point {

    private double x;
    private double y;

    MyPoint(double x, double y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public double getX() {
        return x;
    }

    @Override
    public double getY() {
        return y;
    }

    @Override
    public String toString() {
        return "("+x+";"+y+")";
    }
}

package productdata;
import exceptions.InvalidYCoordinate;

import java.io.Serializable;

/**
 * Class that represents a coordinates of a Product
 */
public class Coordinates implements Serializable {
    private static final long serialVersionUID = 1337000018L;
    private double x;
    private Integer y; //Значение поля должно быть больше -150, Поле не может быть null

    /**
     * Standard constructor
     * @param x x coordinate
     * @param y y coordinate
     * @throws NullPointerException If you are trying to create a Coordinate with a null Y argument
     * @throws InvalidYCoordinate If you are trying to create a Coordinate with an Y argument less than -150
     */
    public Coordinates(double x, Integer y) throws NullPointerException, InvalidYCoordinate {
        if (y == null) throw new NullPointerException();
        if (y < -150 ) throw new InvalidYCoordinate();
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString(){
        return (" x = " + x + " y = " + y);
    }

    /**
     * Returns a fields of object in format for saving
     * @return String in in format for saving
     */
    public String output(){
        return x + ";" + y;
    }

    /**
     *  Getter x
     * @return x coordinate
     */
    public double getX() {
        return x;
    }

    /**
     * Getter y
     * @return y coordinate
     */
    public Integer getY() {
        return y;
    }


}
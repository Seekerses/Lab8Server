package productdata;

import java.io.Serializable;

/**
 * Class that represents a Location of organization
 */
public class Location implements Serializable {

    private static final long serialVersionUID = 1337000019L;
    private Long x; //Поле не может быть null
    private int y;
    private Long z; //Поле не может быть null

    /**
     * Standard constructor
     * @param x x coordinate
     * @param y y coordinate
     * @param z z coordinate
     * @throws NullPointerException If you are trying to create a Location with an null x or z arguments
     */
    public Location(Long x, int y, Long z) throws NullPointerException{
        if ( x == null || z == null) throw new NullPointerException();
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public String toString(){
        return ("x = " + x + " y = " + y + " z = " + z);
    }

    /**
     * Returns a fields value in format for saving
     * @return String in format for saving
     */
    String output(){
        return x + ";" + y + ";" + z;
    }

    @Override
    public boolean equals(Object obj){
        if(obj instanceof Location){
            return (((Location) obj).x.equals(x)  && ((Location) obj).y == y && ((Location) obj).z.equals(z));
        }
        else return false;
    }

}
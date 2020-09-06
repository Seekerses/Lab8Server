package Exceptions;

public class InvalidYCoordinate extends Exception {
    /**
     * Exception of invalid coordinate y
     */
    public InvalidYCoordinate(){
        super("Invalid Y coordinate. It cannot be less tha -150");
    }
}

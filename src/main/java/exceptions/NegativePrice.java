package exceptions;

public class NegativePrice extends Exception {
    /**
     * Exception of invalid price of Product
     */
    public NegativePrice(){
        super("Price cannot be negative number");
    }
}

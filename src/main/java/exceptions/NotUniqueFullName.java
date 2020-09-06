package exceptions;

public class NotUniqueFullName extends Exception {
    /**
     * Exception of not unique full name of Organization
     */
    public NotUniqueFullName(){
        super("Not Unique full name");
    }
}

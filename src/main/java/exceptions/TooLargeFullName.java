package exceptions;

public class TooLargeFullName extends Exception {
    /**
     * Exception of Invalid full name of Organization
     */
    public TooLargeFullName() {
        super("Too large full name. It must be shorter tan 1404 symbols");
    }
}

package consolehandler;

import java.io.IOException;

/**
 * Interface for all interpreters
 */
public interface Interpreter {
    /**
     * This method takes the command from Controller and interpret it according to the current command list
     * @param args Command
     * @throws IOException If an I/O error occurs
     */
    void handle(String[] args) throws IOException;

}
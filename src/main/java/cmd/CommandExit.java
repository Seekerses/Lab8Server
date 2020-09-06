package cmd;

import Control.TableController;
import ServSoft.ServerController;

import java.io.File;
import java.io.IOException;

/**
 * break the programm
 *
 *
 */

public class CommandExit implements Command{

    private static final long serialVersionUID = 1337000005L;

    @Override
    public String execute(String[] args) throws IOException {
        try {
            if (args.length == 1) {
                System.out.println("There is no args for this command!");
            }
        }catch (NullPointerException e) {
            ServerController.getChannel().close();
            TableController.getCurrentTable().save(new File("saved.csv"));
            System.out.println("Program completion...");
            System.exit(0);

        }
        return null;
    }

    /**
     * get name of command
     *
     * @return String
     */

    @Override
    public String toString() {
        return "exit";
    }
}

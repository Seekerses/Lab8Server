package cmd;

import consolehandler.ScriptParser;

import java.io.IOException;
import java.util.ArrayList;

/**
 * execute command from script
 *
 *
 */

public class CommandExecute_Script implements Command{

    private static final long serialVersionUID = 1337000004L;

    ArrayList<String[]> commands;

    @Override
    public String execute(String[] args) throws IOException {
        if (commands == null){
            ScriptParser.parseScript(args[0]);
            execute(args);
            return null;
        }
        else {
            ScriptParser.executeQuery(commands);
            return ("Script was successfully executed");
        }
    }

    /**
     * get name of command
     *
     * @return String
     */

    @Override
    public String toString() {
        return "execute_script";
    }

}

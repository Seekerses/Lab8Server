package cmd;

import consolehandler.ScriptParser;
import java.util.ArrayList;

/**
 * execute command from script
 *
 *
 */

public class CommandExecute_Script implements Command{

    private static final long serialVersionUID = 1337000004L;

    private ArrayList<String[]> commands;

    @Override
    public String execute(String[] args) {
        if (commands == null){
            commands = ScriptParser.parseScript(args[0]);
            return null;
        }
        else {
            return ScriptParser.executeQuery(commands);
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

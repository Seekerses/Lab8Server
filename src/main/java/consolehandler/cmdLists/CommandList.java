package consolehandler.cmdLists;

import cmd.Command;
import java.util.Map;

/**
 * Interface which implements all Command Lists
 */
public interface CommandList {
    /**
     * Returns command from List
     * @return Command
     */
    Map<String,Command> getCommands();

    /**
     * Inserts command to List
     * @param name Command name
     * @param command Command
     */
    static void addCommand(String name, Command command){}
}

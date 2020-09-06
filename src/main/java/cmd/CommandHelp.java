package cmd;

import java.util.Arrays;

/**
 * Get info about all commands
 *
 *
 */

public class CommandHelp implements Command {

    private static final long serialVersionUID = 1337000008L;

    @Override
    public String execute(String[] args){
        try {
            if (args.length == 1) {
                return ("There is no args for this command!");
            }
        }catch (NullPointerException e){
            return ("help: display help for available commands \n " +
                    "info: display information about the collection (type, initialization date, number of elements, etc.) in the standard output stream \n" +
                    "show: output to the standard output stream all the elements of the collection in the string representation \n" +
                    "insert key {element}: add a new element with the given key \n" +
                    "update id {element}: update the value of a collection element whose id is equal to the given \n" +
                    "remove_key key: remove an item from the collection by its key \n" +
                    "clear: clear collection \n" +
                    "save: save collection to file \n" +
                    "change_port: change current client port \n" +
                    "connect IP port: connect to new socket \n" +
                    "save: save collection to file \n" +
                    "execute_script file_name: read and execute the script from the specified file. The script contains the commands in the same form in which the user types them interactively. \n" +
                    "exit: exit the program (without saving to a file) \n" +
                    "remove_lower {element}: remove from the collection all elements which ID is smaller than the specified \n" +
                    "history: print the last 7 commands (without their arguments) \n" +
                    "replace_if_greater key {element}: replace the value by key, if the price of new value is greater than the old \n" +
                    "min_by_name: output any object from the collection whose name field value is minimal \n" +
                    "group_counting_by_coordinates: group the elements of the collection by the value of the coordinates field, display the number of elements in each group \n" +
                    "filter_less_than_manufacturer manufacturer: display elements whose manufacturer Name field value is less than the specified");
        }
        return null;
    }

    /**
     * get name of command
     *
     * @return String
     */

    public String toString(){
        return "help";
    }
}
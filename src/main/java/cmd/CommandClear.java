package cmd;
import consolehandler.TableController;

/**
 * remove all elements from the collection
 *
 *
 */

public class CommandClear implements Command {

    private static final long serialVersionUID = 1337000002L;

    @Override
    public String execute(String[] args) {
        try {
            if (args.length == 1) {
                return ("There is no args for this command!");
            }
        }catch (NullPointerException e) {
            if (TableController.getCurrentTable().getSize() == 0) {
                return ("Collection is already empty.");
            } else {
                TableController.getCurrentTable().clear();
                return ("Collection has been cleared.");
            }
        }
        return null;
    }

    /**
     * get name of command
     *
     * @return String
     */

    public String toString(){
        return "clear";
    }
}

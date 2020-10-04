package cmd;
import BD.DataHandler;
import BD.DataManager;
import BD.DataUserManager;
import consolehandler.TableController;
import server.User;

/**
 * remove all elements from the collection
 *
 *
 */

public class CommandClear implements Command {

    private String login;
    private String password;

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
                User user = new User();
                user.setPassword(password);
                user.setUsername(login);
                DataHandler handler = new DataHandler();
                DataUserManager userManager = new DataUserManager(handler);
                DataManager manager = new DataManager(handler,userManager);


                TableController.getCurrentTable().loadCollection();
                return ("Your Collection has been cleared.");
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

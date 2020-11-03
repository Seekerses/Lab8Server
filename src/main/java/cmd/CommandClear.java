package cmd;
import BD.*;
import consolehandler.TableController;
import clientserverdata.User;

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
                return ("ZeroArgs");
            }
        }catch (NullPointerException e) {
            if (TableController.getCurrentTable().getSize() == 0) {
                return ("EmptyCollection");
            } else {
                User user = new User();
                user.setPassword(password);
                user.setUsername(login);
                DataHandler handler = DataHandler.getInstance();
                BD.DataUserManager userManager = new DataUserManager(handler);
                DataManager manager = new DataManager(handler,userManager);
                if(userManager.checkUserByUsernameAndPassword(user)) {
                    manager.deleteProductByUser(user);
                    return ("CollectionCleared");
                }else{
                    return "NoRights";
                }
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

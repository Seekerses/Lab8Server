package cmd;
import BD.DataHandler;
import BD.DataManager;
import BD.DataUserManager;
import consolehandler.TableController;
import clientserverdata.User;

/**
 * removes element with given key
 *
 *
 */

public class CommandRemove implements Command {

    private String password;
    private String login;

    private static final long serialVersionUID = 1337000012L;

    @Override
    public String execute(String[] args) {
        User user = new User();
        user.setPassword(password);
        user.setUsername(login);
        DataHandler handler = DataHandler.getInstance();
        DataUserManager userManager = new DataUserManager(handler);
        DataManager manager = new DataManager(handler, userManager);
        if(args.length == 0){
            return "Enter key to remove!";
        }
        if(userManager.checkUserByUsernameAndPassword(user)) {
            int count = 0;
            for (String key : TableController.getCurrentTable().getKey()) {
                if (key.equals(args[0])) {
                    count++;
                }
            }
            if (count == 0) {
                return ("No such key\nAvailable keys: " + TableController.getCurrentTable().getKey());
            } else {
                if(manager.checkForRoots(TableController.getCurrentTable().get(args[0]).getId(), user)) {
                    manager.deleteProductById(TableController.getCurrentTable().get(args[0]).getId());
                    return ("Element has been removed.");
                }else{
                    return "You have no rights to do it";
                }
            }
        }else{
            return "No rights to do it(";
        }
    }

    /**
     * get name of command
     *
     * @return String
     */

    public String toString(){
        return "remove_key";
    }
}

package cmd;
import BD.DataHandler;
import BD.DataManager;
import BD.DataUserManager;
import consolehandler.TableController;
import productdata.Product;
import server.User;

import java.util.*;

/**
 * delete all elements with lower id than one's given
 *
 * @author Alexandr
 */

public class Commandremove_lower implements Command{

    private String password;
    private String login;

    private static final long serialVersionUID = 1337000013L;

    /**
     * Iterates through hashtable and remove all elements with id lower than one's given
     *
     * @param args , gives id from input
     */

    @Override
    public String execute(String[] args) {
        User user = new User();
        user.setUsername(login);
        user.setPassword(password);
        DataHandler handler = new DataHandler();
        DataUserManager userManager = new DataUserManager(handler);
        DataManager manager = new DataManager(handler, userManager);
        if(userManager.checkUserByUsernameAndPassword(user)) {
            try {
                Iterator<Map.Entry<String, Product>> it = TableController.getCurrentTable().getSet().iterator();
                int i = Integer.parseInt(args[0]);
                while (it.hasNext()) {
                    Map.Entry<String, Product> map = it.next();
                    if (map.getValue().getId() < i) {
                        if(manager.checkForRoots(map.getValue().getId(), user)) {
                            manager.deleteProductById(map.getValue().getId());
                            it.remove();//against ConcurrentModificationException
                            TableController.getCurrentTable().remove(map.getKey());
                        }
                    }
                }
                TableController.getCurrentTable().loadCollection();
                return ("Elements with lower ID has been removed");
            } catch (NumberFormatException e) {
                return ("Argument must be a number!");
            }
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
        return "remove_lower";
    }
}

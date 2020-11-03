package cmd;
import BD.DataHandler;
import BD.DataManager;
import BD.DataUserManager;
import consolehandler.Initializer;
import consolehandler.TableController;
import productdata.Product;
import productdata.ReaderProductBuilder;
import clientserverdata.User;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.*;

/**
 * replace new product with old one if price of new product is higher
 *
 *
 */

public class Commandreplace_if_greater implements Command,Preparable{

    private static final long serialVersionUID = 1337000014L;

    private String password;
    private String login;

    Product product = null;
    String key = null;

    @Override
    public String execute(String[] args) {
        User user = new User();
        user.setPassword(password);
        user.setUsername(login);
        DataHandler handler = DataHandler.getInstance();
        DataUserManager userManager = new DataUserManager(handler);
        DataManager manager = new DataManager(handler, userManager);
        if(userManager.checkUserByUsernameAndPassword(user)) {
            if (product == null || key == null) {
                prepare(args);
                execute(args);
            } else try {
                int c = 0;
                for (String key : TableController.getCurrentTable().getKey()) {
                    if (key.equals(this.key)) {
                        c++;
                    }
                }
                if (c == 0) {
                    return ("No such key\nAvailable keys: " + TableController.getCurrentTable().getKey());
                } else {
                    for (Map.Entry<String, Product> map : TableController.getCurrentTable().getSet()) {
                        if (map.getKey().compareTo(this.key) == 0) {
                            if (product != null && product.getPrice() > map.getValue().getPrice()) {
                                if(manager.checkForRoots(map.getValue().getId(), user)) {
                                    manager.deleteProductById(map.getValue().getId());
                                    manager.insertProduct(map.getValue().getId(), product, key, user);
                                }else{
                                    return "NoRights";
                                }
                            }
                        }
                    }
                    return ("ElementReplaced");
                }
            } catch (NumberFormatException | SQLException e) {
                return ("ArgIsNumber");
            }
        }
        return "ElementReplaced";
    }
    @Override
    public void prepare(String[] args) {
        if (args == null) {
            String key;
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            try {
                do {
                    System.out.println(" Enter product key: ");
                    key = reader.readLine();
                    if (key == null) System.out.println("Error: null key.");
                } while (key == null);
                this.key = key;
                this.product = ReaderProductBuilder.buildProduct(reader);
            } catch (Exception e) {
                System.out.println("Key is null, please try again with valid key...");
            }
        }
        else{
            this.product = Initializer.build(args);
            this.key = args[0];
        }
    }

    @Override
    public String toString() {
        return "replace_if_greater";
    }

}

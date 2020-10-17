package cmd;
import BD.DataHandler;
import BD.DataManager;
import BD.DataUserManager;
import consolehandler.Initializer;
import productdata.Product;
import productdata.ReaderProductBuilder;
import clientserverdata.User;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.sql.SQLException;

/**
 * get name of command
 *
 *
 */

public class CommandAdd implements Command, Preparable, Serializable {

    Product product;
    String key;

    private String login;
    private String password;
    private static final long serialVersionUID = 1337000000L;

    /**
     * insert product to hashtable
     *
     * @param args is key to new product
     */
    @Override
    public String execute(String[] args) {
        if (product == null || key == null){
            prepare(args);
            execute(args);
        }
        else {
            User user = new User();
            user.setUsername(login);
            user.setPassword(password);

            DataHandler handler = DataHandler.getInstance();
            DataUserManager userManager = new DataUserManager(handler);
            DataManager manager = new DataManager(handler, userManager);
            System.out.println(product.toString());
            if(userManager.checkUserByUsernameAndPassword(user)) {
                try {
                    product.setOwner(user);
                    manager.insertProduct(product, key, user);
                    return ("Insertion complete...");
                } catch (SQLException e) {
                    e.printStackTrace();
                    return "Wrong input";
                }
            }else{
                return "You don't have rights to do it";
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
        return "insert";
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

}
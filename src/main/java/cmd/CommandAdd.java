package cmd;
import consolehandler.Initializer;
import consolehandler.TableController;
import productdata.Product;
import productdata.ReaderProductBuilder;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Serializable;

/**
 * get name of command
 *
 *
 */

public class CommandAdd implements Command, Preparable, Serializable {



    Product product;
    String key;
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
            TableController.getCurrentTable().put(key, product);
            return ("Insertion complete...");
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
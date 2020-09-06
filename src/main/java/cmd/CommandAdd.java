package cmd;
import Control.Initializer;
import Control.TableController;
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

public class CommandAdd implements Command, Serializable {

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
            execute(args);
        }
        else {
            TableController.getCurrentTable().put(key, product);
            return ("Insertion complete...");
        }
        return "";
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

}
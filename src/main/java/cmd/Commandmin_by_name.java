package cmd;
import Control.TableController;
import Control.TableManager;
import productdata.Product;

import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 * get element with the shortest name
 *
 *
 */

public class Commandmin_by_name implements Command {

    private static final long serialVersionUID = 1337000011L;

    @Override
    public String execute(String[] args) {
        try {
            if (args.length == 1) {
                return("There is no args for this command!");
            }
        }catch (NullPointerException e) {
            String min = "Empty table";
            try {
                min = TableController.getCurrentTable().getProducts().stream()
                        .sorted(Comparator.comparing(Product::getName))
                        .min(Comparator.comparing(p -> p.getName().length()))
                        .get().toString();
            }catch (NoSuchElementException ex){
            }
            return min;
        }
        return null;
    }

    /**
     * get name of command
     *
     * @return String
     */

    public String toString() {
        return "min_by_name";
    }
}

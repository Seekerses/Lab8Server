package cmd;
import Control.TableController;
import Control.TableManager;
import cliser.NameComparator;
import cliser.PlaceComparator;
import cliser.Reply;

import java.util.Comparator;
import java.util.Hashtable;

/**
 * show all elements in String format
 *
 *
 */

public class CommandShow implements Command {

    private static final long serialVersionUID = 1337000015L;

    class ShowInfo {
        private String info = "";

        public void addInfo(String info) {
            this.info += info;
        }

        public String getInfo() {
            return info;
        }
    }

    @Override
    public String execute(String[] args) {
        try {
            if (args.length == 1) {
                return ("There is no args for this command!");
            }
        }catch (NullPointerException e) {
            if (TableController.getCurrentTable().getSize() == 0) {
                return ("Collection is empty!");
            } else {
                ShowInfo showInfo = new ShowInfo();
                TableController.getCurrentTable().getProducts().stream().sorted(new NameComparator()).sorted(new PlaceComparator()).forEach(y -> showInfo.addInfo(y.toString()));
                return showInfo.getInfo();
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
        return "show";
    }
}

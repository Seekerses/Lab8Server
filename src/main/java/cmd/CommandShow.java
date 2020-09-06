package cmd;
import consolehandler.TableController;
import clientserverdata.PlaceComparator;
import clientserverdata.NameComparator;

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
                TableController.getCurrentTable().getProducts().stream().sorted(new PlaceComparator()).forEach(y -> showInfo.addInfo(y.toString()));
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

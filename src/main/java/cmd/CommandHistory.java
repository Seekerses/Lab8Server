package cmd;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * gives commands history
 *
 *
 */

public class CommandHistory implements Command{

    private static final long serialVersionUID = 1337000009L;

    private static List<String> history = new ArrayList<>();

    public void addCommand(String name) {
        history.add(name);
    }

    public String execute(String[] args) {
        try {
            if (args.length == 1) {
                return("There is no args for this command!");
            }
        }catch (NullPointerException e) {
            StringBuilder stringBuilder = new StringBuilder("");
            ((history.subList(Math.max(history.size() - 7, 0), history.size()))).forEach(k ->stringBuilder.append(k).append("\n"));
            return stringBuilder.toString();
        }
        return null;
    }

    /**
     * get name of command
     *
     * @return String
     */

    public String toString(){
        return "history";
    }
}
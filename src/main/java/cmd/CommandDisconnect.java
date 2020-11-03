package cmd;


/**
 * break the programm
 *
 *
 */

public class CommandDisconnect implements Command{

    private static final long serialVersionUID = 13370000020L;

    @Override
    public String execute(String[] args) {
        try {
            if (args.length == 1) {
                return ("ZeroArgs");
            }
        }catch (NullPointerException e) {
            return "disconnect";
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
        return "exit";
    }
}

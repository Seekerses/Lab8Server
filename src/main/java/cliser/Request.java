package cliser;

import cmd.Command;

import java.io.Serializable;
import java.util.Arrays;

public class Request implements Serializable {

    String[] args;
    Command command;
    private static final long serialVersionUID = 1337L;

    public Request(Command cmd, String[] args){
        this.args = args;
        this.command = cmd;
    }

    public String[] getArgs() {
        return args;
    }

    public Command getCommand() {
        return command;
    }

    @Override
    public String toString() {
        return Arrays.toString(args) + command.toString();
    }
}

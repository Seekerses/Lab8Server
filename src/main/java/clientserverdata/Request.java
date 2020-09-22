package clientserverdata;

import cmd.Command;

import java.io.Serializable;
import java.net.InetSocketAddress;
import java.util.Arrays;

public class Request implements Serializable {

    private String[] args;
    private Command command;
    String login;
    String password;
    private InetSocketAddress address;

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

    public InetSocketAddress getAddress() {
        return address;
    }

    public void setAddress(InetSocketAddress address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return Arrays.toString(args) + command.toString();
    }
}

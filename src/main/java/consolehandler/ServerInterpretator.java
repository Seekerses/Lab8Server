package consolehandler;

import BD.DataHandler;
import clientserverdata.Request;
import consolehandler.cmdLists.CommandList;
import consolehandler.cmdLists.StdCommandList;
import server.RequestHandler;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;

public class ServerInterpretator implements Interpreter {
    /**
     * Command List that Interpreter uses, changeable
     */
    private CommandList cmdList =  new StdCommandList();

    /**
     * This method takes the command, separate it on command and arguments
     * and then interpret command according to the current Command List
     * @param args Command
     * @throws IOException If an I/O error occurs
     */
    @Override
    public String handle(String[] args) throws IOException {
        if (cmdList.getCommands().containsKey(args[0])) {
            CommandController.getCommandHistory().addCommand(cmdList.getCommands().get(args[0]).toString());
            try {
                String[] arguments;
                if (args.length > 1) {
                    arguments = Arrays.copyOfRange(args,1,args.length);
                } else arguments = null;
                if (arguments != null) for (int i=0;i<= arguments.length-1;i++) {
                    if (";".equals(arguments[i])){
                        arguments[i] = "";
                    }
                }
                Request request = new Request(cmdList.getCommands().get(args[0]),arguments);
                request.setLogin("s285582");
                request.setPassword("sbq939");
                RequestHandler req = new RequestHandler(new Request(cmdList.getCommands().get(args[0]),arguments));
                return req.handleRequest(request).getAnswer();
            } catch (NullPointerException e){
                return ("Wrong arguments...");
            }
        }
        else {
            return ("There is no such command. Enter help to see a list of available commands ..");
        }
    }

    /**
     * This method change the Command List that concrete Interpreter uses
     * @param list New Command List
     */
    public void changeCommandList(CommandList list){
        cmdList = list;
    }
}

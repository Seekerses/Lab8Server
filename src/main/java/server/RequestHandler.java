package server;

import clientserverdata.Reply;
import clientserverdata.Request;
import cmd.CommandHistory;

import java.io.IOException;

public class RequestHandler implements Runnable {

    private Request request;

    public RequestHandler(Request request){
        this.request = request;
    }

    public Reply handleRequest(Request request) {

        Reply reply = null;
        System.out.printf("\b\nIncoming request from %s is: %s \n>", request.getAddress(), request.getCommand());

        try {
            if (request.getCommand() instanceof CommandHistory){
                reply = new Reply(null,
                        ServerController.getServerHistory().execute(request.getArgs()));
            }
            else {
                reply = new Reply(null,
                        request.getCommand().execute(request.getArgs()));
            }
            ServerController.getServerHistory().addCommand(request.getCommand().toString());
            reply.setAddress(request.getAddress());
        }

        catch (IOException e){
            System.out.print("IO exception");
        }
        catch (NullPointerException ex){
            ex.printStackTrace();
            System.out.print("Failed to handle a command.");
        }

        return reply;
    }

    @Override
    public void run() {
        try {
            Reply reply = handleRequest(this.request);
            if (reply != null) {
                ServerController.getScheduler().getReplays().add(reply);
            }
        } catch (NullPointerException e){
            System.out.println("Request list is empty!");
        }
    }
}

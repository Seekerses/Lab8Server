package server;

import clientserverdata.Reply;
import clientserverdata.Request;
import cmd.CommandHistory;

import java.io.IOException;

class RequestHandler implements Runnable {

    private Request request;

    RequestHandler(Request request){
        this.request = request;
    }

    private Reply handleRequest(Request request) {

        Reply reply = null;
        System.out.printf("Incoming request from %s is: %s ", request.getAddress(), request.getCommand());

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
            System.out.print("Failed to handle a command.");
        }

        return reply;
    }

    @Override
    public void run() {
        try {
            ServerController.getScheduler().getReplays().add(handleRequest(this.request));
        } catch (NullPointerException e){
            System.out.println("Request list is empty!");
        }
    }
}

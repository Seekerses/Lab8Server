package server;

import clientserverdata.Reply;
import consolehandler.CommandController;
import consolehandler.CommandInterpreter;
import clientserverdata.Request;
import cmd.CommandHistory;

import java.io.IOException;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.util.Scanner;

public class ServerController {

    private static InetSocketAddress iAddr;
    private static int port = 1337;
    private static boolean running = false;
    private static boolean connected = false;
    private static final CommandHistory serverHistory = new CommandHistory();
    private static DatagramChannel channel;
    private static SocketAddress remoteAddr;
    private static ByteBuffer buf;

    private static void handleRequest(byte[] data){
        Request request = Serializer.deserialize(data);
        assert request != null;
        Reply reply = null;
        System.out.println("\n Accepted a request from client: " + request.getCommand());
        try {
            if (request.getCommand() instanceof CommandHistory){
                reply = new Reply(null,
                        serverHistory.execute(request.getArgs()));
            }
            else {
                reply = new Reply(null,
                        request.getCommand().execute(request.getArgs()));
            }
            serverHistory.addCommand(request.getCommand().toString());

        }
        catch (IOException e){
            System.out.print("IO exception");
        }
        assert reply != null;
        if(reply.getAnswer().equals("disconnect")){
            connected = false;
            remoteAddr=null;
        }else {
            byte[] serReply = Serializer.serialize(reply);
            assert serReply != null;
            System.out.println("Sending a reply...");
            Sender.send(serReply);
        }
    }

    private static void setPort(){
        System.out.print("Please enter a port that you want bind to:\n>");
        while (true){
                Scanner scanner = new Scanner(System.in);
                String numb = scanner.nextLine();
                System.out.println("----");
                if (numb.matches("[0-9]+")) {
                    if (Integer.parseInt(numb) < 65535) {
                        port = Integer.parseInt(numb);
                        return;
                    }else {
                        System.out.println("Unexpectedly port number, please, enter correct port number:");
                    }
                }else {
                    System.out.println("Unexpectedly port number, please, enter correct port number:");
                }
        }
    }

    static SocketAddress getRemoteAddr() {
        return remoteAddr;
    }

    private static void setRemoteAddr(SocketAddress remoteAddr) {
        ServerController.remoteAddr = remoteAddr;
    }

    private static boolean checkConnection(){
        byte[] arr = buf.array();
        return (arr[0] == 1) && (arr[1023] == 1);
    }

    public static void start() throws IOException {
        running = true;
        System.out.println("Server awaiting connections...\n");

        CommandController cmd = new CommandController();
        System.out.println("Enter Command or Help to display a list of commands:");
        System.out.print(">");
        while (running){
            cmd.start(new CommandInterpreter());
            if(!connected) {
                buf = ByteBuffer.allocate(1024);
                SocketAddress clientAddr = channel.receive(buf);
                if (checkConnection()) {
                    setRemoteAddr(clientAddr);
                    connected = true;
                    System.out.println("Connected to client");
                    buf.clear();
                }
            }else {
                byte[] data = Receiver.getReply();
                assert data != null;
                if (data.length > 0) {
                    handleRequest(data);
                }
                connected = false;
            }
        }
    }

    public static DatagramChannel getChannel() {
        return channel;
    }

    public static void connect(){
        try {
            iAddr = new InetSocketAddress(port);
            channel = DatagramChannel.open();
            channel.configureBlocking(false);
            channel.bind(iAddr);
            System.out.println("Server is bounded to:" + iAddr);
        }
        catch (SocketException | UnknownHostException socketException){
            System.out.println("Port or IP is unavailable, please enter other port and IP.");
            ServerController.setPort();
            ServerController.connect();
        } catch (IOException e) {
            System.out.println("Oh no, some IO exceptions occurs...");
        }
    }
}

package ServSoft;

import Control.TableController;
import Control.TableManager;
import cliser.Reply;
import Control.CommandController;
import Control.CommandInterpreter;
import cliser.Request;
import cmd.CommandHistory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.util.Arrays;
import java.util.Scanner;

public class ServerController {

    private static InetSocketAddress iAddr;
    private static int port = 1337;
    private static boolean running = false;
    private static boolean portSet;
    private static boolean connected = false;
    private static final CommandHistory serverHistory = new CommandHistory();
    private static DatagramChannel channel;
    private static SocketAddress remoteAddr;
    private static ByteBuffer buf;

    public static void handleRequest(byte[] data){
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
            setConnected(false);
            remoteAddr=null;
        }else {
            byte[] serReply = Serializer.serialize(reply);
            assert serReply != null;
            System.out.println("Sending a reply...");
            Sender.send(serReply);
        }
    }

    protected static void setPort(){
        portSet = false;
        System.out.println("----\nEnter port\n----");
        while (!portSet){
                Scanner scanner = new Scanner(System.in);
                String numb = scanner.nextLine();
                System.out.println("----");
                if (numb.matches("[0-9]+")) {
                    if (Integer.parseInt(numb) < 65535) {
                        port = Integer.parseInt(numb);
                        portSet = true;
                    }else {
                        System.out.println("----\nUnexpectedly port number, please, enter correct port number\n----");
                    }
                }else {
                    System.out.println("----\nUnexpectedly port number, please, enter correct port number\n----");
                }
        }

    }

    private static void shootDownHook(){
        try {
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                try {
                    TableController.getCurrentTable().save(new File("saved.csv"));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }));
        }catch (ArrayIndexOutOfBoundsException e){}
    }

    public static SocketAddress getRemoteAddr() {
        return remoteAddr;
    }

    public static void setRemoteAddr(SocketAddress remoteAddr) {
        ServerController.remoteAddr = remoteAddr;
    }

    public static boolean checkConnection(){
        byte[] arr = buf.array();
        return (arr[0] == 1) && (arr[1023] == 1);
    }

    public static void setConnected(boolean connected) {
        ServerController.connected = connected;
    }

    public static void start() throws IOException, InterruptedException {
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

    public static int getPort() {
        return port;
    }

    public static DatagramChannel getChannel() {
        return channel;
    }

    public static void connect(int port){
        try {
            iAddr = new InetSocketAddress("localhost",port);
            channel = DatagramChannel.open();
            channel.configureBlocking(false);
            channel.bind(iAddr);
            System.out.println("bound to " + iAddr);
        }
        catch (SocketException | UnknownHostException socketException){
            System.out.println("Port or IP is unavailable, please enter other port and IP.");
            ServerController.setPort();
            ServerController.connect(ServerController.getPort());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

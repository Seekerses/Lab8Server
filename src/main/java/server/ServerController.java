package server;

import clientserverdata.Reply;
import consolehandler.CommandController;
import consolehandler.CommandInterpreter;
import clientserverdata.Request;
import cmd.CommandHistory;

import java.io.IOException;
import java.net.*;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.util.Arrays;
import java.util.Scanner;

public class ServerController {

    private static InetSocketAddress iAddr;
    private static int port = 1337;
    private static final CommandHistory serverHistory = new CommandHistory();
    private static DatagramChannel channel;
    private static SocketAddress remoteAddr;

    private static void handleRequest(byte[] data) {
        Request request = Serializer.deserialize(data);
        assert request != null;
        Reply reply = null;
        System.out.println("Incoming request is: " + request.getCommand());
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
        byte[] serReply = Serializer.serialize(reply);
        assert serReply != null;
        System.out.println("Sending a reply...\n");
        Sender.send(serReply);
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

    private static boolean checkConnection(ByteBuffer buffer){
        byte[] arr = buffer.array();
        return (arr[0] == 1) && (arr[1023] == 1);
    }

    public static void start() throws IOException {
        System.out.println("Server awaiting connections...\n");

        CommandController cmd = new CommandController();
        System.out.println("Enter Command or Help to display a list of commands:");
        System.out.print(">");
        while (true){
            cmd.start(new CommandInterpreter());
            ByteBuffer buf = ByteBuffer.allocate(1024);
            SocketAddress clientAddr = channel.receive(buf);
            if (checkConnection(buf)) {
                setRemoteAddr(clientAddr);
                System.out.println("Incoming request from:" + clientAddr);
                buf.clear();
                channel.connect(clientAddr);
                byte[] data = Receiver.getReply();
                if (data != null) {
                    if (data.length > 0) {
                        handleRequest(data);
                    }
                }
                else {
                    System.out.println("Incoming request is null...");
                }
                channel.disconnect();
            }
        }
    }

    public static DatagramChannel getChannel() {
        return channel;
    }

    public static void connect(){
        try {
            setPort();
            iAddr = new InetSocketAddress("localhost",port);
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

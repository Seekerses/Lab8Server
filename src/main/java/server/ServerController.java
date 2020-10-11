package server;

import cmd.CommandHistory;

import java.io.IOException;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.util.ArrayList;
import java.util.Scanner;

public class ServerController {

    private static int port = 1337;
    private static final CommandHistory serverHistory = new CommandHistory();
    private static DatagramChannel channel;
    private static ServerScheduler scheduler;
    private static ArrayList<InetSocketAddress> clientList;
    private static boolean isUp;
    private static ClientNotificator notifier;

    public static void start() throws IOException {

        clientList = new ArrayList<>();

        System.out.println("Server awaiting connections...\n");

        ByteBuffer buf = ByteBuffer.allocate(1024);
        byte[] ok = new byte[1024];
        ok[0] = 1;
        ok[1023] = 1;
        while (isUp){

            buf.clear();
            SocketAddress clientAddr = channel.receive(buf);


            if (checkConnection(buf) && clientAddr != null) {
                buf.clear();
                buf.put(ok);
                buf.flip();
                channel.send(buf, clientAddr);
                clientList.add((InetSocketAddress)clientAddr);
            }

            if(checkRequest(buf) && clientAddr != null){
                scheduler.getClients().add((InetSocketAddress) clientAddr);
                if(!clientList.contains((InetSocketAddress) clientAddr)){
                    clientList.add((InetSocketAddress) clientAddr);
                }
            }
        }
    }

    public static void connect(){
        try {
            setPort();
            channel = DatagramChannel.open()
                    .setOption(StandardSocketOptions.SO_REUSEADDR, true)
                    .bind(new InetSocketAddress("localhost",port));
            channel.configureBlocking(false);
            System.out.println("Server is bounded to:" +channel.getLocalAddress());
            isUp = true;
        }
        catch (SocketException | UnknownHostException socketException){
            System.out.println("Port or IP is unavailable, please enter other port and IP.");
            ServerController.setPort();
            ServerController.connect();
        } catch (IOException e) {
            System.out.println("Oh no, some IO exceptions occurs...");
        }
    }

    private static boolean checkConnection(ByteBuffer buffer){
        byte[] arr = buffer.array();
        return (arr[0] == 1) && (arr[1023] == 1);
    }

    private static boolean checkRequest(ByteBuffer buffer){
        byte[] arr = buffer.array();
        return (arr[0] == 100) && (arr[1023] == 100);
    }

    private static void setPort(){
        System.out.print("Please enter a port that you want bind to:\n>");
        while (true){
            try {
                Scanner scanner = new Scanner(System.in);
                String numb = scanner.nextLine();
                System.out.println("----");
                if (numb.matches("[0-9]+")) {
                    if (Integer.parseInt(numb) < 65535) {
                        port = Integer.parseInt(numb);
                        return;
                    } else {
                        System.out.println("Unexpectedly port number, please, enter correct port number:");
                    }
                } else {
                    System.out.println("Unexpectedly port number, please, enter correct port number:");
                }
            }catch (Exception ex){
                System.out.println("Wrong symbol sequence.");
                setPort();
            }
        }
    }

    public static DatagramChannel getChannel() {
        return channel;
    }

    static CommandHistory getServerHistory() {
        return serverHistory;
    }


    public static ServerScheduler getScheduler() {
        return scheduler;
    }

    static int getPort() {
        return port;
    }

    public static void setScheduler(ServerScheduler scheduler) {
        ServerController.scheduler = scheduler;
    }

    public static ArrayList<InetSocketAddress> getClientList() {
        return clientList;
    }

    public static ClientNotificator getNotifier() {
        return notifier;
    }

    public static void setNotifier(ClientNotificator notifier) {
        ServerController.notifier = notifier;
    }
}

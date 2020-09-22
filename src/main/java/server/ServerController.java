package server;

import cmd.CommandHistory;

import java.io.IOException;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.util.Enumeration;
import java.util.Scanner;

public class ServerController {

    private static int port = 1337;
    private static final CommandHistory serverHistory = new CommandHistory();
    private static DatagramChannel channel;
    private static ServerScheduler scheduler;
    private static InetAddress mCastGroup;
    private static NetworkInterface networkInterface;
    private static boolean isUp;

    public static void start() throws IOException {

        System.out.println("Server awaiting connections...\n");
        scheduler = new ServerScheduler(5,5);
        Thread initiateThread = new Thread(scheduler);
        initiateThread.start();
        try {
            initiateThread.join();
        } catch (InterruptedException e){
            System.out.println("Loading of scheduler was interrupted!");
        }

        ByteBuffer buf = ByteBuffer.allocate(1024);
        while (isUp){

            buf.clear();
            SocketAddress clientAddr = channel.receive(buf);

            if (checkConnection(buf) && clientAddr != null) {
                System.out.println("Incoming request from:" + clientAddr);
                scheduler.getClients().add((InetSocketAddress) clientAddr);
            }
        }
    }

    public static void connect(){
        try {
            setPort();
            networkInterface = null;
            Enumeration<NetworkInterface> interfaceEnumeration = NetworkInterface.getNetworkInterfaces();
            NetworkInterface iter;
            while (networkInterface == null && interfaceEnumeration.hasMoreElements()){
                iter = interfaceEnumeration.nextElement();
                if (iter.isUp() && iter.supportsMulticast() && !iter.isLoopback()){
                    networkInterface = iter;
                }
            }

            mCastGroup = InetAddress.getByName("230.0.0.0");
            channel = DatagramChannel.open(StandardProtocolFamily.INET)
                    .setOption(StandardSocketOptions.SO_REUSEADDR, true)
                    .setOption(StandardSocketOptions.IP_MULTICAST_IF, networkInterface)
                    .bind(new InetSocketAddress(port));
            channel.configureBlocking(false);
            System.out.println("Server is bounded to:" + mCastGroup.toString() + ":" + port);
            channel.join(mCastGroup,networkInterface);
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

    public static DatagramChannel getChannel() {
        return channel;
    }

    static CommandHistory getServerHistory() {
        return serverHistory;
    }


    static ServerScheduler getScheduler() {
        return scheduler;
    }

    static int getPort() {
        return port;
    }

    static InetAddress getMCastGroup() {
        return mCastGroup;
    }

    static NetworkInterface getNetworkInterface() {
        return networkInterface;
    }
}

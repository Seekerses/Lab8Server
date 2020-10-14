package server;

import clientserverdata.Reply;
import consolehandler.TableController;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.StandardProtocolFamily;
import java.net.StandardSocketOptions;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.util.HashMap;

public class ClientNotificator implements Runnable{

    private volatile boolean  notified = false;
    DatagramChannel channel;


    @Override
    public void run() {
        try {
            channel = DatagramChannel.open()
                    .setOption(StandardSocketOptions.SO_REUSEADDR, true)
                    .bind(new InetSocketAddress("localhost", ServerController.getPort()-1));
            channel.configureBlocking(false);
        } catch (IOException e) {
            e.printStackTrace();
        }


        while(true){
            if(notified){
                try {
                    for (InetSocketAddress client : ServerController.getClientList()) {
                        try {
                            sendUpdate(client);
                        } catch (IOException | InterruptedException e) {
                            System.out.println("Client is broke.");
                        }
                    }
                }finally {
                    notified = false;
                }
            }
        }
    }

    public void notifyMe(){
        notified = true;
    }

    protected void sendUpdate(InetSocketAddress client) throws IOException, InterruptedException {

        if (channel.isConnected()){
            channel.disconnect();
        }
        Reply updatedTable = new Reply(TableController.getCurrentTable().getTable(),"update");
        InetSocketAddress clientAddr = new InetSocketAddress(client.toString().split(":")[0].split("/")[1],
                (Integer.parseInt(client.toString().split(":")[1])+1));
        updatedTable.setAddress(clientAddr);
        byte[] update = new byte[1024];
        update[0] = 22;
        ByteBuffer toClient  = ByteBuffer.wrap(update);
        channel.send(toClient,clientAddr);
        Sender sender = new Sender(updatedTable, channel);
        Thread sendThread = new Thread(sender);
        sendThread.start();
        sendThread.join();
    }
}

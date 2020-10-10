package server;

import clientserverdata.Reply;
import clientserverdata.Request;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketTimeoutException;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.util.Arrays;
import java.util.Objects;

class Receiver implements Runnable {

    private InetSocketAddress client;
    private DatagramChannel channel;

    Receiver(InetSocketAddress client, DatagramChannel channel){
        this.client = client;
        this.channel = channel;
    }

    private byte[] getReply() throws IOException {

        ByteBuffer buf = ByteBuffer.allocate(1024); //buffer for coming bytes
        byte[] clear = new byte[1024]; //std buffer for "everything OK" reply
        byte[] done = new byte[1024]; //std buffer for exchanging done reply

        clear[0] = 111; // Ok signal
        done[0] = 33; // Done signal

        byte[] result = new byte[0];

        PacketUtils.sendAdress(channel);

        try {
            while (true) {

                buf.clear();
                channel.receive(buf);
                if(buf.position() > 0) {
                    if (Arrays.equals(buf.array(), done)) { //when a "exchanging done" received
                        channel.send(ByteBuffer.wrap(clear), channel.getRemoteAddress());
                        return result;
                    }
                    else {
                        channel.send(ByteBuffer.wrap(clear), channel.getRemoteAddress());
                        result = PacketUtils.merge(result, buf.array());
                    }
                }
            }
        }catch (SocketTimeoutException ex){
            System.out.println("Client " + client + " not responding." );
        }
        catch(IOException e){
            System.out.println("Error in IO while receiving message");
        }
        return null;
    }

    @Override
    public void run() {
        Request request = null;
        try {
            channel.connect(client);
            request = Serializer.deserialize(getReply());
            Objects.requireNonNull(request).setAddress((InetSocketAddress) channel.getRemoteAddress());
        }

        catch (IOException ex) {
            System.out.println("Oh,no. Something went wrong!");
        }
        catch (NullPointerException ex){
            System.out.println("Listeners are empty! ");
        }

        finally {
            try {
                channel.disconnect();
            } catch (IOException e) {
                System.out.println("Oh,no. Something went wrong with IO!");
            }
            ServerController.getScheduler().getAvailableChannels().add(channel);
        }
        if(request != null) ServerController.getScheduler().getRequests().add(request);
    }
}

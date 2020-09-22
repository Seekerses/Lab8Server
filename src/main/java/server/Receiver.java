package server;

import clientserverdata.Request;

import java.io.IOException;
import java.net.InetSocketAddress;
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

    private byte[] getReply() {

        ByteBuffer buf = ByteBuffer.allocate(1024); //buffer for coming bytes
        byte[] clear = new byte[1024]; //std buffer for "everything OK" reply
        byte[] done = new byte[1024]; //std buffer for exchanging done reply
        byte[] bad = new byte[1024]; //std buffer for "something went wrong" reply

        clear[0] = 111; // Ok signal
        bad[0] = 22; // Error signal
        done[0] = 33; // Done signal

        byte[] result = new byte[0];
        byte[] assist = new byte[1024];

        try {
            while (true) {

                channel.receive(buf);

                    if (Arrays.equals(buf.array(), done)) { //when a "exchanging done" received
                        return result;
                    }
                    if (PacketUtils.checkHash(buf.array())) {
                        channel.send(ByteBuffer.wrap(clear), channel.getRemoteAddress());
                        if (!Arrays.equals(assist,buf.array())) {
                            result = PacketUtils.merge(result, Arrays.copyOfRange(buf.array(), 0, 1012));
                            assist = Arrays.copyOf(buf.array(),1024);
                        }
                    } else { //when received a broken packet(deprecated func)
                        channel.send(ByteBuffer.wrap(bad), channel.getRemoteAddress());
                    }
                    buf.clear();
            }
        } catch(IOException e){
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
            System.out.println("Listeners are empty! " + Thread.currentThread().getName());
        }

        finally {
            try {
                channel.disconnect();
            } catch (IOException e) {
                System.out.println("Oh,no. Something went wrong with IO!");
            }
            ServerController.getScheduler().getAvailableChannels().add(channel);
        }

        ServerController.getScheduler().getRequests().add(request);
    }
}

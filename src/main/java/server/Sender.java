package server;

import clientserverdata.Reply;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketTimeoutException;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.util.Arrays;

class Sender implements Runnable{

    private Reply reply;
    private DatagramChannel channel;
    private boolean[] args;

    Sender(Reply reply, DatagramChannel channel, boolean... args){
        this.reply = reply;
        this.channel = channel;
        this.args = args;
    }

        private void send(byte[] data ){

            byte[] done = new byte[1024]; //std buffer for exchanging done reply
            done[0] = 33;

            PacketUtils.sendAdress(channel);

            try {

                ByteBuffer buffer = ByteBuffer.allocate(1024);
                ByteBuffer handle = ByteBuffer.allocate(1024);
                boolean last = false;
                int i = 0;
                while(!last) {

                    buffer.clear();
                    if (data.length > 1024) {
                        buffer.put(Arrays.copyOfRange(data, 0, 1024));
                    }
                    else {
                        buffer.put(Arrays.copyOf(data,1024));
                        last = true;
                    }

                    buffer.flip();
                    if (args != null){
                        i++;
                    }
                    channel.send(buffer,channel.getRemoteAddress());
                    handle = PacketUtils.getResponse(channel);
                    if ( handle.array()[0] == 111 ){
                        if ( data.length > 1024) {
                            data = Arrays.copyOfRange(data, 1024, data.length);
                        }
                    }
                    else {
                        last = false;
                    }
                }

                handle.put(new byte[1024]);
                while( handle.array()[0] != 111 ) {
                    buffer.clear();
                    buffer.put(done);
                    buffer.flip();
                    channel.send(buffer, channel.getRemoteAddress());

                    handle = PacketUtils.getResponse(channel);
                }
            }catch (SocketTimeoutException ex){
                System.out.println("Client " + reply.getAddress() + " not responding." );
                ServerController.getClientList().remove(reply.getAddress());
                ServerController.getClientList().remove(new InetSocketAddress(reply.getAddress().getAddress().toString(),
                        reply.getAddress().getPort()-1));
           }
            catch (IOException | InterruptedException e){
                e.printStackTrace();
                System.out.println("Oh, no. IO errors while sending reply!");
            }
        }

    @Override
    public void run() {
        try {
            if (channel.isConnected()){
                channel.disconnect();
            }
            channel.connect(reply.getAddress());
            send(Serializer.serialize(reply));
        } catch (IOException e) {
            System.out.println("Failing in connection to remote address.");
        }
        catch (NullPointerException e){
            System.out.println("Empty address of reply!");
        }
        finally {
            try {
                channel.disconnect();
                ServerController.getScheduler().availableChannels.add(channel);
            } catch (IOException e) {
                System.out.println("Failing while disconnect from remote address.");
            }

            }
    }
}
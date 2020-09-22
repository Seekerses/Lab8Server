package server;

import clientserverdata.Reply;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.util.Arrays;

class Sender implements Runnable{

    private Reply reply;
    private DatagramChannel channel;

    Sender(Reply reply, DatagramChannel channel){
        this.reply = reply;
        this.channel = channel;
    }

        private void send(byte[] data){

            byte[] done = new byte[1024]; //std buffer for exchanging done reply
            done[0] = 33;

            try {

                ByteBuffer buffer = ByteBuffer.allocate(1024);
                ByteBuffer handle = ByteBuffer.allocate(1024);
                boolean last = false;

                while(!last) {

                    if (data.length > 1012) {
                        buffer.put(PacketUtils.formatData(Arrays.copyOfRange(data, 0, 1012)));
                    }
                    else {
                        buffer.put(PacketUtils.formatData(Arrays.copyOf(data,1012)));
                        last = true;
                    }

                    buffer.flip();
                    channel.send(buffer,channel.getRemoteAddress());


                    channel.receive(handle);
                    while (handle.array()[0] != 111 && handle.array()[0] != 22 && Arrays.equals(handle.array(), new byte[1024])){
                        channel.receive(handle);
                    }

                    if ( handle.array()[0] == 111 ){
                        if ( data.length > 1012 ) {
                            data = Arrays.copyOfRange(data, 1012, data.length);
                        }
                    }
                    else {
                        last = false;
                    }

                    buffer.clear();
                    handle.clear();
                }
                buffer.put(done);
                buffer.flip();
                channel.send(buffer,channel.getRemoteAddress());
                buffer.clear();
            }
            catch (IOException e){
                System.out.println("Oh, no. IO errors while sending reply!");
            }
        }

    @Override
    public void run() {
        try {
            channel.connect(reply.getAddress());
        } catch (IOException e) {
            System.out.println("Failing in connection to remote address.");
        }
        catch (NullPointerException e){
            System.out.println("Empty address of reply!");
        }

        send(Serializer.serialize(reply));
        try {
            channel.disconnect();
        } catch (IOException e) {
            System.out.println("Failing while disconnect from remote address.");
        }

        ServerController.getScheduler().availableChannels.add(channel);
    }
}
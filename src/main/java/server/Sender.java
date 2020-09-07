package server;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;

class Sender {

        static void send(byte[] data){
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
                    ServerController.getChannel().send(buffer,ServerController.getRemoteAddr());

                    ServerController.getChannel().receive(handle);

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
                ServerController.getChannel().send(buffer,ServerController.getRemoteAddr());
                buffer.clear();
            }
            catch (IOException e){
                System.out.println("Error");
            }
        }

}
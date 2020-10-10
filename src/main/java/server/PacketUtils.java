package server;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.util.Arrays;

class PacketUtils {

    protected static ByteBuffer getResponse(DatagramChannel channel) throws IOException, InterruptedException {
        ByteBuffer response = ByteBuffer.allocate(1024);
        response.clear();
        Thread.sleep(100);
        channel.receive(response);
        if(response.position() == 0) {
            Thread.sleep(5000);
            channel.receive(response);
            if (response.position() == 0) throw new SocketTimeoutException();
        }
        response.flip();
        return response;
    }

    static byte[] merge(byte[] a1, byte[] a2){
        byte[] result = Arrays.copyOf(a1,a1.length + a2.length);
        int i = 0;
        for (byte b : a2){
            result[a1.length + i] = b;
            i++;
        }
        return result;
    }

}

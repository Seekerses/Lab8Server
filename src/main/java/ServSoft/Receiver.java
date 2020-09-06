package ServSoft;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;

public class Receiver{

    public static byte[] getReply() {

        ByteBuffer buf = ByteBuffer.allocate(1024); //buffer for coming bytes
        byte[] clear = new byte[1024]; //std buffer for "everything OK" reply
        byte[] bad = new byte[1024]; //std buffer for "something went wrong" reply
        clear[0] = 111; // Ok signal
        bad[0] = 22; // Error signal
        byte[] result = new byte[0];

        try {
            while (true) {
                System.out.print("red");

                ServerController.getChannel().receive(buf);
                System.out.print(Arrays.toString(buf.array()));
                if (Arrays.equals(buf.array(), new byte[1024])) {
                    break;
                }
                if (PacketFunctions.checkHash(buf.array())) {
                    ServerController.getChannel().send(ByteBuffer.wrap(clear),ServerController.getRemoteAddr());
                    result = PacketFunctions.merge(result,Arrays.copyOfRange(buf.array(),0,1012));
                }
                else {
                    ServerController.getChannel().send(ByteBuffer.wrap(bad),ServerController.getRemoteAddr());
                }
                buf.clear();
            }
            return result;
        } catch(IOException e){
            System.out.println("Error in IO");
        }
        return null;
    }
}
